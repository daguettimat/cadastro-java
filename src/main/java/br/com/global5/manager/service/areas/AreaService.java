package br.com.global5.manager.service.areas;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;

import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * AreaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-03-13 14:06
 */

public class AreaService extends CrudService<Area> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<Area> filter) {
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

    public List listAreaByUser(String usuario) {
       return crud().criteria().findByNamedQuery(
                Area.FIND_AREA,
                AppUtils.NamedParams("usuario", usuario));

    }
	

	public List<String> getNome(String query) {
        return crud().criteria().ilike("nome", query, MatchMode.ANYWHERE)
                .projection(Projections.property("nome")).list();
    }

    public List<Area> listNome(String query, Integer nivel) {
        return crud().criteria().ilike("nome", query, MatchMode.ANYWHERE)
                .eq("nivel.id", nivel).list();
    }

    public List<Area> listNome(String query, Integer nivel, Integer area) {

        return crud().criteria().ilike("nome", query, MatchMode.ANYWHERE)
                .eq("nivel.id", nivel)
                .eq("root.id", area)
                .list();
    }

    @Override
    public void beforeInsert(Area area) {
        if (!area.hasNome()) {
            throw new BusinessException("O nome da área deve ser preenchido");
        }

        if (crud().eq("nome", area.getNome()).ne("id", area.getId()).count() > 0) {
            throw new BusinessException("O nome da área deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(Area entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Area area) {
        super.remove(area);
    }
    
}
