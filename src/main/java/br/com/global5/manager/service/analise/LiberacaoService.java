package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acLiberacao;
import org.hibernate.Criteria;

/**
 * LiberacaoService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class LiberacaoService extends CrudService<acLiberacao> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acLiberacao> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(acLiberacao acLiberacao) {

//        if () {
//            throw new BusinessException("texto da validação");
//        }
    }
	
    @Override
    public void beforeUpdate(acLiberacao entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(acLiberacao acLiberacao) {
        super.remove(acLiberacao);
    }
    
}
