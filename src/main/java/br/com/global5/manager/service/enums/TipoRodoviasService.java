package br.com.global5.manager.service.enums;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.enums.TipoRodovias;
import org.hibernate.Criteria;

/**
 * TipoRodoviasService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class TipoRodoviasService extends CrudService<TipoRodovias> {

    /**
	 *
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<TipoRodovias> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }

    @Override
    public void beforeInsert(TipoRodovias entity) {}

    @Override
    public void beforeUpdate(TipoRodovias entity) {
        this.beforeInsert(entity);
    }

    @Override
    @Admin
    public void remove(TipoRodovias entity) {
        super.remove(entity);
    }
    
}
