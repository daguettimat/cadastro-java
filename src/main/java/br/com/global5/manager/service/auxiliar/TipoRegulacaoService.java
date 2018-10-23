package br.com.global5.manager.service.auxiliar;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.auxiliar.TipoRegulacao;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * Tipo de Regulacao ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class TipoRegulacaoService extends CrudService<TipoRegulacao> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<TipoRegulacao> filter) {
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
    public void beforeInsert(TipoRegulacao TipoRegulacao) {
        if (!TipoRegulacao.hasDescricao()) {
            throw new BusinessException("A descrição do Tipo de Regulacao deve ser preenchida");
        }

        if (crud().eq("descricao", TipoRegulacao.getDescricao()).ne("id", TipoRegulacao.getId()).count() > 0) {
            throw new BusinessException("O Tipo de Regulacao deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(TipoRegulacao entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(TipoRegulacao tipoReboque) {
        super.remove(tipoReboque);
    }
    
}
