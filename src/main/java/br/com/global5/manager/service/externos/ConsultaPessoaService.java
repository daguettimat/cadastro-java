package br.com.global5.manager.service.externos;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.externos.ConsultaPessoa;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * ConsultaPessoaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-05-25 23:00
 */

public class ConsultaPessoaService extends CrudService<ConsultaPessoa> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201705252300L;

	@Override
    public Criteria configPagination(Filter<ConsultaPessoa> filter) {
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
    public void beforeInsert(ConsultaPessoa ConsultaPessoa) {

    }
	
    @Override
    public void beforeUpdate(ConsultaPessoa entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(ConsultaPessoa conPessoa) {
        super.remove(conPessoa);
    }
    
}
