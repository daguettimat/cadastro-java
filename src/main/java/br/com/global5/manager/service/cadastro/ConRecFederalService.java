package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.cadastro.ConsultaReceitaFederal;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;

/**
 * ConRecFederalService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-04-03 16:08
 */

public class ConRecFederalService extends CrudService<ConsultaReceitaFederal> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201704031608L;

	@Override
    public Criteria configPagination(Filter<ConsultaReceitaFederal> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }
        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(ConsultaReceitaFederal receita) {

    }
	
    @Override
    public void beforeUpdate(ConsultaReceitaFederal entity) {
        this.beforeInsert(entity);
    }

    @Override
    @Admin
    public void remove(ConsultaReceitaFederal receita) {
        super.remove(receita);
    }
    
}
