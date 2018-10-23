package br.com.global5.manager.reports.bean;


import br.com.global5.infra.util.AppUtils;
import br.com.global5.manager.bean.geral.LogonMB;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.reports.model.Faturamento;
import br.com.global5.manager.reports.model.HistoricoFichas;
import br.com.global5.manager.service.geral.UsuarioService;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;
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
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Named
@ViewAccessScoped


public class HistoricoFichasMB implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


    private EntityManager em;

	private List<HistoricoFichas> listaHistorico;

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

    public HistoricoFichasMB() {
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
    }

    public long Tempo(Date dtInicial, Date dtFinal) {
        try {
            return AppUtils.getDateDiff(dtFinal, dtInicial, TimeUnit.DAYS);
        } catch (Exception e) {
            return 0;
        }
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

            try {

                if( areaCliente != null  ) {
                    haveParameters = true;
                }

                if( dtInicial != null || dtFinal != null ) {
                    haveParameters = true;
                }

                if( AppUtils.getDateDiff(dtInicial, dtFinal, TimeUnit.DAYS) > 61 )  {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso!","O período é limitado a 60 dias, por favor tente outras datas dentro do intervalo."));
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

            EntityManager em = usuService.crud().getEntityManager();

            String query = "SELECT " +
                            " row_number() OVER (ORDER BY ficoid DESC, anacoid DESC) AS id, " +
                            "  ficoid, " +
                            "  anacoid, " +
                            "  cast(date_part('day', date_trunc('day', anac_dt_termino ) - date_trunc('day', (SELECT X.anac_dt_termino " +
                            "                             FROM analise_cadastral X " +
                            "                             WHERE X.anacoid < L.anacoid AND X.anac_st_itens @> L.anac_st_itens AND " +
                            "                                   X.anac_status IN (8, 142) AND X.anac_dt_exclusao IS NULL AND " +
                            "                                   X.anac_tipo_ficha IN (1, 3) AND X.anac_cliente_areaoid = L.anac_cliente_areaoid " +
                            "                             ORDER BY 1 DESC " +
                            "                             LIMIT 1))) as INTEGER)   AS data_ultimo_cadastro," +
                            "  anac_dt_termino  AS termino, " +
                            "  T.enum_nome                            AS tipo_ficha, " +
                            "  S.enum_nome                            AS status, " +
                            "  anac_valor, " +
                            "  anac_dt_termino    AS dias_ultimo_cadastro, " +
                            "  CASE WHEN ficoid = anacoid " +
                            "    THEN mot_nome " +
                            "    ELSE ' ' END                            AS motorista, " +
                            "  CASE WHEN ficoid = anacoid " +
                            "    THEN E.enum_nome " +
                            "    ELSE ' ' END                            AS vinculo, " +
                            "  CASE WHEN ficoid = anacoid " +
                            "    THEN c.vei_placa " +
                            "    ELSE ' ' END                            AS cavalo, " +
                            "  CASE WHEN ficoid = anacoid " +
                            "    THEN p1.prop_nome " +
                            "    ELSE ' ' END                            AS proprietario_cavalo, " +
                            "  CASE WHEN ficoid = anacoid " +
                            "    THEN r1.vei_placa " +
                            "    ELSE ' ' END                            AS reboque1, " +
                            "  CASE WHEN ficoid = anacoid " +
                            "    THEN p2.prop_nome " +
                            "    ELSE ' ' END                            AS proprietario_reboque1, " +
                            "  CASE WHEN ficoid = anacoid " +
                            "    THEN r2.vei_placa " +
                            "    ELSE ' ' END                            AS reboque2, " +
                            "  CASE WHEN ficoid = anacoid " +
                            "    THEN p3.prop_nome " +
                            "    ELSE ' ' END                            AS proprietario_reboque2, " +
                            "  CASE WHEN ficoid = anacoid " +
                            "    THEN r3.vei_placa " +
                            "    ELSE ' ' END                            AS reboque3, " +
                            "  CASE WHEN ficoid = anacoid " +
                            "    THEN p4.prop_nome " +
                            "    ELSE ' ' END                            AS proprietario_reboque3 " +
                            " " +
                            "FROM enum_ficha_tipo T, enum_ficha_status S, analise_cadastral L INNER JOIN " +
                            " " +
                            "  (SELECT " +
                            "     anacoid       AS ficoid, " +
                            "     anac_st_itens AS fic_st_itens " +
                            "   FROM analise_cadastral " +
                            "   WHERE anac_cliente_areaoid = |idCliente| AND anac_status IN (8, 9, 142, 143) AND " +
                            "         anac_dt_termino  BETWEEN '|data-inicial| 00:00' AND '|data-final| 23:59') f " +
                            "    ON anacoid <= f.ficoid AND anac_cliente_areaoid = |idCliente| AND anac_st_itens @> fic_st_itens " +
                            " " +
                            "  LEFT OUTER JOIN analise_cadastral_motorista ON anacoid = anacm_anacoid " +
                            "  LEFT OUTER JOIN motorista ON anacm_motoid = motoid " +
                            "  LEFT OUTER JOIN enum_motorista_vinculo E ON anacm_vinculo = E.enumoid " +
                            "  LEFT OUTER JOIN analise_cadastral_veiculo v1 ON anacoid = v1.anacv_anacoid AND v1.anacv_tipo = 46 " +
                            "  LEFT OUTER JOIN veiculo c ON v1.anacv_veioid = c.veioid " +
                            "  LEFT OUTER JOIN proprietario p1 ON c.vei_propoid = p1.propoid " +
                            "  LEFT OUTER JOIN analise_cadastral_veiculo v2 ON anacoid = v2.anacv_anacoid AND v2.anacv_tipo = 47 " +
                            "  LEFT OUTER JOIN veiculo r1 ON v2.anacv_veioid = r1.veioid " +
                            "  LEFT OUTER JOIN proprietario p2 ON r1.vei_propoid = p2.propoid " +
                            "  LEFT OUTER JOIN analise_cadastral_veiculo v3 ON anacoid = v3.anacv_anacoid AND v3.anacv_tipo = 48 " +
                            "  LEFT OUTER JOIN veiculo r2 ON v3.anacv_veioid = r2.veioid " +
                            "  LEFT OUTER JOIN proprietario p3 ON r2.vei_propoid = p3.propoid " +
                            "  LEFT OUTER JOIN analise_cadastral_veiculo v4 ON anacoid = v4.anacv_anacoid AND v4.anacv_tipo = 49 " +
                            "  LEFT OUTER JOIN veiculo r3 ON v4.anacv_veioid = r3.veioid " +
                            "  LEFT OUTER JOIN proprietario p4 ON r3.vei_propoid = p4.propoid " +
                            " " +
                            "WHERE anac_tipo_ficha = T.enumoid AND anac_status = S.enumoid AND anacoid <= ficoid AND anac_cliente_areaoid = |idCliente| AND " +
                            "      anac_status IN (8, 9, 142, 143) " +
                            "ORDER BY ficoid DESC, anacoid DESC;";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fmtInicial = formatter.format(dtInicial);
            String fmtFinal = formatter.format(dtFinal);

            query = query.replace("|data-inicial|", fmtInicial);
            query = query.replace("|data-final|", fmtFinal);
            query = query.replace("|idCliente|", areaCliente.getId().toString());
            listaHistorico = em.createNativeQuery( query, "HistoricoFichaMapping").getResultList();


            if( listaHistorico.size() == 0 ) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso!","Sua pesquisa não produziu resultados. Verifique seus filtros e tente novamente."));
                return;
            }

            try {
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("frmHistoricoFicha:historicoTable");
            } catch (Exception e) {}



        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso!","A consulta sobre o faturamento não pode ser carregada. Informe ao suporte técnico."));
        }
    }

    private String getFile(String fileName) {

        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }

    public void postProcessXLS(Object document) throws ParseException, IOException {

        XSSFWorkbook wb = (XSSFWorkbook) document;
        XSSFSheet sheet = wb.getSheetAt(0);

//        InputStream my_banner_image = new FileInputStream(
//                new File("/opt/gcadastro/images/logo.png"));
//                /* Convert picture to be added into a byte array */
//        byte[] bytes = IOUtils.toByteArray(my_banner_image);
//                /* Add Picture to Workbook, Specify picture type as PNG and Get an Index */
//        int my_picture_id = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
//                /* Close the InputStream. We are ready to attach the image to workbook now */
//        my_banner_image.close();
//                /* Create the drawing container */
//        XSSFDrawing drawing = sheet.createDrawingPatriarch();
//                /* Create an anchor point */
//        XSSFClientAnchor my_anchor = new XSSFClientAnchor();
//                /* Define top left corner, and we can resize picture suitable from there */
//        my_anchor.setCol1(0);
//        my_anchor.setRow1(0);
//                /* Invoke createPicture and pass the anchor point and ID */
//        XSSFPicture  my_picture = drawing.createPicture(my_anchor, my_picture_id);
//                /* Call resize method, which resizes the image */
//        my_picture.resize();

        Iterator<Row> rowIterator = sheet.iterator();

        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        XSSFCellStyle style1 = wb.createCellStyle();
        style1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle style2 = wb.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.WHITE.index);
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int ficha = 0;
            int cor = 0;

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if( cell.getColumnIndex() == 0 ) {
                    ficha = Integer.valueOf(cell.getStringCellValue());
                    cor = 0;
                }
                if( cell.getColumnIndex() == 1 ) {
                    if( Integer.valueOf(cell.getStringCellValue()) ==  ficha ) {
                        cor = -1;
                    }
                }

                if( cor == -1 ) {
                    cell.setCellStyle(style1);
                }

                if (cell.getColumnIndex() > 1) {
                    try {

                        if (!cell.getStringCellValue().equals("")) {
                            cell.setCellValue(AppUtils.parse(cell.getStringCellValue(), Locale.FRANCE));
                        }
                    } catch (Exception e) {}
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

    public List<HistoricoFichas> getListaHistorico() {
        return listaHistorico;
    }

    public void setListaHistorico(List<HistoricoFichas> listaHistorico) {
        this.listaHistorico = listaHistorico;
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
