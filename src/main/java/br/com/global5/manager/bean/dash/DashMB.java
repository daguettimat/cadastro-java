package br.com.global5.manager.bean.dash;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.poi.hssf.usermodel.HSSFAutoFilter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.context.RequestContext;

import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.areacliente.CadastroCliente;
import br.com.global5.manager.model.geral.DashBase1;
import br.com.global5.manager.model.geral.DashBase2;
import br.com.global5.manager.model.geral.DashBase3;
import br.com.global5.manager.model.geral.DashBase4;
import br.com.global5.manager.service.analise.CadastroService;

@Named
@ViewAccessScoped
public class DashMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 202202281151L;
	
	// Dados da Pesquisa
	private Date dtInicioPesquisa = null;
	private Date dtFinalPesquisa = null;
	private int qtdRegistrosDash = 0;
	private String filtroCadastro = "";
	String pDtInicialR = "";
	String pDtFinaR = "";
	
	private List<DashBase1> lstRelDashBase1;
	private List<DashBase2> lstRelDashBase2;
	private List<DashBase3> lstRelDashBase3;
	private List<DashBase4> lstRelDashBase4;
	
	// Variaveis Excel
	private ExcelOptions excelOpt;
	
	// Variaveis para Dlg
	private Map<String, Object> options;

	@Inject
    private CadastroService cadService;

	
	@PostConstruct
	public void init() {
		clear();
		
		options = new HashMap<String, Object>();
		options.put("draggable", true);
		options.put("modal", true);
		options.put("contentWidth", "96%");
		options.put("contentheight", "30%");
		options.put("resizable", false);			

	}
	
	public void clear() {
		filtroCadastro = "base1";
	}
	
	public void verTipoFiltro(){
		if( filtroCadastro == null ){
			
		} else 
			if ( filtroCadastro.equals("base1")){
				verConsumoCadastro();
			} else 
				if ( filtroCadastro.equals("base2")){
					verBase2();
				} else 
					if (filtroCadastro.equals("base3")) {
						verConsumoCadastroBase3();
					} else 
						if (filtroCadastro.equals("base4")) {
							verBase4();
						}
		
			
	}
	
	public void verBase2(){
		String pDtInicial = "";
		String pDtFinal = "";
	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
		
		if(this.getDtInicioPesquisa() != null){
			pDtInicial = formatter.format(this.getDtInicioPesquisa());
			pDtInicialR = fmt.format(this.getDtInicioPesquisa());
		} else {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção","Faltou a data de inicio!") );
			return;
		}
		
		if(this.getDtFinalPesquisa() != null){
			pDtFinal = formatter.format(this.getDtFinalPesquisa());
			pDtFinaR = fmt.format(this.getDtFinalPesquisa());
		} else {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção","Faltou a data final!") );
			return;
		}

		EntityManager em; 
		
		em = cadService.crud().getEntityManager();

		String query = "SELECT *   FROM (SELECT DISTINCT ON (analise_cadastral.anacoid) anacoid as num_ficha,   "
				+" (select  pes_documento1 from java.pessoa where pesoid = t.area_pesoid_responsavel) as cnpj, "                                        
				+" t.area_nome as nome_cliente, "
				+" date(analise_cadastral.anac_dt_termino) as finalizado, "
				+"  vei_placa as placa_Veiculo, "
				+"   CASE "
				+"       when (motorista.mot_documento1 is not null and vei_placa is not null and anac_usuoid_termino <> 8368) then 'Motorista e Veículo' "
				+"       when (motorista.mot_documento1 is not null and vei_placa is null and anac_usuoid_termino <> 8368) then 'Motorista' "
				+"       when (motorista.mot_documento1 is null and vei_placa is not null and anac_usuoid_termino <> 8368) then 'Veículo' "
				+"       when (motorista.mot_documento1 is not null and vei_placa is not null and anac_usuoid_termino = 8368) then 'Abandono Cliente' "
				+"       when (motorista.mot_documento1 is not null and vei_placa is null and anac_usuoid_termino = 8368) then 'Abandono Cliente' "
				+"       when (motorista.mot_documento1 is null and vei_placa is not null and anac_usuoid_termino = 8368) then 'Abandono Cliente' "                                          
				+"        else 'VERIFICAR' "
				+"           end as Status "
				+"				FROM     enum_ficha_status     "
				+"			        INNER JOIN analise_cadastral ON (enumoid= anac_status "
				+"						AND (anac_dt_termino > '"+ pDtInicial +" 00:00'         "                        
				+"						AND  anac_dt_termino < '"+ pDtFinal   +" 23:59'))       "
				+"					INNER JOIN area T ON (anac_cliente_areaoid=T.areaoid  "                    
				+"						AND (anac_dt_termino > '"+ pDtInicial +" 00:00'         "                        
				+"						AND  anac_dt_termino < '"+ pDtFinal   +" 23:59'))       "
				+"						INNER JOIN area U ON (anac_filial_areaoid = U.areaoid)   "
				+"						LEFT  JOIN analise_cadastral_motorista ON (analise_cadastral.anacoid = analise_cadastral_motorista.anacm_anacoid)     "
				+"						LEFT  JOIN motorista ON (analise_cadastral_motorista.anacm_motoid = motorista.motoid)     "
				+"						LEFT  JOIN analise_cadastral_veiculo ON (analise_cadastral.anacoid = anacv_anacoid)       "
				+"                      LEFT  JOIN proprietario ON (propoid = analise_cadastral_veiculo.anacv_propoid)      "
				+"						LEFT  JOIN veiculo ON anacv_veioid = veioid     "
				+"						LEFT  JOIN enum_referencias_avaliacao enumRefPes "
				+"						ON (enumRefPes.enumoid = analise_cadastral_motorista.anacm_ref_pessoal)     "
				+"						    LEFT  JOIN enum_referencias_avaliacao enumRefCom "
				+"						ON (enumRefCom.enumoid = analise_cadastral_motorista.anacm_ref_comercial)     "
				+"							LEFT  JOIN enum_referencias_avaliacao enumRefCri "
				+"						ON (enumRefCri.enumoid = analise_cadastral_motorista.anacm_criminal)  "
				+"						    WHERE  "
				+"							    anac_status in (9) and	"
				+"							    anac_dt_termino > '"+ pDtInicial +" 00:00' "
				+"								AND  0 = 0) AS query   ORDER  BY  num_ficha ASC ";
		
		lstRelDashBase2 = em.createNativeQuery(query, "ListaDashBase2Mapping").getResultList();
		int result = lstRelDashBase2.size();
		
		if(result == 0){		
			lstRelDashBase2 = null;			
		} else {
			qtdRegistrosDash = result;
		}
		
		openDlgDashDataCadastroBase2();
	}
	
	
	public void verConsumoCadastro(){
		
		String pDtInicial = "";
		String pDtFinal = "";
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
		
		if(this.getDtInicioPesquisa() != null){
			pDtInicial = formatter.format(this.getDtInicioPesquisa());
			pDtInicialR = fmt.format(this.getDtInicioPesquisa());
		} else {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção","Faltou a data de inicio!") );
			return;
		}
		
		if(this.getDtFinalPesquisa() != null){
			pDtFinal = formatter.format(this.getDtFinalPesquisa());
			pDtFinaR = fmt.format(this.getDtFinalPesquisa());
		} else {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção","Faltou a data final!") );
			return;
		}
		
		
		EntityManager em; 
				
		em = cadService.crud().getEntityManager();

		String query = "SELECT *   FROM (SELECT DISTINCT ON (analise_cadastral.anacoid) anacoid as num_ficha,   "
                      +"(select  pes_documento1 from java.pessoa where pesoid = t.area_pesoid_responsavel) as cnpj , "                                        
					  +"t.area_nome as nome_cliente, "
					  +"date(analise_cadastral.anac_dt_termino) as finalizado, "
                      +"(select enum_nome from enum_ficha_status where  enumoid = analise_cadastral.anac_status) as status "
					  +" FROM     enum_ficha_status "     
					    + " INNER JOIN analise_cadastral ON (enumoid= anac_status "                                 
															 + " AND (anac_dt_termino > '"+ pDtInicial +" 00:00' "                                 
															 + " AND  anac_dt_termino < '"+ pDtFinal   +" 23:59')) "     
						+ " INNER JOIN area T ON (anac_cliente_areaoid=T.areaoid "                      
															 + " AND (anac_dt_termino > '" + pDtInicial +" 00:00' "                                 
															 + " AND  anac_dt_termino < '" + pDtFinal   +" 23:59')) "     
						+ " INNER JOIN area U ON (anac_filial_areaoid = U.areaoid) "     
						+ " LEFT  JOIN analise_cadastral_motorista ON (analise_cadastral.anacoid = analise_cadastral_motorista.anacm_anacoid) "     
						+ " LEFT  JOIN motorista ON (analise_cadastral_motorista.anacm_motoid = motorista.motoid) "     
						+ " LEFT  JOIN analise_cadastral_veiculo ON (analise_cadastral.anacoid = anacv_anacoid) "     
						+ " LEFT  JOIN veiculo ON anacv_veioid = veioid "     
						+ " LEFT  JOIN enum_referencias_avaliacao enumRefPes " 
						+ " ON (enumRefPes.enumoid = analise_cadastral_motorista.anacm_ref_pessoal) "     
						+ " LEFT  JOIN enum_referencias_avaliacao enumRefCom " 
						+ " ON (enumRefCom.enumoid = analise_cadastral_motorista.anacm_ref_comercial) "     
						+ " LEFT  JOIN enum_referencias_avaliacao enumRefCri " 
						+ " ON (enumRefCri.enumoid = analise_cadastral_motorista.anacm_criminal) "  
						+ " WHERE anac_status in (8,9) and "																			
						+ " anac_dt_termino > '"+ pDtInicial + " 00:00' AND " 
						+ " 0 = 0) AS query   ORDER  BY  num_ficha ASC";
		
		
		lstRelDashBase1 = em.createNativeQuery(query, "ListaDashBase1Mapping").getResultList();
		int result = lstRelDashBase1.size();
		
		if(result == 0){		
			lstRelDashBase1 = null;			
		} else {
			qtdRegistrosDash = result;
		}
		
		openDlgDashDataCadastro();
				
	}
	
	public void customizacaoExcelCadastro(Object document){
		excelOpt = new ExcelOptions();
		
		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow header = sheet.getRow(0);		
		//HSSFAutoFilter filter = sheet.setAutoFilter(CellRangeAddress.valueOf("A1:E1"));
		
		
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.AUTOMATIC.getIndex());
		//cellStyle.setFillPattern(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND.BIG_SPOTS);
		
		for(int i=0; i < header.getPhysicalNumberOfCells(); i++){
			HSSFCell cell = header.getCell(i);
			cell.setCellStyle(cellStyle);
		}
		
		//HSSFAutoFilter filter = sheet.setAutoFilter(null);
		
	}
	

public void verConsumoCadastroBase3(){
		
		String pDtInicial = "";
		String pDtFinal = "";
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
		
		if(this.getDtInicioPesquisa() != null){
			pDtInicial = formatter.format(this.getDtInicioPesquisa());
			pDtInicialR = fmt.format(this.getDtInicioPesquisa());
		} else {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção","Faltou a data de inicio!") );
			return;
		}
		
		if(this.getDtFinalPesquisa() != null){
			pDtFinal = formatter.format(this.getDtFinalPesquisa());
			pDtFinaR = fmt.format(this.getDtFinalPesquisa());
		} else {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção","Faltou a data final!") );
			return;
		}
		
		
		EntityManager em; 
				
		em = cadService.crud().getEntityManager();

		String query = "SELECT *   FROM (SELECT DISTINCT ON (analise_cadastral.anacoid) anacoid as num_ficha,   "
                      +"(select  pes_documento1 from java.pessoa where pesoid = t.area_pesoid_responsavel) as cnpj , "                                        
					  +"t.area_nome as nome_cliente, "
					  +"date(analise_cadastral.anac_dt_termino) as finalizado, "
					  + "mot_nome as motorista, vei_placa as placa, prop_nome as proprietario,  "
                      +"(select enum_nome from enum_ficha_status where  enumoid = analise_cadastral.anac_status) as status "
					  +" FROM     enum_ficha_status "     
					    + " INNER JOIN analise_cadastral ON (enumoid= anac_status "                                 
															 + " AND (anac_dt_termino > '"+ pDtInicial +" 00:00' "                                 
															 + " AND  anac_dt_termino < '"+ pDtFinal   +" 23:59')) "     
						+ " INNER JOIN area T ON (anac_cliente_areaoid=T.areaoid "                      
															 + " AND (anac_dt_termino > '" + pDtInicial +" 00:00' "                                 
															 + " AND  anac_dt_termino < '" + pDtFinal   +" 23:59')) "     
						+ " INNER JOIN area U ON (anac_filial_areaoid = U.areaoid) "     
						+ " LEFT  JOIN analise_cadastral_motorista ON (analise_cadastral.anacoid = analise_cadastral_motorista.anacm_anacoid) "     
						+ " LEFT  JOIN motorista ON (analise_cadastral_motorista.anacm_motoid = motorista.motoid) "     
						+ " LEFT  JOIN analise_cadastral_veiculo ON (analise_cadastral.anacoid = anacv_anacoid) "     
						+ " LEFT  JOIN proprietario ON (propoid = analise_cadastral_veiculo.anacv_propoid) "
						+ " LEFT  JOIN veiculo ON anacv_veioid = veioid "     
						+ " LEFT  JOIN enum_referencias_avaliacao enumRefPes " 
						+ " ON (enumRefPes.enumoid = analise_cadastral_motorista.anacm_ref_pessoal) "     
						+ " LEFT  JOIN enum_referencias_avaliacao enumRefCom " 
						+ " ON (enumRefCom.enumoid = analise_cadastral_motorista.anacm_ref_comercial) "     
						+ " LEFT  JOIN enum_referencias_avaliacao enumRefCri " 
						+ " ON (enumRefCri.enumoid = analise_cadastral_motorista.anacm_criminal) "  
						+ " WHERE anac_status in (8,9) and "																			
						+ " anac_dt_termino > '"+ pDtInicial + " 00:00' AND " 
						+ " 0 = 0) AS query   ORDER  BY  num_ficha ASC";
		
		
		lstRelDashBase3 = em.createNativeQuery(query, "ListaDashBase3Mapping").getResultList();
		int result = lstRelDashBase3.size();
		
		if(result == 0){		
			lstRelDashBase3 = null;			
		} else {
			qtdRegistrosDash = result;
		}
		
		openDlgDashDataCadastroBase3();
				
	}
	
	public void verBase4(){
		String pDtInicial = "";
		String pDtFinal = "";
	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
		
		if(this.getDtInicioPesquisa() != null){
			pDtInicial = formatter.format(this.getDtInicioPesquisa());
			pDtInicialR = fmt.format(this.getDtInicioPesquisa());
		} else {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção","Faltou a data de inicio!") );
			return;
		}
		
		if(this.getDtFinalPesquisa() != null){
			pDtFinal = formatter.format(this.getDtFinalPesquisa());
			pDtFinaR = fmt.format(this.getDtFinalPesquisa());
		} else {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção","Faltou a data final!") );
			return;
		}
	
		EntityManager em; 
		
		em = cadService.crud().getEntityManager();
	
		String query = "SELECT *   FROM (SELECT DISTINCT ON (analise_cadastral.anacoid) anacoid as num_ficha,   "
				+" (select  pes_documento1 from java.pessoa where pesoid = t.area_pesoid_responsavel) as cnpj, "                                        
				+" t.area_nome as nome_cliente, "
				+" date(analise_cadastral.anac_dt_termino) as finalizado, "
				+"  vei_placa as placa_Veiculo, "
				+"  mot_nome as motorista,prop_nome as proprietario, "
				+"   CASE "
				+"       when (motorista.mot_documento1 is not null and vei_placa is not null and anac_usuoid_termino <> 8368) then 'Motorista e Veículo' "
				+"       when (motorista.mot_documento1 is not null and vei_placa is null and anac_usuoid_termino <> 8368) then 'Motorista' "
				+"       when (motorista.mot_documento1 is null and vei_placa is not null and anac_usuoid_termino <> 8368) then 'Veículo' "
				+"       when (motorista.mot_documento1 is not null and vei_placa is not null and anac_usuoid_termino = 8368) then 'Abandono Cliente' "
				+"       when (motorista.mot_documento1 is not null and vei_placa is null and anac_usuoid_termino = 8368) then 'Abandono Cliente' "
				+"       when (motorista.mot_documento1 is null and vei_placa is not null and anac_usuoid_termino = 8368) then 'Abandono Cliente' "                                          
				+"        else 'VERIFICAR' "
				+"           end as Status "
				+"				FROM     enum_ficha_status     "
				+"			        INNER JOIN analise_cadastral ON (enumoid= anac_status "
				+"						AND (anac_dt_termino > '"+ pDtInicial +" 00:00'         "                        
				+"						AND  anac_dt_termino < '"+ pDtFinal   +" 23:59'))       "
				+"					INNER JOIN area T ON (anac_cliente_areaoid=T.areaoid  "                    
				+"						AND (anac_dt_termino > '"+ pDtInicial +" 00:00'         "                        
				+"						AND  anac_dt_termino < '"+ pDtFinal   +" 23:59'))       "
				+"						INNER JOIN area U ON (anac_filial_areaoid = U.areaoid)   "
				+"						LEFT  JOIN analise_cadastral_motorista ON (analise_cadastral.anacoid = analise_cadastral_motorista.anacm_anacoid)     "
				+"						LEFT  JOIN motorista ON (analise_cadastral_motorista.anacm_motoid = motorista.motoid)     "
				+"						LEFT  JOIN analise_cadastral_veiculo ON (analise_cadastral.anacoid = anacv_anacoid)       "
				+"                      LEFT  JOIN proprietario ON (propoid = analise_cadastral_veiculo.anacv_propoid)      "
				+"						LEFT  JOIN veiculo ON anacv_veioid = veioid     "
				+"						LEFT  JOIN enum_referencias_avaliacao enumRefPes "
				+"						ON (enumRefPes.enumoid = analise_cadastral_motorista.anacm_ref_pessoal)     "
				+"						    LEFT  JOIN enum_referencias_avaliacao enumRefCom "
				+"						ON (enumRefCom.enumoid = analise_cadastral_motorista.anacm_ref_comercial)     "
				+"							LEFT  JOIN enum_referencias_avaliacao enumRefCri "
				+"						ON (enumRefCri.enumoid = analise_cadastral_motorista.anacm_criminal)  "
				+"						    WHERE  "
				+"							    anac_status in (9) and	"
				+"							    anac_dt_termino > '"+ pDtInicial +" 00:00' "
				+"								AND  0 = 0) AS query   ORDER  BY  num_ficha ASC ";
		
		lstRelDashBase4 = em.createNativeQuery(query, "ListaDashBase4Mapping").getResultList();
		int result = lstRelDashBase4.size();
		
		if(result == 0){		
			lstRelDashBase4 = null;			
		} else {
			qtdRegistrosDash = result;
		}
		
		openDlgDashDataCadastroBase4();
	}

	


	public void sairTelaDash(String nomeTela) {
		RequestContext.getCurrentInstance().closeDialog(nomeTela);	
	}
	
	public void openDlgDashDataCadastro() {
		options.put("width", "96%");
		options.put("height", "80%");

		RequestContext.getCurrentInstance().openDialog("dlg_dash_data_cadastro", options, null);
		RequestContext.getCurrentInstance().execute("onTop('dlg_dash_data_cadastro')");
	}

	public void openDlgDashDataCadastroBase2() {
		options.put("width", "96%");
		options.put("height", "80%");

		RequestContext.getCurrentInstance().openDialog("dlg_dash_data_cadastro_base2", options, null);
		RequestContext.getCurrentInstance().execute("onTop('dlg_dash_data_cadastro_base2')");
	}
	
	public void openDlgDashDataCadastroBase3() {
		options.put("width", "96%");
		options.put("height", "80%");

		RequestContext.getCurrentInstance().openDialog("dlg_dash_data_cadastro_base3", options, null);
		RequestContext.getCurrentInstance().execute("onTop('dlg_dash_data_cadastro_base3')");
	}

	public void openDlgDashDataCadastroBase4() {
		options.put("width", "96%");
		options.put("height", "80%");

		RequestContext.getCurrentInstance().openDialog("dlg_dash_data_cadastro_base4", options, null);
		RequestContext.getCurrentInstance().execute("onTop('dlg_dash_data_cadastro_base4')");
	}
	
	public void calcDataMesAnterior(){
		// Data atual do servidor - base para geração das datas
		Calendar now = Calendar.getInstance();
		
		// Variaveis do método
		int diaAtual = now.get(Calendar.DAY_OF_MONTH);
		// Mês
		int mesAtual = now.get(Calendar.MONTH); // Mes = 0 é correspondente
															// Janeiro e 11 é Dezembro
		int mesAntes = 0;

		// Ano
		int anoAtual = now.get(Calendar.YEAR);
		int anoAntes = anoAtual;
		
		// Se mês atual é Janeiro (0) altera o valor para 1 (pois é necessário para utilizar na formatação da data)
		if (mesAtual == 1) {
			mesAtual = 1; // Janeiro
			mesAntes = 12; // Dezembro
			anoAntes = anoAtual - 1;
		} else {
			// Se mês for diferente de Janeiro
			//mesAtual = mesAtual + 1; // Formata por adição o mês atual
			mesAntes = mesAtual  - 1; // Formata por subtração o mês anterior
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		GregorianCalendar dtFimInit = new GregorianCalendar();
		GregorianCalendar dtIniInit = new GregorianCalendar();
		
		
	}
	
	public void retornaHome(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../../index.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void calDataFrente(){
		
	}
	
	public Date getDtInicioPesquisa() {
		return dtInicioPesquisa;
	}

	public void setDtInicioPesquisa(Date dtInicioPesquisa) {
		this.dtInicioPesquisa = dtInicioPesquisa;
	}

	public Date getDtFinalPesquisa() {
		return dtFinalPesquisa;
	}

	public void setDtFinalPesquisa(Date dtFinalPesquisa) {
		this.dtFinalPesquisa = dtFinalPesquisa;
	}

	public List<DashBase1> getLstRelDashBase1() {
		return lstRelDashBase1;
	}

	public void setLstRelDashBase1(List<DashBase1> lstRelDashBase1) {
		this.lstRelDashBase1 = lstRelDashBase1;
	}

	public int getQtdRegistrosDash() {
		return qtdRegistrosDash;
	}

	public void setQtdRegistrosDash(int qtdRegistrosDash) {
		this.qtdRegistrosDash = qtdRegistrosDash;
	}

	public String getFiltroCadastro() {
		return filtroCadastro;
	}

	public void setFiltroCadastro(String filtroCadastro) {
		this.filtroCadastro = filtroCadastro;
	}

	public List<DashBase2> getLstRelDashBase2() {
		return lstRelDashBase2;
	}

	public void setLstRelDashBase2(List<DashBase2> lstRelDashBase2) {
		this.lstRelDashBase2 = lstRelDashBase2;
	}

	public String getpDtInicialR() {
		return pDtInicialR;
	}

	public void setpDtInicialR(String pDtInicialR) {
		this.pDtInicialR = pDtInicialR;
	}

	public String getpDtFinaR() {
		return pDtFinaR;
	}

	public void setpDtFinaR(String pDtFinaR) {
		this.pDtFinaR = pDtFinaR;
	}

	public List<DashBase3> getLstRelDashBase3() {
		return lstRelDashBase3;
	}

	public void setLstRelDashBase3(List<DashBase3> lstRelDashBase3) {
		this.lstRelDashBase3 = lstRelDashBase3;
	}

	public List<DashBase4> getLstRelDashBase4() {
		return lstRelDashBase4;
	}

	public void setLstRelDashBase4(List<DashBase4> lstRelDashBase4) {
		this.lstRelDashBase4 = lstRelDashBase4;
	}

}
