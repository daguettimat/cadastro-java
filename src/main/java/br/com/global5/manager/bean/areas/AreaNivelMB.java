package br.com.global5.manager.bean.areas;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;

import br.com.global5.manager.model.areas.AreaNivel;
import br.com.global5.manager.service.areas.AreaNivelService;
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
public class AreaNivelMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<AreaNivel> nivelList;
	private List<AreaNivel> filteredValue;
	private Integer id;
	private AreaNivel nivel;
	private Filter<AreaNivel> filter = new Filter<AreaNivel>(new AreaNivel());

	@Inject
	AreaNivelService nivelService;

	@Inject
	Crud<AreaNivel> nivelCrud;

	@Inject
	CrudService<AreaNivel> nivelCrudService;

	public LazyDataModel<AreaNivel> getNivelList() {
		if( nivelList == null ) {
			nivelList = new LazyDataModel<AreaNivel>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<AreaNivel> load(int first, int pageSize,
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
					List<AreaNivel> list = nivelService.paginate(filter);
					setRowCount(nivelService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public AreaNivel getRowData(String key) {
					return nivelService.findById(new Integer(key));
				}
			};

		}
		return nivelList;
	}

	public void remove() {
		if( nivel != null && nivel.getId() != null) {
			nivelService.remove(nivel);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Nivel " + nivel.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (nivel.getId() == null) {
			nivelService.insert(nivel);
			msg = "Nivel " + nivel.getNome() + " criado com sucesso!";
		} else {
			nivelService.update(nivel);
			msg = "Nivel " + nivel.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));


		//clear();// reload car list
	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/nivelman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novo nives " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		nivel = new AreaNivel();
		filter = new Filter<AreaNivel>(new AreaNivel());
		id = null;
	}

	public void findAreaNivelById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id do Nivel é obrigatório");
		}
		nivel = nivelCrud.get(id);
		if (nivel == null) {
			throw new BusinessException("Nivel não foi encontrado pelo id: " + id);
		}
	}

	public List<String> completeNome(String query) {
		List<String> result = nivelService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((AreaNivel) event.getObject()).getId());

		findAreaNivelById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/nivelman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Nivel " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/nivellst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de Níveis " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		nivel = new AreaNivel();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setNivelList(LazyDataModel<AreaNivel> nivelList) {
		this.nivelList = nivelList;
	}

	public List<AreaNivel> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<AreaNivel> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AreaNivel getNivel() {
		return nivel;
	}

	public void setNivel(AreaNivel nivel) {
		this.nivel = nivel;
	}

	public Filter<AreaNivel> getFilter() {
		return filter;
	}

	public void setFilter(Filter<AreaNivel> filter) {
		this.filter = filter;
	}

	public AreaNivelService getNivelService() {
		return nivelService;
	}

	public void setNivelService(AreaNivelService nivelService) {
		this.nivelService = nivelService;
	}

	public Crud<AreaNivel> getNivelCrud() {
		return nivelCrud;
	}

	public void setNivelCrud(Crud<AreaNivel> nivelCrud) {
		this.nivelCrud = nivelCrud;
	}

	public CrudService<AreaNivel> getNivelCrudService() {
		return nivelCrudService;
	}

	public void setNivelCrudService(CrudService<AreaNivel> nivelCrudService) {
		this.nivelCrudService = nivelCrudService;
	}
}
