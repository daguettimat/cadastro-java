package br.com.global5.manager.service.geral;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.contrato.RegraSeguradoraMotorista;

/**
 * RegraSeguradoraMotoristaService ( Regras de Negócio )
 * @author Francis Bueno
 * @date 2022-04-06 10:10
 */

public class RegraSeguradoraMotoristaService extends CrudService<RegraSeguradoraMotorista> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 202204061010L;

	@Override
    public Criteria configPagination(Filter<RegraSeguradoraMotorista> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
		    
}
