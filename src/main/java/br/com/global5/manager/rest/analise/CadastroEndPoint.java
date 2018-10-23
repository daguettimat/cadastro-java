package br.com.global5.manager.rest.analise;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.analise.acCadastro;
import br.com.global5.manager.service.analise.CadastroService;


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
@Path("/analise/cadastro")
@Produces("application/json;charset=utf-8")
public class CadastroEndPoint {

    @Inject
    CadastroService cadService;

    /**
     * Cria um novo cadastro
     *
     * @status 400 A descricao do cadastro não pode ser vazia
     * @status 400 A descricao do cadastro deve ser unica
     * @status 201 O Tipo de contrato foi criado com sucesso
     */
    @POST
    @Consumes("application/json")
    public Response create(acCadastro entity) {
        cadService.insert(entity);
        return Response.created(UriBuilder.fromResource(CadastroEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    /**
     * Apagar um cadastro baseado no ID
     *
     * @param user nome do usuario habilitado no sistema
     * @param id   do cadastro
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 acCadastro não encontrado
     * @status 204 acCadastro apagado com sucesso
     */
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) {
        acCadastro entity = cadService.findById(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        cadService.remove(entity);
        return Response.noContent().build();
    }

    /**
     * Pesquisa um cadastro pelo ID
     *
     * @param id identificacao do cadastro
     * @status 404 acCadastro nao encontrado
     * @status 304 acCadastro nao modificado
     * @status 200 acCadastro pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request, @Context HttpHeaders headers) {
        acCadastro entity;

        try {
            entity = cadService.findById(id);
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
     * Pesquisa liberacao valida
     *
     * @param  CPF Motorista
     * @param  vinculo do Motorista
     * @param  placas dos veiculos
     * @status 404 Cadastro nao encontrado
     * @status 304 Cadastro nao modificado
     * @status 200 Cadastro pesquisado com sucesso
     */
    @GET
    @Path("/liberacao")
    public Response liberacao(@QueryParam("cliente") String cliente,
                              @QueryParam("cpf") String CPF,
                              @QueryParam("vinculo") String vinculo,
                              @QueryParam("placas") final List<String> placas,
                              @Context Request request,
                              @Context HttpHeaders headers) {
        Integer entity;
        //
        // Procura o conjunto completo
        //
        try {
            entity = cadService.findLiberacao(cliente, CPF, vinculo,placas);
        } catch (NoResultException nre) {
            entity = -1;
        }

        if (entity == -1) {
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
        Filter<acCadastro> filter = new Filter<>();
        acCadastro mercadoria = new acCadastro();
        filter.setEntity(mercadoria);

        filter.setFirst(startPosition).setPageSize(maxResult);
        final List<acCadastro> results = cadService.paginate(filter);

        return Response.ok(results).header("count", cadService.count(filter)).build();
    }

    /**
     * Retorna a quantidade de Cadastros
     */
    @GET
    @Path("count")
    public Response count() {
        Filter<acCadastro> filter = new Filter<>();
        acCadastro tc = new acCadastro();
        filter.setEntity(tc);
        return Response.ok(cadService.count(filter)).build();
    }

    /**
     * @status 400 A descricao não pode ser vazia
     * @status 400 Nenhum cadastro foi informado
     * @status 404 Não foi encontrato o cadastro pelo ID
     * @status 409 O id passado difere do conteudo identificado no sistema
     * @status 204 acCadastro alterado com sucesso
     */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, acCadastro entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if ( id.intValue() != entity.getId()) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (cadService.crud().eq("id", id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            cadService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }

}
