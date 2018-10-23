package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acPerguntas;
import org.hibernate.Criteria;

/**
 * PerguntasService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class PerguntasService extends CrudService<acPerguntas> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acPerguntas> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        return crud().getCriteria();
    }
	
    @Override
    public void beforeInsert(acPerguntas acPerguntas) {

//        if () {
//            throw new BusinessException("texto da validação");
//        }
    }
	
    @Override
    public void beforeUpdate(acPerguntas acPerguntas) {
        this.beforeInsert(acPerguntas);
    }


    @Override
    @Admin
    public void remove(acPerguntas liberacao) {
        super.remove(liberacao);
    }
    
}
