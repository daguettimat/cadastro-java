package br.com.global5.manager.service.analise;

import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.security.Admin;
import br.com.global5.manager.bean.geral.LogonMB;
import br.com.global5.manager.model.analise.acPendencias;
import br.com.global5.manager.model.geral.Usuario;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * CadastroService ( Regras de Negócio )
 * @author zielinski
 * @date 2017-02-21 15:09
 */

public class PendService extends CrudService<acPendencias> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210824L;

	@Override
    public Criteria configPagination(Filter<acPendencias> filter) {
        if (filter.hasParam("id")) {
            crud().eq("id",
                    Integer.parseInt(filter.getParam("id").toString()));
        }

        // see index.xhtml 'model' column facet name filter
        if( filter.getEntity() != null ) {
            if (filter.getEntity().getCadastro() != null ) {
                crud().eq("cadastro.id", filter.getEntity().getCadastro().getId());
            }

        }

        return crud().getCriteria();
    }

    @Override
    public void beforeInsert(acPendencias acpendencias) {}
	
    @Override
    public void beforeUpdate(acPendencias entity) {
        this.beforeInsert(entity);
    }


    @Override
    @Admin
    public void remove(acPendencias acpendencias) {
        super.remove(acpendencias);
    }
    
}
