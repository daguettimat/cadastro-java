package br.com.global5.manager.service.enums;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.enums.VeiculoCategoria;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * VeiculoCategoriaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class VeiculoCategoriaService extends CrudService<VeiculoCategoria> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<VeiculoCategoria> filter) {
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
    public void beforeInsert(VeiculoCategoria VeiculoCategoria) {
        if (!VeiculoCategoria.hasDescricao()) {
            throw new BusinessException("A descrição deve ser preenchida");
        }

        if (crud().eq("descricao", VeiculoCategoria.getDescricao()).ne("id", VeiculoCategoria.getId()).count() > 0) {
            throw new BusinessException("O conteúdo da descrição deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(VeiculoCategoria entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(VeiculoCategoria car) {
        super.remove(car);
    }
    
}
