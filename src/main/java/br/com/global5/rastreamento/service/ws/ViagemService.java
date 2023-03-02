package br.com.global5.rastreamento.service.ws;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.rastreamento.model.trafegus.Viagem;

public class ViagemService extends CrudService<Viagem> {

	public static final long serialVersionUID = 201926061339L;
	
    @Override
    public Criteria configPagination(Filter<Viagem> filter) {

        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	
}
