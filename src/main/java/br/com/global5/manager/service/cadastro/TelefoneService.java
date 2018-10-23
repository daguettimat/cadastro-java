package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * TelefoneService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-04-03 16:08
 */

public class TelefoneService extends CrudService<Telefone> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201704031608L;

	@Override
    public Criteria configPagination(Filter<Telefone> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }
        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(Telefone telefone) {

        if (crud().eq("fone", telefone.getFone()).and().eq("ddd",telefone.getDdd()).ne("id", telefone.getId()).count() > 0) {
            throw new BusinessException("O telefone já esta registrado na base, por favor verifique");
        }
    }
	
    @Override
    public void beforeUpdate(Telefone entity) {
        this.beforeInsert(entity);
    }

    @Override
    @Admin
    public void remove(Telefone telefone) {
        super.remove(telefone);
    }
    
}
