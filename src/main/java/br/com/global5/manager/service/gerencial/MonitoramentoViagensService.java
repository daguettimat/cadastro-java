package br.com.global5.manager.service.gerencial;

import java.util.List;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.gerencial.MonitoramentoViagensCtrFixo;


public class MonitoramentoViagensService extends CrudService<MonitoramentoViagensCtrFixo>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 202126021556L;
	
    @Override
    public Criteria configPagination(Filter<MonitoramentoViagensCtrFixo> filter) {

        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	

    public List findMonitoramentoPorTransacao(Integer idTransacao){
    	
    	
    	
    	return null;
    }
    
	
}
