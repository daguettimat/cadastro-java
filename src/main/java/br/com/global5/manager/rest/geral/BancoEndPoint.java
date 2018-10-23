package br.com.global5.manager.rest.geral;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.geral.Banco;
import br.com.global5.manager.service.geral.BancoService;

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
@Path("/banco")
@Produces("application/json;charset=utf-8")
public class BancoEndPoint {

	@Inject
	BancoService bcoService;
	
    /**
     * Cria um novo banco
     * @status 400 A descricao do banco não pode ser vazia
     * @status 400 A descricao do banco deve ser unica
     * @status 201 O Tipo de contrato foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Banco entity) {
    	bcoService.insert(entity);
    	return Response.created(UriBuilder.fromResource(BancoEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um banco baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do banco
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Banco não encontrado
     * @status 204 Banco apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	Banco entity = bcoService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	bcoService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um banco pelo ID
     * @param id identificacao do banco
     * @status 404 Banco nao encontrado
     * @status 304 Banco nao modificado
     * @status 200 Banco pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        Banco entity;

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
     * @param descricao busca na lista por um nome ou parte dele
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                          @QueryParam("max") @DefaultValue("10") Integer maxResult,
                          @QueryParam("descricao") String descricao) {
        Filter<Banco> filter = new Filter<>();
        Banco mercadoria = new Banco();
        filter.setEntity(mercadoria);
        if(descricao != null){
            filter.getEntity().descricao(descricao);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Banco> results = bcoService.paginate(filter);

       return Response.ok(results).header("count",bcoService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Bancos
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("descricao") String descricao) {
        Filter<Banco> filter = new Filter<>();
        Banco tc = new Banco();
        filter.setEntity(tc);
        if(descricao != null){
            filter.getEntity().descricao(descricao);
        }
        return Response.ok(bcoService.count(filter)).build();
    }    
    
    /**
    * @status 400 A descricao não pode ser vazia
    * @status 400 Nenhum banco foi informado
    * @status 404 Não foi encontrato o banco pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Banco alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  Banco entity) {
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
