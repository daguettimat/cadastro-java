package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acMercadoria;
import org.hibernate.Criteria;

/**
 * CadastroService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class MercadoriaService extends CrudService<acMercadoria> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acMercadoria> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        // see index.xhtml 'model' column facet name filter
        if( filter.getEntity() != null ) {
        }

        return crud().getCriteria();
    }

    @Override
    public void beforeInsert(acMercadoria acMercadoria) {}
	
    @Override
    public void beforeUpdate(acMercadoria entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(acMercadoria acMercadoria) {
        super.remove(acMercadoria);
    }
    
}
