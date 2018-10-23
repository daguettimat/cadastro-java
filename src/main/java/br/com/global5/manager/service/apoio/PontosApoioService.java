package br.com.global5.manager.service.apoio;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.apoio.PontosApoio;
import org.hibernate.Criteria;

/**
 * PontosApoioService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-12-23 15:46
 */

public class PontosApoioService extends CrudService<PontosApoio> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201704031608L;

	@Override
    public Criteria configPagination(Filter<PontosApoio> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }


        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(PontosApoio produto) {}
	
    @Override
    public void beforeUpdate(PontosApoio entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(PontosApoio produto) {
        super.remove(produto);
    }
    
}
