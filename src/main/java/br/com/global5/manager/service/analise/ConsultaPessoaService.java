package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acConsultaPessoa;
import org.hibernate.Criteria;

/**
 * ConsultaPessoaService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-08-01 09:26
 */

public class ConsultaPessoaService extends CrudService<acConsultaPessoa> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acConsultaPessoa> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }
        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(acConsultaPessoa consultaPessoa) {

    }
	
    @Override
    public void beforeUpdate(acConsultaPessoa consultaPessoa) {
        this.beforeInsert(consultaPessoa);
    }


    @Override
    @Admin
    public void remove(acConsultaPessoa consultaPessoa) {
        super.remove(consultaPessoa);
    }
    
}
