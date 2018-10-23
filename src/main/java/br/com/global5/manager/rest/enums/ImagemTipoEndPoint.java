package br.com.global5.manager.rest.enums;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.enums.ImagemTipo;
import br.com.global5.manager.service.enums.ImagemTipoService;

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
@Path("/enums/imagem/tipo")
@Produces("application/json;charset=utf-8")
public class ImagemTipoEndPoint {

	@Inject
    ImagemTipoService ficService;
	
    /**
     * Cria um novo registro
     * @status 400 O cadastro deve ser preenchido
     * @status 400 O cadastro deve ser unico
     * @status 201 O ID foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(ImagemTipo entity) {
    	ficService.insert(entity);
    	return Response.created(UriBuilder.fromResource(ImagemTipoEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um registro baseado no ID
     * @param user descricao do usuario habilitado no sistema
     * @param id do sistema
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 registro não encontrado
     * @status 204 registro apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	ImagemTipo entity = ficService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	ficService.remove(entity);
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
        ImagemTipo entity;

        try {
            entity = ficService.findById(id);
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
     * @param sistema busca na lista por um descricao ou parte dele
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                          @QueryParam("max") @DefaultValue("10") Integer maxResult,
                          @QueryParam("sistema") String sistema) {
        Filter<ImagemTipo> filter = new Filter<>();
        ImagemTipo pes = new ImagemTipo();
        filter.setEntity(pes);
        if(sistema != null){
            filter.getEntity().descricao(sistema);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<ImagemTipo> results = ficService.paginate(filter);

       return Response.ok(results).header("count",ficService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de registros
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("sistema") String sistema) {
        Filter<ImagemTipo> filter = new Filter<>();
        ImagemTipo tc = new ImagemTipo();
        filter.setEntity(tc);
        if(sistema != null){
            filter.getEntity().descricao(sistema);
        }
        return Response.ok(ficService.count(filter)).build();
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
    public Response update(@PathParam("id") Long id,  ImagemTipo entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (ficService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            ficService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
