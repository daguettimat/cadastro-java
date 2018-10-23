package br.com.global5.manager.rest.analise;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.analise.acPerguntas;
import br.com.global5.manager.service.analise.PerguntasService;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.util.List;

/**
 *
 */
@Path("/analise/perguntas")
@Produces("application/json;charset=utf-8")
public class PerguntasEndPoint {

    @Inject
    PerguntasService PergService;

    /**
     * Cria um novo perguntas
     *
     * @status 400 A descricao do perguntas não pode ser vazia
     * @status 400 A descricao do perguntas deve ser unica
     * @status 201 O Tipo de contrato foi criado com sucesso
     */
    @POST
    @Consumes("application/json")
    public Response create(acPerguntas entity) {
        PergService.insert(entity);
        return Response.created(UriBuilder.fromResource(PerguntasEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    /**
     * Apagar um perguntas baseado no ID
     *
     * @param user nome do usuario habilitado no sistema
     * @param id   do perguntas
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 acPerguntas não encontrado
     * @status 204 acPerguntas apagado com sucesso
     */
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) {
        acPerguntas entity = PergService.findById(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        PergService.remove(entity);
        return Response.noContent().build();
    }

    /**
     * Pesquisa um perguntas pelo ID
     *
     * @param id identificacao do perguntas
     * @status 404 acPerguntas nao encontrado
     * @status 304 acPerguntas nao modificado
     * @status 200 acPerguntas pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request, @Context HttpHeaders headers) {
        acPerguntas entity;

        try {
            entity = PergService.findById(id);
        } catch (NoResultException nre) {
            entity = null;
        }

        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        CacheControl cc = new CacheControl();
        cc.setMaxAge(100);
        EntityTag tag = new EntityTag(Integer.toString(entity.hashCode()));
        Response.ResponseBuilder builder = request.evaluatePreconditions(tag);
        if (builder != null) {
            builder.cacheControl(cc);
            return builder.build();
        }
        builder = Response.ok(entity);
        builder.cacheControl(cc);
        builder.tag(tag);
        return builder.build();
    }

    /**
     * @param startPosition posicao inicial da lista desejada
     * @param maxResult     numero máximo de elementos no retorno
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                         @QueryParam("max") @DefaultValue("10") Integer maxResult) {
        Filter<acPerguntas> filter = new Filter<>();
        acPerguntas mercadoria = new acPerguntas();
        filter.setEntity(mercadoria);

        filter.setFirst(startPosition).setPageSize(maxResult);
        final List<acPerguntas> results = PergService.paginate(filter);

        return Response.ok(results).header("count", PergService.count(filter)).build();
    }

    /**
     * Retorna a quantidade de Perguntass
     */
    @GET
    @Path("count")
    public Response count() {
        Filter<acPerguntas> filter = new Filter<>();
        acPerguntas tc = new acPerguntas();
        filter.setEntity(tc);
        return Response.ok(PergService.count(filter)).build();
    }

    /**
     * @status 400 A descricao não pode ser vazia
     * @status 400 Nenhum perguntas foi informado
     * @status 404 Não foi encontrato o perguntas pelo ID
     * @status 409 O id passado difere do conteudo identificado no sistema
     * @status 204 acPerguntas alterado com sucesso
     */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, acPerguntas entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (PergService.crud().eq("id", id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            PergService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }

}
