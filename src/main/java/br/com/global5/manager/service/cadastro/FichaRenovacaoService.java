package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.cadastro.FichaCliente;
import br.com.global5.manager.model.cadastro.FichaRenovacao;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * FichaRenovacaoService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-07-20 10:02
 */

public class FichaRenovacaoService extends CrudService<FichaRenovacao> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201704031608L;

	@Override
    public Criteria configPagination(Filter<FichaRenovacao> filter) {

        crud().between("status.id",213, 214);

        if( filter.getEntity().getFilialArea() != null ) {
            crud().eq("filialArea.id", filter.getEntity().getFilialArea().getId());
        } else {
            if (filter.getEntity().getClienteArea() != null) {
                crud().eq("clienteArea.id", filter.getEntity().getClienteArea().getId());
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
    public void beforeInsert(FichaRenovacao fichaRenovacao) {}
	
    @Override
    public void beforeUpdate(FichaRenovacao entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(FichaRenovacao fichaRenovacao) {
        super.remove(fichaRenovacao);
    }
    
}
