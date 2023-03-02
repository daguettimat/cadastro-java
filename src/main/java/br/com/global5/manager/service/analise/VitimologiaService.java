package br.com.global5.manager.service.analise;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.analise.Vitimologia;

public class VitimologiaService extends CrudService<Vitimologia> {

	private static final long serialVersionUID = 202111251458L;
	
	@Override
	public Criteria configPagination(Filter<Vitimologia> filter){
		
		if(filter.hasParam("id")){
			crud().eq("id", 
					Integer.parseInt(filter.getParam("id").toString()));
		}
		
		if(filter.getEntity() != null){
			if(filter.getEntity().getCodRetorno() != null ) {
				crud().like("codRetorno", filter.getEntity().getCodRetorno(), MatchMode.EXACT);
			}
		}
		
		return crud().getCriteria();
	}

}
