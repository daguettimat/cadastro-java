package br.com.global5.manager.bean.geral;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.geral.Avisos;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.geral.AvisosService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@ViewAccessScoped
public class AvisosMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<Avisos> avisoList;
	private List<Avisos> filteredValue;
	private Integer id;
	private Avisos aviso;
	private Filter<Avisos> filter = new Filter<Avisos>(new Avisos());

	private String nome;

	private Date dtInicial;
	private Date dtFinal;

	private Usuario usuario;

	@Inject
	AvisosService avisoService;

	@Inject
	Crud<Avisos> avisoCrud;

	public LazyDataModel<Avisos> getAvisoList() {
		if( avisoList == null ) {
			avisoList = new LazyDataModel<Avisos>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Avisos> load(int first, int pageSize,
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
					List<Avisos> list = avisoService.paginate(filter);
					setRowCount(avisoService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Avisos getRowData(String key) {
					return avisoService.findById(new Integer(key));
				}
			};

		}
		return avisoList;
	}

	public void remove() {
		if( aviso != null && aviso.getId() != null) {
		    aviso.setUsuarioExclusao(new Usuario(usuario.getId()));
            aviso.setDtExclusao(new Date());
			avisoService.update(aviso);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, aviso.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;
		usuario = checkUsuario.valid();
		aviso.setUsuarioAlteracao(new Usuario(usuario.getId()));
		aviso.setDtAlteracao(new Date());
		if (aviso.getId() == null) {
			avisoService.insert(aviso);
            aviso.setUsuarioCriacao(new Usuario(usuario.getId()));
            aviso.setDtCriacao(new Date());
			msg =  aviso.getNome() + " criado com sucesso!";
		} else {
			avisoService.update(aviso);
			msg =  aviso.getNome() + " alterado com sucesso!";
		}

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso",
                        msg));


		//clear();// reload car list
	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../manutencao/avisoman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir aviso " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		usuario = checkUsuario.valid();

		aviso = new Avisos();
		filter = new Filter<Avisos>(new Avisos());
		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id do Avisos é obrigatório");
		}
		aviso = avisoCrud.get(id);
		if (aviso == null) {
			throw new BusinessException("Avisos não foi encontrado pelo " + id);
		}
	}

	public List<String> completeDescricao(String query) {
		List<String> result = avisoService.getDescricao(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Avisos) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../manutencao/avisoman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Avisos " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}

	public void btnPesquisar() {

		filter.getEntity().setId(id);
		filter.getEntity().setNome(nome);
		filter.getEntity().setDtInicial(dtInicial);
		filter.getEntity().setDtFinal(dtFinal);
		RequestContext.getCurrentInstance().execute("PF('avisoTable').filter()");

	}

	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../manutencao/avisolst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de avisos " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setAvisoList(LazyDataModel<Avisos> avisoList) {
		this.avisoList = avisoList;
	}

	public List<Avisos> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Avisos> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Avisos getAviso() {
		return aviso;
	}

	public void setAviso(Avisos aviso) {
		this.aviso = aviso;
	}

	public Filter<Avisos> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Avisos> filter) {
		this.filter = filter;
	}

	public AvisosService getAvisoService() {
		return avisoService;
	}

	public void setAvisoService(AvisosService avisoService) {
		this.avisoService = avisoService;
	}

	public Crud<Avisos> getAvisoCrud() {
		return avisoCrud;
	}

	public void setAvisoCrud(Crud<Avisos> avisoCrud) {
		this.avisoCrud = avisoCrud;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}
}
