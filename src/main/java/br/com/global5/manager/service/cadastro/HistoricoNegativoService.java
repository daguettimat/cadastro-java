package br.com.global5.manager.service.cadastro;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.model.analise.acMotorista;
import br.com.global5.manager.model.cadastro.HistoricoNegativo;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.template.exception.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * Historico Negativo ( Regras de Negócio )
 * @author Francis Bueno
 * @date 2020-08-27 09:34
 */

public class HistoricoNegativoService extends CrudService<HistoricoNegativo> {
	
	private static final long serialVersionUID = 202027080935L;

	@Override
    public Criteria configPagination(Filter<HistoricoNegativo> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }
        return crud().getCriteria();
    }
 
	
}
