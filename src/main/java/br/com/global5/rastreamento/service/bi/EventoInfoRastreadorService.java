package br.com.global5.rastreamento.service.bi;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.rastreamento.model.trafegus.EventoInfoRastreador;

/**
 * 
 * @author Francis Bueno
 * @date   2019-10-09 17:17
 *
 */
public class EventoInfoRastreadorService extends CrudService<EventoInfoRastreador> {

	private static final long serialVersionUID = 202010091057L;
	
	
	public Criteria configPagination(Filter<EventoInfoRastreador> filter) {
		
		if ( filter.hasParam("id") ) {
			crud().eq("id", Integer.parseInt(filter.getParam("id").toString()));
		}
		
		return crud().getCriteria();
		
	}
	

}
