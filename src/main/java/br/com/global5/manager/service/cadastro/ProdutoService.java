package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.cadastro.Produto;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * ProdutoService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-04-03 16:08
 */

public class ProdutoService extends CrudService<Produto> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201704031608L;

	@Override
    public Criteria configPagination(Filter<Produto> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        if (filter.getEntity() != null) {
            if(filter.getEntity().getNome() != null){
                crud().ilike("nome", filter.getEntity().getNome(), MatchMode.ANYWHERE);
            }
        }

        return crud().getCriteria();
    }
	
    @SuppressWarnings("unchecked")
	public List<String> getNome(String query) {
        return crud().criteria().ilike("nome", query, MatchMode.ANYWHERE)
                .projection(Projections.property("nome")).list();
    }	
	

    @Override
    public void beforeInsert(Produto produto) {
        if (!produto.hasNome()) {
            throw new BusinessException("O nome deve ser preenchido");
        }

        if (crud().eq("nome", produto.getNome()).ne("id", produto.getId()).count() > 0) {
            throw new BusinessException("O ID deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(Produto entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Produto produto) {
        super.remove(produto);
    }
    
}
