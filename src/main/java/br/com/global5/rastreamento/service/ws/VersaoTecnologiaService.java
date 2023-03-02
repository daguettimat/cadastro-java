package br.com.global5.rastreamento.service.ws;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.rastreamento.model.trafegus.VersaoTecnologia;

public class VersaoTecnologiaService extends CrudService<VersaoTecnologia> {

	public static final long serialVersionUID = 201921061726L;
	
    @Override
    public Criteria configPagination(Filter<VersaoTecnologia> filter) {

        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	
}
