package br.com.global5.manager.bean.areas;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
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
public class AreaMB implements Serializable {

	/**
	 *
	 */
	private static final Long serialVersionUID = 1L;
	private LazyDataModel<Area> areaList;
	private List<Area> lstAreas;
	private List<Area> filteredValue;
	private Integer id;
	private Area area;
	private Filter<Area> filter = new Filter<Area>(new Area());
	private Integer areaID;

	@Inject
	AreaService areaService;

	@Inject
	Crud<Area> areaCrud;

	@Inject
	CrudService<Area> areaCrudService;

    @PostConstruct
    public void init() {

        lstAreas = areaService.listAll();

    }

	public LazyDataModel<Area> getAreaList() {
		if( areaList == null ) {
			areaList = new LazyDataModel<Area>() {

				@SuppressWarnings("unchecked")
				@Override
				public List<Area> load(int first, int pageSize,
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
					List<Area> list = areaService.paginate(filter);
					setRowCount(areaService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Area getRowData(String key) {
					return areaService.findById(new Integer(key));
				}
			};

		}
		return areaList;
	}

	public void remove() {
		if( area != null && area.getId() != null) {
			areaService.remove(area);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Area " + area.getNome()
							+ " removida com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		if (area.getId() == null) {
			areaService.insert(area);
			msg = "Area " + area.getNome() + " criada com sucesso!";
		} else {
			areaService.update(area);
			msg = "Area " + area.getNome() + " alterada com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));


		//clear();// reload car list
	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/areaman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novas areas " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		area = new Area();
		filter = new Filter<Area>(new Area());
		id = null;
	}

	public void findAreaById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id do Area é obrigatório");
		}
		area = areaCrud.get(id);

		if (area == null) {
			throw new BusinessException("Área não foi encontrado pelo " + id);
		}
	}

	public List<String> completeNome(String query) {
		List<String> result = areaService.getNome(query);
		return result;
	}

	public List<Area> listArea(String query) {
    	return areaService.listNome(query,2);
	}
	public List<Area> listFilial(String query) {
		return areaService.listNome(query,3);
	}


	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Area) event.getObject()).getId());

		findAreaById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/areaman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Area " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../areas/arealst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de Áreas " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

    public List<Area> getLstAreas() {
        return lstAreas;
    }

    public void setLstAreas(List<Area> lstAreas) {
        this.lstAreas = lstAreas;
    }

    public void onRowUnselect(UnselectEvent event) {
		area = new Area();
	}

	public Filter<Area> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Area> filter) {
		this.filter = filter;
	}

	public List<Area> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Area> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public static Long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setAreaList(LazyDataModel<Area> areaList) {
		this.areaList = areaList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public AreaService getAreaService() {
		return areaService;
	}

	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}

	public Crud<Area> getAreaCrud() {
		return areaCrud;
	}

	public void setAreaCrud(Crud<Area> areaCrud) {
		this.areaCrud = areaCrud;
	}

	public CrudService<Area> getAreaCrudService() {
		return areaCrudService;
	}

	public void setAreaCrudService(CrudService<Area> areaCrudService) {
		this.areaCrudService = areaCrudService;
	}

    public Integer getAreaID() {
        return areaID;
    }

    public void setAreaID(Integer areaID) {
        this.areaID = areaID;
    }
}
