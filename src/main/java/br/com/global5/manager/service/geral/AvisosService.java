package br.com.global5.manager.service.geral;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.geral.Avisos;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * QuadroDeAvisosService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class AvisosService extends CrudService<Avisos> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<Avisos> filter) {
        if (filter.getEntity() != null) {

            if (filter.getEntity().getId() != null) {
                crud().eq("id",filter.getEntity().getId());
            }

            if (filter.getEntity().getNome() != null) {
                crud().ilike("nome", filter.getEntity().getNome(), MatchMode.ANYWHERE);
            }

            if (filter.getEntity().getDtInicial() != null) {
                crud().between("dtFinal", filter.getEntity().getDtInicial(), filter.getEntity().getDtFinal());
            }
        }

        return crud().getCriteria();
    }
	
    @SuppressWarnings("unchecked")
	public List<String> getDescricao(String query) {
        return crud().criteria().ilike("descricao", query, MatchMode.ANYWHERE)
                .projection(Projections.property("descricao")).list();
    }	
	

    @Override
    public void beforeInsert(Avisos Avisos) {
        if (!Avisos.hasNome()) {
            throw new BusinessException("O nome do aviso deve ser preenchido");
        }

        if (crud().eq("nome", Avisos.getNome()).ne("id", Avisos.getId()).count() > 0) {
            throw new BusinessException("O nome do aviso deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(Avisos entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Avisos avisos) {
        super.remove(avisos);
    }
    
}
