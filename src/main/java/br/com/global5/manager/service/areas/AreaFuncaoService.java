package br.com.global5.manager.service.areas;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.areas.AreaFuncao;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * AreaFuncaoService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-03-13 14:00
 */

public class AreaFuncaoService extends CrudService<AreaFuncao> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<AreaFuncao> filter) {
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

    public List<AreaFuncao> listFuncao(String query, Integer area) {
        return crud().criteria().ilike("nome", query, MatchMode.ANYWHERE)
                                .and()
                                .eq("area.id", area)
                                .list();
    }


    @Override
    public void beforeInsert(AreaFuncao Area) {
        if (!Area.hasNome()) {
            throw new BusinessException("O nome da função deve ser preenchido");
        }

        if (crud().eq("nome", Area.getNome()).ne("id", Area.getId()).count() > 0) {
            throw new BusinessException("O nome da função deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(AreaFuncao entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(AreaFuncao areaFuncao) {
        super.remove(areaFuncao);
    }
    
}
