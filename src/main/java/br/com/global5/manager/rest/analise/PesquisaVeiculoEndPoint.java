package br.com.global5.manager.rest.analise;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.analise.acPesquisaVeiculo;
import br.com.global5.manager.service.analise.PesquisaVeiculoService;

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
@Path("/analise/pesquisa/veiculo")
@Produces("application/json;charset=utf-8")
public class PesquisaVeiculoEndPoint {

    @Inject
    PesquisaVeiculoService veiculoService;

    /**
     * Cria um novo registro
     *
     * @status 201 O registro foi criado com sucesso
     */
    @POST
    @Consumes("application/json")
    public Response create(acPesquisaVeiculo entity) {
        veiculoService.insert(entity);
        return Response.created(UriBuilder.fromResource(PesquisaVeiculoEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
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
        acPesquisaVeiculo entity = veiculoService.findById(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        veiculoService.remove(entity);
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
        acPesquisaVeiculo entity;

        try {
            entity = veiculoService.findById(id);
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
        Filter<acPesquisaVeiculo> filter = new Filter<>();
        acPesquisaVeiculo mercadoria = new acPesquisaVeiculo();
        filter.setEntity(mercadoria);

        filter.setFirst(startPosition).setPageSize(maxResult);
        final List<acPesquisaVeiculo> results = veiculoService.paginate(filter);

        return Response.ok(results).header("count", veiculoService.count(filter)).build();
    }

    /**
     * Retorna a quantidade de registros
     */
    @GET
    @Path("count")
    public Response count() {
        Filter<acPesquisaVeiculo> filter = new Filter<>();
        acPesquisaVeiculo tc = new acPesquisaVeiculo();
        filter.setEntity(tc);
        return Response.ok(veiculoService.count(filter)).build();
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
    public Response update(@PathParam("id") Long id, acPesquisaVeiculo entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (veiculoService.crud().eq("id", id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            veiculoService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }

}