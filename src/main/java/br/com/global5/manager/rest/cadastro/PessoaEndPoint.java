package br.com.global5.manager.rest.cadastro;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.cadastro.Pessoa;
import br.com.global5.manager.service.cadastro.PessoaService;

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
@Path("/pessoa")
@Produces("application/json;charset=utf-8")
public class PessoaEndPoint {

	@Inject
	PessoaService locService;
	
    /**
     * Cria uma nova pessoa
     * @status 400 A pessoa não pode ser vazia
     * @status 400 A pessoa deve ser unica
     * @status 201 O ID foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Pessoa entity) {
    	locService.insert(entity);
    	return Response.created(UriBuilder.fromResource(PessoaEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um pessoa baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do pessoa
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Pessoa não encontrada
     * @status 204 Pessoa apagada com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	Pessoa entity = locService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	locService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um pessoa pelo ID
     * @param id identificacao do pessoa
     * @status 404 Pessoa nao encontrada
     * @status 304 Pessoa nao modificada
     * @status 200 Pessoa pesquisada com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        Pessoa entity;

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
     * @param pessoa busca na lista por um nome ou parte dele
     */
    @GET
    public Response list(@QueryParam("start") @DefaultValue("0") Integer startPosition,
                          @QueryParam("max") @DefaultValue("10") Integer maxResult,
                          @QueryParam("pessoa") String pessoa) {
        Filter<Pessoa> filter = new Filter<>();
        Pessoa pes = new Pessoa();
        filter.setEntity(pes);
        if(pessoa != null){
            filter.getEntity().nome(pessoa);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Pessoa> results = locService.paginate(filter);

       return Response.ok(results).header("count",locService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Pessoas
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("pessoa") String pessoa) {
        Filter<Pessoa> filter = new Filter<>();
        Pessoa tc = new Pessoa();
        filter.setEntity(tc);
        if(pessoa != null){
            filter.getEntity().nome(pessoa);
        }
        return Response.ok(locService.count(filter)).build();
    }    
    
    /**
    * @status 400 A pessoa não pode ser vazia
    * @status 400 Nenhum pessoa foi informada
    * @status 404 Não foi encontrato o pessoa pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Pessoa alterada com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  Pessoa entity) {
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
