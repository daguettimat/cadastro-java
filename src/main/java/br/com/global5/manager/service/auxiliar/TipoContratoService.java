package br.com.global5.manager.service.auxiliar;

import java.util.List;

import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.auxiliar.TipoContrato;

/**
 * Tipo de Contrato ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 08:24
 */

public class TipoContratoService extends CrudService<TipoContrato> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<TipoContrato> filter) {
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
    public void beforeInsert(TipoContrato tipoContrato) {
        if (!tipoContrato.hasDescricao()) {
            throw new BusinessException("A descrição do tipo de contrato deve ser preenchida");
        }

        if (crud().eq("descricao", tipoContrato.getDescricao()).ne("id", tipoContrato.getId()).count() > 0) {
            throw new BusinessException("O Tipo de Contrato deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(TipoContrato entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(TipoContrato car) {
        super.remove(car);
    }
    
}
