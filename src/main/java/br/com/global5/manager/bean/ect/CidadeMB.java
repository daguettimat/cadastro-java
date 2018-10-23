package br.com.global5.manager.bean.ect;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.ect.Cidade;
import br.com.global5.manager.model.ect.Pais;
import br.com.global5.manager.model.ect.UF;
import br.com.global5.manager.service.ect.CidadeService;
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
public class CidadeMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<Cidade> cidadeList;
	private List<Cidade> filteredValue;
	private Integer id;
	private Cidade cidade;
	private Filter<Cidade> filter = new Filter<Cidade>(new Cidade());
	private List<Pais> lstPaises;
    private List<UF> lstEstados;
    private String pais;

	@Inject
	CidadeService cidadeService;

	@Inject
	PaisService paisService;

    @Inject
    UFService ufService;

	@Inject
	Crud<Cidade> cidadeCrud;

	@Inject
	CrudService<Cidade> cidadeCrudService;


	@PostConstruct
	public void init() {

		lstPaises = paisService.listAll();
		clear();

	}

	public void carregaUF() {

	    lstEstados = ufService.crud().eq("pais.id",cidade.getPais().getId()).list();


    }

	public LazyDataModel<Cidade> getCidadeList() {
		if( cidadeList == null ) {
			cidadeList = new LazyDataModel<Cidade>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Cidade> load(int first, int pageSize,
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
					List<Cidade> list = cidadeService.paginate(filter);
					setRowCount(cidadeService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Cidade getRowData(String key) {
					Cidade cidade = cidadeService.findById(new Integer(key));
					Hibernate.initialize(cidade.getUf());
				    return cidade;
				}
			};

		}
		return cidadeList;
	}

	public void remove() {
		if( cidade != null && cidade.getId() != null) {
			cidadeService.remove(cidade);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Cidade " + cidade.getNome()
							+ " removida com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (cidade.getId() == null) {
			cidadeService.insert(cidade);
			msg = "Cidade " + cidade.getNome() + " criada com sucesso!";
		} else {
			cidadeService.update(cidade);
			msg = "Cidade " + cidade.getNome() + " alterada com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));

	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/cidademan.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novos registros " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		cidade = new Cidade();
		filter = new Filter<Cidade>(new Cidade());
		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id é obrigatório");
		}
		cidade = cidadeCrud.get(id);
		if (cidade == null) {
			throw new BusinessException("O id não foi encontrado pelo número :" + id);
		}
		carregaUF();
        Hibernate.initialize(cidade.getPais());
		Hibernate.initialize(cidade.getUf());
	}

	public List<String> completeNome(String query) {
		List<String> result = cidadeService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Cidade) event.getObject()).getId());

		findById(this.id);
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/cidademan.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Cidade " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/cidadelst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"A lista  " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		cidade = new Cidade();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setCidadeList(LazyDataModel<Cidade> cidadeList) {
		this.cidadeList = cidadeList;
	}

	public List<Cidade> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Cidade> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Filter<Cidade> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Cidade> filter) {
		this.filter = filter;
	}

	public CidadeService getCidadeService() {
		return cidadeService;
	}

	public void setCidadeService(CidadeService cidadeService) {
		this.cidadeService = cidadeService;
	}

	public Crud<Cidade> getCidadeCrud() {
		return cidadeCrud;
	}

	public void setCidadeCrud(Crud<Cidade> cidadeCrud) {
		this.cidadeCrud = cidadeCrud;
	}

	public CrudService<Cidade> getCidadeCrudService() {
		return cidadeCrudService;
	}

	public void setCidadeCrudService(CrudService<Cidade> cidadeCrudService) {
		this.cidadeCrudService = cidadeCrudService;
	}

    public List<Pais> getLstPaises() {
        return lstPaises;
    }

    public void setLstPaises(List<Pais> lstPaises) {
        this.lstPaises = lstPaises;
    }

    public List<UF> getLstEstados() {
        return lstEstados;
    }

    public void setLstEstados(List<UF> lstEstados) {
        this.lstEstados = lstEstados;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public PaisService getPaisService() {
        return paisService;
    }

    public void setPaisService(PaisService paisService) {
        this.paisService = paisService;
    }

    public UFService getUfService() {
        return ufService;
    }

    public void setUfService(UFService ufService) {
        this.ufService = ufService;
    }
}
