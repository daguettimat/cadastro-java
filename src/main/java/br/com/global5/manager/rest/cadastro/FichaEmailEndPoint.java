package br.com.global5.manager.rest.cadastro;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.cadastro.FichaEmail;
import br.com.global5.manager.service.cadastro.FichaEmailService;

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
@Path("/ficha/email")
@Produces("application/json;charset=utf-8")
public class FichaEmailEndPoint {

    @Inject
    FichaEmailService fichaEmailService;

    /**
     * Cria um novo registro
     *
     * @status 400 O cadastro deve ser preenchido
     * @status 400 O cadastro deve ser unico
     * @status 201 O ID foi criado com sucesso
     */
    @POST
    @Consumes("application/json")
    public Response create(FichaEmail entity) {
        fichaEmailService.insert(entity);
        return Response.created(UriBuilder.fromResource(FichaEmailEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    /**
     * Apagar um registro baseado no ID
     *
     * @param user nome do usuario habilitado no sistema
     * @param id   do sistema
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 registro não encontrado
     * @status 204 registro apagado com sucesso
     */
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) {
        FichaEmail entity = fichaEmailService.findById(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        fichaEmailService.remove(entity);
        return Response.noContent().build();
    }

    /**
     * Pesquisa um registro pelo ID
     *
     * @param id identificacao do registro
     * @status 404 Registro nao encontrado
     * @status 304 Registro nao modificado
     * @status 200 Registro pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request, @Context HttpHeaders headers) {
        FichaEmail entity;

        try {
            entity = fichaEmailService.findById(id);
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
     * @param documento     busca na lista por um documento ou parte dele
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                         @QueryParam("max") @DefaultValue("10") Integer maxResult,
                         @QueryParam("documento") String documento) {
        Filter<FichaEmail> filter = new Filter<>();
        FichaEmail pes = new FichaEmail();
        filter.setEntity(pes);

        filter.setFirst(startPosition).setPageSize(maxResult);
        final List<FichaEmail> results = fichaEmailService.paginate(filter);

        return Response.ok(results).header("count", fichaEmailService.count(filter)).build();
    }

    /**
     * Retorna a quantidade de registros
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("documento") String documento) {
        Filter<FichaEmail> filter = new Filter<>();
        FichaEmail tc = new FichaEmail();
        filter.setEntity(tc);

        return Response.ok(fichaEmailService.count(filter)).build();
    }

    /**
     * @status 400 O registro nao pode vazio
     * @status 400 Nenhum registro foi informado
     * @status 404 Não foi encontrato o registro pelo ID
     * @status 409 O id passado difere do conteudo identificado no sistema
     * @status 204 Registro alterado com sucesso
     */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, FichaEmail entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (fichaEmailService.crud().eq("id", id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            fichaEmailService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }

}
