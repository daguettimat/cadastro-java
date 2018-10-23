package br.com.global5.manager.bean.geral;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.geral.Pagina;
import br.com.global5.manager.service.geral.PaginaService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
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
public class PaginaMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<Pagina> pagList;
	private List<Pagina> lstPaginas;
	private List<Pagina> filteredValue;
	private Integer id;
	private Pagina pagina;
	private Filter<Pagina> filter = new Filter<Pagina>(new Pagina());

	@Inject
	PaginaService pagService;

	@Inject
	Crud<Pagina> pagCrud;

	@Inject
	CrudService<Pagina> pagCrudService;

	@PostConstruct
	public void init() {

		filter.addParam("tipo", "M");

		lstPaginas = pagService.crud().eq("tipo", "H").or().eq("tipo", "M")
                .list();
        clear();

	}

	public LazyDataModel<Pagina> getPagList() {
		if( pagList == null ) {
			pagList = new LazyDataModel<Pagina>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Pagina> load(int first, int pageSize,
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
					List<Pagina> list = pagService.paginate(filter);
					setRowCount(pagService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Pagina getRowData(String key) {
					return pagService.findById(new Integer(key));
				}
			};

		}
		return pagList;
	}



	public void remove() {
		if( pagina != null && pagina.getId() != null) {
			pagService.remove(pagina);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Pagina " + pagina.getDescricao()
							+ " removida com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (pagina.getId() == null) {
			pagService.insert(pagina);
			msg = "Pagina " + pagina.getDescricao() + " criada com sucesso!";
		} else {
			pagService.update(pagina);
			msg = "Pagina " + pagina.getDescricao() + " alterada com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));


		//clear();
	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/paginaman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novas paginas " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		pagina = new Pagina();
		filter = new Filter<Pagina>(new Pagina());
		id = null;
	}

	public void findPaginaById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id da Pagina é obrigatório");
		}
		pagina = pagCrud.get(id);
		if (pagina == null) {
			throw new BusinessException("Pagina não foi encontrada pelo id:" + id);
		}
        Hibernate.initialize(pagina.getRoot());
	}

	public List<String> completeDescricao(String query) {
		List<String> result = pagService.getDescricao(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = ((Pagina) event.getObject()).getId();

		findPaginaById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/paginaman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Pagina " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/paginalst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de paginas " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		pagina = new Pagina();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setPagList(LazyDataModel<Pagina> pagList) {
		this.pagList = pagList;
	}

	public List<Pagina> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Pagina> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Pagina getPagina() {
		return pagina;
	}

	public void setPagina(Pagina pagina) {
		this.pagina = pagina;
	}

	public Filter<Pagina> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Pagina> filter) {
		this.filter = filter;
	}

	public PaginaService getPagService() {
		return pagService;
	}

	public void setPagService(PaginaService pagService) {
		this.pagService = pagService;
	}

	public Crud<Pagina> getPagCrud() {
		return pagCrud;
	}

	public void setPagCrud(Crud<Pagina> pagCrud) {
		this.pagCrud = pagCrud;
	}

	public CrudService<Pagina> getPagCrudService() {
		return pagCrudService;
	}

	public void setPagCrudService(CrudService<Pagina> pagCrudService) {
		this.pagCrudService = pagCrudService;
	}

    public List<Pagina> getLstPaginas() {
        return lstPaginas;
    }

    public void setLstPaginas(List<Pagina> lstPaginas) {
        this.lstPaginas = lstPaginas;
    }
}
