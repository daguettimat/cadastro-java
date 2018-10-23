package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.cadastro.CNH;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * CNHService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-04-03 16:08
 */

public class CNHService extends CrudService<CNH> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201704031608L;

	@Override
    public Criteria configPagination(Filter<CNH> filter) {
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
	
    @SuppressWarnings("unchecked")
	public List<String> getDocumento(String query) {
        return crud().criteria().ilike("documento", query, MatchMode.ANYWHERE)
                .projection(Projections.property("documento")).list();
    }	

    @Override
    public void beforeInsert(CNH cnh) {
        if (!cnh.hasDocumento()) {
            throw new BusinessException("O numero do documento deve ser preenchido");
        }

        if (crud().eq("documento", cnh.getDocumento()).ne("id", cnh.getId()).count() > 0) {
            throw new BusinessException("O documento já esta registrado na base, por favor verifique");
        }
    }
	
    @Override
    public void beforeUpdate(CNH entity) {
        this.beforeInsert(entity);
    }

    @Override
    @Admin
    public void remove(CNH cnh) {
        super.remove(cnh);
    }
    
}
