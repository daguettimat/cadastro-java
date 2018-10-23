package br.com.global5.manager.bean.geral;

import br.com.global5.infra.Crud;
import br.com.global5.infra.CrudService;
import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.model.auxiliar.TipoUsuario;
import br.com.global5.manager.service.geral.UsuarioService;
import br.com.global5.manager.service.auxiliar.TipoUsuarioService;
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
public class UsuarioMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<Usuario> usuarioList;
	private List<Usuario> filteredValue;
	private List<TipoUsuario> lstTipo;
	private Integer id;
	private Usuario usuario;
	private String confirmarSenha;
	private Filter<Usuario> filter = new Filter<Usuario>(new Usuario());
	private boolean Externo;


	@Inject
	TipoUsuarioService tpuService;

	@Inject
	UsuarioService usuarioService;

	@Inject
	Crud<Usuario> usuarioCrud;

	@Inject
	CrudService<Usuario> usuarioCrudService;

	@PostConstruct
	public void init() {
		lstTipo = null; // tpuService.crud().isNull("exclusao").listAll();
		confirmarSenha = null;
		usuario = new Usuario();
	}

	public LazyDataModel<Usuario> getUsuarioList() {
		if( usuarioList == null ) {
			usuarioList = new LazyDataModel<Usuario>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<Usuario> load(int first, int pageSize,
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
					List<Usuario> list = usuarioService.paginate(filter);
					setRowCount(usuarioService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public Usuario getRowData(String key) {
					return usuarioService.findById(new Integer(key));
				}
			};

		}
		return usuarioList;
	}

	public void remove() {
		if( usuario != null && usuario.getId() != null) {
			usuarioService.remove(usuario);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Usuário " + usuario.getNome()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;

        usuario.setExterno(Externo);
        usuario.setPasswd(AppUtils.toMd5(usuario.getLogin().toLowerCase()+usuario.getEmail()));

		if (usuario.getId() == null) {
			usuarioService.insert(usuario);
			msg = "Usuário " + usuario.getNome() + " criado com sucesso!";
		} else {
			usuarioService.update(usuario);
			msg = "Usuário " + usuario.getNome() + " alterado com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,msg,null));


	}

	public void insert() {
		try {
			clear();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/usuariosman.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir novos usuarios " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}


	public void clear() {
		usuario = new Usuario();
		filter = new Filter<Usuario>(new Usuario());
		id = null;
		confirmarSenha = null;
	}

	public void findUsuarioById(Integer id) {
	    clear();
		if (id == null) {
			throw new BusinessException("O id do Usuário é obrigatório");
		}
		usuario = usuarioCrud.get(id);
		if (usuario == null) {
			throw new BusinessException("Usuário não foi encontrado pelo " + id);
		}
        Externo = usuario.getExterno();
        Hibernate.initialize(usuario.getTipo());
	}

	public List<String> completeDescricao(String query) {
		List<String> result = usuarioService.getNome(query);
		return result;
	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((Usuario) event.getObject()).getId());

		findUsuarioById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/usuariosman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Usuário " + getId()
							+ " não pode ser carregado. Informe ao suporte técnico.",null));
		}

	}

	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../cadastro/usuarioslst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de Usuários " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		usuario = new Usuario();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setUsuarioList(LazyDataModel<Usuario> usuarioList) {
		this.usuarioList = usuarioList;
	}

	public List<Usuario> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Usuario> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Filter<Usuario> getFilter() {
		return filter;
	}

	public void setFilter(Filter<Usuario> filter) {
		this.filter = filter;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Crud<Usuario> getUsuarioCrud() {
		return usuarioCrud;
	}

	public void setUsuarioCrud(Crud<Usuario> usuarioCrud) {
		this.usuarioCrud = usuarioCrud;
	}

	public CrudService<Usuario> getUsuarioCrudService() {
		return usuarioCrudService;
	}

	public void setUsuarioCrudService(CrudService<Usuario> usuarioCrudService) {
		this.usuarioCrudService = usuarioCrudService;
	}

    public List<TipoUsuario> getLstTipo() {
        return lstTipo;
    }

    public void setLstTipo(List<TipoUsuario> lstTipo) {
        this.lstTipo = lstTipo;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

	public TipoUsuarioService getTpuService() {
		return tpuService;
	}

	public void setTpuService(TipoUsuarioService tpuService) {
		this.tpuService = tpuService;
	}

	public boolean isExterno() {
        return Externo;
    }

    public void setExterno(boolean externo) {
        Externo = externo;
    }
}
