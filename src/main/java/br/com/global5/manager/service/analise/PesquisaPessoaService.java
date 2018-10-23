package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acPesquisaPessoa;
import org.hibernate.Criteria;

/**
 * PesquisaPessoaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-08-01 09:26
 */

public class PesquisaPessoaService extends CrudService<acPesquisaPessoa> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acPesquisaPessoa> filter) {
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

        try {
            if (!filter.getEntity().getCpf().isEmpty()) {
                crud().eq("cpf", filter.getEntity().getCpf());
            }
        } catch (Exception e) {}


        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(acPesquisaPessoa acPesquisaPessoa) {

    }
	
    @Override
    public void beforeUpdate(acPesquisaPessoa acPesquisaPessoa) {
        this.beforeInsert(acPesquisaPessoa);
    }


    @Override
    @Admin
    public void remove(acPesquisaPessoa acPesquisaPessoa) {
        super.remove(acPesquisaPessoa);
    }
    
}
