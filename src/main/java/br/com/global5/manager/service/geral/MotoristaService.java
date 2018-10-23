package br.com.global5.manager.service.geral;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.infra.util.HibernateUtil;
import br.com.global5.manager.model.geral.Motorista;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * MotoristaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-03-01 15:50
 */

public class MotoristaService extends CrudService<Motorista> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201703071514L;

	@Override
    public Criteria configPagination(Filter<Motorista> filter) {
        if (filter.getEntity() != null) {

            if (filter.getEntity().getId() != null) {
                crud().eq("id",filter.getEntity().getId());
            }

            if (filter.getEntity().getNome() != null) {
                crud().ilike("nome", filter.getEntity().getNome(), MatchMode.ANYWHERE);
            }

            if (filter.getEntity().getMae() != null) {
                crud().ilike("mae", filter.getEntity().getMae(), MatchMode.ANYWHERE);
            }

            if (filter.getEntity().getDoc1() != null) {
                crud().ilike("doc1", filter.getEntity().getDoc1(), MatchMode.ANYWHERE);
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
        return crud().criteria().ilike("nome", query, MatchMode.ANYWHERE).maxResult(20)
                .projection(Projections.property("nome")).list();
    }

    public Motorista getCPF(String query) {
        Motorista motorista =  (Motorista) crud().criteria()
                .fecth("localizador.tipoEndereco", FetchMode.JOIN)
                .fecth("telefone.tipo", FetchMode.JOIN)
                .fecth("telefone.origem", FetchMode.JOIN)
                .eq("doc1", query).and().eq("usuarioExclusao",null).maxResult(1)
                .find();

        return motorista;

    }




    @Override
    public void beforeInsert(Motorista motorista) {
        if (!motorista.hasNome()) {
            throw new BusinessException("O nome do motorista é obrigatório");
        }

        if (crud().eq("nome", motorista.getNome()).ne("id", motorista.getId()).count() > 0) {
            throw new BusinessException("O nome do motorista deve ser único");
        }
    }


	
    @Override
    public void beforeUpdate(Motorista entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Motorista motorista) {
        super.remove(motorista);
    }
    
}
