package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acCadastro;
import br.com.global5.manager.model.analise.acMotorista;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import java.util.List;

import javax.persistence.EntityManager;

/**
 * MotoristaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class MotoristaService extends CrudService<acMotorista> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acMotorista> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(acMotorista acMotorista) {

//        if () {
//            throw new BusinessException("texto da validação");
//        }
    }

    public Integer findLiberacao(String query) {

        EntityManager em = this.crud().getEntityManager();

        String queryStr = " select max(anac_aliboid) as liberacao" +
                    "         from analise_cadastral_motorista acm" +
                    "         join analise_cadastral ac" +
                    "           on acm.anacm_anacoid = ac.anacoid" +
                    "         join motorista m" +
                    "           on m.motoid = acm.anacm_motoid" +
                    "        where m.mot_documento1 = '|cpf|'";


        queryStr = queryStr.replace("|cpf|", query);

        return (Integer) em.createNativeQuery( queryStr ).getSingleResult();

    }

    @Override
    public void beforeUpdate(acMotorista acMotorista) {
        this.beforeInsert(acMotorista);
    }


    @Override
    @Admin
    public void remove(acMotorista acMotorista) {
        super.remove(acMotorista);
    }
    
}
