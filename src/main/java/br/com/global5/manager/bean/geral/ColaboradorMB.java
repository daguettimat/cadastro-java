package br.com.global5.manager.bean.geral;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.geral.Colaborador;
import br.com.global5.manager.service.geral.ColaboradorService;
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
public class ColaboradorMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<Colaborador> colList;
	private List<Colaborador> filteredValue;
	private Long id;
	private Colaborador colaborador;
	private Filter<Colaborador> filter = new Filter<Colaborador>(new Colaborador());

	@Inject
	ColaboradorService colService;

	@Inject
	Crud<Colaborador> colCrud;

	@Inject
	CrudService<Colaborador> colCrudService;

	public LazyDataModel<Colaborador> getColList() {
		if( colList == null ) {
			colList = new LazyDataModel<Colaborador>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Colaborador> load(int first, int pageSize,
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
					List<Colaborador> list = colService.paginate(filter);
					setRowCount(colService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Colaborador getRowData(String key) {
					return colService.findById(new Long(key));
				}
			};

		}
		return colList;
	}

	public void remove() {
		if( colaborador != null && colaborador.getId() != null) {
			colService.remove(colaborador);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Colaborador " + colaborador.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (colaborador.getId() == null) {
			colService.insert(colaborador);
			msg = "Colaborador " + colaborador.getNome() + " criado com sucesso!";
		} else {
			colService.update(colaborador);
			msg = "Colaborador " + colaborador.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));


		//clear();// reload car list
	}

	public void clear() {
		colaborador = new Colaborador();
		filter = new Filter<Colaborador>(new Colaborador());
		id = null;
	}

	public void insert() {
		try {
			clear();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../man/colaborador.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novo colaborador " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}


	public void findColaboradorById(Long id) {
		if (id == null) {
			throw new BusinessException("O id do Colaborador é obrigatório");
		}
		colaborador = colCrud.get(id);
		if (colaborador == null) {
			throw new BusinessException("Colaborador não foi encontrado pelo " + id);
		}
	}

	public List<String> completeDescricao(String query) {
		List<String> result = colService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Long.valueOf(((Colaborador) event.getObject()).getId());

		findColaboradorById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../man/colaborador.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Colaborador " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}

	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../list/colaborador.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de Colaboradores " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		colaborador = new Colaborador();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setColList(LazyDataModel<Colaborador> colList) {
		this.colList = colList;
	}

	public List<Colaborador> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Colaborador> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Filter<Colaborador> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Colaborador> filter) {
		this.filter = filter;
	}

	public ColaboradorService getColService() {
		return colService;
	}

	public void setColService(ColaboradorService colService) {
		this.colService = colService;
	}

	public Crud<Colaborador> getColCrud() {
		return colCrud;
	}

	public void setColCrud(Crud<Colaborador> colCrud) {
		this.colCrud = colCrud;
	}

	public CrudService<Colaborador> getColCrudService() {
		return colCrudService;
	}

	public void setColCrudService(CrudService<Colaborador> colCrudService) {
		this.colCrudService = colCrudService;
	}
}
