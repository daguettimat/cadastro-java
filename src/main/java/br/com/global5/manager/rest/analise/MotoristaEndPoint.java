package br.com.global5.manager.rest.analise;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.analise.acMotorista;
import br.com.global5.manager.service.analise.MotoristaService;

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
@Path("/analise/motoristas")
@Produces("application/json;charset=utf-8")
public class MotoristaEndPoint {

    @Inject
    MotoristaService motService;

    /**
     * Cria um novo registro
     *
     * @status 201 O registro foi criado com sucesso
     */
    @POST
    @Consumes("application/json")
    public Response create(acMotorista entity) {
        motService.insert(entity);
        return Response.created(UriBuilder.fromResource(MotoristaEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    /**
     * Apagar um registro baseado no ID
     *
     * @param user nome do usuario habilitado no sistema
     * @param id   do registro
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 registro não encontrada
     * @status 204 registro apagado com sucesso
     */
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) {
        acMotorista entity = motService.findById(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        motService.remove(entity);
        return Response.noContent().build();
    }

    /**
     * Pesquisa um veiculos pelo ID
     *
     * @param id identificacao do veiculos
     * @status 404 Registro nao encontrado
     * @status 304 Registro nao modificado
     * @status 200 Registro pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request, @Context HttpHeaders headers) {
        acMotorista entity;

        try {
            entity = motService.findById(id);
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
     * Pesquisa pela placa do veiculo a liberacao
     *
     * @param cpf identificacao do veiculos
     * @status 404 Registro nao encontrado
     * @status 304 Registro nao modificado
     * @status 200 Registro pesquisado com sucesso
     */
    @GET
    @Path("liberacao/{cpf:[0-9][0-9]*}")
    public Response findLiberacao(@PathParam("cpf") String cpf, @Context Request request, @Context HttpHeaders headers) {
        Integer entity;

        // motoid = 306083
        // anacoid = 1269832
        // cpf motorista = 02096337194
        try {
            entity = motService.findLiberacao(cpf);

        } catch (NoResultException nre) {
            entity = null;
        }

        if (entity == 0) {
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
        Filter<acMotorista> filter = new Filter<>();
        acMotorista mercadoria = new acMotorista();
        filter.setEntity(mercadoria);

        filter.setFirst(startPosition).setPageSize(maxResult);
        final List<acMotorista> results = motService.paginate(filter);

        return Response.ok(results).header("count", motService.count(filter)).build();
    }

    /**
     * Retorna a quantidade de registros
     */
    @GET
    @Path("count")
    public Response count() {
        Filter<acMotorista> filter = new Filter<>();
        acMotorista tc = new acMotorista();
        filter.setEntity(tc);
        return Response.ok(motService.count(filter)).build();
    }

    /**
     * @status 400 Nenhum registro foi informado
     * @status 404 Não foi encontrada o registro pelo ID
     * @status 409 O id passado difere do conteudo identificado no sistema
     * @status 204 Registro alterada com sucesso
     */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, acMotorista entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (motService.crud().eq("id", id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            motService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }

}
