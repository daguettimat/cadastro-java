package br.com.global5.manager.service.apoio;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.apoio.ServicosSeguranca;
import org.hibernate.Criteria;

/**
 * ServicosSegurancaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-12-26 08:58
 */

public class ServicosSegurancaService extends CrudService<ServicosSeguranca> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201712260857L;

	@Override
    public Criteria configPagination(Filter<ServicosSeguranca> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(ServicosSeguranca entity) {}
	
    @Override
    public void beforeUpdate(ServicosSeguranca entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(ServicosSeguranca entity) {
        super.remove(entity);
    }
    
}
