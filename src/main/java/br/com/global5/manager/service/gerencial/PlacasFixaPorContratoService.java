package br.com.global5.manager.service.gerencial;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.gerencial.PlacasFixaPorContrato;

public class PlacasFixaPorContratoService extends CrudService<PlacasFixaPorContrato>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 202120021017L;
	
    @Override
    public Criteria configPagination(Filter<PlacasFixaPorContrato> filter) {

        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	
    public boolean findPlaca(String placaConsultar , Integer idCliente){
    	
    	EntityManager em = this.crud().getEntityManager();
    	
    	String queryStr = " select pcv_placafixa from tmp_placasfixas "
    			+ "	where pcv_idcliente  = " + idCliente + " and " 
    			+ "       pcv_placafixa  ='" + placaConsultar + "';";
    	// em.createNativeQuery(query, "ListaPlacasFixaPorContratoMapping").getResultList();
    	List obj = em.createNamedQuery(queryStr).getResultList();
    	
    	if (obj != null) {
    		return true;
    	} 
    	
    	
    	return false;
    }
    
	
}
