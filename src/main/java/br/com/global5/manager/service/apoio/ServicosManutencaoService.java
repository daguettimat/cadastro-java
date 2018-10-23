package br.com.global5.manager.service.apoio;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.apoio.ServicosManutencao;
import org.hibernate.Criteria;

/**
 * ServicosManutencaoService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-12-26 08:58
 */

public class ServicosManutencaoService extends CrudService<ServicosManutencao> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201712260857L;

	@Override
    public Criteria configPagination(Filter<ServicosManutencao> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(ServicosManutencao entity) {}
	
    @Override
    public void beforeUpdate(ServicosManutencao entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(ServicosManutencao entity) {
        super.remove(entity);
    }
    
}
