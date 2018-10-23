package br.com.global5.manager.bean.geral;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.geral.Banco;
import br.com.global5.manager.service.geral.BancoService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.event.SelectEvent;
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
public class BancoMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<Banco> bcoList;
	private List<Banco> filteredValue;
	private Long id;
	private Banco banco;
	private Filter<Banco> filter = new Filter<Banco>(new Banco());

	@Inject
	BancoService bcoService;

	@Inject
	Crud<Banco> bcoCrud;

	@Inject
	CrudService<Banco> bcoCrudService;

	public LazyDataModel<Banco> getBcoList() {
		if( bcoList == null ) {
			bcoList = new LazyDataModel<Banco>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Banco> load(int first, int pageSize,
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
					List<Banco> list = bcoService.paginate(filter);
					setRowCount(bcoService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Banco getRowData(String key) {
					return bcoService.findById(new Long(key));
				}
			};

		}
		return bcoList;
	}

	public void remove() {
		if( banco != null && banco.getId() != null) {
			bcoService.remove(banco);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Banco " + banco.getDescricao()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (banco.getId() == null) {
			bcoService.insert(banco);
			msg = "Banco " + banco.getDescricao() + " criado com sucesso!";
		} else {
			bcoService.update(banco);
			msg = "Banco " + banco.getDescricao() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));


		//clear();// reload car list
	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../man/banco.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novos tipos de bancos " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		banco = new Banco();
		filter = new Filter<Banco>(new Banco());
		id = null;
	}

	public void findBancoById(Long id) {
		if (id == null) {
			throw new BusinessException("O id do Banco é obrigatório");
		}
		banco = bcoCrud.get(id);
		if (banco == null) {
			throw new BusinessException("Banco não foi encontrado pelo " + id);
		}
	}

	public List<String> completeDescricao(String query) {
		List<String> result = bcoService.getDescricao(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Long.valueOf(((Banco) event.getObject()).getId());

		findBancoById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../man/banco.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Banco " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../list/banco.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de Tipos de bancos " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setBcoList(LazyDataModel<Banco> bcoList) {
		this.bcoList = bcoList;
	}

	public List<Banco> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Banco> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Filter<Banco> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Banco> filter) {
		this.filter = filter;
	}

	public BancoService getBcoService() {
		return bcoService;
	}

	public void setBcoService(BancoService bcoService) {
		this.bcoService = bcoService;
	}

	public Crud<Banco> getBcoCrud() {
		return bcoCrud;
	}

	public void setBcoCrud(Crud<Banco> bcoCrud) {
		this.bcoCrud = bcoCrud;
	}

	public CrudService<Banco> getBcoCrudService() {
		return bcoCrudService;
	}

	public void setBcoCrudService(CrudService<Banco> bcoCrudService) {
		this.bcoCrudService = bcoCrudService;
	}
}
