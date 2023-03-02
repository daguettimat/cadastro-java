package br.com.global5.manager.service.geral;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.contrato.produtos.ClienteVeiculo;

public class ClienteVeiculoService extends CrudService<ClienteVeiculo> {

	private static final long serialVersionUID = 202007221038L;
	
	@Override
    public Criteria configPagination(Filter<ClienteVeiculo> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }
        return crud().getCriteria();
    }
	
}
