package br.com.global5.manager.rest.areas;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.areas.AreaFuncao;
import br.com.global5.manager.model.areas.AreaFuncao;
import br.com.global5.manager.service.areas.AreaFuncaoService;
import br.com.global5.manager.service.areas.AreaFuncaoService;


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
@Path("/areas/funcao")
@Produces("application/json;charset=utf-8")
public class AreaFuncaoEndPoint {

	@Inject
    AreaFuncaoService funcaoService;
	
    /**
     * Cria um novo funcao
     * @status 400 O nome da funcao não pode ser vazia
     * @status 400 O nome da funcao deve ser unica
     * @status 201 A funcao foi criada com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(AreaFuncao entity) {
    	funcaoService.insert(entity);
    	return Response.created(UriBuilder.fromResource(AreaFuncaoEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um funcao baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id da funcao
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 AreaFuncao não encontrada
     * @status 204 AreaFuncao apagada com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	AreaFuncao entity = funcaoService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	funcaoService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um funcao pelo ID
     * @param id identificacao do funcao
     * @status 404 AreaFuncao nao encontrada
     * @status 304 AreaFuncao nao modificada
     * @status 200 AreaFuncao pesquisada com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        AreaFuncao entity;

        try {
            entity = funcaoService.findById(id);
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
     * @param startPosition posicao inicial da lista desejada
     * @param maxResult numero máximo de elementos no retorno
     * @param nome busca na lista por um nome ou parte dele
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                          @QueryParam("max") @DefaultValue("10") Integer maxResult,
                          @QueryParam("nome") String nome) {
        Filter<AreaFuncao> filter = new Filter<>();
        AreaFuncao mercadoria = new AreaFuncao();
        filter.setEntity(mercadoria);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<AreaFuncao> results = funcaoService.paginate(filter);

       return Response.ok(results).header("count",funcaoService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de AreaFuncaos
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("nome") String nome) {
        Filter<AreaFuncao> filter = new Filter<>();
        AreaFuncao tc = new AreaFuncao();
        filter.setEntity(tc);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
        return Response.ok(funcaoService.count(filter)).build();
    }    
    
    /**
    * @status 400 O nome não pode ser vazio
    * @status 400 Nenhuma funcao foi informada
    * @status 404 Não foi encontrato a funcao pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 AreaFuncao alterada com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  AreaFuncao entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (funcaoService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            funcaoService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
