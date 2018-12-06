package br.com.global5.manager.service.permissao;

import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.permissao.Formulario;

/**
 * 
 * 
 * @author Francis Bueno
 * @date 2018-11-18
 *
 */
public class FormularioService extends CrudService<Formulario> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 201811201108L;
	
	@Override
	public Criteria configPagination(Filter<Formulario> filter){
		
		if( filter.hasParam("id")){
			crud().eq("id", Integer.parseInt(filter.getParam("id").toString()));
		}
		
		if( filter.getEntity() != null ){
			if( filter.getEntity().getTitulo() != null ){
				crud().ilike("titulo", filter.getEntity().getTitulo(), MatchMode.ANYWHERE);
			}
		}
	
		return crud().getCriteria();
		
	}

	@SuppressWarnings("unchecked")
	public List<String> getTitulo(String query){
		
		return crud().criteria().ilike("titulo", query, MatchMode.ANYWHERE).projection(Projections.property("titulo")).list();
		
	}
	
	
	
}
