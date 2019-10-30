package br.com.global5.rastreamento.service.bi;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.rastreamento.model.bi.EventoInfoViagem;

/**
 * 
 * @author Francis Bueno
 * @date 2019-03-14
 *
 */
public class EventoInfoViagemService extends CrudService<EventoInfoViagem> {

	private static final long serialVersionUID = 1L;

	@Override
	public Criteria configPagination(Filter<EventoInfoViagem> filter) {
		
		if ( filter.hasParam("id") ) {
			crud().eq("id", Integer.parseInt(filter.getParam("id").toString()));
		}
		
		return crud().getCriteria();
		
	}
	
}
