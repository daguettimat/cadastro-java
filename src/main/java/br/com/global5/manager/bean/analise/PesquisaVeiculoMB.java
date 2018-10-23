package br.com.global5.manager.bean.analise;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.model.analise.acConsultaVeiculo;
import br.com.global5.manager.model.analise.acPesquisaVeiculo;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.cadastro.Produto;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.analise.ConsultaVeiculoService;
import br.com.global5.manager.service.analise.PesquisaVeiculoService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
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
public class PesquisaVeiculoMB implements Serializable {

	private static final long serialVersionUID = 1L;
	private LazyDataModel<acPesquisaVeiculo> pesList;
	private List<acPesquisaVeiculo> filteredValue;
	private Integer id;
	private acPesquisaVeiculo pesqVeiculo;
	private Filter<acPesquisaVeiculo> filter = new Filter<>(new acPesquisaVeiculo());
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
	private PesquisaVeiculoService veiService;

	@Inject
    private ConsultaVeiculoService conService;

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

	public LazyDataModel<acPesquisaVeiculo> getPesList() {
		if( pesList == null ) {

			pesList = new LazyDataModel<acPesquisaVeiculo>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;



				@SuppressWarnings("unchecked")
				@Override
				public List<acPesquisaVeiculo> load(int first, int pageSize,
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
					List<acPesquisaVeiculo> list = veiService.paginate(filter);
					setRowCount(veiService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public acPesquisaVeiculo getRowData(String key) {
					return veiService.findById(new Integer(key));
				}
			};

		}
		return pesList;
	}

	public String formatCPF(String value) {
		return AppUtils.formataCpf(value);
	}

	public void remove() {
		if( pesqVeiculo != null && pesqVeiculo.getId() != null) {
			veiService.remove(pesqVeiculo);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Consulta de Veículos " + pesqVeiculo.getPlaca()
							+ " removida com sucesso",null));
			clear();
		}
	}

	public void update() {
		String msg;

		if (pesqVeiculo.getId() == null) {
		    pesqVeiculo.setProduto(new Produto(5));
		    pesqVeiculo.setConsulta(null);
		    pesqVeiculo.setDtCriacao(new Date());
		    pesqVeiculo.setDtResposta(null);
		    pesqVeiculo.setUsuConsulta(new Usuario(usuario.getId()));
		    if( usuario.getPessoa().getFuncao().getArea().getRoot() == null  ) {
                pesqVeiculo.setClienteArea(new Area(usuario.getPessoa().getFuncao().getArea().getId()));
                pesqVeiculo.setFilialArea(null);
            } else {
                pesqVeiculo.setClienteArea(new Area(usuario.getPessoa().getFuncao().getArea().getRoot().getId()));
                pesqVeiculo.setFilialArea(new Area(usuario.getPessoa().getFuncao().getArea().getId()));
            }

            String SQL = "select cp.conp_valor1_unitario " +
                         "  from contrato c " +
                         "       join contrato_produto cp " +
                         "            on cp.conp_conoid = c.conoid " +
                         "       join produto p " +
                         "            on p.prodoid = cp.conp_prodoid " +
                         " where c.con_areaoid = :idArea " +
                         "   and cp.conp_prodoid = 4";

            Query query = veiService.crud().getEntityManager().createNativeQuery(SQL);
            query.setParameter("idArea", pesqVeiculo.getClienteArea().getId());

            try {
                Object valor = query.getSingleResult();
                pesqVeiculo.setValor((Double) valor);
            } catch (NoResultException nre) {
                pesqVeiculo.setValor(0d);
            }

			veiService.insert(pesqVeiculo);
			msg = "Consulta de " + pesqVeiculo.getPlaca() + " gerada com sucesso!";
		} else {
			veiService.update(pesqVeiculo);
			msg = "Consulte de " + pesqVeiculo.getPlaca() + " alterada com sucesso!";
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso",msg));

	}

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/pesquisaveiculoman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Pesquisa de Veículo " + getId()
                            + " não pode ser carregada. Informe ao suporte técnico.",null));
        }
    }

	public void clear() {
		pesqVeiculo = new acPesquisaVeiculo();
		filter = new Filter<>(new acPesquisaVeiculo());
		filter.getEntity().setClienteArea(new Area());
		filter.getEntity().setFilialArea(new Area());
		filter.getEntity().setPlaca(null);
		filter.getEntity().setChassi(null);
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
		pesqVeiculo = veiService.crud().get(id);
		if (pesqVeiculo == null) {
			throw new BusinessException("Pesquisa de Veiculo não foi encontrada pelo id: " + id);
		}
		Hibernate.initialize(pesqVeiculo.getConsulta());
		Hibernate.initialize(pesqVeiculo.getUsuConsulta());
		Hibernate.initialize(pesqVeiculo.getProduto());
	}

	public String formatJSON() {

		try {

			String file = UUID.randomUUID().toString();

			String retorno = "";
			String s;

			acConsultaVeiculo cp;

            try {
                System.out.println(pesqVeiculo.getConsulta().toString());
                cp = conService.crud().get(pesqVeiculo.getConsulta().getId());
                if ( cp  == null ) {
                    return "Sem resposta";
                }

            } catch (Exception e) {

                return "Sem resposta";

            }

			PrintWriter writer = new PrintWriter("/tmp/"+ file , "UTF-8");

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
		this.id = ((acPesquisaVeiculo) event.getObject()).getId();

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/pesquisaveiculoman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Pesquisa de Pessoa  " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}

	}

	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../analise/pesquisaveiculolst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Pesquisa de Veículo " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

    public void dummy() {

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setPesList(LazyDataModel<acPesquisaVeiculo> pesList) {
        this.pesList = pesList;
    }

    public List<acPesquisaVeiculo> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<acPesquisaVeiculo> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public acPesquisaVeiculo getPesqVeiculo() {
        return pesqVeiculo;
    }

    public void setPesqVeiculo(acPesquisaVeiculo pesqVeiculo) {
        this.pesqVeiculo = pesqVeiculo;
    }

    public Filter<acPesquisaVeiculo> getFilter() {
        return filter;
    }

    public void setFilter(Filter<acPesquisaVeiculo> filter) {
        this.filter = filter;
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

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
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

    public PesquisaVeiculoService getVeiService() {
        return veiService;
    }

    public void setVeiService(PesquisaVeiculoService veiService) {
        this.veiService = veiService;
    }

    public ConsultaVeiculoService getConService() {
        return conService;
    }

    public void setConService(ConsultaVeiculoService conService) {
        this.conService = conService;
    }
}
