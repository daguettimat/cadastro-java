package br.com.global5.manager.bean.analise;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.model.analise.acConsultaPessoa;
import br.com.global5.manager.model.analise.acPesquisaPessoa;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.cadastro.Produto;
import br.com.global5.manager.model.externos.ConsultaPessoa;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.analise.ConsultaPessoaService;
import br.com.global5.manager.service.analise.PesquisaPessoaService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.*;
import java.util.*;

@Named
@ViewAccessScoped
public class PesquisaPessoaMB implements Serializable {

	private static final long serialVersionUID = 1L;
	private LazyDataModel<acPesquisaPessoa> pesList;
	private List<acPesquisaPessoa> filteredValue;
	private Integer id;
	private acPesquisaPessoa pessoa;
	private Filter<acPesquisaPessoa> filter = new Filter<>(new acPesquisaPessoa());
	private Usuario usuario;
	private int nFicha;
	private boolean habilitaArea;
	private boolean habilitaFilial;
	private Area areaCliente;
	private Area areaFilial;

	private String nomePessoa;

    private Date dtInicial;
    private Date dtFinal;

	@Inject
	private PesquisaPessoaService pesService;

	@Inject
    private ConsultaPessoaService conService;

	@PostConstruct
	public void init() {

		clear();

	}

    public void selectFilial(SelectEvent event) {
        areaFilial = (Area) event.getObject();
        if( ! usuario.isInterno() ) {
            filter.getEntity().setFilialArea(new Area(areaFilial.getId()));
        }

    }

    public void selectTransp(SelectEvent event) {
        areaCliente = (Area) event.getObject();
        if( ! usuario.isInterno() ) {
            filter.getEntity().setClienteArea(new Area(areaCliente.getId()));
        }
    }


	public void updateUsuario(Usuario usuario) {

	    this.usuario = usuario;

        if( usuario.isInterno() || (usuario.getTipo().equals("A") || getUsuario().getTipo().equals("H")) ) {
            habilitaArea = true;
            habilitaFilial = true;
        } else {
            if( usuario.getPessoa().getFuncao().getArea().getNivel().getId() == 2 ) {
                areaCliente = usuario.getPessoa().getFuncao().getArea();
                habilitaArea = false;
                habilitaFilial = true;
            } else {
                if( usuario.getPessoa().getFuncao().getArea().getNivel().getId() == 3 ) {
                    areaCliente =  usuario.getPessoa().getFuncao().getArea().getRoot();
                    areaFilial = usuario.getPessoa().getFuncao().getArea();
                    habilitaFilial = false;
                    habilitaArea = false;
                }
            }
        }
    }

	public LazyDataModel<acPesquisaPessoa> getPesList() {
		if( pesList == null ) {

			pesList = new LazyDataModel<acPesquisaPessoa>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;



				@SuppressWarnings("unchecked")
				@Override
				public List<acPesquisaPessoa> load(int first, int pageSize,
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
					List<acPesquisaPessoa> list = pesService.paginate(filter);
					setRowCount(pesService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public acPesquisaPessoa getRowData(String key) {
					return pesService.findById(new Integer(key));
				}
			};

		}
		return pesList;
	}

	public String formatCPF(String value) {
		return AppUtils.formataCpf(value);
	}

	public void remove() {
		if( pessoa != null && pessoa.getId() != null) {
			pesService.remove(pessoa);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Consulta de Pessoas " + pessoa.getNome()
							+ " removida com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;

		if (pessoa.getId() == null) {
		    pessoa.setIdProduto(new Produto(4));
		    pessoa.setConsulta(null);
		    pessoa.setDtCriacao(new Date());
		    pessoa.setDtResposta(null);
		    pessoa.setUsuConsulta(new Usuario(usuario.getId()));
		    if( usuario.getPessoa().getFuncao().getArea().getRoot() == null  ) {
                pessoa.setClienteArea(new Area(usuario.getPessoa().getFuncao().getArea().getId()));
                pessoa.setFilialArea(null);
            } else {
                pessoa.setClienteArea(new Area(usuario.getPessoa().getFuncao().getArea().getRoot().getId()));
                pessoa.setFilialArea(new Area(usuario.getPessoa().getFuncao().getArea().getId()));
            }

            String SQL = "select cp.conp_valor1_unitario " +
                         "  from contrato c " +
                         "       join contrato_produto cp " +
                         "            on cp.conp_conoid = c.conoid " +
                         "       join produto p " +
                         "            on p.prodoid = cp.conp_prodoid " +
                         " where c.con_areaoid = :idArea " +
                         "   and cp.conp_prodoid = 4";

            Query query = pesService.crud().getEntityManager().createNativeQuery(SQL);
            query.setParameter("idArea", pessoa.getClienteArea().getId());

            try {
                Object valor = query.getSingleResult();
                pessoa.setValor((Double) valor);
            } catch (NoResultException nre) {
                pessoa.setValor(0d);
            }

			pesService.insert(pessoa);
			msg = "Consulta de " + pessoa.getNome() + " gerada com sucesso!";
		} else {
			pesService.update(pessoa);
			msg = "Consulte de " + pessoa.getNome() + " alterada com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));

	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/pesquisapessoaman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir Pesquisa de Pessoa " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		pessoa = new acPesquisaPessoa();
		filter = new Filter<>(new acPesquisaPessoa());
		filter.getEntity().setClienteArea(new Area());
		filter.getEntity().setFilialArea(new Area());
		filter.getEntity().setNome(null);
        Calendar now = Calendar.getInstance();

        now.add(Calendar.DATE, -1);

        filter.getEntity().setDtInicial(now.getTime());

        now.add(Calendar.DATE, 2);
        filter.getEntity().setDtFinal(now.getTime());

		id = null;
	}

	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id é obrigatório");
		}
		pessoa = pesService.crud().get(id);
		if (pessoa == null) {
			throw new BusinessException("Pesquisa de pessoa não foi encontrada pelo id: " + id);
		}
	}

	public String formatJSON() {

		try {

			String file = UUID.randomUUID().toString();

			String retorno = "";
			String s;

			acConsultaPessoa cp;

			PrintWriter writer = new PrintWriter("/tmp/"+ file , "UTF-8");

			try {
				if (pessoa.getConsulta().getResposta() == null) {
					return "Sem resposta";
				}
			} catch (Exception e) {
				return "Sem resposta";
			}

			try {

				cp = conService.crud().get(pessoa.getConsulta().getId());

			} catch (Exception e) {

				return "Sem resposta";

			}

			writer.println(cp.getResposta());

			writer.close();

			String[] cmd = {
					"/bin/bash",
					"-c",
					"python /opt/gcadastro/jsonhtml.py /tmp/" + file
			};
			Process p =  Runtime.getRuntime().exec(cmd);

			BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(p.getInputStream()));

			while ((s = stdInput.readLine()) != null) {
				retorno += s;
			}

			BufferedReader stdError = new BufferedReader(new
					InputStreamReader(p.getErrorStream()));

			while ((s = stdError.readLine()) != null) {
				retorno += s;
			}

			return retorno;


		} catch (IOException e) {
			e.printStackTrace();
		}

		return "Sem resposta";

	}


	public void onRowSelect(SelectEvent event) {
		this.id = ((acPesquisaPessoa) event.getObject()).getId();

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/pesquisapessoaman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Pesquisa de Pessoa  " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}

	}

	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/pesquisapessoalst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Pesquisa de Pessoa " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

    public void dummy() {

    }

	public Filter<acPesquisaPessoa> getFilter() {
		return filter;
	}

	public void setFilter(Filter<acPesquisaPessoa> filter) {
		this.filter = filter;
	}

	public List<acPesquisaPessoa> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<acPesquisaPessoa> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setPesList(LazyDataModel<acPesquisaPessoa> pesList) {
		this.pesList = pesList;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public acPesquisaPessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(acPesquisaPessoa pessoa) {
		this.pessoa = pessoa;
	}

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

	public int getnFicha() {
		return nFicha;
	}

	public void setnFicha(int nFicha) {
		this.nFicha = nFicha;
	}

    public boolean isHabilitaArea() {
        return habilitaArea;
    }

    public void setHabilitaArea(boolean habilitaArea) {
        this.habilitaArea = habilitaArea;
    }

    public boolean isHabilitaFilial() {
        return habilitaFilial;
    }

    public void setHabilitaFilial(boolean habilitaFilial) {
        this.habilitaFilial = habilitaFilial;
    }

    public Area getAreaCliente() {
        return areaCliente;
    }

    public void setAreaCliente(Area areaCliente) {
        this.areaCliente = areaCliente;
    }

    public Area getAreaFilial() {
        return areaFilial;
    }

    public void setAreaFilial(Area areaFilial) {
        this.areaFilial = areaFilial;
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

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }
}
