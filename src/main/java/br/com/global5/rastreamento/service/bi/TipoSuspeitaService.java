package br.com.global5.rastreamento.service.bi;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.rastreamento.model.enums.TipoSuspeita;

public class TipoSuspeitaService extends CrudService<TipoSuspeita> {

	private static final long serialVersionUID = 201903191427L;
	
	@Override
    public Criteria configPagination(Filter<TipoSuspeita> filter) {
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
	
}
