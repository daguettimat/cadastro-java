package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acReferencia;
import org.hibernate.Criteria;

/**
 * ReferenciaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class ReferenciaService extends CrudService<acReferencia> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acReferencia> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(acReferencia acReferencia) {

//        if () {
//            throw new BusinessException("texto da validação");
//        }
    }
	
    @Override
    public void beforeUpdate(acReferencia acReferencia) {
        this.beforeInsert(acReferencia);
    }


    @Override
    @Admin
    public void remove(acReferencia acReferencia) {
        super.remove(acReferencia);
    }
    
}
