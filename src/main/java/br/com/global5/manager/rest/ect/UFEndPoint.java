package br.com.global5.manager.rest.ect;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.ect.UF;
import br.com.global5.manager.service.ect.UFService;

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
@Path("/uf")
@Produces("application/json;charset=utf-8")
public class UFEndPoint {

	@Inject
    UFService ufService;
	
    /**
     * Cria um novo uf
     * @status 400 O nome do estado não pode ser vazio
     * @status 400 O nome do estado deve ser unico
     * @status 201 O Tipo de contrato foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(UF entity) {
    	ufService.insert(entity);
    	return Response.created(UriBuilder.fromResource(UFEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um uf baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do uf
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 UF não encontrado
     * @status 204 UF apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	UF entity = ufService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	ufService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um uf pelo ID
     * @param id identificacao do uf
     * @status 404 UF nao encontrado
     * @status 304 UF nao modificado
     * @status 200 UF pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        UF entity;

        try {
            entity = ufService.findById(id);
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
        Filter<UF> filter = new Filter<>();
        UF mercadoria = new UF();
        filter.setEntity(mercadoria);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<UF> results = ufService.paginate(filter);

       return Response.ok(results).header("count",ufService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de UFs
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("nome") String nome) {
        Filter<UF> filter = new Filter<>();
        UF tc = new UF();
        filter.setEntity(tc);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
        return Response.ok(ufService.count(filter)).build();
    }    
    
    /**
    * @status 400 A nome não pode ser vazia
    * @status 400 Nenhum uf foi informado
    * @status 404 Não foi encontrato o uf pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 UF alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  UF entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (ufService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            ufService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
