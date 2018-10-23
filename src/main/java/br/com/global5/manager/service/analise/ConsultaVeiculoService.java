package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acConsultaVeiculo;
import org.hibernate.Criteria;

/**
 * ConsultaPessoaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-08-04 13:30
 */

public class ConsultaVeiculoService extends CrudService<acConsultaVeiculo> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acConsultaVeiculo> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }
        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(acConsultaVeiculo consultaPessoa) {

    }
	
    @Override
    public void beforeUpdate(acConsultaVeiculo consultaPessoa) {
        this.beforeInsert(consultaPessoa);
    }


    @Override
    @Admin
    public void remove(acConsultaVeiculo consultaPessoa) {
        super.remove(consultaPessoa);
    }
    
}
