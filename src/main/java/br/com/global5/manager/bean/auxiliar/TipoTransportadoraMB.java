package br.com.global5.manager.bean.auxiliar;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.auxiliar.TipoTransportadora;
import br.com.global5.manager.service.auxiliar.TipoTransportadoraService;
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
public class TipoTransportadoraMB implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<TipoTransportadora> traList;
	private List<TipoTransportadora> filteredValue;
	private Long id;
	private TipoTransportadora TipoTransportadora;
	private Filter<TipoTransportadora> filter = new Filter<TipoTransportadora>(new TipoTransportadora());

	@Inject
	TipoTransportadoraService traService;
	
	@Inject
	Crud<TipoTransportadora> traCrud;
	
	@Inject
	CrudService<TipoTransportadora> traCrudService;
	
	public LazyDataModel<TipoTransportadora> getTraList() {
		if( traList == null ) {
			traList = new LazyDataModel<TipoTransportadora>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<TipoTransportadora> load(int first, int pageSize,
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
                    List<TipoTransportadora> list = traService.paginate(filter);
                    setRowCount(traService.count(filter));					
					return list;
				}
				
                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public TipoTransportadora getRowData(String key) {
                    return traService.findById(new Long(key));
                }
			};
		
		}
		return traList;
	}
	
	public void remove() {
        if( TipoTransportadora != null && TipoTransportadora.getId() != null) {
            traService.remove(TipoTransportadora);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Tipo de Transportadora " + TipoTransportadora.getDescricao()
                            + " removido com sucesso",null));
            clear();
        }		
	}
	
    public void update() {
        String msg;
        if (TipoTransportadora.getId() == null) {
            traService.insert(TipoTransportadora);
            msg = "Tipo de Transportadora " + TipoTransportadora.getDescricao() + " criado com sucesso!";
        } else {
        	traService.update(TipoTransportadora);
            msg = "Tipo de Transportadora " + TipoTransportadora.getDescricao() + " alterado com sucesso!";
        }
        
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));
        
       
        //clear();// reload car list
    }

	public void insert() {
		try {
			clear();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../man/tipotransportadora.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novos tipos de transportadora " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void clear() {
		TipoTransportadora = new TipoTransportadora();
		filter = new Filter<TipoTransportadora>(new TipoTransportadora());
		id = null;
	}
	
    public void findTipoTransportadoraById(Long id) {
        if (id == null) {
			throw new BusinessException("O id do Tipo de Transportadora é obrigatório");
        }
        TipoTransportadora = traCrud.get(id);
        if (TipoTransportadora == null) {
			throw new BusinessException("Tipo de Transportadora não foi encontrado pelo id: " + id );
        }
    }	
    
    public List<String> completeDescricao(String query) {
        List<String> result = traService.getDescricao(query);
        return result;
    }    
    
    public void onRowSelect(SelectEvent event) {
        setId(((TipoTransportadora) event.getObject()).getId());
        findTipoTransportadoraById(getId());
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../man/tipotra.xhtml?id=" + getId());
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Tipo de Transportadora " + getId()
	                            + " não pode ser carregado. Informe ao suporte técnico.",null));
		}

    }
    
    public void btnBack() {
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../list/tipotra.xhtml");
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista do Tipo de Transportadora " + getId()
	                            + " não pode ser carregada. Informe ao suporte técnico.",null));
		}    	
    }

    public void onRowUnselect(UnselectEvent event) {
        TipoTransportadora = new TipoTransportadora();
    }

    public Filter<TipoTransportadora> getFilter() {
        return filter;
    }

    public void setFilter(Filter<TipoTransportadora> filter) {
        this.filter = filter;
    }

	public List<TipoTransportadora> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<TipoTransportadora> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoTransportadora getTipoTransportadora() {
		return TipoTransportadora;
	}

	public void setTipoTransportadora(TipoTransportadora TipoTransportadora) {
		this.TipoTransportadora = TipoTransportadora;
	}

	public TipoTransportadoraService gettraService() {
		return traService;
	}

	public void settraService(TipoTransportadoraService traService) {
		this.traService = traService;
	}

	public Crud<TipoTransportadora> gettraCrud() {
		return traCrud;
	}

	public void settraCrud(Crud<TipoTransportadora> traCrud) {
		this.traCrud = traCrud;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setTcList(LazyDataModel<TipoTransportadora> traList) {
		this.traList = traList;
	}	
    
    
	
	

}
