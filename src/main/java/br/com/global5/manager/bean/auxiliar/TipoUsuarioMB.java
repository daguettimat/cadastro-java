package br.com.global5.manager.bean.auxiliar;


import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.auxiliar.TipoUsuario;
import br.com.global5.manager.service.auxiliar.TipoUsuarioService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewAccessScoped
public class TipoUsuarioMB implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<TipoUsuario> tcList;
	private List<TipoUsuario> filteredValue;
	private Integer id;
	private TipoUsuario tipoUsuario;
	private Filter<TipoUsuario> filter = new Filter<TipoUsuario>(new TipoUsuario());

	@Inject
	TipoUsuarioService tuService;
	
	@Inject
	Crud<TipoUsuario> tuCrud;
	
	@Inject
	CrudService<TipoUsuario> tuCrudService;
	
	public LazyDataModel<TipoUsuario> getTcList() {
		if( tcList == null ) {
			tcList = new LazyDataModel<TipoUsuario>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<TipoUsuario> load(int first, int pageSize,
                        String sortField, SortOrder sortOrder,
                        Map<String, Object> filters) {
					
					br.com.global5.infra.enumerator.SortOrder order = null;
					if( sortOrder != null ) {
						 order = sortOrder.equals(SortOrder.ASCENDING) ? br.com.global5.infra.enumerator.SortOrder.ASCENDING
	                                : sortOrder.equals(SortOrder.DESCENDING) ? br.com.global5.infra.enumerator.SortOrder.DESCENDING
	                                : br.com.global5.infra.enumerator.SortOrder.UNSORTED;
					}
                    filter.setFirst(first).setPageSize(pageSize)
                    .setSortField(sortField).setSortOrder(order)
                    .setParams(filters);
                    List<TipoUsuario> list = tuService.paginate(filter);
                    setRowCount(tuService.count(filter));					
					return list;
				}
				
                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public TipoUsuario getRowData(String key) {
                    return tuService.findById(new Integer(key));
                }
			};
		
		}
		return tcList;
	}
	
	public void remove() {
        if( tipoUsuario != null && tipoUsuario.getId() != null) {
            tuService.remove(tipoUsuario);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Tipo de Usuário " + tipoUsuario.getDescricao()
                            + " removido com sucesso",null));
            clear();
        }		
	}
	
    public void update() {
        String msg;
        if (tipoUsuario.getId() == null) {
            tuService.insert(tipoUsuario);
            msg = "Tipo de Usuário " + tipoUsuario.getDescricao() + " criado com sucesso!";
        } else {
        	tuService.update(tipoUsuario);
            msg = "Tipo de Usuário " + tipoUsuario.getDescricao() + " alterado com sucesso!";
        }
        
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));
        
       
        //clear();// reload car list
    }

	public void insert() {
		try {
			clear();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../man/tipousuario.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novos tipos de usuario " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void clear() {
		tipoUsuario = new TipoUsuario();
		filter = new Filter<TipoUsuario>(new TipoUsuario());
		id = null;
	}
	
    public void findTipoUsuarioById(Integer id) {
        if (id == null) {
			throw new BusinessException("O id do Tipo de Usuário é obrigatório");
        }
        tipoUsuario = tuCrud.get(id);
        if (tipoUsuario == null) {
			throw new BusinessException("Tipo de Usuário não foi encontrado pelo " + id);
        }
    }	
    
    public List<String> completeDescricao(String query) {
        List<String> result = tuService.getDescricao(query);
        return result;
    }    
    
    public void onRowSelect(SelectEvent event) {
        setId(((TipoUsuario) event.getObject()).getId());
        findTipoUsuarioById(getId());
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../man/tipousuario.xhtml?id=" + getId());
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Tipo de Usuário " + getId()
	                            + " não pode ser carregado. Informe ao suporte técnico.",null));
		}

    }
    
    public void btnBack() {
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../list/tipocontrato.xhtml");
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de Tipos de Usuarios " + getId()
	                            + " não pode ser carregada. Informe ao suporte técnico.",null));
		}    	
    }

    public void onRowUnselect(UnselectEvent event) {
        tipoUsuario = new TipoUsuario();
    }

    public Filter<TipoUsuario> getFilter() {
        return filter;
    }

    public void setFilter(Filter<TipoUsuario> filter) {
        this.filter = filter;
    }

	public List<TipoUsuario> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<TipoUsuario> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setTcList(LazyDataModel<TipoUsuario> tcList) {
		this.tcList = tcList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public TipoUsuarioService getTuService() {
		return tuService;
	}

	public void setTuService(TipoUsuarioService tuService) {
		this.tuService = tuService;
	}

	public Crud<TipoUsuario> getTuCrud() {
		return tuCrud;
	}

	public void setTuCrud(Crud<TipoUsuario> tuCrud) {
		this.tuCrud = tuCrud;
	}

	public CrudService<TipoUsuario> getTuCrudService() {
		return tuCrudService;
	}

	public void setTuCrudService(CrudService<TipoUsuario> tuCrudService) {
		this.tuCrudService = tuCrudService;
	}
}
