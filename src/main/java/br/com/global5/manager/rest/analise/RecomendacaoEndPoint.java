package br.com.global5.manager.rest.analise;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.analise.acRecomendacao;
import br.com.global5.manager.service.analise.RecomendacaoService;

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
@Path("/analise/recomendacao")
@Produces("application/json;charset=utf-8")
public class RecomendacaoEndPoint {

    @Inject
    RecomendacaoService recService;

    /**
     * Cria um novo recomendacao
     *
     * @status 400 A descricao da recomendacao não pode ser vazia
     * @status 400 A descricao da recomendacao deve ser unica
     * @status 201 O Tipo de contrato foi criado com sucesso
     */
    @POST
    @Consumes("application/json")
    public Response create(acRecomendacao entity) {
        recService.insert(entity);
        return Response.created(UriBuilder.fromResource(RecomendacaoEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    /**
     * Apagar um recomendacao baseado no ID
     *
     * @param user nome do usuario habilitado no sistema
     * @param id   do recomendacao
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 acRecomendacao não encontrado
     * @status 204 acRecomendacao apagado com sucesso
     */
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) {
        acRecomendacao entity = recService.findById(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        recService.remove(entity);
        return Response.noContent().build();
    }

    /**
     * Pesquisa um recomendacao pelo ID
     *
     * @param id identificacao do recomendacao
     * @status 404 acRecomendacao nao encontrado
     * @status 304 acRecomendacao nao modificado
     * @status 200 acRecomendacao pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request, @Context HttpHeaders headers) {
        acRecomendacao entity;

        try {
            entity = recService.findById(id);
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
        Filter<acRecomendacao> filter = new Filter<>();
        acRecomendacao mercadoria = new acRecomendacao();
        filter.setEntity(mercadoria);

        filter.setFirst(startPosition).setPageSize(maxResult);
        final List<acRecomendacao> results = recService.paginate(filter);

        return Response.ok(results).header("count", recService.count(filter)).build();
    }

    /**
     * Retorna a quantidade de acRecomendacao
     */
    @GET
    @Path("count")
    public Response count() {
        Filter<acRecomendacao> filter = new Filter<>();
        acRecomendacao tc = new acRecomendacao();
        filter.setEntity(tc);
        return Response.ok(recService.count(filter)).build();
    }

    /**
     * @status 400 A descricao não pode ser vazia
     * @status 400 Nenhum recomendacao foi informado
     * @status 404 Não foi encontrato o recomendacao pelo ID
     * @status 409 O id passado difere do conteudo identificado no sistema
     * @status 204 acRecomendacao alterado com sucesso
     */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, acRecomendacao entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (recService.crud().eq("id", id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            recService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }

}
