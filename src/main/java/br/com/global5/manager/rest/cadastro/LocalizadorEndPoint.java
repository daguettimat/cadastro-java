package br.com.global5.manager.rest.cadastro;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.cadastro.Localizador;
import br.com.global5.manager.service.cadastro.LocalizadorService;

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
@Path("/endereco")
@Produces("application/json;charset=utf-8")
public class LocalizadorEndPoint {

	@Inject
	LocalizadorService locService;
	
    /**
     * Cria um novo localizador
     * @status 400 A logradouro do localizador não pode ser vazia
     * @status 400 A logradouro do localizador deve ser unica
     * @status 201 O Localizador foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Localizador entity) {
    	locService.insert(entity);
    	return Response.created(UriBuilder.fromResource(LocalizadorEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um localizador baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do localizador
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Localizador não encontrado
     * @status 204 Localizador apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	Localizador entity = locService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	locService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um localizador pelo ID
     * @param id identificacao do localizador
     * @status 404 Localizador nao encontrado
     * @status 304 Localizador nao modificado
     * @status 200 Localizador pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        Localizador entity;

        try {
            entity = locService.findById(id);
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
     * @param logradouro busca na lista por um nome ou parte dele
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                          @QueryParam("max") @DefaultValue("10") Integer maxResult,
                          @QueryParam("logradouro") String logradouro) {
        Filter<Localizador> filter = new Filter<>();
        Localizador localizador = new Localizador();
        filter.setEntity(localizador);
        if(logradouro != null){
            filter.getEntity().nome(logradouro);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Localizador> results = locService.paginate(filter);

       return Response.ok(results).header("count",locService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Localizadors
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("logradouro") String logradouro) {
        Filter<Localizador> filter = new Filter<>();
        Localizador tc = new Localizador();
        filter.setEntity(tc);
        if(logradouro != null){
            filter.getEntity().nome(logradouro);
        }
        return Response.ok(locService.count(filter)).build();
    }    
    
    /**
    * @status 400 A logradouro não pode ser vazia
    * @status 400 Nenhum localizador foi informado
    * @status 404 Não foi encontrato o localizador pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Localizador alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  Localizador entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (locService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            locService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
