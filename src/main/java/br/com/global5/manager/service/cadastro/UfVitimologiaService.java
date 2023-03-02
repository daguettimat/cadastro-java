package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.contrato.ProdutoVitimologia;
import br.com.global5.manager.model.contrato.UfVitimologia;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * ProdutoVitimologiaService ( Regras de Negócio )
 * @author Francis
 * @date 2021-10-07 14:43
 */

public class UfVitimologiaService extends CrudService<UfVitimologia> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 202110071443L;

	@Override
    public Criteria configPagination(Filter<UfVitimologia> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }
        
        if (filter.getEntity() != null) {
            if(filter.getEntity().getRegiao() != null){
                crud().ilike("nome", filter.getEntity().getRegiao(), MatchMode.ANYWHERE);
            }
        }
        return crud().getCriteria();
    }
 

}
