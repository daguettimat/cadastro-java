package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.geral.ContratoProduto;

import org.hibernate.Criteria;


/**
 * 
 */

public class ContratoProdutoService extends CrudService<ContratoProduto> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 202005051100L;

	@Override
    public Criteria configPagination(Filter<ContratoProduto> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }
        return crud().getCriteria();
    }
    
}
