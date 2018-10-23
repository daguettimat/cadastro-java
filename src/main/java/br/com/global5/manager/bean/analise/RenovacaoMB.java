package br.com.global5.manager.bean.analise;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.cadastro.FichaRenovacao;
import br.com.global5.manager.model.enums.RenovacaoStatus;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.cadastro.FichaRenovacaoService;
import br.com.global5.template.exception.BusinessException;
import com.google.gson.Gson;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
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
import java.util.concurrent.TimeUnit;

@Named
@ViewAccessScoped
public class RenovacaoMB implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<FichaRenovacao> renovacaoList;
	private List<FichaRenovacao> filteredValue;
	private Integer id;
	private FichaRenovacao renovacao;
	private Filter<FichaRenovacao> filter = new Filter<>(new FichaRenovacao());
	private Usuario usuario;

	@Inject
	FichaRenovacaoService renovacaoService;

	public void updateUsuario(Usuario usuario) {
	    this.usuario = usuario;
    }

	public LazyDataModel<FichaRenovacao> getRenovacaoList() {
		if( renovacaoList == null ) {

		    if( ! usuario.isInterno() ) {

                if (usuario.getPessoa().getFuncao().getArea().getRoot() != null) {
                    filter.getEntity().setFilialArea(new Area(usuario.getPessoa().getFuncao().getArea().getId()));
                }

                if (usuario.getPessoa().getFuncao().getArea().getRoot() == null) {
                    filter.getEntity().setClienteArea(new Area(usuario.getPessoa().getFuncao().getArea().getId()));
                }
            }


			renovacaoList = new LazyDataModel<FichaRenovacao>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<FichaRenovacao> load(int first, int pageSize,
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
                    List<FichaRenovacao> list = renovacaoService.paginate(filter);
                    setRowCount(renovacaoService.count(filter));
					return list;
				}
				
                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public FichaRenovacao getRowData(String key) {
                    return renovacaoService.findById(new Integer(key));
                }
			};
		
		}
		return renovacaoList;
	}

	public String showTimeleft(Date dtVecimento) {

	    long dias = AppUtils.getDateDiff(dtVecimento, new Date(), TimeUnit.DAYS);

        return (dias < 0 ? "Falta(m) " + (dias * -1) + " dia(s)" : " :*: Expirada :*: ");
    }

	public String showVeiculos(String lst) {
		try {
            FichaRenovacao.lVeiculo[] veiculos = new Gson().fromJson(lst, FichaRenovacao.lVeiculo[].class);
		    String placas = "";
		    for(FichaRenovacao.lVeiculo veiculo : veiculos) {
		        placas += veiculo.getPlaca() + " :: ";
            }
            return placas;
		} catch (Exception e) {
			return "";
		}
	}

    public void renew(int status) {
		renovacao.setUsuarioResposta(new Usuario(usuario.getId()));
        renovacao.setStatus(new RenovacaoStatus(status));
        update();

    }

    public void update() {
        if (renovacao.getId() == null) {
            renovacaoService.insert(renovacao);
        } else {
        	renovacaoService.update(renovacao);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('renovTable').filter()");



    }

    public void dlg(String dlg, int idFicha) {
        findById(idFicha);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('" + dlg + "').show();onTop('" +  dlg + "');");
    }


	public void clear() {
		renovacao = new FichaRenovacao();
		filter = new Filter<>(new FichaRenovacao());
		filter.getEntity().setStatus(new RenovacaoStatus(213));
		id = null;
	}
	
    public void findById(Integer id) {
        if (id == null) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"Atenção","O id é obrigatório"));
        }
        renovacao = renovacaoService.crud().get(id);
        if (renovacao == null) {
			throw new BusinessException("Registro não foi encontrado pelo id: " + id );
        }
    }	
    
    public List<String> completeNome(String query) {
        return renovacaoService.getNome(query);
    }    
    
    public void onRowSelect(SelectEvent event) {
        id = ((FichaRenovacao) event.getObject()).getId();
        findById(getId());
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../manutencao/renovacaoman.xhtml?id=" + id);
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Ficha Renovação " + id
								+ " não pode ser carregada. Informe ao suporte técnico."));
		}

    }
    
    public void btnBack() {
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../manutencao/renovacaolst.xhtml");
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Ficha Renovação " + id
								+ " não pode ser carregada. Informe ao suporte técnico."));
		}
    }

    public void onRowUnselect(UnselectEvent event) {
        renovacao = new FichaRenovacao();
    }

    public Filter<FichaRenovacao> getFilter() {
        return filter;
    }

    public void setFilter(Filter<FichaRenovacao> filter) {
        this.filter = filter;
    }

	public List<FichaRenovacao> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<FichaRenovacao> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setRenovacaoList(LazyDataModel<FichaRenovacao> renovacaoList) {
		this.renovacaoList = renovacaoList;
	}

	public FichaRenovacao getRenovacao() {
		return renovacao;
	}

	public void setRenovacao(FichaRenovacao renovacao) {
		this.renovacao = renovacao;
	}

	public FichaRenovacaoService getRenovacaoService() {
		return renovacaoService;
	}

	public void setRenovacaoService(FichaRenovacaoService renovacaoService) {
		this.renovacaoService = renovacaoService;
	}

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
