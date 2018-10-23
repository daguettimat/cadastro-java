package br.com.global5.manager.service.geral;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.geral.Mercadoria;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * Mercadoria ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 08:24
 */

public class MercadoriaService extends CrudService<Mercadoria> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<Mercadoria> filter) {
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
    public void beforeInsert(Mercadoria mercadoria) {
        if (!mercadoria.hasDescricao()) {
            throw new BusinessException("A descrição do tipo de mercadoria deve ser preenchida");
        }

        if (crud().eq("descricao", mercadoria.getDescricao()).ne("id", mercadoria.getId()).count() > 0) {
            throw new BusinessException("A descrição do tipo de mercadoria deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(Mercadoria entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Mercadoria car) {
        super.remove(car);
    }
    
}
