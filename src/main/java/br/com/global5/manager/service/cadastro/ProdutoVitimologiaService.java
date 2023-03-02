package br.com.global5.manager.service.cadastro;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.contrato.ProdutoVitimologia;

/**
 * ProdutoVitimologiaService ( Regras de Negócio )
 * @author Francis
 * @date 2021-10-06 11:22
 */

public class ProdutoVitimologiaService extends CrudService<ProdutoVitimologia> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 202110061122L;

	@Override
    public Criteria configPagination(Filter<ProdutoVitimologia> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
 

}
