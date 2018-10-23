package br.com.global5.manager.reports.bean;


import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.bean.geral.LogonMB;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.reports.model.FatDetalhado;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.manager.service.geral.UsuarioService;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
@ViewAccessScoped


public class FatDetalhadoMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


    private EntityManager em;



    private List<FatDetalhado> listaFaturamento;

	private int id;
	private Usuario usuario;

	@Inject
    private UsuarioService usuService;

	//
    // Variáveis de Filtro
    //

    private Date dtInicial;
    private Date dtFinal;

    private String hrInicial;
    private String hrFinal;

    private String nomeCliente;
    private String nomeUnidade;

    private Area   areaCliente;
    private Area   areaFilial;

    private boolean habilitaArea;
    private boolean habilitaFilial;

    public FatDetalhadoMB() {
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

        nomeCliente = "";
        nomeUnidade = "";

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

        clear();

        if( usuario.isInterno() || (usuario.getTipo().equals("A") || getUsuario().getTipo().equals("H")) ) {
            habilitaArea = true;
            habilitaFilial = true;
        } else {
            if( usuario.getPessoa().getFuncao().getArea().getNivel().getId() == 2 ) {
                areaCliente = usuario.getPessoa().getFuncao().getArea();
                nomeCliente = usuario.getPessoa().getFuncao().getArea().getNome();
                habilitaArea = false;
                habilitaFilial = true;
            } else {
                if( usuario.getPessoa().getFuncao().getArea().getNivel().getId() == 3 ) {
                    areaCliente = usuario.getPessoa().getFuncao().getArea().getRoot();
                    areaFilial = usuario.getPessoa().getFuncao().getArea();
                    nomeCliente =  usuario.getPessoa().getFuncao().getArea().getRoot().getNome();
                    nomeUnidade = usuario.getPessoa().getFuncao().getArea().getNome();
                    habilitaFilial = false;
                    habilitaArea = false;
                }
            }
        }

    }

    public void selectFilial(SelectEvent event) {
        areaFilial = (Area) event.getObject();
        nomeUnidade = areaFilial.getNome();
    }

    public void selectTransp(SelectEvent event) {
        areaCliente = (Area) event.getObject();
        nomeCliente = areaCliente.getNome();
        // areaFilial = null;
    }


    public String flag(int id) {
        return AppUtils.pathFlag(id);
    }



	public void clear() {

	}

    public void btnBack() {
        try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
		} catch (IOException e) {
	           FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"FatDetalhado Detalhado " + getId()
	                            + " não pode ser carregada. Informe ao suporte técnico.",null));
		}
    }

    public void btnPesquisarData() {

	    boolean haveParameters = false;
        try {

            em = usuService.crud().getEntityManager();

            String parameters = " ";

            try {


                if( areaCliente != null  ) {
                    //parameters += " tranome ilike '%" + nomeCliente + "%' AND";
                    parameters += " T.areaoid = " + areaCliente.getId() + " AND";
                    haveParameters = true;
                }

                if( areaFilial != null ) {
                    //parameters += " utranome ilike '%" + nomeUnidade + "%' AND";
                    parameters += " U.areaoid = " + areaFilial.getId() + " AND";
                    haveParameters = true;
                }

                if( dtInicial != null || dtFinal != null ) {
                    haveParameters = true;
                }

                if( areaCliente == null ) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção!","O fornecimento da transportadora é obrigatório!"));
                    return;

                }


                if( AppUtils.getDateDiff(dtInicial, dtFinal, TimeUnit.DAYS) > 91 )  {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso!","O período é limitado a 30 dias, devido ao cálculo da fatura mínima."));
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
                haveParameters = false;
            }

            if( !haveParameters ) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso!","Lista de parametros não produz resultados, nenhum filtro pode ser realizado."));
                return;

            }



            parameters += " 0 = 0 AND ";

            EntityManager em = usuService.crud().getEntityManager();

            String query =  "SELECT " +
                            "  row_number() over() as id, " +
                            "  T.area_nome     AS tranome, " +
                            "  U.area_nome     AS utranome, " +
                            "  T.areaoid       AS traoid, " +
                            "  U.areaoid       AS utraoid, " +
                            "  anacoid         AS ficoid, " +
                            " " +
                            "  (SELECT anacm_vinculo " +
                            "   FROM  analise_cadastral_motorista " +
                            "   WHERE anacm_anacoid = anacoid ) AS anacm_vinculo, " +
                            " " +
                            "  (SELECT enum_nome " +
                            "   FROM enum_motorista_vinculo, analise_cadastral_motorista " +
                            "   WHERE anacm_anacoid = anacoid AND anacm_vinculo = enumoid) AS tiponome, " +
                            "  (SELECT mot_nome " +
                            "   FROM analise_cadastral_motorista, motorista " +
                            "   WHERE anacm_anacoid = anacoid AND anacm_motoid = motoid)   AS motnome, " +
                            "  (SELECT vei_placa " +
                            "   FROM analise_cadastral_veiculo, veiculo " +
                            "   WHERE anacv_anacoid = anacoid AND anacv_veioid = veioid AND anacv_tipo = 46 " +
                            "   LIMIT 1)                                                   AS cavalo, " +
                            "  (SELECT vei_placa " +
                            "   FROM analise_cadastral_veiculo, veiculo " +
                            "   WHERE anacv_anacoid = anacoid AND anacv_veioid = veioid AND anacv_tipo = 47 " +
                            "   LIMIT 1)                                                   AS reboque_1, " +
                            "  (SELECT vei_placa " +
                            "   FROM analise_cadastral_veiculo, veiculo " +
                            "   WHERE anacv_anacoid = anacoid AND anacv_veioid = veioid AND anacv_tipo = 48 " +
                            "   LIMIT 1)                                                   AS reboque_2, " +
                            "  anac_centro_custo                                           AS ficcentro_custo, " +
                            "  anac_valor                                                  AS valorfic, " +
                            "  to_char(anac_dt_criacao, 'dd/mm/yyyy HH24:MI:SS')           AS criacao," +
                            "  to_char(anac_dt_termino, 'dd/mm/yyyy HH24:MI:SS')           AS termino,"+
                            "  to_char(date_trunc('day', anac_dt_termino)  + INTERVAL ' 1 day' * anac_dias_embarque, 'dd/mm/yyyy')" +
                                                                                        "  AS validade," +
                            "  usu_nome                                                    AS usunome, " +
                            "  anac_status, " +
                            "  anac_tipo_ficha, " +
                            "  ft.enum_nome as dscTipoFicha,  " +
                            "  anac_tipo_ficha, " +
                            "  f.enum_nome as dscFichaStatus " +
                            "FROM area T, area U, usuario, analise_cadastral, enum_ficha_status f, enum_ficha_tipo ft " +
                            "WHERE " +  parameters +
                            "      anac_cliente_areaoid = T.areaoid " +
                            "  AND anac_filial_areaoid = U.areaoid " +
                            "  AND anac_usuoid_criacao = usuoid " +
                            "  AND anac_status = f.enumoid " +
                            "  AND anac_tipo_ficha = ft.enumoid " +
                            "  AND anac_dt_termino BETWEEN '|data-inicial| 00:00:00' AND '|data-final| 23:59:59' " +
                            "  AND anac_dt_exclusao IS NULL ORDER BY 2; "  ;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fmtInicial = formatter.format(dtInicial);
            String fmtFinal = formatter.format(dtFinal);

            query = query.replace("|data-inicial|", fmtInicial);
            query = query.replace("|data-final|", fmtFinal);

            listaFaturamento = em.createNativeQuery(query, "FatDetalhadoMapping").getResultList();

            try {
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("frmFatDetalhado:fatTable");
            } catch (Exception e) {}

            if( listaFaturamento.size() == 0 ) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso!","Sua pesquisa não produziu resultados. Verifique seus filtros e tente novamente."));
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso!","A consulta sobre o FatDetalhado não pode ser carregada. Informe ao suporte técnico."));
        }
    }

    public void postProcessXLS(Object document) throws ParseException {

        XSSFWorkbook wb = (XSSFWorkbook) document;
        XSSFSheet sheet = wb.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();

        if (rowIterator.hasNext()) {
            rowIterator.next();
        }



        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if (cell.getColumnIndex() >= 12) {
                    try {
                        cell.setCellValue(AppUtils.parse(cell.getStringCellValue(), Locale.FRANCE));
                    } catch (Exception e) {}
                }
            }
        }
    }

    public static String fmtCNPJ(String cnpj) {
        Pattern pattern = Pattern.compile("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})");
        Matcher matcher = pattern.matcher(cnpj);
        if (matcher.matches())
            cnpj = matcher.replaceAll("$1.$2.$3.$4-$5");
        return cnpj;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getNomeUnidade() {
        return nomeUnidade;
    }

    public void setNomeUnidade(String nomeUnidade) {
        this.nomeUnidade = nomeUnidade;
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

    public List<FatDetalhado> getListaFaturamento() {
        return listaFaturamento;
    }

    public void setListaFaturamento(List<FatDetalhado> listaFaturamento) {
        this.listaFaturamento = listaFaturamento;
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
}
