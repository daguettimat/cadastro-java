package br.com.global5.manager.service.enums;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.enums.ManutencaoAbastecimento;
import org.hibernate.Criteria;

/**
 * ManutencaoAbastecimentoService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class ManutencaoAbastecimentoService extends CrudService<ManutencaoAbastecimento> {

    /**
	 *
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<ManutencaoAbastecimento> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }

    @Override
    public void beforeInsert(ManutencaoAbastecimento entity) {}

    @Override
    public void beforeUpdate(ManutencaoAbastecimento entity) {
        this.beforeInsert(entity);
    }

    @Override
    @Admin
    public void remove(ManutencaoAbastecimento entity) {
        super.remove(entity);
    }
    
}
