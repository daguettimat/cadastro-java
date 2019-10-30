package br.com.global5.manager.service.autotrac;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.autotrac.AutotracSinal;

import org.hibernate.Criteria;

/**
 * AutotracSinalService ( Regras de Negócio )
 * @author Francis Bueno
 * @date 2019-10-04 13:52
 */

public class AutotracSinalService extends CrudService<AutotracSinal> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201907101752L;

	@Override
    public Criteria configPagination(Filter<AutotracSinal> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
		    
}
