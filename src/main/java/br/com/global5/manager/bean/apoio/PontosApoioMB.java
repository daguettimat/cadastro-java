package br.com.global5.manager.bean.apoio;

import br.com.global5.infra.model.Filter;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.apoio.*;
import br.com.global5.manager.model.apoio.ServicosMotorista;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.manager.model.enums.*;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.apoio.PontosApoioService;
import br.com.global5.manager.service.apoio.ServicosManutencaoService;
import br.com.global5.manager.service.apoio.ServicosMotoristaService;
import br.com.global5.manager.service.apoio.ServicosSegurancaService;
import br.com.global5.manager.service.cadastro.TelefoneService;
import br.com.global5.manager.service.enums.*;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
@ViewAccessScoped
public class PontosApoioMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    private List<String> images;
	private List<TipoApoio> lstTipoApoio;
	private List<Bandeiras> lstBandeiras;
	private List<TipoRodovias> lstTipoRodovias;
	private List<ManutencaoAbastecimento> lstServManutencao;
	private List<br.com.global5.manager.model.enums.ServicosMotorista> lstServMotoristas;
	private List<FerramentasSeguranca> lstServSeguranca;
	private List<HorarioFuncionamento> lstHorFuncionamento;
	private LazyDataModel<ServicosManutencao> lstManutencao;
    private LazyDataModel<ServicosMotorista> lstMotoristas;
    private LazyDataModel<ServicosSeguranca> lstSeguranca;
    private List<PontosApoio> filteredValue;
    private LazyDataModel<PontosApoio> pontosList;
    private List<ServicosManutencao> filteredManutencao;
    private List<br.com.global5.manager.model.apoio.ServicosMotorista> filteredMotoristas;
    private List<ServicosSeguranca> filteredSegurancas;
 	private int id;
	private int tipoApoio;
	private int tipoRodovia;
	private int tipoBandeira;
	private int tipoServicoAbast;
	private int tipoServicoMotorista;
	private int tipoServicoSeguranca;
	private int horarioFuncionamento;
	private int horarioFuncMotoristas;
    private int horarioFuncSeguranca;
	private PontosApoio pontos;
	private ServicosManutencao srvManutencao;
    private ServicosMotorista srvMotoristas;
    private ServicosSeguranca srvSeguranca;
	private Filter<PontosApoio> filter = new Filter<>(new PontosApoio());
    private Filter<ServicosManutencao> filterManutencao = new Filter<>(new ServicosManutencao());
    private Filter<ServicosMotorista> filterMotoristas = new Filter<>(new ServicosMotorista());
    private Filter<ServicosSeguranca> filterSeguranca = new Filter<>(new ServicosSeguranca());

	private String nome;

	private Date dtInicial;
	private Date dtFinal;

	private Usuario usuario;

	private Servicos servManutencao;
	private Servicos servSeguranca;
	private Servicos servMotorista;

    private List<UploadedFile> uploadedFiles;

	@Inject
	PontosApoioService pontosService;

	@Inject
    ServicosManutencaoService manService;

	@Inject
    ServicosMotoristaService motService;

	@Inject
    ServicosSegurancaService segService;

	@Inject
    TipoRodoviasService tprService;

	@Inject
    BandeirasService banService;

	@Inject
    TipoApoioService tpaService;

	@Inject
    HorarioFuncionamentoService horFuncService;

	@Inject
    ManutencaoAbastecimentoService manAbastService;

    @Inject
    FerramentasSegurancaService ferSegService;

    @Inject
    br.com.global5.manager.service.enums.ServicosMotoristaService servMotService;

    @Inject
    TelefoneService telService;

    @PostConstruct
    public void init() {

        checkUsuario.isOption(true);

        clear();

    }

	public LazyDataModel<PontosApoio> getPontosList() {
		if( pontosList == null ) {
			pontosList = new LazyDataModel<PontosApoio>() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<PontosApoio> load(int first, int pageSize,
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
					List<PontosApoio> list = pontosService.paginate(filter);
					setRowCount(pontosService.count(filter));
					return list;
				}

				@Override
				public int getRowCount() {
					return super.getRowCount();
				}

				@Override
				public PontosApoio getRowData(String key) {
					return pontosService.findById(new Integer(key));
				}
			};

		}
		return pontosList;
	}

    public LazyDataModel<ServicosManutencao> getLstManutencao() {
        if( lstManutencao == null ) {
            lstManutencao = new LazyDataModel<ServicosManutencao>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                @Override
                public List<ServicosManutencao> load(int first, int pageSize,
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
                    List<ServicosManutencao> list = manService.paginate(filterManutencao);
                    setRowCount(manService.count(filterManutencao));
                    return list;
                }

                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public ServicosManutencao getRowData(String key) {
                    return manService.findById(new Integer(key));
                }
            };

        }
        return lstManutencao;
    }

    public LazyDataModel<ServicosSeguranca> getLstSeguranca() {
        if( lstSeguranca == null ) {
            lstSeguranca = new LazyDataModel<ServicosSeguranca>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                @Override
                public List<ServicosSeguranca> load(int first, int pageSize,
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
                    List<ServicosSeguranca> list = segService.paginate(filterSeguranca);
                    setRowCount(segService.count(filterSeguranca));
                    return list;
                }

                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public ServicosSeguranca getRowData(String key) {
                    return segService.findById(new Integer(key));
                }
            };

        }
        return lstSeguranca;
    }



	public void remove() {
		if( pontos != null && pontos.getId() != null) {
		    pontos.setUsuarioExclusao(new Usuario(usuario.getId()));
            pontos.setDtExclusao(new Date());
			pontosService.update(pontos);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, pontos.getNomeFantasia()
							+ " removido com sucesso",null));
			clear();
		}
	}

	public void update() {

		String msg;

		usuario = checkUsuario.valid();
		pontos.setUsuarioAlteracao(new Usuario(usuario.getId()));
		pontos.setDtAlteracao(new Date());
        if(pontos.getTelFixo().getDdd().length() > 0) {
            pontos.setTelFixo(checkTelefone(pontos.getTelFixo().getDdd(), pontos.getTelFixo().getFone(), 78, 74 ));
        }

        if(pontos.getTelCelular().getDdd().length() > 0) {
            pontos.setTelCelular(checkTelefone(pontos.getTelCelular().getDdd(), pontos.getTelCelular().getFone(), 79, 74 ));
        }

        if(pontos.getTelPublico().getDdd().length() > 0) {
            pontos.setTelPublico(checkTelefone(pontos.getTelPublico().getDdd(), pontos.getTelPublico().getFone(), 78, 74 ));
        }

        pontos.setTipoApoio(new TipoApoio(tipoApoio));
        pontos.setBandeira(new Bandeiras(tipoBandeira));

		if (pontos.getId() == null) {
            pontos.setUsuarioCriacao(new Usuario(usuario.getId()));
            pontos.setDtCriacao(new Date());
            pontosService.insert(pontos);
			msg =  " Registro Criado com Sucesso!";
		} else {
			pontosService.update(pontos);
			msg =  " Registro Alterado com Sucesso!";
		}

		salvarUpload();

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso",
                        msg));

	}

	public void updateServicoSeguranca() {
        if(id == 0) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso",
                            "Você deve salvar um ponto de apoio primeiro, para depois lançar seu serviços"));
            return;
        }

        segService.crud().saveOrUpdate(
                new ServicosSeguranca(servSeguranca.getId(),
                        new PontosApoio(id),
                        servSeguranca.getTipoServico(),
                        servSeguranca.isDisponivel(),
                        new HorarioFuncionamento(servSeguranca.getHorFunc()),
                        servSeguranca.getValor(),
                        servSeguranca.getObservacoes()
                ));
    }

	public void updateServicoMotoristas() {
        if(id == 0) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso",
                            "Você deve salvar um ponto de apoio primeiro, para depois lançar seu serviços"));
            return;
        }

        motService.crud().saveOrUpdate(
                new ServicosMotorista(servMotorista.getId(),
                                    new PontosApoio(id),
                                    servMotorista.getTipoServico(),
                                    servMotorista.isDisponivel(),
                                    new HorarioFuncionamento(servMotorista.getHorFunc()),
                                    servMotorista.getValor(),
                                    servMotorista.getObservacoes()
                ));
    }

	public void updateServicoAbastecimento() {
	    if(id == 0) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso",
                            "Você deve salvar um ponto de apoio primeiro, para depois lançar seu serviços"));
            return;
        }

        if( servManutencao.getId() == 0 ) {
            manService.crud().save(
                    new ServicosManutencao(
                            new PontosApoio(id),
                            servManutencao.getTipoServico(),
                            servManutencao.isDisponivel(),
                            new HorarioFuncionamento(servManutencao.getHorFunc()),
                            servManutencao.getValor(),
                            servManutencao.getObservacoes()
                    ));
        } else {
            manService.crud().update(
                    new ServicosManutencao(servManutencao.getId(),
                            new PontosApoio(id),
                            servManutencao.getTipoServico(),
                            servManutencao.isDisponivel(),
                            new HorarioFuncionamento(servManutencao.getHorFunc()),
                            servManutencao.getValor(),
                            servManutencao.getObservacoes()
                    ));

        }

    }

	public Telefone checkTelefone(String ddd, String fone, int tipo, int origem) {

	    int count = telService.crud().eq("ddd", ddd).eq("fone", fone).count();
	    if( count > 0 ) {
            return telService.crud().eq("ddd", ddd).eq("fone", fone).list().get(0);
        } else {
	        Telefone telefone = new Telefone(ddd, fone, new TelefoneTipo(tipo),
                                             new TelefoneOrigem(origem), null, new Date());
	        telService.crud().save(telefone);
            return telefone;
        }

    }

    public void insert() {
        try {
            clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../manutencao/pontosman.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir pontos " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.",null));
        }
    }

    public void initTables() {

        lstTipoApoio = tpaService.crud().isNull("exclusao").list();
        lstTipoRodovias = tprService.crud().isNull("exclusao").list();
        lstBandeiras = banService.crud().isNull("exclusao").list();
        lstServManutencao = manAbastService.crud().isNull("exclusao").list();
        lstServSeguranca = ferSegService.crud().isNull("exclusao").list();
        lstHorFuncionamento = horFuncService.crud().isNull("exclusao").list();
        lstServMotoristas = servMotService.crud().isNull("exclusao").list();

    }

	public void clear() {
		usuario = checkUsuario.valid();

		pontos = new PontosApoio();
		filter = new Filter<PontosApoio>(new PontosApoio());

		initTables();

        pontos.setTelPublico(new Telefone());
        pontos.setTelCelular(new Telefone());
        pontos.setTelFixo(new Telefone());

        servManutencao = new Servicos(0, 0, false, 0, 0, "");
        servMotorista = new Servicos(0, 0, false, 0, 0, "");
        servSeguranca = new Servicos(0, 0, false, 0, 0, "");
		id = 0;
	}

	public void findById(Integer id) {
		if (id == null) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso!","O id é obrigatório!"));

		}
		clear();
		this.id = id;
		pontos = pontosService.crud().get(id);
		if (pontos == null) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso!","O id não foi encontrado!"));
		}

		filterSeguranca.getEntity().setPontosApoio(new PontosApoio(id));
		filterManutencao.getEntity().setPontosApoio(new PontosApoio(id));
		filterMotoristas.getEntity().setPontosApoio(new PontosApoio(id));

		tipoApoio = pontos.getTipoApoio().getId();
		tipoBandeira = pontos.getBandeira().getId();

        RequestContext.getCurrentInstance().execute("PF('abastecimetoTable').filter()");
        RequestContext.getCurrentInstance().execute("PF('motoristasTable').filter()");
        RequestContext.getCurrentInstance().execute("PF('seguranacaTable').filter()");

	}

	public void onRowSelect(SelectEvent event) {
		this.id = Integer.valueOf(((PontosApoio) event.getObject()).getId());

		findById(getId());
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../manutencao/pontosman.xhtml?id=" + getId());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","O registro não pode ser carregado."));
		}

	}

	public void btnPesquisar() {

		filter.getEntity().setId(id);
//		filter.getEntity().setNome(nome);
//		filter.getEntity().setDtInicial(dtInicial);
//		filter.getEntity().setDtFinal(dtFinal);
		RequestContext.getCurrentInstance().execute("PF('pontosTable').filter()");

	}

	public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../manutencao/pontoslst.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de pontoss " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

    private void loadImages() throws IOException {

        Path dir = FileSystems.getDefault().getPath( AppUtils.dirImagens +
                AppUtils.formataCompetencia(pontos.getDtCriacao()) + "/" );

        DirectoryStream<Path> stream = Files.newDirectoryStream( dir, "PA" + pontos.getId()
                + " - *.{jpg,jpeg,png,gif,bmp}" );

        images = new ArrayList<>();
        for (Path path : stream) {
            images.add( AppUtils.imageName(  "/" + AppUtils.formataCompetencia(pontos.getDtCriacao()) +
                    "/"  + path.getFileName().toString(), true));

        }
        stream.close();

    }

    public void salvarUpload() {

        String dir = AppUtils.dirImagens + AppUtils.formataCompetencia(pontos.getDtCriacao() ) + "/" ;
        File files = new File(dir);
        if (!files.exists()) {
            if (files.mkdirs()) {
                System.out.println("Diretorios Criados com sucesso! (" + dir + ")");
            } else {
                System.out.println("Não foi possível criar diretorio! (" + dir + ")");
            }
        }

        int idx = 1;
        String filename = "PA" + pontos.getId() + " - ";
        for (UploadedFile uploadedFile : uploadedFiles) {
            AppUtils.saveImage(dir, uploadedFile, idx++, filename);
        }

        uploadedFiles.clear();

    }

    public void upload(FileUploadEvent event) {
        uploadedFiles.add(event.getFile());
    }

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public void setAvisoList(LazyDataModel<PontosApoio> pontosList) {
		this.pontosList = pontosList;
	}

	public List<PontosApoio> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<PontosApoio> filteredValue) {
		this.filteredValue = filteredValue;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PontosApoio getAviso() {
		return pontos;
	}

	public void setAviso(PontosApoio pontos) {
		this.pontos = pontos;
	}

	public Filter<PontosApoio> getFilter() {
		return filter;
	}

	public void setFilter(Filter<PontosApoio> filter) {
		this.filter = filter;
	}

	public PontosApoioService getAvisoService() {
		return pontosService;
	}

	public void setAvisoService(PontosApoioService pontosService) {
		this.pontosService = pontosService;
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

    public void setPontosList(LazyDataModel<PontosApoio> pontosList) {
        this.pontosList = pontosList;
    }

    public PontosApoio getPontos() {
        return pontos;
    }

    public void setPontos(PontosApoio pontos) {
        this.pontos = pontos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PontosApoioService getPontosService() {
        return pontosService;
    }

    public void setPontosService(PontosApoioService pontosService) {
        this.pontosService = pontosService;
    }

    public ServicosManutencaoService getManService() {
        return manService;
    }

    public void setManService(ServicosManutencaoService manService) {
        this.manService = manService;
    }

    public ServicosMotoristaService getMotService() {
        return motService;
    }

    public void setMotService(ServicosMotoristaService motService) {
        this.motService = motService;
    }

    public ServicosSegurancaService getSegService() {
        return segService;
    }

    public void setSegService(ServicosSegurancaService segService) {
        this.segService = segService;
    }

    public List<TipoApoio> getLstTipoApoio() {
        return lstTipoApoio;
    }

    public void setLstTipoApoio(List<TipoApoio> lstTipoApoio) {
        this.lstTipoApoio = lstTipoApoio;
    }

    public List<Bandeiras> getLstBandeiras() {
        return lstBandeiras;
    }

    public void setLstBandeiras(List<Bandeiras> lstBandeiras) {
        this.lstBandeiras = lstBandeiras;
    }

    public List<TipoRodovias> getLstTipoRodovias() {
        return lstTipoRodovias;
    }

    public void setLstTipoRodovias(List<TipoRodovias> lstTipoRodovias) {
        this.lstTipoRodovias = lstTipoRodovias;
    }

    public TipoRodoviasService getTprService() {
        return tprService;
    }

    public void setTprService(TipoRodoviasService tprService) {
        this.tprService = tprService;
    }

    public BandeirasService getBanService() {
        return banService;
    }

    public void setBanService(BandeirasService banService) {
        this.banService = banService;
    }

    public TipoApoioService getTpaService() {
        return tpaService;
    }

    public void setTpaService(TipoApoioService tpaService) {
        this.tpaService = tpaService;
    }

    public int getTipoApoio() {
        return tipoApoio;
    }

    public void setTipoApoio(int tipoApoio) {
        this.tipoApoio = tipoApoio;
    }

    public int getTipoRodovia() {
        return tipoRodovia;
    }

    public void setTipoRodovia(int tipoRodovia) {
        this.tipoRodovia = tipoRodovia;
    }

    public int getTipoBandeira() {
        return tipoBandeira;
    }

    public void setTipoBandeira(int tipoBandeira) {
        this.tipoBandeira = tipoBandeira;
    }

    public Servicos getServManutencao() {
        return servManutencao;
    }

    public void setServManutencao(Servicos servManutencao) {
        this.servManutencao = servManutencao;
    }

    public Servicos getServSeguranca() {
        return servSeguranca;
    }

    public void setServSeguranca(Servicos servSeguranca) {
        this.servSeguranca = servSeguranca;
    }

    public Servicos getServMotorista() {
        return servMotorista;
    }

    public void setServMotorista(Servicos servMotorista) {
        this.servMotorista = servMotorista;
    }

    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public TelefoneService getTelService() {
        return telService;
    }

    public void setTelService(TelefoneService telService) {
        this.telService = telService;
    }

    public int getTipoServicoAbast() {
        return tipoServicoAbast;
    }

    public void setTipoServicoAbast(int tipoServicoAbast) {
        this.tipoServicoAbast = tipoServicoAbast;
    }

    public List<ManutencaoAbastecimento> getLstServManutencao() {
        return lstServManutencao;
    }

    public void setLstServManutencao(List<ManutencaoAbastecimento> lstServManutencao) {
        this.lstServManutencao = lstServManutencao;
    }

    public List<FerramentasSeguranca> getLstServSeguranca() {
        return lstServSeguranca;
    }

    public void setLstServSeguranca(List<FerramentasSeguranca> lstServSeguranca) {
        this.lstServSeguranca = lstServSeguranca;
    }

    public ManutencaoAbastecimentoService getManAbastService() {
        return manAbastService;
    }

    public void setManAbastService(ManutencaoAbastecimentoService manAbastService) {
        this.manAbastService = manAbastService;
    }

    public FerramentasSegurancaService getFerSegService() {
        return ferSegService;
    }

    public void setFerSegService(FerramentasSegurancaService ferSegService) {
        this.ferSegService = ferSegService;
    }

    public List<HorarioFuncionamento> getLstHorFuncionamento() {
        return lstHorFuncionamento;
    }

    public void setLstHorFuncionamento(List<HorarioFuncionamento> lstHorFuncionamento) {
        this.lstHorFuncionamento = lstHorFuncionamento;
    }

    public int getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(int horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public HorarioFuncionamentoService getHorFuncService() {
        return horFuncService;
    }

    public void setHorFuncService(HorarioFuncionamentoService horFuncService) {
        this.horFuncService = horFuncService;
    }

    public void setLstManutencao(LazyDataModel<ServicosManutencao> lstManutencao) {
        this.lstManutencao = lstManutencao;
    }

    public List<ServicosManutencao> getFilteredManutencao() {
        return filteredManutencao;
    }

    public void setFilteredManutencao(List<ServicosManutencao> filteredManutencao) {
        this.filteredManutencao = filteredManutencao;
    }

    public Filter<ServicosManutencao> getFilterManutencao() {
        return filterManutencao;
    }

    public void setFilterManutencao(Filter<ServicosManutencao> filterManutencao) {
        this.filterManutencao = filterManutencao;
    }

    public ServicosManutencao getSrvManutencao() {
        return srvManutencao;
    }

    public void setSrvManutencao(ServicosManutencao srvManutencao) {
        this.srvManutencao = srvManutencao;
    }

    public int getHorarioFuncMotoristas() {
        return horarioFuncMotoristas;
    }

    public void setHorarioFuncMotoristas(int horarioFuncMotoristas) {
        this.horarioFuncMotoristas = horarioFuncMotoristas;
    }

    public int getTipoServicoMotorista() {
        return tipoServicoMotorista;
    }

    public void setTipoServicoMotorista(int tipoServicoMotorista) {
        this.tipoServicoMotorista = tipoServicoMotorista;

    }

    public List<br.com.global5.manager.model.enums.ServicosMotorista> getLstServMotoristas() {
        return lstServMotoristas;
    }

    public void setLstServMotoristas(List<br.com.global5.manager.model.enums.ServicosMotorista> lstServMotoristas) {
        this.lstServMotoristas = lstServMotoristas;
    }

    public br.com.global5.manager.service.enums.ServicosMotoristaService getServMotService() {
        return servMotService;
    }

    public void setServMotService(br.com.global5.manager.service.enums.ServicosMotoristaService servMotService) {
        this.servMotService = servMotService;
    }

    public LazyDataModel<ServicosMotorista> getLstMotoristas() {
        return lstMotoristas;
    }

    public void setLstMotoristas(LazyDataModel<ServicosMotorista> lstMotoristas) {
        this.lstMotoristas = lstMotoristas;
    }

    public ServicosMotorista getSrvMotoristas() {
        return srvMotoristas;
    }

    public void setSrvMotoristas(ServicosMotorista srvMotoristas) {
        this.srvMotoristas = srvMotoristas;
    }

    public Filter<ServicosMotorista> getFilterMotoristas() {
        return filterMotoristas;
    }

    public void setFilterMotoristas(Filter<ServicosMotorista> filterMotoristas) {
        this.filterMotoristas = filterMotoristas;
    }

    public List<ServicosMotorista> getFilteredMotoristas() {
        return filteredMotoristas;
    }

    public void setFilteredMotoristas(List<ServicosMotorista> filteredMotoristas) {
        this.filteredMotoristas = filteredMotoristas;
    }



    public void setLstSeguranca(LazyDataModel<ServicosSeguranca> lstSeguranca) {
        this.lstSeguranca = lstSeguranca;
    }

    public List<ServicosSeguranca> getFilteredSegurancas() {
        return filteredSegurancas;
    }

    public void setFilteredSegurancas(List<ServicosSeguranca> filteredSegurancas) {
        this.filteredSegurancas = filteredSegurancas;
    }

    public int getTipoServicoSeguranca() {
        return tipoServicoSeguranca;
    }

    public void setTipoServicoSeguranca(int tipoServicoSeguranca) {
        this.tipoServicoSeguranca = tipoServicoSeguranca;
    }

    public int getHorarioFuncSeguranca() {
        return horarioFuncSeguranca;
    }

    public void setHorarioFuncSeguranca(int horarioFuncSeguranca) {
        this.horarioFuncSeguranca = horarioFuncSeguranca;
    }

    public ServicosSeguranca getSrvSeguranca() {
        return srvSeguranca;
    }

    public void setSrvSeguranca(ServicosSeguranca srvSeguranca) {
        this.srvSeguranca = srvSeguranca;
    }

    public Filter<ServicosSeguranca> getFilterSeguranca() {
        return filterSeguranca;
    }

    public void setFilterSeguranca(Filter<ServicosSeguranca> filterSeguranca) {
        this.filterSeguranca = filterSeguranca;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
