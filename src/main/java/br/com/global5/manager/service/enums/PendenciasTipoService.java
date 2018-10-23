package br.com.global5.manager.service.enums;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.enums.PendenciasTipo;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * PendenciasTipoService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class PendenciasTipoService extends CrudService<PendenciasTipo> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<PendenciasTipo> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        // see index.xhtml 'model' column facet name filter
        if (filter.getEntity() != null) {
            if(filter.getEntity().getDescricao() != null){
                crud().ilike("descricao", filter.getEntity().getDescricao(), MatchMode.ANYWHERE);
            }
        }

        return crud().getCriteria();
    }
	
    @SuppressWarnings("unchecked")
	public List<String> getDescricao(String query) {
        return crud().criteria().ilike("descricao", query, MatchMode.ANYWHERE)
                .projection(Projections.property("descricao")).list();
    }	
	

    @Override
    public void beforeInsert(PendenciasTipo PendenciasTipo) {
        if (!PendenciasTipo.hasDescricao()) {
            throw new BusinessException("A descrição deve ser preenchida");
        }

    }
	
    @Override
    public void beforeUpdate(PendenciasTipo entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(PendenciasTipo car) {
        super.remove(car);
    }
    
}
