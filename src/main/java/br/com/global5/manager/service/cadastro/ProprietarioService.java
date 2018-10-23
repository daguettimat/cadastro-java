package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.cadastro.Proprietario;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * ProprietarioService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-04-03 16:08
 */

public class ProprietarioService extends CrudService<Proprietario> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201704031608L;

	@Override
    public Criteria configPagination(Filter<Proprietario> filter) {
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
    public void beforeInsert(Proprietario proprietario) {
        if (!proprietario.hasNome()) {
            throw new BusinessException("O nome deve ser preenchido");
        }

    }
	
    @Override
    public void beforeUpdate(Proprietario entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Proprietario proprietario) {
        super.remove(proprietario);
    }
    
}
