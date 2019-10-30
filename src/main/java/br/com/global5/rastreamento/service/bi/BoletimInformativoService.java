package br.com.global5.rastreamento.service.bi;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.rastreamento.model.bi.BoletimInformativo;

/**
 * 
 * @author Francis Bueno
 * @date   2019-03-13 17:17
 *
 */
public class BoletimInformativoService extends CrudService<BoletimInformativo> {

	private static final long serialVersionUID = 201903131717L;
	
	
	public Criteria configPagination(Filter<BoletimInformativo> filter) {
		
		if ( filter.hasParam("id") ) {
			crud().eq("id", Integer.parseInt(filter.getParam("id").toString()));
		}
		
		return crud().getCriteria();
		
	}
	

}
