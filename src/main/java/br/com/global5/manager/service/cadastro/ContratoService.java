package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.contrato.Contrato;
import org.hibernate.Criteria;


/**
 * 
 */

public class ContratoService extends CrudService<Contrato> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201804091100L;

	@Override
    public Criteria configPagination(Filter<Contrato> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }
        return crud().getCriteria();
    }
    
}
