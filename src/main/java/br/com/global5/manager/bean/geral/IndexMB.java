package br.com.global5.manager.bean.geral;

import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.chamado.Chamado;
import br.com.global5.manager.chamado.service.ChamadoService;
import br.com.global5.manager.model.analise.acResumoFichas;
import br.com.global5.manager.model.analise.acTimeLine;
import br.com.global5.manager.model.contrato.produtos.AcAreaProduto;
import br.com.global5.manager.model.geral.Avisos;
import br.com.global5.manager.model.geral.AvisosLidos;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.analise.CadastroService;
import br.com.global5.manager.service.analise.MotoristaService;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.manager.service.cadastro.FichaRenovacaoService;
import br.com.global5.manager.service.cadastro.PessoaService;
import br.com.global5.manager.service.geral.AvisosLidosService;
import br.com.global5.manager.service.geral.AvisosService;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Created by zielinski on 23/02/17.
 */

@Named
@SessionScoped
public class IndexMB implements Serializable {

    @Inject
    private CadastroService cadService;

    @Inject
    private MotoristaService motService;

    @Inject
    private AvisosService avisosService;

    @Inject
    private AvisosLidosService avisosLidosService;


    @Inject
    private FichaRenovacaoService renovacaoService;
    
    
    @Inject
    private ChamadoService chamadoService;

    @Inject
	private PessoaService pesService;
    
    @Inject
    private AreaService areaService;
    
    private  Date today;
    private  Date past;
    private  Usuario usuario;
    private  String sqlQuery;

    private boolean filial;
    private int client;
    private boolean admin;
    private boolean nivelUsuario = false;
    private String msgUsuario;
    private int msgUsuarioId;
    private Avisos avisos;
    private List<Avisos> lstAvisos;
    private List<Object> lstResumo;
    private Integer total;
    private Integer aprovadas;
    private Integer reprovadas;

    private BarChartModel model;
    
    
    private BarChartModel modelCre;
    private List<Object> lstResumoCRE;
    private Integer totalCRE;
    private Integer abertosCRE;
    private Integer bancosCRE;
    
    private BarChartModel modelCreT;
    private List<Object> lstResumoCRET;
    private BigDecimal totalCRET;
    private BigDecimal abertosCRET;
    private BigDecimal bancosCRET;
    
    public IndexMB() {

        try {
            usuario = checkUsuario.valid();            
            Hibernate.initialize(usuario.getPessoa());
            Hibernate.initialize(usuario.getPessoa().getFuncao());
            Hibernate.initialize(usuario.getPessoa().getFuncao().getArea());
            Hibernate.initialize(usuario.getPessoa().getFuncao().getArea().getRoot());
            filial = (usuario.getPessoa().getFuncao().getArea().getRoot() != null);
            client = usuario.getPessoa().getFuncao().getArea().getId();

            admin = ( usuario.getTipo().equals("H") || usuario.getTipo().equals("A") );


            if( usuario.isInterno() ) {
                sqlQuery = "SELECT max(anac_dt_criacao) as date FROM analise_cadastral cad";
            } else {
                if( usuario.getPessoa().getFuncao().getArea().getRoot() != null ) {
                    sqlQuery = "SELECT max(anac_dt_criacao) as date " +
                            "     FROM analise_cadastral cad " +
                            "    WHERE anac_filial_areaoid = " + usuario.getPessoa().getFuncao().getArea().getRoot().getId();
                } else {
                    int user = usuario.getPessoa().getFuncao().getArea().getId();
                    sqlQuery = "SELECT max(anac_dt_criacao) as date " +
                            "     FROM analise_cadastral cad " +
                            "    WHERE anac_filial_areaoid = " + user;
                }
            }

        } catch (Exception e) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml");
            } catch (IOException e1) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso!","Não foi possível efetuar o login! Verifique e tente novamente!"));
            }
        }

    }
    
    
    public void resumoGeral() {

        usuario = checkUsuario.valid();

        EntityManager em = avisosService.crud().getEntityManager();

        String query =
                "SELECT    row_number() over () as id, " +
                        "  CAST(EXTRACT(MONTH FROM ac.anac_dt_criacao) AS INTEGER) AS mes," +
                        "  CAST(EXTRACT(YEAR FROM ac.anac_dt_criacao) AS INTEGER) AS ano," +
                        "  count(*) FILTER (WHERE ac.anac_status BETWEEN 0 AND 5) AS analise," +
                        "  count(*) FILTER (WHERE ac.anac_status = 6)  AS pendentes," +
                        "  count(*) FILTER (WHERE ac.anac_status = 7)  AS excluidas," +
                        "  count(*) FILTER (WHERE ac.anac_status = 8)  AS recomendadas," +
                        "  count(*) FILTER (WHERE ac.anac_status = 9)  AS naorecomendadas," +
                        "  count(*) FILTER (WHERE ac.anac_status = 28) AS renovacoes," +
                        "  count(*) as total" +
                        " FROM analise_cadastral ac" +
                        "   LEFT JOIN area a" +
                        "          ON ac.anac_cliente_areaoid = a.areaoid" +
                        "   LEFT JOIN area_funcao af" +
                        "          ON a.areaoid = af.afun_areaoid" +
                        "   LEFT JOIN pessoa p" +
                        "          ON af.afunoid = p.pes_afunoid" +
                        "   LEFT JOIN usuario u" +
                        "          ON u.usu_pesoid = p.pesoid" +
                        " WHERE u.usuoid = " + usuario.getId() +
                        "   AND EXTRACT(YEAR FROM ac.anac_dt_criacao) = EXTRACT(YEAR FROM NOW())" +
                        " GROUP BY" +
                        "  EXTRACT(MONTH FROM ac.anac_dt_criacao)," +
                        "  EXTRACT(YEAR FROM ac.anac_dt_criacao)" +
                        " ORDER BY" +
                        "  EXTRACT(YEAR FROM ac.anac_dt_criacao)," +
                        "  EXTRACT(MONTH FROM ac.anac_dt_criacao);";


        lstResumo = em.createNativeQuery(query).getResultList();

        model = new BarChartModel();

        int Max = 0;
        int ultimo = 0;

        for(Object ficha : lstResumo ) {
            Object[] object = (Object[]) ficha;
            BarChartSeries cs = new BarChartSeries();
            cs.setLabel(object[1] + "/" + object[2]);
//            cs.set("Analise", ((BigInteger) object[3]).intValue());
//            cs.set("Pendentes", ((BigInteger) object[4]).intValue());
//            cs.set("Excluidas", ((BigInteger) object[5]).intValue());
            cs.set("Recomendadas",((BigInteger) object[6]).intValue());
            cs.set("Não Recomendadas", ((BigInteger) object[7]).intValue());
//            cs.set("Renovações", ((BigInteger) object[8]).intValue());
//            cs.set("Total", ((BigInteger) object[9]).intValue());

            if( Max < ((BigInteger) object[9]).intValue() ) {
                Max = ((BigInteger) object[9]).intValue();
            }

            if( ultimo < ((Integer) object[1]).intValue() ) {
                total = ((BigInteger) object[9]).intValue();
                aprovadas = (((BigInteger) object[6]).intValue() * 100 ) / total;
                reprovadas = (((BigInteger) object[7]).intValue() * 100 ) / total;
                total = total * 100 / Max;

            }
            model.addSeries(cs);
        }

        model.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        model.setLegendPosition("ne");

        Axis xAxis = model.getAxis(AxisType.X);
        xAxis.setLabel("Meses");

        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Fichas");
        yAxis.setMin(0);
        yAxis.setMax(abs(Max * 1.10));
    }
    
    public void enviarFicha() {
        try {
            usuario = checkUsuario.valid();
            FacesContext.getCurrentInstance().getExternalContext().redirect("pages/fichas/enviarFichaMenu.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Lista Cadastral não pode ser carregada. " +
                            "Informe ao suporte técnico.", null));
        }

    }
    
    public void enviarFichaVitimologia() {
        try {
        	usuario = checkUsuario.valid();
            FacesContext.getCurrentInstance().getExternalContext().redirect("pages/fichas_vitimologia/enviarFichaMenu.xhtml");
//            FacesContext.getCurrentInstance().getExternalContext().redirect("pages/fichas_vitimologia/fichaUFVitimologia.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Lista Cadastral não pode ser carregada. " +
                            "Informe ao suporte técnico.", null));
        }

    }
    
    public void listaAvisos(Usuario usuario) {

        usuario = checkUsuario.valid();

        EntityManager em = avisosService.crud().getEntityManager();

        String query =
                "SELECT qa.*" +
                "  FROM quadro_de_avisos qa" +
                "  LEFT JOIN quadro_de_avisos_lidos qal" +
                "         ON qa.qavsoid = qal.qavsl_qavsoid" +
                " WHERE qa.qavs_dt_validade >= now()" +
                "   AND qa.qavs_dt_inicio <= now()" +
                "   AND qa.qavsoid NOT IN " +
                        "( SELECT qavsl_qavsoid " +
                        "    FROM quadro_de_avisos_lidos " +
                        "   WHERE qavsl_usuoid = " + usuario.getId() +") LIMIT 1;";


        lstAvisos = em.createNativeQuery(query, Avisos.class).getResultList();

        if( lstAvisos.size() > 0 ) {

            for (Avisos avisos : lstAvisos) {
                msgUsuario = avisos.getDocumento();
                msgUsuarioId = avisos.getId();
                this.avisos = avisos;
                break;
            }
        } else {
            msgUsuario = "";
            msgUsuarioId = 0;
        }


    }

    public void encerrarAviso() {

        if( msgUsuario != "") {
            AvisosLidos avisosLidos = new AvisosLidos(avisos, new Date(), usuario);
            avisosLidosService.crud().saveOrUpdate(avisosLidos);
            msgUsuario = "";
        }


    }

    @PostConstruct
    public void init()  {

        if( usuario == null ) {
            return;
        }

        List retorno = motService.crud().criteria().rawQuery(sqlQuery);
        if( retorno.get(0) == null ) {
            today = new Date();
        } else {
            today = (Date) retorno.get(0);
        }

        if( usuario.getPessoa().getFuncao().getArea().getNivel().getId() == 2 ) {
            nivelUsuario = true;
        }

        Calendar now = Calendar.getInstance();
        now.setTime(today);
        now.set(Calendar.HOUR, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);

        today = now.getTime();

        now.add(Calendar.DATE, -2);
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);

        past = now.getTime();

        resumoGeral();
        
    }
    
    
    public int aprovadas() {
        if( usuario == null ) {
            return 0;
        }

        if( admin ) {
            int total =  cadService.crud().eq("status.id", 8)
                    .between("dtCriacao",past,today).count();

            int excluidas = cadService.crud().eq("status.id", 7)
                    .between("dtCriacao",past,today).count();

            return total - excluidas;

        } else {
            int total =  cadService.crud().eq("status.id", 8)
                    .between("dtCriacao",past,today)
                    .eq(filial ? "filialArea.id" : "clienteArea.id", client).count();

            int excluidas =  cadService.crud().eq("status.id", 7)
                    .between("dtCriacao",past,today)
                    .eq(filial ? "filialArea.id" : "clienteArea.id", client).count();

            return total - excluidas;
        }
    }

    public int renovacoes() {
        if( usuario == null ) {
            return 0;
        }

        if( admin ) {
            return renovacaoService.crud().between("status.id", 213, 214).count();
        } else {
            return renovacaoService.crud().between("status.id", 213, 214)
                    .eq(filial ? "filialArea.id" : "clienteArea.id", client).count();
        }
    }

    public int reanalise() {
        if( usuario == null ) {
            return 0;
        }

        if( admin ) {

            Calendar now = Calendar.getInstance();
            now.setTime(today);
            now.set(Calendar.HOUR, 23);
            now.set(Calendar.MINUTE, 59);
            now.set(Calendar.SECOND, 59);

            now.add(Calendar.DATE, -16);

            return cadService.crud().eq("status.id", 219)
                    .between("dtCriacao",now.getTime(),today).count();
        }

        return 0;
    }


    public int ultimas() {
        if( usuario == null ) {
            return 0;
        }

        if( admin ) {
            return cadService.crud().between("dtCriacao", past, today).count();
        } else {
            return cadService.crud().between("dtCriacao", past, today)
                    .eq(filial ? "filialArea.id" : "clienteArea.id", client).count();
        }
    }

    public int analise() {
        if( usuario == null ) {
            return 0;
        }
        if( admin ) {

            int result1 = cadService.crud().eq("status.id", 4)
                    .between("dtCriacao", past, today).count();

            int result2 = cadService.crud().eq("status.id", 5)
                    .between("dtCriacao", past, today).count();

            return result1 + result2;
        } else {
            int result3 = cadService.crud().eq("status.id", 4)
                    .between("dtCriacao", past, today)
                    .eq( filial ? "filialArea.id" : "clienteArea.id", client).count();

            int result4 = cadService.crud().eq("status.id", 5)
                    .between("dtCriacao", past, today)
                    .eq( filial ? "filialArea.id" : "clienteArea.id", client).count();

            return result3 + result4;
        }
    }

    public int reprovadas() {
        if( usuario == null ) {
            return 0;
        }

        int client = 0;

        if( admin ) {
            return cadService.crud().eq("status.id", 9)
                    .between("dtCriacao", past, today).count();
        } else {
            return cadService.crud().eq("status.id", 9)
                    .between("dtCriacao", past, today)
                    .isNull("dtExclusao")
                    .eq(filial ? "filialArea.id" : "clienteArea.id", client).count();
        }

    }

    public int aguardaCliente() {
        if( usuario == null ) {
            return 0;
        }
        if( admin ) {
            return cadService.crud().eq("status.id", 6)
                    .between("dtCriacao", past, today).count();
        } else {
            return cadService.crud().eq("status.id", 6)
                    .between("dtCriacao", past, today)
                    .eq( filial ? "filialArea.id" : "clienteArea.id", client).count();
        }


    }

    public String flagPendencia(Integer id) {

        return AppUtils.flagPendencia(id);
    }

    public String timeline() throws Exception {

        if( usuario == null ) {
            return null;
        }

        EntityManager em = renovacaoService.crud().getEntityManager();
        String where;
        if( usuario.isInterno() ) {
            where = "";
        } else {
            where = "WHERE " + (filial ? "u.areaoid" : "t.areaoid") + " = " + client;
        }

        String query = "SELECT * FROM (" +
                                "  SELECT DISTINCT ON (analise_cadastral.anacoid)" +
                                "    analise_cadastral.anacoid," +
                                "    t.areaoid  AS clienteoid," +
                                "    u.areaoid  AS filialoid," +
                                "    analise_cadastral.anac_dt_criacao," +
                                "    analise_cadastral.anac_status," +
                                "    analise_cadastral.anac_tipo_ficha," +
                                "    analise_cadastral.anac_aliboid," +
                                "    analise_cadastral_motorista.anacm_motoid," +
                                "    motorista.mot_documento1," +
                                "    motorista.mot_nome," +
                                "    enum_ficha_status.enum_nome  AS anac_status_nome," +
                                "    (SELECT usu_nome" +
                                "     FROM usuario" +
                                "     WHERE usuoid = anac_usuoid_criacao)  AS usu_criacao," +
                                "    (SELECT usuario_1.usu_nome" +
                                "     FROM usuario usuario_1" +
                                "     WHERE usuario_1.usuoid = analise_cadastral.anac_usuoid_termino) AS usu_termino," +
                                "    to_char(analise_cadastral.anac_dt_criacao, 'dd/mm/yyyy hh24:mi') AS cadastro," +
                                "    analise_cadastral.anac_dt_exclusao" +
                                "  FROM usuario," +
                                "    enum_ficha_status" +
                                "    INNER JOIN" +
                                "    analise_cadastral ON (enumoid = anac_status AND anac_dt_criacao > '|data-inicial| 00:00')" +
                                "    INNER JOIN area T ON (anac_cliente_areaoid = T.areaoid)" +
                                "    INNER JOIN area U ON (anac_filial_areaoid = U.areaoid)" +
                                "    LEFT JOIN analise_cadastral_motorista" +
                                "      ON (analise_cadastral.anacoid = analise_cadastral_motorista.anacm_anacoid AND anacm_dt_registro > '|data-inicial| 00:00')" +
                                "    LEFT JOIN motorista ON (analise_cadastral_motorista.anacm_motoid = motorista.motoid)" +
                                "    LEFT JOIN analise_cadastral_veiculo c" +
                                "      ON (analise_cadastral.anacoid = c.anacv_anacoid AND c.anacv_dt_registro > '|data-inicial| 00:00')" +
                                "    LEFT JOIN veiculo v1 ON c.anacv_veioid = v1.veioid AND v1.vei_tipo = 59" +
                                "    LEFT JOIN analise_cadastral_veiculo r" +
                                "      ON analise_cadastral.anacoid = r.anacv_anacoid AND r.anacv_dt_registro > '|data-inicial| 00:00'" +
                                "    LEFT JOIN veiculo v2 ON r.anacv_veioid = v2.veioid AND v2.vei_tipo = 60" +
                                where +
                                ") AS RESULT " +
                                "ORDER BY anac_dt_criacao DESC;";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fmtInicial = formatter.format(past);

        query = query.replace("|data-inicial|", fmtInicial);

        List<acTimeLine> listaFicha = em.createNativeQuery(query, "TimeLineMapping").getResultList();

        if( listaFicha.size() == 0 ) {
            return "";
        }

        StringBuilder html = new StringBuilder();
        html.append("<ul class=\"timeline\">");
        boolean firstRecord = true;
        String dtAnalise = "";
        for(acTimeLine timeLine : listaFicha) {

            if( ! dtAnalise.equals(timeLine.getDtCadastro().substring(0, 10))) {
                dtAnalise = timeLine.getDtCadastro().substring(0, 10);
                firstRecord = true;
            }

            switch (timeLine.getStatus()) {
                //
                // Gera Em processamento
                //
                case 4:
                case 5:
                    html.append(geraEmProcessamento(timeLine.getDtCadastro(), timeLine.getId().toString(),firstRecord));
                    firstRecord = false;
                    break;
                //
                // Aguardando Cliente
                //
                case 6:
                    html.append(geraAlerta(timeLine.getDtCadastro(), timeLine.getId().toString(),firstRecord));
                    firstRecord = false;
                    break;
                //
                // Aprovadas
                //
                case 8:
                    html.append(geraAprovada(timeLine.getDtCadastro(), timeLine.getId().toString(), timeLine.getCpf(), timeLine.getNomeMotorista(), timeLine.getLiberacao().toString(), firstRecord));
                    firstRecord = false;
                    break;
                //
                // Reprovadas
                //
                case 9:
                    html.append(geraReprovada(timeLine.getDtCadastro(), timeLine.getId().toString(),firstRecord));
                    firstRecord = false;
                    break;

            }

        }
        html.append("</ul>");


        return html.toString();
    }

    private String geraTitulo(boolean tituloData, String data) throws ParseException {

        StringBuilder html = new StringBuilder();
        SimpleDateFormat fmtBR = new SimpleDateFormat("E, dd MMM yyyy");
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = fmt.parse(data);

        if(tituloData) {
            html.append("<li class=\"time-label\">");
            html.append("<span class=\"bg-red\">");
            html.append(fmtBR.format(date));
            html.append("</span>");
            html.append("</li>");
            return html.toString();
        } else {
            return "";
        }

    }

    private String geraReprovada(String data, String numeroFicha, boolean tituloData) throws ParseException {

        SimpleDateFormat fmtBR = new SimpleDateFormat("E, dd MMM yyyy");
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = fmt.parse(data);

        StringBuilder html = new StringBuilder();

        html.append(geraTitulo(tituloData, data));
        html.append(   "<li>");
        html.append(      "<i style=\"padding-top:6px;\" class=\"fa fa-flag bg-red\"></i>");
        html.append(      "<div class=\"timeline-item\">");
        html.append(         "<span class=\"time\"><i  class=\"fa fa-clock-o\"></i>"); html.append(AppUtils.toTimeline(data));  html.append("</span>");
        html.append(         "<h3 class=\"timeline-header\"><b>Ficha <a href=\"#\">"); html.append(numeroFicha);  html.append("</a> não pode ser recomendada...</b></h3>");
        html.append(         "<div class=\"timeline-body\">");
        html.append(            "<span>A ficha possui dados, que não permitiram sua recomendação...</span>");
        html.append(         "</div>");
        html.append(         "<div class=\"timeline-footer\">");
        html.append(            "<a target=\"_blank\" href=\"http://sisold.global5.com.br/ficha_cadastral.php?ficha="); html.append(numeroFicha);  html.append("&amp;sys=1\" class=\"btn btn-success btn-xs\">Ver detalhes</a>");
        html.append(         "</div>");
        html.append(      "</div>");
        html.append(   "</li>");

        // fa-exclamation-triangle

        return html.toString();
    }

    private String geraAlerta(String data, String numeroFicha, boolean tituloData) throws ParseException {

        SimpleDateFormat fmtBR = new SimpleDateFormat("E, dd MMM yyyy");
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = fmt.parse(data);

        StringBuilder html = new StringBuilder();

        html.append(geraTitulo(tituloData, data));
        html.append(   "<li>");
        html.append(      "<i style=\"padding-top:6px;\" class=\"fa fa-exclamation-triangle bg-yellow\"></i>");
        html.append(      "<div class=\"timeline-item\">");
        html.append(         "<span class=\"time\"><i  class=\"fa fa-clock-o\"></i>"); html.append(AppUtils.toTimeline(data));  html.append("</span>");
        html.append(         "<h3 class=\"timeline-header\"><b>Ficha <a href=\"#\">"); html.append(numeroFicha);  html.append("</a> precisa de sua atenção...</b></h3>");
        html.append(         "<div class=\"timeline-body\">");
        html.append(            "<span>A ficha possui dados, que necessitam de sua atenção, por favor verifique o quadro de pendências...</span>");
        html.append(         "</div>");
        html.append(         "<div class=\"timeline-footer\">");
        html.append(            "<a target=\"_blank\" href=\"http://sisold.global5.com.br/ficha_cadastral.php?ficha="); html.append(numeroFicha);  html.append("&amp;sys=1\" class=\"btn btn-success btn-xs\">Ver detalhes</a>");
        html.append(         "</div>");
        html.append(      "</div>");
        html.append(   "</li>");

        // fa-exclamation-triangle

        return html.toString();
    }

    private String geraEmProcessamento(String data, String numeroFicha, boolean tituloData) throws ParseException {

        SimpleDateFormat fmtBR = new SimpleDateFormat("E, dd MMM yyyy");
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = fmt.parse(data);

        StringBuilder html = new StringBuilder();

        html.append(geraTitulo(tituloData, data));
        html.append(   "<li>");
        html.append(      "<i style=\"padding-top:6px;\" class=\"fa fa-gavel bg-green\"></i>");
        html.append(      "<div class=\"timeline-item\">");
        html.append(         "<span class=\"time\"><i  class=\"fa fa-clock-o\">&#160;</i>"); html.append(AppUtils.toTimeline(data));  html.append("</span>");
        html.append(         "<h3 class=\"timeline-header\"><b>Ficha <a href=\"#\">"); html.append(numeroFicha);  html.append("</a> esta em fase de análise...</b></h3>");
        html.append(         "<div class=\"timeline-body\">");
        html.append(            "<span>A ficha esta sendo análisada, aguarde, logo teremos uma reposta...</span>");
        html.append(         "</div>");
        html.append(         "<div class=\"timeline-footer\">");
        html.append(            "<a target=\"_blank\" href=\"http://sisold.global5.com.br/ficha_cadastral.php?ficha="); html.append(numeroFicha);  html.append("&amp;sys=1\" class=\"btn btn-success btn-xs\">Ver detalhes</a>");
        html.append(         "</div>");
        html.append(      "</div>");
        html.append(   "</li>");

        // fa-exclamation-triangle

        return html.toString();
    }



    private String geraAprovada(String data, String numeroFicha, String cpf, String nome, String idLiberacao, boolean tituloData) throws Exception {

        SimpleDateFormat fmtBR = new SimpleDateFormat("E, dd MMM yyyy");
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = fmt.parse(data);

        StringBuilder html = new StringBuilder();

        html.append(geraTitulo(tituloData, data));
        html.append(   "<li>");
        html.append(       "<i style=\"padding-top:6px;\" class=\"fa fa-envelope bg-blue\"></i>");
        html.append(       "<div class=\"timeline-item ui-fluid\">");
        html.append(          "<span class=\"time\"><i class=\"fa fa-clock-o\"></i>&nbsp;"); html.append(AppUtils.toTimeline(data)); html.append("</span>");
        html.append(          "<h3 class=\"timeline-header\"><a href=\"#\">Recomendado</a> ...</h3>");
        html.append(          "<div class=\"timeline-body\">");
        html.append(             "<div class=\"row\">");
        html.append(                "<div class=\"col-lg-2\">");
        html.append(                   "<label for=\"numeroFica\">Ficha</label>");
        html.append(                   "<input class=\"form-control\" id=\"numeroFicha\" name=\"numeroFicha\" value=\""); html.append(numeroFicha);  html.append("\" type=\"text\" />");
        html.append(                "</div>");

        if(cpf != null) {
            html.append("<div class=\"col-lg-2\">");
            html.append(   "<label for=\"cpfFicha\">C.P.F.</label>");
            html.append(   "<input class=\"form-control\" id=\"cpfFicha\" type=\"text\" value=\""); html.append( cpf );  html.append( "\"/>");
            html.append("</div>");
            html.append("<div class=\"col-lg-4\">");
            html.append(   "<label for=\"nomeFicha\">Nome Motorista</label>");
            html.append(   "<input class=\"form-control\" id=\"nomeFicha\" type=\"text\" value=\""); html.append( nome ) ; html.append( "\"/>");
            html.append("</div>");
        }

        html.append(                "<div class=\"col-lg-2\">");
        html.append(                    "<label for=\"libFicha\">Liberação</label>");
        html.append(                    "<input class=\"form-control\" id=\"libFicha\" type=\"text\" value=\""); html.append(idLiberacao); html.append( "\"/>");
        html.append(                "</div>");

        html.append(             "</div>");
        html.append(          "</div>");
        html.append(          "<div class=\"timeline-footer\">");
        html.append(             "<a target=\"_blank\" href=\"http://sisold.global5.com.br/liberacao_email.php?origem=SYS&amp;ficha="); html.append( numeroFicha); html.append( "\" class=\"btn btn-warning btn-xs\">Ver detalhes</a>");
        html.append(          "</div>");
        html.append(       "</div>");
        html.append(    "</li>");

        return html.toString();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Date getPast() {
        return past;
    }

    public void setPast(Date past) {
        this.past = past;
    }

    public boolean isFilial() {
        return filial;
    }

    public void setFilial(boolean filial) {
        this.filial = filial;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isNivelUsuario() {
        return nivelUsuario;
    }

    public void setNivelUsuario(boolean nivelUsuario) {
        this.nivelUsuario = nivelUsuario;
    }

    public String getMsgUsuario() {
        return msgUsuario;
    }

    public void setMsgUsuario(String msgUsuario) {
        this.msgUsuario = msgUsuario;
    }

    public int getMsgUsuarioId() {
        return msgUsuarioId;
    }

    public void setMsgUsuarioId(int msgUsuarioId) {
        this.msgUsuarioId = msgUsuarioId;
    }

    public Avisos getAvisos() {
        return avisos;
    }

    public void setAvisos(Avisos avisos) {
        this.avisos = avisos;
    }

    public List<Avisos> getLstAvisos() {
        return lstAvisos;
    }

    public void setLstAvisos(List<Avisos> lstAvisos) {
        this.lstAvisos = lstAvisos;
    }

    public BarChartModel getModel() {
        return model;
    }

    public void setModel(BarChartModel model) {
        this.model = model;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getAprovadas() {
        return aprovadas;
    }

    public void setAprovadas(Integer aprovadas) {
        this.aprovadas = aprovadas;
    }

    public Integer getReprovadas() {
        return reprovadas;
    }

    public void setReprovadas(Integer reprovadas) {
        this.reprovadas = reprovadas;
    }


	public BarChartModel getModelCre() {
		return modelCre;
	}


	public void setModelCre(BarChartModel modelCre) {
		this.modelCre = modelCre;
	}


	public BarChartModel getModelCreT() {
		return modelCreT;
	}


	public void setModelCreT(BarChartModel modelCreT) {
		this.modelCreT = modelCreT;
	}


	public List<Object> getLstResumoCRE() {
		return lstResumoCRE;
	}


	public void setLstResumoCRE(List<Object> lstResumoCRE) {
		this.lstResumoCRE = lstResumoCRE;
	}


	public Integer getTotalCRE() {
		return totalCRE;
	}


	public void setTotalCRE(Integer totalCRE) {
		this.totalCRE = totalCRE;
	}


	public Integer getAbertosCRE() {
		return abertosCRE;
	}


	public void setAbertosCRE(Integer abertosCRE) {
		this.abertosCRE = abertosCRE;
	}


	public List<Object> getLstResumoCRET() {
		return lstResumoCRET;
	}


	public void setLstResumoCRET(List<Object> lstResumoCRET) {
		this.lstResumoCRET = lstResumoCRET;
	}


	public BigDecimal getTotalCRET() {
		return totalCRET;
	}


	public void setTotalCRET(BigDecimal totalCRET) {
		this.totalCRET = totalCRET;
	}


	public BigDecimal getAbertosCRET() {
		return abertosCRET;
	}


	public void setAbertosCRET(BigDecimal abertosCRET) {
		this.abertosCRET = abertosCRET;
	}


	public BigDecimal getBancosCRET() {
		return bancosCRET;
	}


	public void setBancosCRET(BigDecimal bancosCRET) {
		this.bancosCRET = bancosCRET;
	}
    
}
