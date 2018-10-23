package br.com.global5.manager.service.auxiliar;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.auxiliar.Cor;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * Cores ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class CorService extends CrudService<Cor> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<Cor> filter) {
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
    public void beforeInsert(Cor Cor) {
        if (!Cor.hasNome()) {
            throw new BusinessException("O nome da cor deve ser preenchido");
    }

        if (crud().eq("nome", Cor.getNome()).ne("id", Cor.getId()).count() > 0) {
            throw new BusinessException("A cor deve ser única");
        }
    }
	
    @Override
    public void beforeUpdate(Cor entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Cor car) {
        super.remove(car);
    }
    
}
