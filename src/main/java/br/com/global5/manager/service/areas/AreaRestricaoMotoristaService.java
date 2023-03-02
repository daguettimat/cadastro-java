package br.com.global5.manager.service.areas;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.areas.AreaRestricaoMotorista;
import org.hibernate.Criteria;

/**
 * PessoaService ( Regras de Negócio )
 * @author Francis Bueno
 * @date 2020-05-06 09:10
 */

public class AreaRestricaoMotoristaService extends CrudService<AreaRestricaoMotorista> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 202005060910L;

	@Override
    public Criteria configPagination(Filter<AreaRestricaoMotorista> filter) {

        if (filter.getEntity() != null) {

            if (filter.getEntity().getId() != null) {
                crud().eq("id",filter.getEntity().getId());
            }
        }        

        return crud().getCriteria();
    }
	
     
}
