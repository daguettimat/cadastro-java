package br.com.global5.manager.rest.areas;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.service.areas.AreaService;

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
@Path("/areas")
@Produces("application/json;charset=utf-8")
public class AreaEndPoint {

	@Inject
    AreaService bcoService;
	
    /**
     * Cria um novo area
     * @status 400 O nome da area não pode ser vazia
     * @status 400 O nome da area deve ser unica
     * @status 201 A area foi criada com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Area entity) {
    	bcoService.insert(entity);
    	return Response.created(UriBuilder.fromResource(AreaEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um area baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do area
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Area não encontrada
     * @status 204 Area apagada com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	Area entity = bcoService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	bcoService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um area pelo ID
     * @param id identificacao do area
     * @status 404 Area nao encontrada
     * @status 304 Area nao modificada
     * @status 200 Area pesquisada com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        Area entity;

        try {
            entity = bcoService.findById(id);
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
        Filter<Area> filter = new Filter<>();
        Area mercadoria = new Area();
        filter.setEntity(mercadoria);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Area> results = bcoService.paginate(filter);

       return Response.ok(results).header("count",bcoService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Areas
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("nome") String nome) {
        Filter<Area> filter = new Filter<>();
        Area tc = new Area();
        filter.setEntity(tc);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
        return Response.ok(bcoService.count(filter)).build();
    }    
    
    /**
    * @status 400 O nome não pode ser vazio
    * @status 400 Nenhuma area foi informada
    * @status 404 Não foi encontrato a area pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Area alterada com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  Area entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (bcoService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            bcoService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
