package br.com.global5.manager.service.cadastro;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.analise.FichaFinanceiraMotorista;

/**
 * FichaFinanceiraMotoristaService ( Regras de Negócio )
 * @author Francis
 * @date 2022-02-06 13:12
 */

public class FichaFinanceiraMotoristaService extends CrudService<FichaFinanceiraMotorista> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 202302061312L;

	@Override
    public Criteria configPagination(Filter<FichaFinanceiraMotorista> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
 

}
