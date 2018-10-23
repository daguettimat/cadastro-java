package br.com.global5.manager.bean.cadastro;

import br.com.global5.infra.model.Filter;
import br.com.global5.manager.model.cadastro.FichaEmail;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.cadastro.FichaEmailService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Named
@ViewAccessScoped
public class FichaEmailMB implements Serializable {

	/**
	 *
 	 */

	private static final long serialVersionUID = 1L;
	private LazyDataModel<FichaEmail> fichaList;
	private List<FichaEmail> filteredValue;
	private Integer id;
	private FichaEmail fichaEmail;
	private Filter<FichaEmail> filter = new Filter<>(new FichaEmail());
	private Usuario usuario;


	@Inject
	private FichaEmailService ficeService;

	@PostConstruct
	public void init() {

		clear();

	}

	public LazyDataModel<FichaEmail> getFichaList() {
		if( fichaList == null ) {
			fichaList = new LazyDataModel<FichaEmail>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<FichaEmail> load(int first, int pageSize,
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
					List<FichaEmail> list = ficeService.paginate(filter);
					setRowCount(ficeService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public FichaEmail getRowData(String key) {
					return ficeService.findById(new Integer(key));
				}
			};

		}
		return fichaList;
	}

	public void updateUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void remove() {
		if( fichaEmail != null && fichaEmail.getId() != null) {
			ficeService.remove(fichaEmail);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Ficha Email " + fichaEmail.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;

		if (fichaEmail.getId() == null) {

            fichaEmail.setUsuarioCriacao(usuario);
            fichaEmail.setDtCriacao(new Date());

            fichaEmail.setUsuarioAlteracao(usuario);
            fichaEmail.setDtAlteracao(new Date());

            fichaEmail.setDocumento(UUID.randomUUID().toString());

            fichaEmail.setConcluido(false);

			ficeService.insert(fichaEmail);
			msg = "Solicitação de envio da planilha de cadastro para " + fichaEmail.getNome() + " criada com sucesso!";
		} else {
            fichaEmail.setUsuarioAlteracao(usuario);
            fichaEmail.setDtAlteracao(new Date());

			ficeService.update(fichaEmail);
			msg = "Solicitação de envio da planilha de cadastro para " + fichaEmail.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));


	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/fichaemailman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Adicionar o " + getId()
                            + " não pode ser concluido. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		fichaEmail = new FichaEmail();
		filter = new Filter<>(new FichaEmail());

		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,"ERRO!","O ID é obrigatório!"));
		}
		fichaEmail = ficeService.crud().get(id);
		if (fichaEmail == null) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,"Aviso","Ficha Email " + getId()
							+ " não encontrado."));
		}
	}

	public void onRowSelect(SelectEvent event) {
		this.id = ((FichaEmail) event.getObject()).getId();

		findById(id);
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/fichaemailman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Ficha Email " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico."));
		}

	}


	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/fichaemaillst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Lista do cadastro de Emails " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico."));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		fichaEmail = new FichaEmail();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setFichaList(LazyDataModel<FichaEmail> fichaList) {
		this.fichaList = fichaList;
	}

	public List<FichaEmail> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<FichaEmail> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FichaEmail getFichaEmail() {
		return fichaEmail;
	}

	public void setFichaEmail(FichaEmail fichaEmail) {
		this.fichaEmail = fichaEmail;
	}

	public Filter<FichaEmail> getFilter() {
		return filter;
	}

	public void setFilter(Filter<FichaEmail> filter) {
		this.filter = filter;
	}

}
