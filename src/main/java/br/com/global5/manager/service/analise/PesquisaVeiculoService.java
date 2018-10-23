package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acPesquisaVeiculo;
import org.hibernate.Criteria;

/**
 * PesquisaPessoaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-08-01 09:26
 */

public class PesquisaVeiculoService extends CrudService<acPesquisaVeiculo> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acPesquisaVeiculo> filter) {
        if( filter.getEntity().getId() != null ) {
            crud().eq("id", filter.getEntity().getId());
        }

        if( filter.getEntity().getDtInicial() != null    ){
            crud().between("dtCriacao",filter.getEntity().getDtInicial(), filter.getEntity().getDtFinal());
        }

        if( filter.getEntity().getFilialArea().getId() != null ) {
            crud().eq("filialArea.id", filter.getEntity().getFilialArea().getId());
        }

        if( filter.getEntity().getClienteArea().getId() != null ) {
            crud().eq("clienteArea.id", filter.getEntity().getClienteArea().getId());
        }


        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(acPesquisaVeiculo acPesquisaVeiculo) {

    }
	
    @Override
    public void beforeUpdate(acPesquisaVeiculo acPesquisaVeiculo) {
        this.beforeInsert(acPesquisaVeiculo);
    }


    @Override
    @Admin
    public void remove(acPesquisaVeiculo acPesquisaVeiculo) {
        super.remove(acPesquisaVeiculo);
    }
    
}
