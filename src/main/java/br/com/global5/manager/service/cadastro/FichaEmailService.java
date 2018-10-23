package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.cadastro.FichaEmail;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * FichaEmailService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-04-03 16:08
 */

public class FichaEmailService extends CrudService<FichaEmail> {

    /**
	 *
	 */
	private static final long serialVersionUID = 201704031608L;

	@Override
    public Criteria configPagination(Filter<FichaEmail> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        if (filter.getEntity() != null) {
            if(filter.getEntity().getDocumento() != null){
                crud().ilike("documento", filter.getEntity().getDocumento(), MatchMode.ANYWHERE);
            }
        }

        return crud().getCriteria();
    }

    @Override
    public void beforeInsert(FichaEmail fichaEmail) {

    }

    @Override
    public void beforeUpdate(FichaEmail entity) {
        this.beforeInsert(entity);
    }

    @Override
    @Admin
    public void remove(FichaEmail fichaEmail) {
        super.remove(fichaEmail);
    }

}
