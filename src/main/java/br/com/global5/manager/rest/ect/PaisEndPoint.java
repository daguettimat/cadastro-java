package br.com.global5.manager.rest.ect;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.ect.Pais;
import br.com.global5.manager.service.ect.PaisService;

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
@Path("/pais")
@Produces("application/json;charset=utf-8")
public class PaisEndPoint {

	@Inject
    PaisService paisService;
	
    /**
     * Cria um novo pais
     * @status 400 A nome do pais não pode ser vazia
     * @status 400 A nome do pais deve ser unica
     * @status 201 O Tipo de contrato foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Pais entity) {
    	paisService.insert(entity);
    	return Response.created(UriBuilder.fromResource(PaisEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um pais baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do pais
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Pais não encontrado
     * @status 204 Pais apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	Pais entity = paisService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	paisService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um pais pelo ID
     * @param id identificacao do pais
     * @status 404 Pais nao encontrado
     * @status 304 Pais nao modificado
     * @status 200 Pais pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        Pais entity;

        try {
            entity = paisService.findById(id);
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
        Filter<Pais> filter = new Filter<>();
        Pais mercadoria = new Pais();
        filter.setEntity(mercadoria);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Pais> results = paisService.paginate(filter);

       return Response.ok(results).header("count",paisService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Paiss
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("nome") String nome) {
        Filter<Pais> filter = new Filter<>();
        Pais tc = new Pais();
        filter.setEntity(tc);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
        return Response.ok(paisService.count(filter)).build();
    }    
    
    /**
    * @status 400 A nome não pode ser vazia
    * @status 400 Nenhum pais foi informado
    * @status 404 Não foi encontrato o pais pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Pais alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  Pais entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (paisService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            paisService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
