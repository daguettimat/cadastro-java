package br.com.global5.manager.service.areacliente;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.areacliente.Virtual;

public class VirtualService extends CrudService<Virtual> {
	
    private static final long serialVersionUID = 201910241451L;

    @Override
    public Criteria configPagination(Filter<Virtual> filter) {

        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }

}
