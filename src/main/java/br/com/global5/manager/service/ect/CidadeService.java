package br.com.global5.manager.service.ect;


import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.ect.Cidade;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * CidadeService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class CidadeService extends CrudService<Cidade> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<Cidade> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        // see index.xhtml 'model' column facet name filter
        if (filter.getEntity() != null) {
            if(filter.getEntity().getNome() != null){
                crud().ilike("nome", filter.getEntity().getNome(), MatchMode.ANYWHERE);
            }
        }

        return crud().getCriteria();
    }
	
    @SuppressWarnings("unchecked")
	public List<String> getNome(String query) {
        return crud().criteria().ilike("nome", query, MatchMode.ANYWHERE)
                .projection(Projections.property("nome")).list();
    }	
	

    @Override
    public void beforeInsert(Cidade cidade) {
        if (!cidade.hasNome()) {
            throw new BusinessException("O nome da cidade deve ser preenchida");
        }

        if (crud().eq("nome", cidade.getNome()).ne("id", cidade.getId()).count() > 0) {
            throw new BusinessException("O nome do cidade deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(Cidade entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Cidade cidade) {
        super.remove(cidade);
    }
    
}
