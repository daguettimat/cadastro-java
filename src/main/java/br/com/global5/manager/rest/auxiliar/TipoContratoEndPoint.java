package br.com.global5.manager.rest.auxiliar;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.auxiliar.TipoContrato;
import br.com.global5.manager.service.auxiliar.TipoContratoService;

/**
*
*/
@Path("/tipocontrato")
@Produces("application/json;charset=utf-8")
public class TipoContratoEndPoint {

	@Inject
	TipoContratoService tcService;
	
    /**
     * Cria um novo tipo de contrato
     * @status 400 A descricao do tipo de contrato não pode ser vazia
     * @status 400 A descricao do tipo de contrato deve ser unica
     * @status 201 O Tipo de contrato foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(TipoContrato entity) {
    	tcService.insert(entity);
    	return Response.created(UriBuilder.fromResource(TipoContratoEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um tipo de contrato baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do tipo de contrato
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Tipo de Contrato não encontrado
     * @status 204 Tipo de Contrato apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	TipoContrato entity = tcService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	tcService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um tipo de contrato pelo ID
     * @param id identificacao do tipo de contrato
     * @status 404 Tipo de Contrato nao encontrado
     * @status 304 Tipo de Contrato nao modificado
     * @status 200 Tipo de Contrato pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Integer id, @Context Request request,@Context HttpHeaders headers) {
        TipoContrato entity;

        try {
            entity = tcService.findById(id);
        } catch (NoResultException nre) {
            entity = null;
        }

        if(entity == null){
            return Response.status(Status.NOT_FOUND).build();
        }

        CacheControl cc = new CacheControl();
        cc.setMaxAge(100);
        EntityTag tag = new EntityTag(Integer.toString(entity.hashCode()));
        Response.ResponseBuilder builder =  request.evaluatePreconditions(tag);
        if(builder != null){
            builder.cacheControl(cc);
            return builder.build();
        }
        builder = Response.ok(entity);
        builder.cacheControl(cc);
        builder.tag(tag);
        return builder.build();
    }  
    
    /**
     * @param startPosition posicao inicial de pesquisa
     * @param maxResult numero máximo de resultados
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                          @QueryParam("max") @DefaultValue("10") Integer maxResult,
                          @QueryParam("descricao") String descricao) {
        Filter<TipoContrato> filter = new Filter<>();
        TipoContrato tipoContrato = new TipoContrato();
        filter.setEntity(tipoContrato);
        if(descricao != null){
            filter.getEntity().descricao(descricao);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<TipoContrato> results = tcService.paginate(filter);

       return Response.ok(results).header("count",tcService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Tipos de Contrato
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("descricao") String descricao) {
        Filter<TipoContrato> filter = new Filter<>();
        TipoContrato tc = new TipoContrato();
        filter.setEntity(tc);
        if(descricao != null){
            filter.getEntity().descricao(descricao);
        }
        return Response.ok(tcService.count(filter)).build();
    }    
    
    /**
    * @status 400 A descricao não pode ser vazia
    * @status 400 Nenhum tipo de contrato foi informado
    * @status 404 Não foi encontrato o tipo de contrato pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Tipo de Contrato alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Integer id,  TipoContrato entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (tcService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            tcService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
