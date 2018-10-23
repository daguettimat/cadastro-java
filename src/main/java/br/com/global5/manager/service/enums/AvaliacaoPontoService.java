package br.com.global5.manager.service.enums;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.enums.AvaliacaoPonto;
import org.hibernate.Criteria;

/**
 * AvaliacaoPontoService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class AvaliacaoPontoService extends CrudService<AvaliacaoPonto> {

    /**
	 *
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<AvaliacaoPonto> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }

    @Override
    public void beforeInsert(AvaliacaoPonto entity) {}

    @Override
    public void beforeUpdate(AvaliacaoPonto entity) {
        this.beforeInsert(entity);
    }

    @Override
    @Admin
    public void remove(AvaliacaoPonto entity) {
        super.remove(entity);
    }
    
}
