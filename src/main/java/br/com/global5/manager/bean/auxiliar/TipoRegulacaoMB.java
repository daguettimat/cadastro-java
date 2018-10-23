package br.com.global5.manager.bean.auxiliar;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.auxiliar.TipoRegulacao;
import br.com.global5.manager.service.auxiliar.TipoRegulacaoService;
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
public class TipoRegulacaoMB implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<TipoRegulacao> regulacaoList;
	private List<TipoRegulacao> filteredValue;
	private Long id;
	private TipoRegulacao TipoRegulacao;
	private Filter<TipoRegulacao> filter = new Filter<TipoRegulacao>(new TipoRegulacao());

	@Inject
	TipoRegulacaoService regService;
	
	@Inject
	Crud<TipoRegulacao> regCrud;
	
	@Inject
	CrudService<TipoRegulacao> regCrudService;
	
	public LazyDataModel<TipoRegulacao> getRegulacaoList() {
		if( regulacaoList == null ) {
			regulacaoList = new LazyDataModel<TipoRegulacao>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<TipoRegulacao> load(int first, int pageSize,
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
                    List<TipoRegulacao> list = regService.paginate(filter);
                    setRowCount(regService.count(filter));					
					return list;
				}
				
                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public TipoRegulacao getRowData(String key) {
                    return regService.findById(new Long(key));
                }
			};
		
		}
		return regulacaoList;
	}
	
	public void remove() {
        if( TipoRegulacao != null && TipoRegulacao.getId() != null) {
            regService.remove(TipoRegulacao);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Tipo de Regulação " + TipoRegulacao.getDescricao()
                            + " removido com sucesso",null));
            clear();
        }		
	}
	
    public void update() {
        String msg;
        if (TipoRegulacao.getId() == null) {
            regService.insert(TipoRegulacao);
            msg = "Tipo de Regulação " + TipoRegulacao.getDescricao() + " criado com sucesso!";
        } else {
        	regService.update(TipoRegulacao);
            msg = "Tipo de Regulação " + TipoRegulacao.getDescricao() + " alterado com sucesso!";
        }
        
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));
        
       
        //clear();// reload car list
    }

	public void insert() {
		try {
			clear();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../man/tiporegulacao.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novos tipos de regulacao " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void clear() {
		TipoRegulacao = new TipoRegulacao();
		filter = new Filter<TipoRegulacao>(new TipoRegulacao());
		id = null;
	}
	
    public void findTipoRegulacaoById(Long id) {
        if (id == null) {
			throw new BusinessException("O id do Tipo de Regulação é obrigatório");
        }
        TipoRegulacao = regCrud.get(id);
        if (TipoRegulacao == null) {
			throw new BusinessException("Tipo de Regulação não foi encontrado pelo id: " + id );
        }
    }	
    
    public List<String> completeDescricao(String query) {
        List<String> result = regService.getDescricao(query);
        return result;
    }    
    
    public void onRowSelect(SelectEvent event) {
        setId(((TipoRegulacao) event.getObject()).getId());
        findTipoRegulacaoById(getId());
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../man/tiporeg.xhtml?id=" + getId());
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Tipo de Regulação " + getId()
	                            + " não pode ser carregado. Informe ao suporte técnico.",null));
		}

    }
    
    public void btnBack() {
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../list/tiporeg.xhtml");
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista do Tipo de Regulação " + getId()
	                            + " não pode ser carregada. Informe ao suporte técnico.",null));
		}    	
    }

    public void onRowUnselect(UnselectEvent event) {
        TipoRegulacao = new TipoRegulacao();
    }

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setRegulacaoList(LazyDataModel<br.com.global5.manager.model.auxiliar.TipoRegulacao> regulacaoList) {
		this.regulacaoList = regulacaoList;
	}

	public List<br.com.global5.manager.model.auxiliar.TipoRegulacao> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<br.com.global5.manager.model.auxiliar.TipoRegulacao> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public br.com.global5.manager.model.auxiliar.TipoRegulacao getTipoRegulacao() {
		return TipoRegulacao;
	}

	public void setTipoRegulacao(br.com.global5.manager.model.auxiliar.TipoRegulacao tipoRegulacao) {
		TipoRegulacao = tipoRegulacao;
	}

	public Filter<br.com.global5.manager.model.auxiliar.TipoRegulacao> getFilter() {
		return filter;
	}

	public void setFilter(Filter<br.com.global5.manager.model.auxiliar.TipoRegulacao> filter) {
		this.filter = filter;
	}

	public TipoRegulacaoService getRegService() {
		return regService;
	}

	public void setRegService(TipoRegulacaoService regService) {
		this.regService = regService;
	}

	public Crud<br.com.global5.manager.model.auxiliar.TipoRegulacao> getRegCrud() {
		return regCrud;
	}

	public void setRegCrud(Crud<br.com.global5.manager.model.auxiliar.TipoRegulacao> regCrud) {
		this.regCrud = regCrud;
	}

	public CrudService<br.com.global5.manager.model.auxiliar.TipoRegulacao> getRegCrudService() {
		return regCrudService;
	}

	public void setRegCrudService(CrudService<br.com.global5.manager.model.auxiliar.TipoRegulacao> regCrudService) {
		this.regCrudService = regCrudService;
	}
}
