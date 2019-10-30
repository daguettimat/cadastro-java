package br.com.global5.rastreamento.service.bi;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.rastreamento.model.bi.BoletimInformativoRelato;

/**
 * 
 * @author Francis Bueno
 * @date   2019-04-02 16:23
 *
 */
public class BoletimInformativoRelatoService extends CrudService<BoletimInformativoRelato> {

	private static final long serialVersionUID = 201904021623L;
	
	
	public Criteria configPagination(Filter<BoletimInformativoRelato> filter) {
		
		if ( filter.hasParam("id") ) {
			crud().eq("id", Integer.parseInt(filter.getParam("id").toString()));
		}
		
		return crud().getCriteria();
		
	}
	

}
