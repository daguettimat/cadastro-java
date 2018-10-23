package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acVeiculos;
import org.hibernate.Criteria;

import javax.persistence.EntityManager;

/**
 * VeiculosService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class VeiculosService extends CrudService<acVeiculos> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acVeiculos> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(acVeiculos acVeiculos) {

//        if () {
//            throw new BusinessException("texto da validação");
//        }
    }
	
    @Override
    public void beforeUpdate(acVeiculos acVeiculos) {
        this.beforeInsert(acVeiculos);
    }


    @Override
    @Admin
    public void remove(acVeiculos acVeiculos) {
        super.remove(acVeiculos);
    }

    public Integer findLiberacao(String query) {

        EntityManager em = this.crud().getEntityManager();

        String queryStr =
                " select max(anac_aliboid) as liberacao from analise_cadastral_veiculo acv" +
                "   join analise_cadastral ac" +
                "     on acv.anacv_anacoid = ac.anacoid" +
                "   join java.veiculo v" +
                "     on v.veioid = acv.anacv_veioid" +
                "  where v.vei_placa = '|placa|'";


        queryStr = queryStr.replace("|placa|", query);

        return (Integer) em.createNativeQuery( queryStr ).getSingleResult();

    }
    
}
