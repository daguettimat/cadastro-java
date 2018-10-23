package br.com.global5.manager.bean.cadastro;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.bean.geral.LogonMB;
import br.com.global5.manager.model.cadastro.Marca;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.cadastro.MarcaService;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@ViewAccessScoped
public class MarcaMB implements Serializable {

	/**
	 *
	 */

	private static final long serialVersionUID = 1L;
	private LazyDataModel<Marca> marcaList;
	private List<Marca> filteredValue;
	private Integer id;
	private Marca marca;
	private Filter<Marca> filter = new Filter<Marca>(new Marca());
    private Usuario usuario;

   
	@Inject
	MarcaService marcaService;

	@Inject
	Crud<Marca> marcaCrud;

	@Inject
	CrudService<Marca> marcaCrudService;

	@PostConstruct
	public void init() {

		clear();



	}

	public LazyDataModel<Marca> getMarcaList() {
		if( marcaList == null ) {
			marcaList = new LazyDataModel<Marca>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Marca> load(int first, int pageSize,
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
					List<Marca> list = marcaService.paginate(filter);
					setRowCount(marcaService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Marca getRowData(String key) {
					return marcaService.findById(new Integer(key));
				}
			};

		}
		return marcaList;
	}

	public void remove() {
		if( marca != null && marca.getId() != null) {
			marcaService.remove(marca);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Marca " + marca.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;

		if (marca.getId() == null) {
			this.marca.setUsuCriacao(checkUsuario.valid());
			this.marca.setDtCriacao(new Date());
			this.marca.setUsuAlteracao(checkUsuario.valid());
			this.marca.setDtAlteracao(new Date());
			marcaService.insert(marca);
			msg = "Cadastro da Marca " + marca.getNome() + " criado com sucesso!";
		} else {
		    this.marca.setUsuAlteracao(checkUsuario.valid());
		    this.marca.setDtAlteracao(new Date());
			marcaService.update(marca);
			msg = "Cadastro da Marca " + marca.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));


	}

	public void insert() {
		try {
			clear();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/marcaman.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir marcas " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}
	}

	public void clear() {
        usuario = checkUsuario.valid();
		marca = new Marca();
		filter = new Filter<Marca>(new Marca());
		id = null;
		
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id da Marca é obrigatório");
		}
		marca = marcaCrud.get(id);
		if (marca == null) {
			throw new BusinessException("Marca não foi encontrado pelo id: " + id);
		}
	}

	public List<String> completeNome(String query) {
		List<String> result = marcaService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Marca) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/marcaman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Marca " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/marcalst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista do cadastro de marcas " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		marca = new Marca();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setMarcaList(LazyDataModel<Marca> marcaList) {
		this.marcaList = marcaList;
	}

	public List<Marca> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Marca> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public Filter<Marca> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Marca> filter) {
		this.filter = filter;
	}

	public MarcaService getMarcaService() {
		return marcaService;
	}

	public void setMarcaService(MarcaService marcaService) {
		this.marcaService = marcaService;
	}

	public Crud<Marca> getMarcaCrud() {
		return marcaCrud;
	}

	public void setMarcaCrud(Crud<Marca> marcaCrud) {
		this.marcaCrud = marcaCrud;
	}

	public CrudService<Marca> getMarcaCrudService() {
		return marcaCrudService;
	}

	public void setMarcaCrudService(CrudService<Marca> marcaCrudService) {
		this.marcaCrudService = marcaCrudService;
	}

	
	}
