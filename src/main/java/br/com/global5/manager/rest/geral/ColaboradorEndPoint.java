package br.com.global5.manager.rest.geral;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.rest.RestSecured;
import br.com.global5.manager.model.geral.Colaborador;
import br.com.global5.manager.service.geral.ColaboradorService;

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
@Path("/colaborador")
@Produces("application/json;charset=utf-8")
public class ColaboradorEndPoint {

	@Inject
	ColaboradorService colService;
	
    /**
     * Cria um novo colaborador
     * @status 400 O nome do colaborador não pode ser vazio
     * @status 400 O nome do colaborador deve ser unico
     * @status 201 O Tipo de contrato foi criado com sucesso
     */
    @POST
    @Consumes("application/json")	
    public Response create(Colaborador entity) {
    	colService.insert(entity);
    	return Response.created(UriBuilder.fromResource(ColaboradorEndPoint.class).path(String.valueOf(entity.getId())).build()).build();
    }
    
    /**
     * Apagar um colaborador baseado no ID
     * @param user nome do usuario habilitado no sistema
     * @param id do colaborador
     * @status 401 somente usuarios autorizados podem acessar essa funcao
     * @status 403 somente usuarios autenticados podem acessar essa funcao
     * @status 404 Colaborador não encontrado
     * @status 204 Colaborador apagado com sucesso
     */    
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @RestSecured
    public Response deleteById(@HeaderParam("user") String user, @PathParam("id") Long id) {
    	Colaborador entity = colService.findById(id);
    	if( entity == null ) {
    		return Response.status(Status.NOT_FOUND).build();
        }
    	colService.remove(entity);
    	return Response.noContent().build();
    }
    
    /**
     * Pesquisa um colaborador pelo ID
     * @param id identificacao do colaborador
     * @status 404 Colaborador nao encontrado
     * @status 304 Colaborador nao modificado
     * @status 200 Colaborador pesquisado com sucesso
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response findById(@PathParam("id") Long id, @Context Request request,@Context HttpHeaders headers) {
        Colaborador entity;

        try {
            entity = colService.findById(id);
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
        Filter<Colaborador> filter = new Filter<>();
        Colaborador colaborador = new Colaborador();
        filter.setEntity(colaborador);
        if(nome != null){
            filter.getEntity().Nome(nome);
        }
       filter.setFirst(startPosition).setPageSize(maxResult);
       final List<Colaborador> results = colService.paginate(filter);

       return Response.ok(results).header("count",colService.count(filter)).build();
    }

    /**
     * Retorna a quantidade de Colaboradores
     */
    @GET
    @Path("count")
    public Response count(@QueryParam("nome") String nome) {
        Filter<Colaborador> filter = new Filter<>();
        Colaborador colaborador = new Colaborador();
        filter.setEntity(colaborador);
        if(nome != null){
            filter.getEntity().Nome(nome);
        }
        return Response.ok(colService.count(filter)).build();
    }

    /**
    * @status 400 O nome não pode ser vazia
    * @status 400 Nenhum colaborador foi informado
    * @status 404 Não foi encontrato o colaborador pelo ID
    * @status 409 O id passado difere do conteudo identificado no sistema
    * @status 204 Colaborador alterado com sucesso
    */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id,  Colaborador entity) {
        if (entity == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (!id.equals(entity.getId())) {
            return Response.status(Status.CONFLICT).entity(entity).build();
        }
        if (colService.crud().eq("id",id).count() == 0) {
            return Response.status(Status.NOT_FOUND).build();
        }
        try {
            colService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }    
    
}
