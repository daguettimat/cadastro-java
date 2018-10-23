package br.com.global5.manager.service.enums;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.enums.CNHCategoria;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * CNHCategoriaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class CNHCategoriaService extends CrudService<CNHCategoria> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<CNHCategoria> filter) {
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
    public void beforeInsert(CNHCategoria CNHCategoria) {
        if (!CNHCategoria.hasDescricao()) {
            throw new BusinessException("A descrição deve ser preenchida");
        }

        if (crud().eq("descricao", CNHCategoria.getDescricao()).ne("id", CNHCategoria.getId()).count() > 0) {
            throw new BusinessException("O conteúdo da descrição deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(CNHCategoria entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(CNHCategoria car) {
        super.remove(car);
    }
    
}
