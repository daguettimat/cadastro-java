package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.cadastro.Pessoa;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * PessoaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-03-23 13:16
 */

public class PessoaService extends CrudService<Pessoa> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<Pessoa> filter) {

        if (filter.getEntity() != null) {

            if (filter.getEntity().getId() != null) {
                crud().eq("id",filter.getEntity().getId());
            }

            if (filter.getEntity().getNome() != null) {
                crud().ilike("nome", filter.getEntity().getNome(), MatchMode.ANYWHERE);
            }

            if (filter.getEntity().getDocumento1() != null) {
                crud().ilike("documento1", filter.getEntity().getDocumento1(), MatchMode.ANYWHERE);
            }

            if (filter.getEntity().getDtInicial() != null) {
                crud().between("dtCriacao", filter.getEntity().getDtInicial(), filter.getEntity().getDtFinal());
            }
        }

        if (! filter.getEntity().isMostrarExcluidos()) crud().isNull("dtExclusao");

        return crud().getCriteria();
    }
	
    @SuppressWarnings("unchecked")
	public List<String> getNome(String query) {
        return crud().criteria().ilike("nome", query, MatchMode.ANYWHERE)
                .projection(Projections.property("nome")).list();
    }	
	

    @Override
    public void beforeInsert(Pessoa pessoa) {
        if (!pessoa.hasNome()) {
            throw new BusinessException("O nome deve ser preenchido");
        }

        if (crud().eq("nome", pessoa.getNome()).ne("id", pessoa.getId()).count() > 0) {
            throw new BusinessException("O ID deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(Pessoa entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Pessoa pessoa) {
        super.remove(pessoa);
    }
    
}
