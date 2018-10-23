package br.com.global5.manager.rest.geral;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.geral.Motorista;
import br.com.global5.manager.service.geral.MotoristaService;

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
@Path("/motorista")
@Produces("application/json;charset=utf-8")
public class MotoristaEndPoint {

	@Inject
	MotoristaService motoristaService;
	
    /**
     * Cria um novo motorista
     * @status 400 O nome do motorista não pode ser vazio
     * @status 400 O nome do motorista deve ser unico
     * @status 201 O acMotorista foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Motorista entity) {
    	motoristaService.insert(entity);
    	return Response.created(UriBuilder.fromResource(MotoristaEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um motorista baseado no ID
     * @param user nome do motorista habilitado no sistema
     * @param id do motorista
     * @status 401 somente motoristas autorizados podem acessar essa funcao
     * @status 403 somente motoristas autenticados podem acessar essa funcao
     * @status 404 acMotorista não encontrado
     * @status 204 acMotorista apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Integer id) { 
    	Motorista entity = motoristaService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	motoristaService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um motorista pelo ID
     * @param id identificacao do motorista
     * @status 404 acMotorista nao encontrado
     * @status 304 acMotorista nao modificado
     * @status 200 acMotorista pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        Motorista entity;

        try {
            entity = motoristaService.findById(id);
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
     * Pesquisa um motorista pelo ID
     * @param cpf identificacao do motorista
     * @status 404 acMotorista nao encontrado
     * @status 304 acMotorista nao modificado
     * @status 200 acMotorista pesquisado com sucesso
     */
    @GET
    @Path("/cpf/{cpf:[0-9][0-9]*}")
    public Response findByCPF(@PathParam("cpf") String cpf, @Context Request request,@Context HttpHeaders headers) {
        Motorista entity;

        try {
            entity = motoristaService.getCPF(cpf);
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
        Filter<Motorista> filter = new Filter<>();
        Motorista mercadoria = new Motorista();
        filter.setEntity(mercadoria);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Motorista> results = motoristaService.paginate(filter);

       return Response.ok(results).header("count",motoristaService.count(filter)).build();
    }  
    
    /**
     * Retorna a quantidade de Motoristas
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("nome") String nome) {
        Filter<Motorista> filter = new Filter<>();
        Motorista tc = new Motorista();
        filter.setEntity(tc);
        if(nome != null){
            filter.getEntity().nome(nome);
        }
        return Response.ok(motoristaService.count(filter)).build();
    }    
    
    /**
    * @status 400 A nome não pode ser vazia
    * @status 400 Nenhum motorista foi informado
    * @status 404 Não foi encontrato o motorista pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 acMotorista alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  Motorista entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (motoristaService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            motoristaService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
