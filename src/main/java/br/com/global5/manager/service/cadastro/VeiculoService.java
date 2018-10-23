package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.cadastro.Veiculo;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * VeiculoService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-03-23 10:06
 */

public class VeiculoService extends CrudService<Veiculo> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<Veiculo> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        if (filter.getEntity() != null) {
            if(filter.getEntity().getPlaca() != null){
                crud().ilike("placa", filter.getEntity().getPlaca(), MatchMode.ANYWHERE);
            }

            if(filter.getEntity().getPlaca() != null){
                crud().ilike("renavam", filter.getEntity().getRenavam(), MatchMode.ANYWHERE);
            }

            if(filter.getEntity().getPlaca() != null){
                crud().ilike("chassi", filter.getEntity().getChassi(), MatchMode.ANYWHERE);
            }

        }

        return crud().getCriteria();
    }
	

    @Override
    public void beforeInsert(Veiculo Veiculo) {
        if (!Veiculo.hasPlaca()) {
            throw new BusinessException("A placa deve ser preenchida");
        }

        if (crud().eq("placa", Veiculo.getPlaca()).ne("id", Veiculo.getId()).count() > 0) {
            throw new BusinessException("O ID deve ser único");
        }
    }

    public Veiculo findPlaca(String query) {

	    return (Veiculo) crud().criteria()
                .fecth("marca", FetchMode.JOIN)
                .fecth("modelo", FetchMode.JOIN)
                .fecth("cor", FetchMode.JOIN)
                .fecth("tipo", FetchMode.JOIN)
                .fecth("proprietario", FetchMode.JOIN)
                .fecth("proprietario.endereco", FetchMode.JOIN)
                .fecth("proprietario.endereco.tipoEndereco", FetchMode.JOIN)
                .fecth("proprietario.endereco.telefone", FetchMode.JOIN)
                .fecth("categoriaVeiculo", FetchMode.JOIN)
                .eq("placa", query).and().eq("usuarioExclusao",null).maxResult(1)
                .find();

    }
	
    @Override
    public void beforeUpdate(Veiculo entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(Veiculo veiculo) {
        super.remove(veiculo);
    }
    
}
