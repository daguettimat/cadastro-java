package br.com.global5.manager.service.gerencial;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.gerencial.TransacaoGerencial;


public class TransacaoGerencialService extends CrudService<TransacaoGerencial> {

	private static final long serialVersionUID = 202103021030L;

    @Override
    public Criteria configPagination(Filter<TransacaoGerencial> filter) {

        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
    
    
}
