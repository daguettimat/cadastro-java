package br.com.global5.manager.rest.analise;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.analise.acLiberacao;
import br.com.global5.manager.service.analise.LiberacaoService;

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
@Path("/analise/liberacao")
@Produces("application/json;charset=utf-8")
public class LiberacaoEndPoint {

    @Inject
    LiberacaoService libService;

    /**
     * Cria um novo liberacao
     *
     * @status 400 A descricao do liberacao não pode ser vazia
     * @status 400 A descricao do liberacao deve ser unica
     * @status 201 O Tipo de contrato foi criado com sucesso
     */
    @POST
    @Consumes("application/json")
    public Response create(acLiberacao entity) {
        libService.insert(entity);
        return Response.created(UriBuilder.fromResource(LiberacaoEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    /**
     * Apagar um liberacao baseado no ID
     *
     * @param user nome do usuario habilitado no sistema
     * @param id   do liberacao
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 acLiberacao não encontrado
     * @status 204 acLiberacao apagado com sucesso
     */
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) {
        acLiberacao entity = libService.findById(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        libService.remove(entity);
        return Response.noContent().build();
    }

    /**
     * Pesquisa um liberacao pelo ID
     *
     * @param id identificacao do liberacao
     * @status 404 acLiberacao nao encontrado
     * @status 304 acLiberacao nao modificado
     * @status 200 acLiberacao pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request, @Context HttpHeaders headers) {
        acLiberacao entity;

        try {
            entity = libService.findById(id);
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
        Filter<acLiberacao> filter = new Filter<>();
        acLiberacao mercadoria = new acLiberacao();
        filter.setEntity(mercadoria);

        filter.setFirst(startPosition).setPageSize(maxResult);
        final List<acLiberacao> results = libService.paginate(filter);

        return Response.ok(results).header("count", libService.count(filter)).build();
    }

    /**
     * Retorna a quantidade de Liberacaos
     */
    @GET
    @Path("count")
    public Response count() {
        Filter<acLiberacao> filter = new Filter<>();
        acLiberacao tc = new acLiberacao();
        filter.setEntity(tc);
        return Response.ok(libService.count(filter)).build();
    }

    /**
     * @status 400 A descricao não pode ser vazia
     * @status 400 Nenhum liberacao foi informado
     * @status 404 Não foi encontrato o liberacao pelo ID
     * @status 409 O id passado difere do conteudo identificado no sistema
     * @status 204 acLiberacao alterado com sucesso
     */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, acLiberacao entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (libService.crud().eq("id", id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            libService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }

}
