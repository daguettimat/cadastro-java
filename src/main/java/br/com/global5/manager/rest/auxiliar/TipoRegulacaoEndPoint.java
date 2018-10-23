package br.com.global5.manager.rest.auxiliar;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.auxiliar.TipoRegulacao;
import br.com.global5.manager.service.auxiliar.TipoRegulacaoService;

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
@Path("/tiporegulacao")
@Produces("application/json;charset=utf-8")
public class TipoRegulacaoEndPoint {

	@Inject
	TipoRegulacaoService regulacaoService;
	

    /**
     * Cria um novo Tipo de Regulacao
     * @status 400 A descricao do Tipo de Regulacao não pode ser vazia
     * @status 400 A descricao do Tipo de Regulacao deve ser unica
     * @status 201 O Tipo de Regulacao foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(TipoRegulacao entity) {
    	regulacaoService.insert(entity);
    	return Response.created(UriBuilder.fromResource(TipoRegulacaoEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um Tipo de Regulacao baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do Tipo de Regulacao
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Tipo de Regulacao não encontrado
     * @status 204 Tipo de Regulacao apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	TipoRegulacao entity = regulacaoService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	regulacaoService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um Tipo de Regulacao pelo ID
     * @param id identificacao do Tipo de Regulacao
     * @status 404 Tipo de Regulacao nao encontrado
     * @status 304 Tipo de Regulacao nao modificado
     * @status 200 Tipo de Regulacao pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        TipoRegulacao entity;

        try {
            entity = regulacaoService.findById(id);
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
     * @param startPosition posicao inicial da lista
     * @param maxResult numeros de elementos para busca
     * @param descricao pesquisa os registros pela descricao fornecida
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                          @QueryParam("max") @DefaultValue("10") Integer maxResult,
                          @QueryParam("descricao") String descricao) {
        Filter<TipoRegulacao> filter = new Filter<>();
        TipoRegulacao TipoRegulacao = new TipoRegulacao();
        filter.setEntity(TipoRegulacao);
        if(descricao != null){
            filter.getEntity().descricao(descricao);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<TipoRegulacao> results = regulacaoService.paginate(filter);

       return Response.ok(results).header("count",regulacaoService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Tipos de Contrato
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("descricao") String descricao) {
        Filter<TipoRegulacao> filter = new Filter<>();
        TipoRegulacao tc = new TipoRegulacao();
        filter.setEntity(tc);
        if(descricao != null){
            filter.getEntity().descricao(descricao);
        }
        return Response.ok(regulacaoService.count(filter)).build();
    }    
    
    /**
    * @status 400 A descricao não pode ser vazia
    * @status 400 Nenhum Tipo de Regulacao foi informado
    * @status 404 Não foi encontrato o Tipo de Regulacao pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Tipo de Regulacao alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  TipoRegulacao entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (regulacaoService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            regulacaoService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
