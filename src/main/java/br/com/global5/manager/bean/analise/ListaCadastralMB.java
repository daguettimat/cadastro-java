package br.com.global5.manager.bean.analise;


import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.ValidaBrasil;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.bean.geral.LogonMB;
import br.com.global5.manager.model.analise.acCadastro;
import br.com.global5.manager.model.analise.acListaFicha;
import br.com.global5.manager.model.enums.FichaStatus;
import br.com.global5.manager.model.enums.FichaTipo;
import br.com.global5.manager.model.enums.MotoristaVinculo;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.analise.CadastroService;
import br.com.global5.manager.service.enums.FichaStatusService;
import br.com.global5.manager.service.enums.FichaTipoService;
import br.com.global5.manager.service.enums.MotoristaVinculoService;
import br.com.global5.manager.service.geral.UsuarioService;
import br.com.global5.template.exception.BusinessException;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.primefaces.event.UnselectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@ViewAccessScoped
public class ListaCadastralMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


    private EntityManager em;

	private List<acListaFicha> listaFicha;

    private List<FichaStatus> lstFichaStatus;
    private List<FichaTipo> lstFichaTipo;
    private List<Usuario> lstUsuariosInternos;
    private List<MotoristaVinculo> lstTipoMotorista;
	private int id;
    private int tipoMotorista;
	private acCadastro acCadastro;
	private Usuario usuario;

	//
    // Variáveis de Filtro
    //

    private int nFicha;
    private int nLiberacao;
    private int nRenavam;
    private String nChassis;
    private String placa;

    private Date dtInicial;
    private Date dtFinal;

    private String hrInicial;
    private String hrFinal;

    private int idFichaTipo;
    private int idFinalizador;

    private int fichaStatus = 4;

    private String  Motorista;

    private String  usuFinalizador;

    private String nomeCliente;

	@Inject
    private CadastroService listaService;

    @Inject
    private FichaStatusService fichaStatusService;

    @Inject
    private FichaTipoService fichaTipoService;

    @Inject
    private UsuarioService usuService;

    @Inject
    private MotoristaVinculoService motVinculoService;

    public ListaCadastralMB() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        try {
            LogonMB logonMB = (LogonMB) session.getAttribute("logonMB");
            if( logonMB == null ) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } else {
                usuario = logonMB.getUsuarioLogado();
                Hibernate.initialize(usuario.getPessoa());
                Hibernate.initialize(usuario.getPessoa().getFuncao());
                Hibernate.initialize(usuario.getPessoa().getFuncao().getArea());
                Hibernate.initialize(usuario.getPessoa().getFuncao().getArea().getRoot());
            }
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Não foi possível redirecionar para a página de login.",null));
        }

        Motorista = "";
        nomeCliente = "";
        placa = "";
        nChassis = "";

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);

        dtInicial = now.getTime();

        now.set(Calendar.HOUR, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);

        dtFinal = now.getTime();
    }

	@PostConstruct
    public void init() {

        lstFichaStatus = fichaStatusService.listAll();
        lstFichaTipo = fichaTipoService.listAll();
        lstUsuariosInternos = usuService.listaUsuariosInternos();
        lstTipoMotorista = motVinculoService.crud().isNull("exclusao").list();

        clear();

    }

    public String flag(int id) {
        return AppUtils.pathFlag(id);
    }


	public void clear() {

	}

    public void findById(int id) {
        if (id == 0) {
			throw new BusinessException("O id é obrigatório");
        }

    }

    public void btnBack() {
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista Cadastral " + getId()
	                            + " não pode ser carregada. Informe ao suporte técnico.",null));
		}
    }

    public void recomendadas(Date today, Date past) {
        dtInicial = past;
        dtFinal = today;
        fichaStatus = 8;
        btnPesquisarData();
        trocaPagina();
    }

    public void emanalise(Date today, Date past) {
        dtInicial = past;
        dtFinal = today;
        fichaStatus = 4;
        btnPesquisarData();
        trocaPagina();
    }

    public void reprovadas(Date today, Date past) {
        dtInicial = past;
        dtFinal = today;
        fichaStatus = 9;
        btnPesquisarData();
        trocaPagina();
    }

    public void reanalise(Date today) {

        Date past;

        Calendar now = Calendar.getInstance();
        now.setTime(today);
        now.set(Calendar.HOUR, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);

        now.add(Calendar.DATE, -16);


        dtInicial = now.getTime();
        dtFinal = today;
        fichaStatus = 219;
        btnPesquisarData();
        trocaPagina();
    }

    public void ultimasfichas(Date today, Date past) {
        dtInicial = past;
        dtFinal = today;
        btnPesquisarData();
        trocaPagina();

    }

    private void trocaPagina() {
        try {
            usuario = checkUsuario.valid();
            if( usuario.isInterno() ) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("pages/analise/analiseCadastral.xhtml");
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("pages/analise/analiseListar.xhtml");
            }
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Lista Cadastral não pode ser carregada. " +
                            "Informe ao suporte técnico.", null));
        }

    }


    public void btnPesquisarData() {

	    boolean haveParameters;
        try {

            em = listaService.crud().getEntityManager();

            String parameters = " ";

            try {

                if (nFicha > 0) {
                    parameters += " anacoid = " + nFicha + " AND ";
                    haveParameters = true;
                }

                if (nLiberacao > 0) {
                    parameters += " analise_cadastral.anac_aliboid = " + nLiberacao + " AND ";
                    haveParameters = true;
                }

                if( fichaStatus != 0 ) {
                    if( fichaStatus == 4 ) {
                        parameters += " (analise_cadastral.anac_status = 4 OR " +
                                      "  analise_cadastral.anac_status = 5) AND ";
                    } else {
                        parameters += " analise_cadastral.anac_status = " + fichaStatus + " AND ";
                    }
                    haveParameters = true;
                }

                if( nRenavam > 0 ) {
                    parameters += " vei_renavam = '" + nRenavam + "' AND ";
                    haveParameters = true;
                }

                if(! nChassis.isEmpty()) {
                    parameters += " vei_chassi ILIKE '%" + nChassis + "%' AND ";
                    haveParameters = true;
                }

                if( ! nomeCliente.isEmpty() ) {
                    parameters += " t.area_nome ilike '%" + nomeCliente + "%' AND ";
                    haveParameters = true;
                }

                if( ! placa.isEmpty() ) {
                    parameters += " vei_placa = '" + placa + "' AND ";
                    haveParameters = true;
                }

                if( idFichaTipo >  0 ) {
                    parameters += " analise_cadastral.anac_tipo_ficha = " + idFichaTipo + " AND ";
                    haveParameters = true;

                }

                if( tipoMotorista > 0 ) {
                    parameters += " analise_cadastral_motorista.anacm_vinculo = " + tipoMotorista + " AND ";
                    haveParameters = true;
                }


                if(! Motorista.isEmpty() ) {
                    if(ValidaBrasil.validateDocumento(Motorista)) {
                        Motorista = (Normalizer
                                .normalize(Motorista, Normalizer.Form.NFD)
                                .replaceAll("[\\W]", ""));

                        parameters += " motorista.mot_documento1 like '%" + Motorista + "%' AND";
                        haveParameters = true;
                    } else {
                        parameters += " motorista.mot_nome ilike '%" + Motorista + "%' AND";
                        haveParameters = true;
                    }
                }
                if(idFinalizador > 0) {
                    parameters += " anac_usuoid_criacao =" + idFinalizador + " AND";
                    haveParameters = true;
                }

                if( dtInicial == null || dtFinal == null ) {
                    haveParameters = false;
                } else {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String fmtFinal = formatter.format(dtFinal);
                    parameters += " anac_dt_criacao < '" + fmtFinal + " 23:59' AND";
                    haveParameters = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
                haveParameters = false;
            }

            if( !haveParameters ) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso!","Configura sua lista de parametros, nenhum filtro pode ser realizado."));
                return;

            }

            if( ! usuario.getTipo().equals("H") && ! usuario.getTipo().equals("A") ) {
                if( usuario.getPessoa().getFuncao().getArea().getNivel().getId() == 2 ) {
                    parameters += " t.areaoid = " + usuario.getPessoa().getFuncao().getArea().getId() + " AND ";
                }
                if( usuario.getPessoa().getFuncao().getArea().getNivel().getId() == 3 ) {
                    parameters += " u.areaoid = " + usuario.getPessoa().getFuncao().getArea().getId() + " AND ";
                }

            }

            parameters += " 0 = 0";

            EntityManager em = listaService.crud().getEntityManager();

            String query =  "SELECT DISTINCT ON (analise_cadastral.anacoid) anacoid, " +
                            "    t.areaoid as idCliente, " +
                            "    u.areaoid as idFilial, " +
                            "    u.area_nome as nomeFilial, " +
                            "    t.area_nome as nomeCliente, " +
                            "    analise_cadastral_motorista.anacm_ref_pessoal as refPessoal, " +
                            "    enumRefPes.enum_nome as refPessoalDsc, " +
                            "    analise_cadastral_motorista.anacm_ref_comercial as refComercial, " +
                            "    enumRefCom.enum_nome as refComercialDsc, " +
                            "    analise_cadastral_motorista.anacm_criminal as refCriminal, " +
                            "    enumRefCri.enum_nome as refCriminalDsc, " +
                            "    analise_cadastral.anac_aliboid as numLiberacao, " +
                            "    analise_cadastral.anac_dt_criacao as dtCriacao, " +
                            "    analise_cadastral.anac_dt_limite_renovacao as dtVencimento, " +
                            "    analise_cadastral.anac_dt_exclusao as dtExclusao, " +
                            "    analise_cadastral.anac_dt_termino as dtTermino, " +
                            "    analise_cadastral.anac_status as status, " +
                            "    analise_cadastral.anac_tipo_ficha as tipoFicha, " +
                            "    analise_cadastral_motorista.anacm_motoid as idMotorista, " +
                            "    analise_cadastral_motorista.anacm_vinculo as tipoMotorista, " +
                            "    anacm_dt_registro as dtRegMotorista, " +
                            "    anacv_dt_registro as dtRegVeiculo, " +
                            "    motorista.mot_nome as nomeMotorista, " +
                            "    enum_ficha_status.enum_nome as statusFicha, " +
                            "    anacv_tipo as tipoAcVeiculo, " +
                            "    vei_placa as placaVeiculo, " +
                            "    veioid as idVeiculo, " +
                            "    vei_tipo as tipoVeiculo, " +
                            "    (select usu_nome from usuario where usuoid=anac_usuoid_criacao) as usuCriacao, " +
                            "    (SELECT usu_nome " +
                            "       FROM usuario " +
                            "      WHERE usuoid = analise_cadastral.anac_usuoid_alteracao)       as usuAlteracao, " +
                            "    ( SELECT usuario_1.usu_nome " +
                            "        FROM usuario usuario_1 " +
                            "       WHERE usuario_1.usuoid = analise_cadastral.anac_usuoid_termino) as usuTermino, " +
                            "    to_char(analise_cadastral.anac_dt_criacao, 'dd/mm/yyyy hh24:mi') as dtCadastro, " +
                            "        CASE " +
                            "            WHEN analise_cadastral.anac_dt_exclusao IS NOT NULL THEN ( SELECT (to_char(analise_cadastral.anac_dt_exclusao, 'dd/mm/yyyy') || '-') || usuario_1.usu_nome " +
                            "                                                                         FROM usuario usuario_1 " +
                            "                                                                        WHERE usuario_1.usuoid = analise_cadastral.anac_usuoid_exclusao) " +
                            "            ELSE ' ' " +
                            "        END AS dtExclusaoFicha " +
                            "   FROM " +
                            "    enum_ficha_status " +
                            "    INNER JOIN analise_cadastral ON (enumoid= anac_status " +
                            "                                AND (anac_dt_criacao > '|data-inicial| 00:00' " +
                            "                                AND  anac_dt_criacao < '|data-final| 23:59')) " +
                            "    INNER JOIN area T ON (anac_cliente_areaoid=T.areaoid " +
                            "                     AND (anac_dt_criacao > '|data-inicial| 00:00' " +
                            "                     AND  anac_dt_criacao < '|data-final| 23:59')) " +
                            "    INNER JOIN area U ON (anac_filial_areaoid = U.areaoid) " +
                            "    LEFT  JOIN analise_cadastral_motorista ON (analise_cadastral.anacoid = analise_cadastral_motorista.anacm_anacoid " +
                            "                                          AND (anacm_dt_registro > '|data-inicial| 00:00' " +
                            "                                          AND  anacm_dt_registro < '|data-final| 23:59')) " +
                            "    LEFT  JOIN motorista ON (analise_cadastral_motorista.anacm_motoid = motorista.motoid) " +
                            "    LEFT  JOIN analise_cadastral_veiculo ON (analise_cadastral.anacoid = anacv_anacoid " +
                            "                                        AND (anacv_dt_registro > '|data-inicial| 00:00' " +
                            "                                        AND  anacv_dt_registro < '|data-final| 23:59')) " +
                            "    LEFT  JOIN veiculo ON anacv_veioid = veioid " +
                            "    LEFT  JOIN enum_referencias_avaliacao enumRefPes ON (enumRefPes.enumoid = analise_cadastral_motorista.anacm_ref_pessoal) " +
                            "    LEFT  JOIN enum_referencias_avaliacao enumRefCom ON (enumRefCom.enumoid = analise_cadastral_motorista.anacm_ref_comercial) " +
                            "    LEFT  JOIN enum_referencias_avaliacao enumRefCri ON (enumRefCri.enumoid = analise_cadastral_motorista.anacm_criminal)" +
                            "  WHERE " + parameters ;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fmtInicial = formatter.format(dtInicial);
            String fmtFinal = formatter.format(dtFinal);

            query = query.replace("|data-inicial|", fmtInicial);
            query = query.replace("|data-final|", fmtFinal);

            query = "SELECT * " +
                    "  FROM (" + query +  ") AS query " +
                    "  ORDER  BY dtVencimento DESC, dtCriacao ASC,  statusFicha ASC";

            listaFicha = em.createNativeQuery( query, "ListaFichaMapping").getResultList();

            try {
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("frmListaCadastral:cadastroTable");
            } catch (Exception e) {}


            if( listaFicha.size() == 0 ) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso!", "Sua pesquisa não produziu resultados. Verifique seus filtros e tente novamente."));
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso!","Lista Cadastral não pode ser carregada. Informe ao suporte técnico."));
        }
    }

    public void onRowUnselect(UnselectEvent event) {
        acCadastro = new acCadastro();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<acListaFicha> getListaFicha() {
        return listaFicha;
    }

    public void setListaFicha(List<acListaFicha> listaFicha) {
        this.listaFicha = listaFicha;
    }

    public List<FichaStatus> getLstFichaStatus() {
        return lstFichaStatus;
    }

    public void setLstFichaStatus(List<FichaStatus> lstFichaStatus) {
        this.lstFichaStatus = lstFichaStatus;
    }

    public List<FichaTipo> getLstFichaTipo() {
        return lstFichaTipo;
    }

    public void setLstFichaTipo(List<FichaTipo> lstFichaTipo) {
        this.lstFichaTipo = lstFichaTipo;
    }

    public List<Usuario> getLstUsuariosInternos() {
        return lstUsuariosInternos;
    }

    public void setLstUsuariosInternos(List<Usuario> lstUsuariosInternos) {
        this.lstUsuariosInternos = lstUsuariosInternos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public br.com.global5.manager.model.analise.acCadastro getAcCadastro() {
        return acCadastro;
    }

    public void setAcCadastro(br.com.global5.manager.model.analise.acCadastro acCadastro) {
        this.acCadastro = acCadastro;
    }

    public int getnFicha() {
        return nFicha;
    }

    public void setnFicha(int nFicha) {
        this.nFicha = nFicha;
    }

    public int getnLiberacao() {
        return nLiberacao;
    }

    public void setnLiberacao(int nLiberacao) {
        this.nLiberacao = nLiberacao;
    }

    public int getnRenavam() {
        return nRenavam;
    }

    public void setnRenavam(int nRenavam) {
        this.nRenavam = nRenavam;
    }

    public String getnChassis() {
        return nChassis;
    }

    public void setnChassis(String nChassis) {
        this.nChassis = nChassis;
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

    public int getIdFichaTipo() {
        return idFichaTipo;
    }

    public void setIdFichaTipo(int idFichaTipo) {
        this.idFichaTipo = idFichaTipo;
    }

    public int getIdFinalizador() {
        return idFinalizador;
    }

    public void setIdFinalizador(int idFinalizador) {
        this.idFinalizador = idFinalizador;
    }

    public int getFichaStatus() {
        return fichaStatus;
    }

    public void setFichaStatus(int fichaStatus) {
        this.fichaStatus = fichaStatus;
    }

    public CadastroService getListaService() {
        return listaService;
    }

    public void setListaService(CadastroService listaService) {
        this.listaService = listaService;
    }

    public FichaStatusService getFichaStatusService() {
        return fichaStatusService;
    }

    public void setFichaStatusService(FichaStatusService fichaStatusService) {
        this.fichaStatusService = fichaStatusService;
    }

    public FichaTipoService getFichaTipoService() {
        return fichaTipoService;
    }

    public void setFichaTipoService(FichaTipoService fichaTipoService) {
        this.fichaTipoService = fichaTipoService;
    }

    public UsuarioService getUsuService() {
        return usuService;
    }

    public void setUsuService(UsuarioService usuService) {
        this.usuService = usuService;
    }

    public String getHrInicial() {
        return hrInicial;
    }

    public void setHrInicial(String hrInicial) {
        this.hrInicial = hrInicial;
    }

    public String getHrFinal() {
        return hrFinal;
    }

    public void setHrFinal(String hrFinal) {
        this.hrFinal = hrFinal;
    }

    public String getMotorista() {
        return Motorista;
    }

    public void setMotorista(String motorista) {
        Motorista = motorista;
    }

    public String getUsuFinalizador() {
        return usuFinalizador;
    }

    public void setUsuFinalizador(String usuFinalizador) {
        this.usuFinalizador = usuFinalizador;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public List<MotoristaVinculo> getLstTipoMotorista() {
        return lstTipoMotorista;
    }

    public void setLstTipoMotorista(List<MotoristaVinculo> lstTipoMotorista) {
        this.lstTipoMotorista = lstTipoMotorista;
    }

    public int getTipoMotorista() {
        return tipoMotorista;
    }

    public void setTipoMotorista(int tipoMotorista) {
        this.tipoMotorista = tipoMotorista;
    }

    public MotoristaVinculoService getMotVinculoService() {
        return motVinculoService;
    }

    public void setMotVinculoService(MotoristaVinculoService motVinculoService) {
        this.motVinculoService = motVinculoService;
    }
}
