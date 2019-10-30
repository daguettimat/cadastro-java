package br.com.global5.rastreamento.service.bi;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.rastreamento.model.bi.Evento;

/**
 * 
 * @author Francis Bueno
 * @date  2019-03-13 17:37
 *
 */
public class EventoService extends CrudService<Evento>{

	private static final long serialVersionUID = 201903131737L;
	
	public Criteria configPagination(Filter<Evento> filter) {
		
		if ( filter.hasParam("id") ) {
			
			crud().eq("id", Integer.parseInt(filter.getParam("id").toString()));
			
		}
		
		return crud().getCriteria();
	}

}
