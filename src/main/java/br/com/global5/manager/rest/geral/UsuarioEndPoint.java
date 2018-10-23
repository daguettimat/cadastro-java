package br.com.global5.manager.rest.geral;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.geral.UsuarioService;

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
@Path("/usuario")
@Produces("application/json;charset=utf-8")
public class UsuarioEndPoint {

	@Inject
	UsuarioService usuarioService;
	
    /**
     * Cria um novo usuario
     * @status 400 O nome do usuario não pode ser vazio
     * @status 400 O nome do usuario deve ser unico
     * @status 201 O Usuario foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Usuario entity) {
    	usuarioService.insert(entity);
    	return Response.created(UriBuilder.fromResource(UsuarioEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um usuario baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do usuario
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Usuario não encontrado
     * @status 204 Usuario apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	Usuario entity = usuarioService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	usuarioService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um usuario pelo ID
     * @param id identificacao do usuario
     * @status 404 Usuario nao encontrado
     * @status 304 Usuario nao modificado
     * @status 200 Usuario pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        Usuario entity;

        try {
            entity = usuarioService.findById(id);
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
        Filter<Usuario> filter = new Filter<>();
        Usuario mercadoria = new Usuario();
        filter.setEntity(mercadoria);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Usuario> results = usuarioService.paginate(filter);

       return Response.ok(results).header("count",usuarioService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Usuarios
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("nome") String nome) {
        Filter<Usuario> filter = new Filter<>();
        Usuario tc = new Usuario();
        filter.setEntity(tc);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
        return Response.ok(usuarioService.count(filter)).build();
    }    
    
    /**
    * @status 400 A nome não pode ser vazia
    * @status 400 Nenhum usuario foi informado
    * @status 404 Não foi encontrato o usuario pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Usuario alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  Usuario entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (usuarioService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            usuarioService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
