package br.com.global5.rastreamento.service.ws;

import org.hibernate.Criteria;


import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.rastreamento.model.trafegus.Transacao;


public class TransacaoService extends CrudService<Transacao> {

	private static final long serialVersionUID = 201917061553L;

    @Override
    public Criteria configPagination(Filter<Transacao> filter) {

        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
}
