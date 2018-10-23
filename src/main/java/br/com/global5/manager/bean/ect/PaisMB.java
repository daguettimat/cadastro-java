package br.com.global5.manager.bean.ect;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.ect.Pais;
import br.com.global5.manager.service.ect.PaisService;
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
public class PaisMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<Pais> paisList;
	private List<Pais> filteredValue;
	private Integer id;
	private Pais pais;
	private Filter<Pais> filter = new Filter<Pais>(new Pais());

	@Inject
	PaisService paisService;

	@Inject
	Crud<Pais> paisCrud;

	@Inject
	CrudService<Pais> paisCrudService;

	public LazyDataModel<Pais> getPaisList() {
		if( paisList == null ) {
			paisList = new LazyDataModel<Pais>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Pais> load(int first, int pageSize,
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
					List<Pais> list = paisService.paginate(filter);
					setRowCount(paisService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Pais getRowData(String key) {
					return paisService.findById(new Integer(key));
				}
			};

		}
		return paisList;
	}

	public void remove() {
		if( pais != null && pais.getId() != null) {
			paisService.remove(pais);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Pais " + pais.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (pais.getId() == null) {
			paisService.insert(pais);
			msg = "Pais " + pais.getNome() + " criado com sucesso!";
		} else {
			paisService.update(pais);
			msg = "Pais " + pais.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));


		//clear();// reload car list
	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/paisman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novos registros " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		pais = new Pais();
		filter = new Filter<Pais>(new Pais());
		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id do Pais é obrigatório");
		}
		pais = paisCrud.get(id);
		if (pais == null) {
			throw new BusinessException("Pais não foi encontrado pelo " + id);
		}
	}

	public List<String> completeNome(String query) {
		List<String> result = paisService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Pais) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/paisman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Pais " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/paislst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"A lista de paises " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		pais = new Pais();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setPaisList(LazyDataModel<Pais> paisList) {
		this.paisList = paisList;
	}

	public List<Pais> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Pais> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Filter<Pais> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Pais> filter) {
		this.filter = filter;
	}

	public PaisService getPaisService() {
		return paisService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public Crud<Pais> getPaisCrud() {
		return paisCrud;
	}

	public void setPaisCrud(Crud<Pais> paisCrud) {
		this.paisCrud = paisCrud;
	}

	public CrudService<Pais> getPaisCrudService() {
		return paisCrudService;
	}

	public void setPaisCrudService(CrudService<Pais> paisCrudService) {
		this.paisCrudService = paisCrudService;
	}
}
