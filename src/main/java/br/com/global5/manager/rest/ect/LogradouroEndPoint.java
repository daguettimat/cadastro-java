package br.com.global5.manager.rest.ect;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.ect.Logradouro;
import br.com.global5.manager.service.ect.LogradouroService;

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
@Path("/logradouro")
@Produces("application/json;charset=utf-8")
public class LogradouroEndPoint {

	@Inject
    LogradouroService logrService;
	
    /**
     * Cria um novo logr
     * @status 400 O nome nao pode ser vazio
     * @status 400 O nome deve ser unico
     * @status 201 A inserção ocorreu com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Logradouro entity) {
    	logrService.insert(entity);
    	return Response.created(UriBuilder.fromResource(LogradouroEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um registro baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do registro
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 registro não encontrado
     * @status 204 registro apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	Logradouro entity = logrService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	logrService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um registro pelo ID
     * @param id identificacao do logr
     * @status 404 Registro nao encontrado
     * @status 304 Registro nao modificado
     * @status 200 Registro pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        Logradouro entity;

        try {
            entity = logrService.findById(id);
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
     * Pesquisa um registro pelo ID
     * @param cep que sera buscado pela query
     * @status 404 Registro nao encontrado
     * @status 304 Registro nao modificado
     * @status 200 Registro pesquisado com sucesso
     */
    @GET
    @Path("/{cep:[0-9][0-9]*}")
    public Response findByCep(@PathParam("cep") String cep, @Context Request request,@Context HttpHeaders headers) {
        Filter<Logradouro> filter = new Filter<>();
        Logradouro logr = new Logradouro();
        filter.setEntity(logr);

        final Logradouro results = logrService.getCep(cep);

        return Response.ok(results).header("count",1).build();
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
        Filter<Logradouro> filter = new Filter<>();
        Logradouro logr = new Logradouro();
        filter.setEntity(logr);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Logradouro> results = logrService.paginate(filter);

       return Response.ok(results).header("count",logrService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de registros
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("nome") String nome) {
        Filter<Logradouro> filter = new Filter<>();
        Logradouro tc = new Logradouro();
        filter.setEntity(tc);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
        return Response.ok(logrService.count(filter)).build();
    }    
    
    /**
    * @status 400 O nome não pode ser vazio
    * @status 400 Nenhum id de registro foi informado
    * @status 404 Não foi encontrato o registro pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Registro alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  Logradouro entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (logrService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            logrService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
