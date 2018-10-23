package br.com.global5.manager.bean.ect;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.ect.Bairro;
import br.com.global5.manager.model.ect.Cidade;
import br.com.global5.manager.model.ect.Pais;
import br.com.global5.manager.model.ect.UF;
import br.com.global5.manager.service.ect.BairroService;
import br.com.global5.manager.service.ect.CidadeService;
import br.com.global5.manager.service.ect.PaisService;
import br.com.global5.manager.service.ect.UFService;
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
public class BairroMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<Bairro> bairroList;
	private List<Bairro> filteredValue;
	private Integer id;
	private Bairro bairro;
	private Filter<Bairro> filter = new Filter<Bairro>(new Bairro());
    private List<Pais> lstPaises;
    private List<UF> lstEstados;
    private List<Cidade> lstCidades;

	@Inject
	BairroService bairroService;

    @Inject
    CidadeService cidadeService;

    @Inject
    UFService ufService;

    @Inject
    PaisService paisService;

	@Inject
	Crud<Bairro> bairroCrud;

	@Inject
	CrudService<Bairro> bairroCrudService;

    @PostConstruct
    public void init() {

        lstPaises = paisService.listAll();
        clear();

    }

    public List<UF> carregaUF() {

        lstEstados  = ufService.crud().eq("pais.id",bairro.getCidade().getPais().getId()).list();
        return lstEstados;

    }


    public List<Cidade> carregaCidade() {

    	lstCidades = cidadeService.crud().addCriterion(Restrictions.eq("pais.id", bairro.getCidade().getPais().getId()))
				.addCriterion(Restrictions.eq("uf.id", bairro.getCidade().getUf().getId())).list();

        return lstCidades;

    }

	public LazyDataModel<Bairro> getBairroList() {
		if( bairroList == null ) {
			bairroList = new LazyDataModel<Bairro>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Bairro> load(int first, int pageSize,
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
					List<Bairro> list = bairroService.paginate(filter);
					setRowCount(bairroService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Bairro getRowData(String key) {
					Bairro bairro = bairroService.findById(new Integer(key));
					Hibernate.initialize(bairro.getCidade());
					Hibernate.initialize(bairro.getCidade().getUf());
				    return bairro;
				}
			};

		}
		return bairroList;
	}

	public void remove() {
		if( bairro != null && bairro.getId() != null) {
			bairroService.remove(bairro);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Bairro " + bairro.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (bairro.getId() == null) {
			bairroService.insert(bairro);
			msg = "Bairro " + bairro.getNome() + " criado com sucesso!";
		} else {
			bairroService.update(bairro);
			msg = "Bairro " + bairro.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));


	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/bairroman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novos registros " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		bairro = new Bairro();
		filter = new Filter<Bairro>(new Bairro());
		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id do Bairro é obrigatório");
		}
		bairro = bairroCrud.get(id);
		if (bairro == null) {
			throw new BusinessException("Bairro não foi encontrado pelo " + id);
		}
		carregaUF();
		carregaCidade();
        Hibernate.initialize(bairro.getCidade());
		Hibernate.initialize(bairro.getCidade().getUf());
		Hibernate.initialize(bairro.getCidade().getPais());
	}

	public List<String> completeNome(String query) {
		List<String> result = bairroService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Bairro) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/bairroman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Bairro " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../ect/bairrolst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"A lista  " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		bairro = new Bairro();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setBairroList(LazyDataModel<Bairro> bairroList) {
		this.bairroList = bairroList;
	}

	public List<Bairro> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Bairro> filteredValue) {
		this.filteredValue = filteredValue;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public Filter<Bairro> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Bairro> filter) {
		this.filter = filter;
	}

	public BairroService getBairroService() {
		return bairroService;
	}

	public void setBairroService(BairroService bairroService) {
		this.bairroService = bairroService;
	}

	public Crud<Bairro> getBairroCrud() {
		return bairroCrud;
	}

	public void setBairroCrud(Crud<Bairro> bairroCrud) {
		this.bairroCrud = bairroCrud;
	}

	public CrudService<Bairro> getBairroCrudService() {
		return bairroCrudService;
	}

	public void setBairroCrudService(CrudService<Bairro> bairroCrudService) {
		this.bairroCrudService = bairroCrudService;
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

	public List<Cidade> getLstCidades() {
		return lstCidades;
	}

	public void setLstCidades(List<Cidade> lstCidades) {
		this.lstCidades = lstCidades;
	}
}
