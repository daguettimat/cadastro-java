package br.com.global5.manager.rest.ect;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.ect.Cidade;
import br.com.global5.manager.service.ect.CidadeService;

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
@Path("/cidade")
@Produces("application/json;charset=utf-8")
public class CidadeEndPoint {

	@Inject
    CidadeService cidadeService;
	
    /**
     * Cria uma novo cidade
     * @status 400 O nome do cidade não pode ser vazio
     * @status 400 O nome do cidade deve ser unico
     * @status 201 A cidade foi criada com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Cidade entity) {
    	cidadeService.insert(entity);
    	return Response.created(UriBuilder.fromResource(CidadeEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um cidade baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do cidade
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Cidade não encontrado
     * @status 204 Cidade apagada com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	Cidade entity = cidadeService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	cidadeService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um cidade pelo ID
     * @param id identificacao do cidade
     * @status 404 Cidade nao encontrada
     * @status 304 Cidade nao modificada
     * @status 200 Cidade pesquisada com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        Cidade entity;

        try {
            entity = cidadeService.findById(id);
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
        Filter<Cidade> filter = new Filter<>();
        Cidade mercadoria = new Cidade();
        filter.setEntity(mercadoria);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Cidade> results = cidadeService.paginate(filter);

       return Response.ok(results).header("count",cidadeService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Cidades
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("nome") String nome) {
        Filter<Cidade> filter = new Filter<>();
        Cidade tc = new Cidade();
        filter.setEntity(tc);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
        return Response.ok(cidadeService.count(filter)).build();
    }    
    
    /**
    * @status 400 A nome da cidade não pode ser vazio
    * @status 400 Nenhum cidade foi informada
    * @status 404 Não foi encontrato a cidade pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Cidade alterada com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  Cidade entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (cidadeService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            cidadeService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
