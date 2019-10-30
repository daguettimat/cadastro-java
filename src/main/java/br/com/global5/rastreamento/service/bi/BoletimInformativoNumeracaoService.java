package br.com.global5.rastreamento.service.bi;

import org.hibernate.Criteria;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.rastreamento.model.bi.BoletimInformativoNumeracao;

/**
 * 
 * @author Francis Bueno
 * @date   2019-04-08 11:33
 *
 */
public class BoletimInformativoNumeracaoService extends CrudService<BoletimInformativoNumeracao> {

	private static final long serialVersionUID = 201904081133L;
	
	
	public Criteria configPagination(Filter<BoletimInformativoNumeracao> filter) {
		
		if ( filter.hasParam("id") ) {
			crud().eq("id", Integer.parseInt(filter.getParam("id").toString()));
		}
		
		return crud().getCriteria();
		
	}
	

}
