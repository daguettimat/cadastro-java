package br.com.global5.manager.bean.auxiliar;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.auxiliar.TipoContrato;
import br.com.global5.manager.service.auxiliar.TipoContratoService;

@Named
@ViewAccessScoped
public class TipoContratoMB implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<TipoContrato> tcList;
	private List<TipoContrato> filteredValue;
	private Integer id;
	private TipoContrato tipoContrato;
	private Filter<TipoContrato> filter = new Filter<TipoContrato>(new TipoContrato());

	@Inject
	TipoContratoService tcService;
	
	@Inject
	Crud<TipoContrato> tcCrud;
	
	@Inject
	CrudService<TipoContrato> tcCrudService;
	
	public LazyDataModel<TipoContrato> getTcList() {
		if( tcList == null ) {
			tcList = new LazyDataModel<TipoContrato>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<TipoContrato> load(int first, int pageSize,
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
                    List<TipoContrato> list = tcService.paginate(filter);
                    setRowCount(tcService.count(filter));					
					return list;
				}
				
                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public TipoContrato getRowData(String key) {
                    return tcService.findById(new Integer(key));
                }
			};
		
		}
		return tcList;
	}
	
	public void remove() {
        if( tipoContrato != null && tipoContrato.getId() != null) {
            tcService.remove(tipoContrato);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Tipo de Contrato " + tipoContrato.getDescricao()
                            + " removido com sucesso",null));
            clear();
        }		
	}
	
    public void update() {
        String msg;
        if (tipoContrato.getId() == null) {
            tcService.insert(tipoContrato);
            msg = "Tipo de Contrato " + tipoContrato.getDescricao() + " criado com sucesso!";
        } else {
        	tcService.update(tipoContrato);
            msg = "Tipo Contrato " + tipoContrato.getDescricao() + " alterado com sucesso!";
        }
        
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));
        
       
        //clear();// reload car list
    }

	public void insert() {
		try {
			clear();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../man/tipocontrato.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novos tipos de contrato " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	
	public void clear() {
		tipoContrato = new TipoContrato();
		filter = new Filter<TipoContrato>(new TipoContrato());
		id = null;
	}
	
    public void findTipoContratoById(Integer id) {
        if (id == null) {
			throw new BusinessException("O id do Tipo de Contrato é obrigatório");
        }
        tipoContrato = tcCrud.get(id);
        if (tipoContrato == null) {
			throw new BusinessException("Tipo de Contrato não foi encontrado pelo " + id);
        }
    }	
    
    public List<String> completeDescricao(String query) {
        List<String> result = tcService.getDescricao(query);
        return result;
    }    
    
    public void onRowSelect(SelectEvent event) {
        setId(((TipoContrato) event.getObject()).getId());
        findTipoContratoById(getId());
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../man/tipocontrato.xhtml?id=" + getId());
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Tipo de Contrato " + getId()
	                            + " não pode ser carregado. Informe ao suporte técnico.",null));
		}

    }
    
    public void btnBack() {
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../list/tipocontrato.xhtml");
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de Tipos de Contrato " + getId()
	                            + " não pode ser carregada. Informe ao suporte técnico.",null));
		}    	
    }

    public void onRowUnselect(UnselectEvent event) {
        tipoContrato = new TipoContrato();
    }

    public Filter<TipoContrato> getFilter() {
        return filter;
    }

    public void setFilter(Filter<TipoContrato> filter) {
        this.filter = filter;
    }

	public List<TipoContrato> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<TipoContrato> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TipoContrato getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(TipoContrato tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public TipoContratoService getTcService() {
		return tcService;
	}

	public void setTcService(TipoContratoService tcService) {
		this.tcService = tcService;
	}

	public Crud<TipoContrato> getTcCrud() {
		return tcCrud;
	}

	public void setTcCrud(Crud<TipoContrato> tcCrud) {
		this.tcCrud = tcCrud;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setTcList(LazyDataModel<TipoContrato> tcList) {
		this.tcList = tcList;
	}	
    
    
	
	

}
