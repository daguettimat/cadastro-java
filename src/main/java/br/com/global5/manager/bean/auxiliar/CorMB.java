package br.com.global5.manager.bean.auxiliar;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.auxiliar.Cor;
import br.com.global5.manager.service.auxiliar.CorService;
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
public class CorMB implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<Cor> corList;
	private List<Cor> filteredValue;
	private Integer id;
	private Cor cor;
	private Filter<Cor> filter = new Filter<Cor>(new Cor());

	@Inject
	CorService corService;
	
	@Inject
	Crud<Cor> corCrud;
	
	@Inject
	CrudService<Cor> motCrudService;
	
	public LazyDataModel<Cor> getCorList() {
		if( corList == null ) {
			corList = new LazyDataModel<Cor>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Cor> load(int first, int pageSize,
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
                    List<Cor> list = corService.paginate(filter);
                    setRowCount(corService.count(filter));					
					return list;
				}
				
                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public Cor getRowData(String key) {
                    return corService.findById(new Integer(key));
                }
			};
		
		}
		return corList;
	}
	
	public void remove() {
        if( cor != null && cor.getId() != null) {
            corService.remove(cor);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Cor " + cor.getNome()
                            + " removido com sucesso",null));
            clear();
        }		
	}
	
    public void update() {
        String msg;
        if (cor.getId() == null) {
            corService.insert(cor);
            msg = "Cor " + cor.getNome() + " criada com sucesso!";
        } else {
        	corService.update(cor);
            msg = "Cor " + cor.getNome() + " alterada com sucesso!";
        }
        
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));
        
       
        //clear();// reload car list
    }
	public void insert() {
		try {
			clear();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../manutencao/corman.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novas cores " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	
	public void clear() {
		cor = new Cor();
		filter = new Filter<Cor>(new Cor());
		id = null;
	}
	
    public void findById(Integer id) {
        if (id == null) {
			throw new BusinessException("O id é obrigatório");
        }
        cor = corCrud.get(id);
        if (cor == null) {
			throw new BusinessException("Registro não foi encontrado pelo id: " + id );
        }
    }	
    
    public List<String> completeNome(String query) {
        List<String> result = corService.getNome(query);
        return result;
    }    
    
    public void onRowSelect(SelectEvent event) {
        setId(((Cor) event.getObject()).getId());
        findById(getId());
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../manutencao/corman.xhtml?id=" + getId());
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Cor " + getId()
	                            + " não pode ser carregada. Informe ao suporte técnico.",null));
		}

    }
    
    public void btnBack() {
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../manutencao/corlst.xhtml");
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de Cores " + getId()
	                            + " não pode ser carregada. Informe ao suporte técnico.",null));
		}    	
    }

    public void onRowUnselect(UnselectEvent event) {
        cor = new Cor();
    }

    public Filter<Cor> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Cor> filter) {
        this.filter = filter;
    }

	public List<Cor> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Cor> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cor getCor() {
		return cor;
	}

	public void setCor(Cor Cor) {
		this.cor = Cor;
	}

	public CorService getmotService() {
		return corService;
	}

	public void setmotService(CorService motService) {
		this.corService = motService;
	}

	public Crud<Cor> getmotCrud() {
		return corCrud;
	}

	public void setmotCrud(Crud<Cor> motCrud) {
		this.corCrud = motCrud;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setTcList(LazyDataModel<Cor> corList) {
		this.corList = corList;
	}	
    
    
	
	

}
