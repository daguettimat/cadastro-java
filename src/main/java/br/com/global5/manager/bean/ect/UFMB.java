package br.com.global5.manager.bean.ect;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.ect.Pais;
import br.com.global5.manager.model.ect.UF;
import br.com.global5.manager.service.ect.PaisService;
import br.com.global5.manager.service.ect.UFService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
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
public class UFMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<UF> ufList;
	private List<UF> filteredValue;
	private Integer id;
	private UF estado;
	private Filter<UF> filter = new Filter<UF>(new UF());
	private List<Pais> lstPaises;

	@Inject
	UFService ufService;

	@Inject
    PaisService paisService;

	@Inject
	Crud<UF> ufCrud;

	@Inject
	CrudService<UF> ufCrudService;

    @PostConstruct
    public void init() {

        lstPaises = paisService.listAll();
        clear();

    }

	public LazyDataModel<UF> getUfList() {
		if( ufList == null ) {
			ufList = new LazyDataModel<UF>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<UF> load(int first, int pageSize,
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
					List<UF> list = ufService.paginate(filter);
					setRowCount(ufService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public UF getRowData(String key) {
					return ufService.findById(new Integer(key));
				}
			};

		}
		return ufList;
	}

	public void remove() {
		if( estado != null && estado.getId() != null) {
			ufService.remove(estado);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"UF " + estado.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (estado.getId() == null) {
			ufService.insert(estado);
			msg = "UF " + estado.getNome() + " criado com sucesso!";
		} else {
			ufService.update(estado);
			msg = "UF " + estado.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));

	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/ufman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novos registros " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		estado = new UF();
		filter = new Filter<UF>(new UF());
		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id do UF é obrigatório");
		}
		estado = ufCrud.get(id);
		if (estado == null) {
			throw new BusinessException("UF não foi encontrado pelo " + id);
		}
		Hibernate.initialize(estado.getPais());
	}

	public List<String> completeNome(String query) {
		List<String> result = ufService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((UF) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/ufman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"UF " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/uflst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"A lista  " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		estado = new UF();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<UF> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<UF> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public void setUfList(LazyDataModel<UF> ufList) {
        this.ufList = ufList;
    }

    public UF getEstado() {
        return estado;
    }

    public void setEstado(UF estado) {
        this.estado = estado;
    }

    public UFService getUfService() {
        return ufService;
    }

    public void setUfService(UFService ufService) {
        this.ufService = ufService;
    }

    public Crud<UF> getUfCrud() {
        return ufCrud;
    }

    public void setUfCrud(Crud<UF> ufCrud) {
        this.ufCrud = ufCrud;
    }

    public CrudService<UF> getUfCrudService() {
        return ufCrudService;
    }

    public void setUfCrudService(CrudService<UF> ufCrudService) {
        this.ufCrudService = ufCrudService;
    }

    public Filter<UF> getFilter() {
		return filter;
	}

	public void setFilter(Filter<UF> filter) {
		this.filter = filter;
	}

	public UFService getUFService() {
		return ufService;
	}

	public void setUFService(UFService ufService) {
		this.ufService = ufService;
	}

	public Crud<UF> getUFCrud() {
		return ufCrud;
	}

	public void setUFCrud(Crud<UF> ufCrud) {
		this.ufCrud = ufCrud;
	}

	public CrudService<UF> getUFCrudService() {
		return ufCrudService;
	}

	public void setUFCrudService(CrudService<UF> ufCrudService) {
		this.ufCrudService = ufCrudService;
	}

    public List<Pais> getLstPaises() {
        return lstPaises;
    }

    public void setLstPaises(List<Pais> lstPaises) {
        this.lstPaises = lstPaises;
    }

    public PaisService getPaisService() {
        return paisService;
    }

    public void setPaisService(PaisService paisService) {
        this.paisService = paisService;
    }
}
