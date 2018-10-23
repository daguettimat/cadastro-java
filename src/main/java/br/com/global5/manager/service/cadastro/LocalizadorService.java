package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.model.cadastro.Localizador;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * LocalizadorService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-03-23 10:06
 */

public class LocalizadorService extends CrudService<Localizador> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<Localizador> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        if (filter.getEntity() != null) {
            if(filter.getEntity().getLogradouro() != null){
                crud().ilike("logradouro", filter.getEntity().getLogradouro(), MatchMode.ANYWHERE);
            }
        }

        return crud().getCriteria();
    }
	
    @SuppressWarnings("unchecked")
	public List<String> getLogradouro(String query) {
        return crud().criteria().ilike("logradouro", query, MatchMode.ANYWHERE)
                .projection(Projections.property("logradouro")).list();
    }

    public List<Localizador> findByEndereco(String cep, String numero) {
        return crud().criteria().findByNamedQuery(
                Localizador.FIND_CEP,
                AppUtils.NamedParams("cep", cep, "numero",numero));

    }
	

    @Override
    public void beforeInsert(Localizador Localizador) {
        if (!Localizador.hasNome()) {
            throw new BusinessException("O nome deve ser preenchido");
        }

        if (crud().eq("logradouro", Localizador.getLogradouro()).ne("id", Localizador.getId()).count() > 0) {
            throw new BusinessException("O ID deve ser único");
        }
    }
	
    @Override
    public void beforeUpdate(Localizador entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Localizador localizador) {
        super.remove(localizador);
    }
    
}
