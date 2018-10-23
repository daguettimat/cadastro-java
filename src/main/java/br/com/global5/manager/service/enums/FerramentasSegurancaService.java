package br.com.global5.manager.service.enums;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.enums.FerramentasSeguranca;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * FerramentasSegurancaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class FerramentasSegurancaService extends CrudService<FerramentasSeguranca> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<FerramentasSeguranca> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(FerramentasSeguranca entity) {}
	
    @Override
    public void beforeUpdate(FerramentasSeguranca entity) {
        this.beforeInsert(entity);
    }

    @Override
    @Admin
    public void remove(FerramentasSeguranca entity) {
        super.remove(entity);
    }
    
}
