package br.com.global5.manager.rest.auxiliar;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.auxiliar.TipoUsuario;
import br.com.global5.manager.service.auxiliar.TipoUsuarioService;

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
@Path("/tipousuario")
@Produces("application/json;charset=utf-8")
public class TipoUsuarioEndPoint {

	@Inject
	TipoUsuarioService tcService;
	
    /**
     * Cria um novo tipo de usuario
     * @status 400 A descricao do tipo de usuario não pode ser vazia
     * @status 400 A descricao do tipo de usuario deve ser unica
     * @status 201 O Tipo de Usuário foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(TipoUsuario entity) {
    	tcService.insert(entity);
    	return Response.created(UriBuilder.fromResource(TipoUsuarioEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um tipo de usuario baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do tipo de usuario
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Tipo de Usuário não encontrado
     * @status 204 Tipo de Usuário apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	TipoUsuario entity = tcService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	tcService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um tipo de usuario pelo ID
     * @param id identificacao do tipo de usuario
     * @status 404 Tipo de Usuário nao encontrado
     * @status 304 Tipo de Usuário nao modificado
     * @status 200 Tipo de Usuário pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Integer id, @Context Request request,@Context HttpHeaders headers) {
        TipoUsuario entity;

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
     * @param startPosition id inicial da consulta
     * @param maxResult numero de elementos para recuperar
     * @param descricao lista os tipos pela descricao
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                          @QueryParam("max") @DefaultValue("10") Integer maxResult,
                          @QueryParam("descricao") String descricao) {
        Filter<TipoUsuario> filter = new Filter<>();
        TipoUsuario tipoContrato = new TipoUsuario();
        filter.setEntity(tipoContrato);
        if(descricao != null){
            filter.getEntity().descricao(descricao);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<TipoUsuario> results = tcService.paginate(filter);

       return Response.ok(results).header("count",tcService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Tipo de Usuário
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("descricao") String descricao) {
        Filter<TipoUsuario> filter = new Filter<>();
        TipoUsuario tc = new TipoUsuario();
        filter.setEntity(tc);
        if(descricao != null){
            filter.getEntity().descricao(descricao);
        }
        return Response.ok(tcService.count(filter)).build();
    }    
    
    /**
    * @status 400 A descricao não pode ser vazia
    * @status 400 Nenhum tipo de usuario foi informado
    * @status 404 Não foi encontrato o tipo de usuario pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Tipo de Usuário alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Integer id,  TipoUsuario entity) {
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
