package br.com.global5.manager.rest.geral;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.geral.Mercadoria;
import br.com.global5.manager.service.geral.MercadoriaService;

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
@Path("/mercadoria")
@Produces("application/json;charset=utf-8")
public class MercadoriaEndPoint {

	@Inject
	MercadoriaService mService;
	
    /**
     * Cria um novo tipo de mercadoria
     * @status 400 A descricao do tipo de mercadoria não pode ser vazia
     * @status 400 A descricao do tipo de mercadoria deve ser unica
     * @status 201 O Tipo de contrato foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Mercadoria entity) {
    	mService.insert(entity);
    	return Response.created(UriBuilder.fromResource(MercadoriaEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um tipo de mercadoria baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do tipo de mercadoria
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Tipo de Mercadoria não encontrado
     * @status 204 Tipo de Mercadoria apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	Mercadoria entity = mService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	mService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um tipo de mercadoria pelo ID
     * @param id identificacao do tipo de mercadoria
     * @status 404 Tipo de Mercadoria nao encontrado
     * @status 304 Tipo de Mercadoria nao modificado
     * @status 200 Tipo de Mercadoria pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Integer id, @Context Request request,@Context HttpHeaders headers) {
        Mercadoria entity;

        try {
            entity = mService.findById(id);
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
     * @param descricao busca na lista por um nome ou parte dele
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                          @QueryParam("max") @DefaultValue("10") Integer maxResult,
                          @QueryParam("descricao") String descricao) {
        Filter<Mercadoria> filter = new Filter<>();
        Mercadoria mercadoria = new Mercadoria();
        filter.setEntity(mercadoria);
        if(descricao != null){
            filter.getEntity().descricao(descricao);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Mercadoria> results = mService.paginate(filter);

       return Response.ok(results).header("count",mService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Mercadorias
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("descricao") String descricao) {
        Filter<Mercadoria> filter = new Filter<>();
        Mercadoria tc = new Mercadoria();
        filter.setEntity(tc);
        if(descricao != null){
            filter.getEntity().descricao(descricao);
        }
        return Response.ok(mService.count(filter)).build();
    }    
    
    /**
    * @status 400 A descricao não pode ser vazia
    * @status 400 Nenhum tipo de mercadoria foi informado
    * @status 404 Não foi encontrato o tipo de mercadoria pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Tipo de Mercadoria alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Integer id,  Mercadoria entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (mService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            mService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
