package br.com.global5.manager.bean.ect;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.ect.*;
import br.com.global5.manager.service.ect.*;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
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
public class LogradouroMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<Logradouro> logrList;
	private List<Logradouro> filteredValue;
	private Integer id;
	private Logradouro logr;
	private Filter<Logradouro> filter = new Filter<Logradouro>(new Logradouro());
	private List<Pais> lstPaises;
	private List<UF> lstEstados;
	private List<Cidade> lstCidades;
	private List<Bairro> lstBairros;

	@Inject
	LogradouroService logrService;

	@Inject
	BairroService bairroService;

	@Inject
	CidadeService cidadeService;

	@Inject
	UFService ufService;

	@Inject
	PaisService paisService;

	@Inject
	Crud<Logradouro> logrCrud;

	@Inject
	CrudService<Logradouro> logrCrudService;

	@PostConstruct
	public void init() {

		lstPaises = paisService.listAll();
		clear();

	}

	public List<UF> carregaUF() {

		lstEstados  = ufService.crud().eq("pais.id",logr.getCidade().getPais().getId()).list();
		return lstEstados;

	}


	public List<Cidade> carregaCidade() {

		lstCidades = cidadeService.crud().addCriterion(Restrictions.eq("pais.id", logr.getCidade().getPais().getId()))
				.addCriterion(Restrictions.eq("uf.id", logr.getCidade().getUf().getId())).list();

		return lstCidades;

	}

	public List<Bairro> carregaBairro() {

		lstBairros = bairroService.crud()
				.addCriterion(Restrictions.eq("cidade.id", logr.getCidade().getId())).list();

		return lstBairros;

	}


	public LazyDataModel<Logradouro> getLogrList() {
		if( logrList == null ) {
			logrList = new LazyDataModel<Logradouro>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Logradouro> load(int first, int pageSize,
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
					List<Logradouro> list = logrService.paginate(filter);
					setRowCount(logrService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Logradouro getRowData(String key) {
					Logradouro logr = logrService.findById(new Integer(key));
					Hibernate.initialize(logr.getCidade());
					Hibernate.initialize(logr.getCidade().getUf());
					Hibernate.initialize(logr.getIniBairro());
					return logr;
				}
			};

		}
		return logrList;
	}

	public void remove() {
		if( logr != null && logr.getId() != null) {
			logrService.remove(logr);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Logradouro " + logr.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (logr.getId() == null) {
			logrService.insert(logr);
			msg = "Logradouro " + logr.getNome() + " criado com sucesso!";
		} else {
			logrService.update(logr);
			msg = "Logradouro " + logr.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));

	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/logradouroman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novos registros " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		logr = new Logradouro();
		filter = new Filter<Logradouro>(new Logradouro());
		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id do Logradouro é obrigatório");
		}
		logr = logrCrud.get(id);
		if (logr == null) {
			throw new BusinessException("Logradouro não foi encontrado pelo " + id);
		}
		Hibernate.initialize(logr.getCidade().getPais());
		Hibernate.initialize(logr.getCidade().getUf());
		Hibernate.initialize(logr.getIniBairro());
		carregaUF();
		carregaCidade();
		carregaUF();
        carregaBairro();

	}

	public List<String> completeNome(String query) {
		List<String> result = logrService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Logradouro) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/logradouroman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Logradouro " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/logradourolst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"A lista  " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		logr = new Logradouro();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setLogradouroList(LazyDataModel<Logradouro> logrList) {
		this.logrList = logrList;
	}

	public List<Logradouro> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Logradouro> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public void setLogrList(LazyDataModel<Logradouro> logrList) {
		this.logrList = logrList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Logradouro getLogr() {
		return logr;
	}

	public void setLogr(Logradouro logr) {
		this.logr = logr;
	}

	public Filter<Logradouro> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Logradouro> filter) {
		this.filter = filter;
	}

	public LogradouroService getLogrService() {
		return logrService;
	}

	public void setLogrService(LogradouroService logrService) {
		this.logrService = logrService;
	}

	public Crud<Logradouro> getLogrCrud() {
		return logrCrud;
	}

	public void setLogrCrud(Crud<Logradouro> logrCrud) {
		this.logrCrud = logrCrud;
	}

	public CrudService<Logradouro> getLogrCrudService() {
		return logrCrudService;
	}

	public void setLogrCrudService(CrudService<Logradouro> logrCrudService) {
		this.logrCrudService = logrCrudService;
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

	public List<Cidade> getLstCidades() {
		return lstCidades;
	}

	public void setLstCidades(List<Cidade> lstCidades) {
		this.lstCidades = lstCidades;
	}

	public List<Bairro> getLstBairros() {
		return lstBairros;
	}

	public void setLstBairros(List<Bairro> lstBairros) {
		this.lstBairros = lstBairros;
	}

	public BairroService getBairroService() {
		return bairroService;
	}

	public void setBairroService(BairroService bairroService) {
		this.bairroService = bairroService;
	}

	public CidadeService getCidadeService() {
		return cidadeService;
	}

	public void setCidadeService(CidadeService cidadeService) {
		this.cidadeService = cidadeService;
	}

	public UFService getUfService() {
		return ufService;
	}

	public void setUfService(UFService ufService) {
		this.ufService = ufService;
	}

	public PaisService getPaisService() {
		return paisService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}


}
