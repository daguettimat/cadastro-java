package br.com.global5.manager.bean.areas;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.areas.AreaFuncao;
import br.com.global5.manager.service.areas.AreaFuncaoService;
import br.com.global5.manager.service.areas.AreaService;
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
public class AreaFuncaoMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<AreaFuncao> funcaoList;
	private List<AreaFuncao> filteredValue;
	private Integer id;
	private AreaFuncao funcao;
	private List<Area> lstAreas;
	private Filter<AreaFuncao> filter = new Filter<AreaFuncao>(new AreaFuncao());

	@Inject
	AreaFuncaoService funcaoService;

	@Inject
	Crud<AreaFuncao> funcaoCrud;

	@Inject
	CrudService<AreaFuncao> funcaoCrudService;

	@Inject
    AreaService areaService;

    @PostConstruct
    public void init() {

        lstAreas = areaService.listAll();
        clear();

    }

	public LazyDataModel<AreaFuncao> getFuncaoList() {
		if( funcaoList == null ) {
			funcaoList = new LazyDataModel<AreaFuncao>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<AreaFuncao> load(int first, int pageSize,
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
					List<AreaFuncao> list = funcaoService.paginate(filter);
					setRowCount(funcaoService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public AreaFuncao getRowData(String key) {
					return funcaoService.findById(Integer.valueOf(key));
				}
			};

		}
		return funcaoList;
	}

	public void remove() {
		if( funcao != null && funcao.getId() != null) {
			funcaoService.remove(funcao);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Função " + funcao.getNome()
							+ " removida com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (funcao.getId() == null) {
			funcaoService.insert(funcao);
			msg = "Função " + funcao.getNome() + " criada com sucesso!";
		} else {
			funcaoService.update(funcao);
			msg = "Função " + funcao.getNome() + " alterada com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));


		//clear();// reload car list
	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/funcaoman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novo nives " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		funcao = new AreaFuncao();
		filter = new Filter<AreaFuncao>(new AreaFuncao());
		funcao.setArea(new Area());
        id = null;
	}

	public void findAreaFuncaoById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id do Função é obrigatório");
		}
		funcao = funcaoCrud.get(id);
        if (funcao == null) {
			throw new BusinessException("Função não foi encontrado pelo " + id);
		}
        Hibernate.initialize(funcao.getArea());
	}

	public List<String> completeNome(String query) {
		List<String> result = funcaoService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {

		this.id = ((AreaFuncao) event.getObject()).getId();

		findAreaFuncaoById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/funcaoman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Função " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/funcaolst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de Funções " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		funcao = new AreaFuncao();
	}

	public Filter<AreaFuncao> getFilter() {
		return filter;
	}

	public void setFilter(Filter<AreaFuncao> filter) {
		this.filter = filter;
	}

	public List<AreaFuncao> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<AreaFuncao> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

    public void setFuncaoList(LazyDataModel<AreaFuncao> funcaoList) {
        this.funcaoList = funcaoList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AreaFuncao getFuncao() {
        return funcao;
    }

    public void setFuncao(AreaFuncao funcao) {
        this.funcao = funcao;
    }

    public AreaFuncaoService getFuncaoService() {
        return funcaoService;
    }

    public void setFuncaoService(AreaFuncaoService funcaoService) {
        this.funcaoService = funcaoService;
    }

    public Crud<AreaFuncao> getFuncaoCrud() {
        return funcaoCrud;
    }

    public void setFuncaoCrud(Crud<AreaFuncao> funcaoCrud) {
        this.funcaoCrud = funcaoCrud;
    }

    public CrudService<AreaFuncao> getFuncaoCrudService() {
        return funcaoCrudService;
    }

    public void setFuncaoCrudService(CrudService<AreaFuncao> funcaoCrudService) {
        this.funcaoCrudService = funcaoCrudService;
    }

    public List<Area> getLstAreas() {
        return lstAreas;
    }

    public void setLstAreas(List<Area> lstAreas) {
        this.lstAreas = lstAreas;
    }

    public AreaService getAreaService() {
        return areaService;
    }

    public void setAreaService(AreaService areaService) {
        this.areaService = areaService;
    }
}
