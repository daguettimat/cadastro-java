package br.com.global5.manager.reports.bean;


import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.bean.geral.LogonMB;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.reports.model.Faturamento;
import br.com.global5.manager.service.geral.UsuarioService;
import org.apache.commons.lang.StringUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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


public class FaturamentoMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


    private EntityManager em;

	private List<Faturamento> listaFaturamento;

	private int id;
	private Usuario usuario;

	@Inject
    private UsuarioService usuService;

	//
    // Variáveis de Filtro
    //

    private Date dtInicial;
    private Date dtFinal;

    private String nomeCliente;
    private String nomeUnidade;

    private Area   areaCliente;
    private Area   areaFilial;

    private boolean habilitaArea;
    private boolean habilitaFilial;

    public FaturamentoMB() {
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
                nomeCliente = usuario.getPessoa().getFuncao().getArea().getNome();
                areaCliente = usuario.getPessoa().getFuncao().getArea();
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
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista Cadastral " + getId()
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

                if( AppUtils.getDateDiff(dtInicial, dtFinal, TimeUnit.DAYS) > 31 )  {
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

//            if( usuario.getTipo() != "H" && usuario.getTipo() != "A" ) {
//                if( usuario.getPessoa().getFuncao().getArea().getNivel().getId() == 2 ) {
//                    parameters += " t.areaoid = " + usuario.getPessoa().getFuncao().getArea().getId() + " AND ";
//                }
//                if( usuario.getPessoa().getFuncao().getArea().getNivel().getId() == 3 ) {
//                    parameters += " u.areaoid = " + usuario.getPessoa().getFuncao().getArea().getId() + " AND ";
//                }
//
//            }

            parameters += " 0 = 0 AND ";

            EntityManager em = usuService.crud().getEntityManager();

            String query = "SELECT " +
                            "  row_number() over() as id, " +
                            "  T.areaoid      AS traoid, " +
                            "  T.area_nome    AS tranome, " +
                            "  U.areaoid      AS utraoid, " +
                            "  U.area_nome    AS utranome, " +
                            "  pes_documento1 AS utracnpj, " +
                            "  con_fatura_minima  AS trfminima, " +
                            "  null          AS tracontr_recbto, " +
                            "  (SELECT sum(anac_valor) " +
                            "   FROM analise_cadastral " +
                            "   WHERE anac_cliente_areaoid = T.areaoid " +
                            "     AND anac_status IN (8, 9, 142, 143) " +
                            "     AND anac_dt_exclusao IS NULL " +
                            "     AND anac_dt_termino BETWEEN '|data-inicial| 00:00:00' AND '|data-final| 23:59:59')  AS valorTotal, " +
                            "  (SELECT sum(anac_valor) " +
                            "   FROM analise_cadastral " +
                            "   WHERE anac_cliente_areaoid = T.areaoid " +
                            "     AND anac_filial_areaoid = U.areaoid " +
                            "     AND anac_status IN (8, 9, 142, 143) " +
                            "     AND anac_dt_exclusao IS NULL " +
                            "     AND anac_dt_termino BETWEEN '|data-inicial| 00:00:00' AND '|data-final| 23:59:59')  AS valor " +
                            "FROM contrato_produto, contrato, pessoa, area T " +
                            "  JOIN area U ON (U.area_anvloid = 3 AND " +
                            "                  T.area_anvloid = 2 AND " +
                            "                  U.area_areaoid_pai = T.areaoid) " +
                            "WHERE " + parameters +
                            "      con_areaoid = T.areaoid " +
                            "  AND T.area_dt_criacao < '|data-final|'" +
                            "  AND T.area_dt_exclusao IS NULL " +
                            "  AND con_areaoid = T.areaoid " +
                            "  AND conoid = conp_conoid " +
                            "  AND conp_prodoid = 1 " +
                            "  AND con_pesoid = pesoid " +
                            "  AND con_dt_exclusao is null " +
                            "  AND (T.area_status = 'A' OR (SELECT count(*) " +
                            "                                 FROM analise_cadastral " +
                            "                                WHERE anac_dt_termino BETWEEN '|data-inicial| 00:00:00' AND '|data-final| 23:59:59' " +
                            "                                  AND anac_filial_areaoid = U.areaoid) > 0) " +
                            "ORDER BY T.area_nome, U.area_nome";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fmtInicial = formatter.format(dtInicial);
            String fmtFinal = formatter.format(dtFinal);

            query = query.replace("|data-inicial|", fmtInicial);
            query = query.replace("|data-final|", fmtFinal);

            listaFaturamento = em.createNativeQuery( query, "FaturamentoMapping").getResultList();

            try {
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("frmFaturamento:fatTable");
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
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso!","A consulta sobre o faturamento não pode ser carregada. Informe ao suporte técnico."));
        }
    }

    public static String fmtCNPJ(String cnpj) {
        return AppUtils.formataCnpj(cnpj);
    }

    public void postProcessXLS(Object document) throws ParseException {

        XSSFWorkbook wb = (XSSFWorkbook) document;
        XSSFSheet sheet = wb.getSheetAt(0);

        Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();

        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getColumnIndex() > 1) {
                    if(!cell.getStringCellValue().equals("")) {
                        cell.setCellValue(AppUtils.parse(cell.getStringCellValue(), Locale.FRANCE));
                    }
                }
            }
        }
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

    public List<Faturamento> getListaFaturamento() {
        return listaFaturamento;
    }

    public void setListaFaturamento(List<Faturamento> listaFaturamento) {
        this.listaFaturamento = listaFaturamento;
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
}
