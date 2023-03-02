package br.com.global5.manager.service.geral;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.geral.ContratoProduto;

import org.hibernate.Criteria;
import org.hibernate.annotations.FetchMode;

import java.util.List;

/**
 * MotoristaService ( Regras de Negócio )
 * @author Francis Bueno
 * @date 2021-03-01 16:00
 */

public class ContratoProdutoService extends CrudService<ContratoProduto> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 202103011601L;

	@Override
    public Criteria configPagination(Filter<ContratoProduto> filter) {
        if (filter.getEntity() != null) {

            if (filter.getEntity().getId() != null) {
                crud().eq("id",filter.getEntity().getId());
            }
        }

        return crud().getCriteria();
    }
    
	public ContratoProduto getProduto(Integer numContrato, Integer idProduto){
		
		ContratoProduto contratoProduto = (ContratoProduto) crud().criteria()
						.eq("contrato", numContrato).and()
						.eq("produto", idProduto).and()
						.eq("produtoAtivo", true)
						.find();
		
		ContratoProduto x = contratoProduto;			
		x.getContrato();
		
		return contratoProduto;
		
	}
	
}
