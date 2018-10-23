package br.com.global5.manager.rest.cadastro;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.cadastro.Modelo;
import br.com.global5.manager.service.cadastro.ModeloService;

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
@Path("/modelo")
@Produces("application/json;charset=utf-8")
public class ModeloEndPoint {

	@Inject
    ModeloService modeloService;
	
    /**
     * Cria um novo registro
     * @status 400 O cadastro deve ser preenchido
     * @status 400 O cadastro deve ser unico
     * @status 201 O ID foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Modelo entity) {
    	modeloService.insert(entity);
    	return Response.created(UriBuilder.fromResource(ModeloEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um registro baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id da modelo
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 registro não encontrado
     * @status 204 registro apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	Modelo entity = modeloService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	modeloService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um registro pelo ID
     * @param id identificacao do registro
     * @status 404 Registro nao encontrado
     * @status 304 Registro nao modificado
     * @status 200 Registro pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        Modelo entity;

        try {
            entity = modeloService.findById(id);
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
     * @param modelo busca na lista por um nome ou parte dele
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                          @QueryParam("max") @DefaultValue("10") Integer maxResult,
                          @QueryParam("modelo") String modelo) {
        Filter<Modelo> filter = new Filter<>();
        Modelo pes = new Modelo();
        filter.setEntity(pes);
        if(modelo != null){
            filter.getEntity().Nome(modelo);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Modelo> results = modeloService.paginate(filter);

       return Response.ok(results).header("count",modeloService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de registros
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("modelo") String modelo) {
        Filter<Modelo> filter = new Filter<>();
        Modelo tc = new Modelo();
        filter.setEntity(tc);
        if(modelo != null){
            filter.getEntity().Nome(modelo);
        }
        return Response.ok(modeloService.count(filter)).build();
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
    public Response update(@PathParam("id") Long id,  Modelo entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (modeloService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            modeloService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
