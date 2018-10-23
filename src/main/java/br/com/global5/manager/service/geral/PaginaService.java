package br.com.global5.manager.service.geral;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.geral.Pagina;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * PaginaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-03-16 10:00
 */

public class PaginaService extends CrudService<Pagina> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<Pagina> filter) {
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
    public void beforeInsert(Pagina pagina) {
        if (!pagina.hasDescricao()) {
            throw new BusinessException("A descrição da pagina deve ser preenchida");
        }

        if (crud().eq("descricao", pagina.getDescricao()).ne("id", pagina.getId()).count() > 0) {
            throw new BusinessException("A página deve ser única");
        }
    }
	
    @Override
    public void beforeUpdate(Pagina entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Pagina pagina) {
        super.remove(pagina);
    }
    
}
