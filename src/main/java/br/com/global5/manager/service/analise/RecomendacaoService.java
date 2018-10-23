package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acRecomendacao;
import org.hibernate.Criteria;

/**
 * RecomendacaoService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class RecomendacaoService extends CrudService<acRecomendacao> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acRecomendacao> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }




        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(acRecomendacao acRecomendacao) {

//        if () {
//            throw new BusinessException("texto da validação");
//        }
    }
	
    @Override
    public void beforeUpdate(acRecomendacao acRecomendacao) {
        this.beforeInsert(acRecomendacao);
    }


    @Override
    @Admin
    public void remove(acRecomendacao acRecomendacao) {
        super.remove(acRecomendacao);
    }
    
}
