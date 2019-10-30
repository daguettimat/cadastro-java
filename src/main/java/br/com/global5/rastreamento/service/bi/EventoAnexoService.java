package br.com.global5.rastreamento.service.bi;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.rastreamento.model.bi.EventoAnexo;

/**
 * 
 * @author Francis Bueno
 * @date   2019-04-26 16:32
 *
 */
public class EventoAnexoService extends CrudService<EventoAnexo> {

	private static final long serialVersionUID = 201904261632L;
	
	
	public Criteria configPagination(Filter<EventoAnexo> filter) {
		
		if ( filter.hasParam("id") ) {
			crud().eq("id", Integer.parseInt(filter.getParam("id").toString()));
		}
		
		return crud().getCriteria();
		
	}
	

}
