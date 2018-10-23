package br.com.global5.manager.service.enums;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.enums.TipoApoio;
import org.hibernate.Criteria;

/**
 * TipoApoioService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class TipoApoioService extends CrudService<TipoApoio> {

    /**
	 *
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<TipoApoio> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }

    @Override
    public void beforeInsert(TipoApoio entity) {}

    @Override
    public void beforeUpdate(TipoApoio entity) {
        this.beforeInsert(entity);
    }

    @Override
    @Admin
    public void remove(TipoApoio entity) {
        super.remove(entity);
    }
    
}
