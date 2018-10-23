package br.com.global5.manager.service.geral;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.geral.AvisosLidos;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * QuadroDeAvisosService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class AvisosLidosService extends CrudService<AvisosLidos> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201712171618L;

	@Override
    public Criteria configPagination(Filter<AvisosLidos> filter) {
        if (filter.getEntity() != null) {

            if (filter.getEntity().getId() != null) {
                crud().eq("id",filter.getEntity().getId());
            }

        }

        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(AvisosLidos avisosLidos) {

    }
	
    @Override
    public void beforeUpdate(AvisosLidos entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(AvisosLidos avisosLidos) {
        super.remove(avisosLidos);
    }
    
}
