package br.com.global5.manager.bean.geral;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.swing.text.MaskFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.OperacoesArquivos;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.areacliente.DetalheFixos;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.contrato.Contrato;
import br.com.global5.manager.model.contrato.produtos.ClienteIsca;
import br.com.global5.manager.model.contrato.produtos.ClienteVeiculo;
import br.com.global5.manager.model.enums.StatusContrato;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.manager.service.cadastro.ContratoService;
import br.com.global5.manager.service.geral.ClienteVeiculoService;
import br.com.global5.template.exception.BusinessException;


@Named 
@ViewAccessScoped
public class ClienteVeiculoMB implements Serializable   {

	private static final long serialVersionUID = 202007221058L;
	
	private ClienteVeiculo cliVei;
	private ClienteIsca    cliIsca;
	
	private Contrato ctr;
	
	private Integer id;
	private Integer idPlaca;
	private Integer idIsca;
	
	private EntityManager em;
	
	private String tituloDlgVeiculoIsca = "";
	private Integer idArea = null;
	private Integer qtdPlacas = null;
	private Integer qtdIscas = null;
	private String nomeCliente = "";
	private String placaOuIsca = "";
	private String nomeTelaUsuario = "";
	private String subNomeTelaUsuario = "";
	private String subCampoPlace = "";
	private Integer MaxLengthPlacaOuIsca = null;
	
	private List<String> lstPlacasOuIscas;
	private JSONArray arrayJsonPlacas;
	
	private List<ClienteVeiculo> lstClienteVeiculo;
	private List<ClienteVeiculo> lstClienteVeiculoSelect;
	
	private List<ClienteIsca> lstClienteIsca;
	
	private boolean mostrarDadosPlaca = false;
	private boolean mostrarDadosIsca = false;
	
	// Variaveis teste Excel
	private ExcelOptions excelOpt;	
	private DetalheFixos detFixos;	
	ArrayList<DetalheFixos> lstDetalheFixos = new ArrayList<DetalheFixos>();

	
	// Variaveis para area cliente
	private String areaNome = "";
	private String areaNivelNome = "";
	private String areaCnpj = "";
	private String idContratoRastreamento = "";
	private String placaPadraoAlteradaMercosul = "";
	boolean abrirTransfPlacaMercosul = true;
	private boolean mostraEdicao = false;
	private String previousPage = null;
	private boolean abertoMonitoramento = false;
	
	@Inject
	ClienteVeiculoService clienteVeiculoService;
	
	//@Inject
	//ClienteIscaService clienteIscaService;
	@Inject
	ContratoService contratoService;
	
	@Inject
	AreaService areaService;
	
	@PostConstruct
	public void init() {
		clear();
		
	}

	private void clear() {
		
		ctr = new Contrato();
		ctr.setStatusContrato(new StatusContrato());		
		
		cliVei = new ClienteVeiculo();
		cliVei.setArea(new Area());
		cliVei.setUsuCriacao(new Usuario());
//		cliVei.setUsuExclusao(new Usuario());
		
		cliIsca = new ClienteIsca();
		cliIsca.setArea(new Area());
		cliIsca.setUsuCriacao(new Usuario());
//		cliIsca.setUsuExclusao(new Usuario());
		
		id = null;
		idArea = null;
		
		lstPlacasOuIscas = new ArrayList<String>();
		arrayJsonPlacas = new JSONArray();
		ctrMonitoramento();
		//lstClienteVeiculo = clienteVeiculoService.crud().listAll();
				
	}
	
	public void openDlgVeiculoIscas(Integer idTipo, Integer areaId, String areaNome) {

		this.setNomeCliente(areaNome);
		this.setIdArea(areaId);
		
		// 1 = Veiculo Fixos
		if(idTipo.equals(1)) {
			this.setTituloDlgVeiculoIsca("Veículos fixos");			
		}
		
		// 2 = Iscas Fixas
		if(idTipo.equals(2)){
			this.setTituloDlgVeiculoIsca("Iscas valor fixo");
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		// Atualiza dlgPesquisa
		context.update("frmCadastroCnpj:frmCadastroArea:dlgVeiculoIscas");
		
	}
	
	/**
	 * @author Francis Bueno
	 * @param idTipo
	 * @param placaOuIsca
	 * Método que coleta a placa digitada pelo usuario no campo inputPlacaOuIsca
	 */
	public void coletaPlacaOuIsca(Integer idTipo, String placaOuIsca){
		
		if( idTipo.equals(1)){
			// Coleta Placas
			if(!placaOuIsca.equals("")){
				// Guarda o codigo da placa no campo placaOuIsca
				this.setPlacaOuIsca(placaOuIsca);	
			}
			
		}
		
		if( idTipo.equals(2)){
			// Coleta Iscas
			if(!placaOuIsca.equals("")){
				this.setPlacaOuIsca(placaOuIsca);				
			}
		}
		
	}
	
	/**
	 * @author Francis Bueno
	 * @param idTipo
	 * 
	 */
	public void adicionaPlacaOuIsca(Integer idTipo){
		if(idTipo == 1) {			
			
			String test = this.cliVei.getPlacaVeiculo().toString();

			// 
			if(this.cliVei.getPlacaVeiculo().length()>5){
				
				// Caso encontre não encontre as 3 primeiras letra da placa que está entre A e Z
			    String caracterPosicao0a3 = this.cliVei.getPlacaVeiculo().substring(0,3); // this.getPlacaOuIsca().substring(0,3);
			    
			    
				if( !caracterPosicao0a3.matches("[A-Z]*")){
					FacesContext.getCurrentInstance().addMessage(null, 
							new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Placa precisa iniciar com 3 letras!"));
					return;
				}
				
				
				if(this.cliVei.getPlacaVeiculo().length() == 8 ){
					String caracterPosicaoPadrao = this.cliVei.getPlacaVeiculo().substring(4, 8);
					
					if(caracterPosicaoPadrao.replaceAll("[^0123456789]", "").length() != 4){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Placa precisa iniciar ter 4 números!"));
						return;
					}
				} else if (this.cliVei.getPlacaVeiculo().length() == 7){
					String caracterPosicaoMercosul1 = this.cliVei.getPlacaVeiculo().substring(3, 4);
					String caracterPosicaoMercosul2 = this.cliVei.getPlacaVeiculo().substring(4, 5);
					String caracterPosicaoMercosul34 = this.cliVei.getPlacaVeiculo().substring(5, 7);

					if(!caracterPosicaoMercosul1.matches("[1-9]*")){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Placa precisa iniciar ter 4 números!"));
						return;
					}
					if(!caracterPosicaoMercosul2.matches("[A-J]*")){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Placa precisa ter na 2ª casa o caracter de A - J !"));
						return;							
					}
					String caracterPosicaoPadraoMerc = this.cliVei.getPlacaVeiculo().substring(5,7);
//					if(!caracterPosicaoMercosul34.matches("[1-9]*")){
					if(caracterPosicaoPadraoMerc.replaceAll("[^0123456789]", "").length() != 2){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Placa precisa iniciar ter 2 números após a letra na 2a casa!"));
						return;
					}				
				}
				
				// Continua processo de validação da placa
				
				String resultadoFormatoPlaca  = validaFormatoPlaca();
				
				// Salva a placa
				if(idTipo == 1 && resultadoFormatoPlaca.equals("valido")){
					
					// Se a placa pesquisada tipo normal ou mercosul se já existe no campo placaVeiculo
					Criteria criPesqVeic = clienteVeiculoService.crud().getSession().createCriteria(ClienteVeiculo.class);					
					criPesqVeic.add(Restrictions.ilike("placaVeiculo", this.cliVei.getPlacaVeiculo().toUpperCase())); //this.getPlacaOuIsca().toUpperCase()					
					criPesqVeic.add(Restrictions.eq("area.id", this.getIdArea()));
					criPesqVeic.add(Restrictions.isNull("usuExclusao"));
					
					int result = criPesqVeic.list().size();
					
					// Se a placa pesquisada tipo padrão já foi registrada no campo placaAnteriorMercosul
					Criteria criPesqVeicM = clienteVeiculoService.crud().getSession().createCriteria(ClienteVeiculo.class);
					criPesqVeicM.add(Restrictions.eq("placaAnteriorMercosul", this.cliVei.getPlacaVeiculo().toUpperCase())); // this.getPlacaOuIsca().toUpperCase()
					criPesqVeicM.add(Restrictions.eq("area.id", this.getIdArea()));
					criPesqVeicM.add(Restrictions.isNull("usuExclusao"));
					
					int resultM = criPesqVeicM.list().size();
					
					// Se a placa pesquisada tipo Mercosul se ela já existe no campo placaVeiculo com o codigo padrao
					Criteria criPesqVeicP = clienteVeiculoService.crud().getSession().createCriteria(ClienteVeiculo.class);
					
					String revertPlacaMercosul = solicitarTransformacaoPlacaVeiculoMercosulParaPadrao(this.cliVei.getPlacaVeiculo().toUpperCase()); 
					//this.getPlacaOuIsca().toUpperCase()
					String revertPlacaPadrao = solicitarTransformacaoPlacaPadraoParaVeiculoMercosul(this.cliVei.getPlacaVeiculo().toUpperCase());
					//this.getPlacaOuIsca().toUpperCase()
					
					int resultP = 0;
					int resultPM = 0;
					
					if (revertPlacaMercosul != "" && revertPlacaMercosul != null) {
						// Pesquisa placa mercosul convertida para Padrao	
						criPesqVeicP.add(Restrictions.ilike("placaVeiculo", revertPlacaMercosul));
						criPesqVeicP.add(Restrictions.eq("area.id", this.getIdArea()));
						criPesqVeicP.add(Restrictions.isNull("usuExclusao"));
						
						 resultP = criPesqVeicP.list().size();
						
					}

					if (revertPlacaPadrao != "" && revertPlacaPadrao != null) {
						// Pesquisa placa padrão convertida em mercosul
						criPesqVeicP.add(Restrictions.ilike("placaVeiculo", revertPlacaPadrao));
						criPesqVeicP.add(Restrictions.eq("area.id", this.getIdArea()));
						criPesqVeicP.add(Restrictions.isNull("usuExclusao"));
						
						 resultPM = criPesqVeicP.list().size();
						
					}			
					
					if( result == 0 && resultM == 0 && resultP == 0 && resultPM == 0) {
						// Adiciona Placas
						
						if(idArea != null){
						ClienteVeiculo cliVeiculo = new ClienteVeiculo();
					
							String dtInicioString = "";
							Date dtInicioVigencia = null;
							
							Calendar calendar = Calendar.getInstance();
							int year = calendar.get(Calendar.YEAR);
							int month = calendar.get(Calendar.MONTH);
							int day = calendar.get(Calendar.DAY_OF_MONTH);
							
							dtInicioString = year + "-" + ( month + 1) + "-" +day;							
							
							try {
								dtInicioVigencia = new SimpleDateFormat("yyyy-MM-dd").parse(dtInicioString);
								
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						if(!this.cliVei.getPlacaVeiculo().equals("")){
							//placaOuIsca.equals("")
							cliVeiculo.setArea(new Area(this.getIdArea()));
							cliVeiculo.setPlacaVeiculo(cliVei.getPlacaVeiculo().toUpperCase());
							cliVeiculo.setDtCriacao(new Date());
							cliVeiculo.setUsuCriacao(checkUsuario.valid());
							cliVeiculo.setDtCriacao(new Date());
							cliVeiculo.setDtInicioVigencia(dtInicioVigencia);			
							
							clienteVeiculoService.crud().saveOrUpdate(cliVeiculo);
														
							String placa = this.cliVei.getPlacaVeiculo(); //this.getPlacaOuIsca();
							this.setPlacaOuIsca("");
							
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
									"A placa informada: " + placa + " foi adicionada com sucesso!" ));
							
							cliVei = new ClienteVeiculo();

						}
		
						}
		
						this.getLstClienteVeiculo();
						// Atualizar página	
						RequestContext context = RequestContext.getCurrentInstance();
//						context.update("frmCadastroCnpj:frmCadastroArea:inputPlacaOuIsca");
						context.update("frmCadastroCnpj:formTable:tablePesqCli");	
						context.update("frmCadastroCnpj:inptPlaca");	
					} else 
						
						if(result > 0){
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
								"A placa que deseja cadastrar já está ativa no sistema!"));
							return;				
					} else 
						if (resultM > 0 ) {
							
							if (resultM == 1){
								
								ClienteVeiculo cliVeiculo = (ClienteVeiculo) criPesqVeicM.list().get(0);
								
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
										"A placa informada " + this.cliVei.getPlacaVeiculo().toUpperCase() + ", já está cadastrada no formato Mercosul, placa:  " + cliVeiculo.getPlacaVeiculo() ));
											// this.getPlacaOuIsca().toUpperCase()
									return;												
							} else {
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
										"Foram encontradas mais do que um registro para a placa informada " + this.cliVei.getPlacaVeiculo().toUpperCase() + ", por favor nos envie um print da tela!"));
										// this.getPlacaOuIsca().toUpperCase()
									return;												
							}

						}
						else 
							if (resultP > 0){
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
										"Foram encontradas registro para a placa informada " + 
												this.cliVei.getPlacaVeiculo().toUpperCase()
												 + ", \n a placa está cadastrada no formato antigo: " + revertPlacaMercosul));
											// this.getPlacaOuIsca().toUpperCase()
									return;																				
							}
							else 
								if (resultPM > 0){
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
											"Foram encontradas registro para a placa informada " +  this.cliVei.getPlacaVeiculo().toUpperCase() 
											 + ", \n a placa está cadastrada no formato Mercosul: " + revertPlacaPadrao));
										// this.getPlacaOuIsca().toUpperCase()
										return;																				
								}							
					
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
							"Está informando um formato inválido de placa!"));
						return;				
				}
	
				
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
						"Está adicionando um formato inválido de placa, inferior a 6 caracteres!"));
					return;
			}
		}			
	
	}

	/**
	 * @author Francis Bueno
	 * Método que gera arquivo em excel para download
	 * --- Nota: ainda estou pensando na forma do download que não está concluida
	 */
	public void gerarExcelVeiculoFixoA(){
		
		// Criando o arquivo e uma planilha chamada "Product"
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Product");
		
		// Definindo alguns padroes de layout
		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeight((short)400);
		
        //Carregando os produtos
        List<ClienteVeiculo> products = clienteVeiculoService.crud().eq("area.id", getIdArea()).isNull("usuExclusao") .list();
        
        
        int rownum = 0;
        int cellnum = 0;
        Cell cell;
        Row row;

        
		
        //Configurando estilos de células (Cores, alinhamento, formatação, etc..)
        HSSFDataFormat numberFormat = workbook.createDataFormat();

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(CellStyle.ALIGN_CENTER);
        textStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        CellStyle numberStyle = workbook.createCellStyle();
        //numberStyle.setDataFormat(numberFormat.getFormat(“#,##0.00”));
        numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00"));
        numberStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);        

        // Configurando Header
        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Code");
        //setCellValue(“Code”);

        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Name");

        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Price");
        
        // Adicionando os dados dos produtos na planilha
        //DetalheFixos product : products
        
        for (int i = 0; i < products.size(); i++) {
        	
            row = sheet.createRow(rownum++);
            cellnum = 0;

            cell = row.createCell(cellnum++);
            cell.setCellStyle(textStyle);
            cell.setCellValue(products.get(i).getId());

            cell = row.createCell(cellnum++);
            cell.setCellStyle(textStyle);
            cell.setCellValue(products.get(i).getPlacaVeiculo());

        }

        String caminho = "/var/www/clientes/teste_";
		String caminhoDelete = "/var/www/clientes";
		String teste = "aaa";
        try {
			
        	//Escrevendo o arquivo em disco
        	FileOutputStream out = new FileOutputStream(new File(caminho +getIdArea()+ ".xls"));
        	workbook.write(out);
        	out.close();
        	workbook.close();
        	System.out.println("Sucesso!");

        	} catch (FileNotFoundException e) {
        		e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        
        	// Baixar arquivo
			AppUtils appUtil = new AppUtils();

			File directory = new File(caminhoDelete);
	
			if (directory.exists()) {
	
				OperacoesArquivos.downloadFile("zipado" + getIdArea()+".zip", caminho, "zip", FacesContext.getCurrentInstance());
	
				//appUtil.deleteBoleto1(caminhoDelete);
	
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção",
						"O Download já foi realizado! Para novo download realize uma nova Consulta!"));
			}

	}

	/**
	 * @author Francis Bueno
	 * @param document
	 * Exporta dados da tabela no formato excel e csv
	 */
	public void getCustomizacaoExcelVeiculoFixosBkp(Object document){
		
		excelOpt = new ExcelOptions();
		
		// Criando o arquivo e uma planilha chamada "Product"
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Product");
		
		// Definindo alguns padroes de layout
		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeight((short)400);
		
        //Carregando os produtos
        List<ClienteVeiculo> products = clienteVeiculoService.crud().eq("area.id", getIdArea()).isNull("usuExclusao") .list();
        
        
        int rownum = 0;
        int cellnum = 0;
        Cell cell;
        Row row;

        
		
        //Configurando estilos de células (Cores, alinhamento, formatação, etc..)
        HSSFDataFormat numberFormat = workbook.createDataFormat();

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(CellStyle.ALIGN_CENTER);
        textStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        CellStyle numberStyle = workbook.createCellStyle();
        //numberStyle.setDataFormat(numberFormat.getFormat(“#,##0.00”));
        numberStyle.setDataFormat(numberFormat.getFormat("#,##0.00"));
        numberStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);        

        // Configurando Header
        row = sheet.createRow(rownum++);
        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Code");
        //setCellValue(“Code”);

        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Name");

        cell = row.createCell(cellnum++);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Price");
        
        // Adicionando os dados dos produtos na planilha
        //DetalheFixos product : products
        
        for (int i = 0; i < products.size(); i++) {
        	
            row = sheet.createRow(rownum++);
            cellnum = 0;

            cell = row.createCell(cellnum++);
            cell.setCellStyle(textStyle);
            cell.setCellValue(products.get(i).getId());

            cell = row.createCell(cellnum++);
            cell.setCellStyle(textStyle);
            cell.setCellValue(products.get(i).getPlacaVeiculo());

        }
		
	}


	/**
	 * @author Francis Bueno
	 * @return
	 * Valida o FormatoPlaca, retorna erro ou valido para as consultas de placas do tipo padrao e mercosul
	 * 
	 */
	private String validaFormatoPlaca() {

    	Integer qtdCaracteresPlaca = null;
    	String  caracterPosicao = "";
    	String  caracterPosHifen = "";
    	
    	// Para placas do tipo padrão e Mercosul são maiores que 5 caracteres
    	if(this.cliVei.getPlacaVeiculo().length()>5){
    		//this.getPlacaOuIsca().length()>5
    		// Qtd de caracteres da placa
    		qtdCaracteresPlaca = this.cliVei.getPlacaVeiculo().length(); // this.getPlacaOuIsca().length();
    		
    		// Caso a qtd contada seja 7 ou 8 então continua validação caso contrario return semPlaca
    		if( qtdCaracteresPlaca == 7 || qtdCaracteresPlaca == 8  ){
    			
    			// Copia o caracter na posição 3 , 4
    			caracterPosicao = this.cliVei.getPlacaVeiculo().substring(3, 4); //this.getPlacaOuIsca().substring(3,4);
    			
    			// Caso a copia seja "-" , a placa é tipo padrao antigo (ABC-1234)
    			if( caracterPosicao.equals("-") ){
    				
    				// Copia caracteres apos o hifen
    				caracterPosHifen = this.cliVei.getPlacaVeiculo().substring(4, qtdCaracteresPlaca); 
    				//this.getPlacaOuIsca().substring(4,qtdCaracteresPlaca);    				
    				
    				// Checar os caracteres após o hifen
    				Boolean checkAlfPosHifen = checarStringAlfabeto(caracterPosHifen);
    				
    				// Caso retorne true, resposta erro
    				if( checkAlfPosHifen == true ) {    					
    					return "erro";
    				}  else {
    					// caso contrario resposta placa validada
    					return "valido";
    				}
    				
    			} else {
    				
    				// Para placas tipo Mercosul
    				if ( qtdCaracteresPlaca == 7) {    					

    					// Copia a posicao da placa na casa 4,5	
    					caracterPosicao = this.cliVei.getPlacaVeiculo().substring(4, 5); // this.getPlacaOuIsca().substring(4,5);
    					
    					// Caso encontre uma letra que está entre A e J
    					if( caracterPosicao.matches("[A-J]*")){
    						
    						// Existe Letra do padrão mercosul na 5 casa da placa então :    						        					        					
        					// localiza se o codigo tem hifem (caso exista não permite a inclusão)
    						// senão encontrar apresenta -1 senão retorna a posição;
        					int findHifen = this.cliVei.getPlacaVeiculo().indexOf("-"); //this.getPlacaOuIsca().indexOf("-");					
        					
        					if (findHifen == -1) {
        						// caso a placa não tenha o hifen
        						// Variaves para contagem de numeros e letras que consta na placa
        						List<Character> numbers = new ArrayList<>();
        						List<Character> letters = new ArrayList<>();
        						
        						// For que conta cada caracter para numeros e letras
        						for(char ch: this.cliVei.getPlacaVeiculo().toCharArray()) {
        							//this.getPlacaOuIsca().toCharArray()
        							if( Character.isDigit(ch)){
        								numbers.add(ch);
        							} else {
        								letters.add(ch);
        							}
        						}
        						
        						//Placa Nacional Convencional
        						if(letters.size() == 3 && numbers.size() == 4) {
        							return "erro";
        						}

        						//Placa Nacional padrão Mercosul
        						if(letters.size() == 4 && numbers.size() == 3) {
        							return "valido";
        						}
        						
        					} else {
        						
        						return "erro";	
        					} 
    						
    					} else {
    						
    						// NÃO Existe Letra do padrão mercosul na 5 casa da placa
    						return "erro";    						
    						
    					}   					
    					
    				} 
    				
    				if ( qtdCaracteresPlaca == 6 ) {
    					// não permite placa com 6 caracteres para tipo nacional
    					return "erro";
    				}
    				
    			}
    			
    		} else {
    			return "semPlaca";
    		}
    		
    	} else {
    		return "semPlaca";
    	}
    	return "erro";
    }

	/**
	 * @author Francis Bueno
	 * @param textoPlaca
	 * @return
	 * 
	 * retorna true para checar string do alfabeto ou false caso contrario
	 * 
	 */
    public Boolean checarStringAlfabeto(String textoPlaca){
        
    	for (int i = 0; i < textoPlaca.length(); i++) {
    	      if (Character.isAlphabetic(textoPlaca.charAt(i))) 
    	         return true;
    	   }
    	   return false;
    }
    
    public void btnBack() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../../../index.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"O index.xhtml não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}
	
	public void findVeiculosIscas(Integer idTipo, Integer areaId, String areaNome){
		clear();
		this.setNomeCliente(areaNome);
		this.setIdArea(areaId);
		
		// 1 = Veiculo Fixos
		if(idTipo.equals(1)) {
			this.setTituloDlgVeiculoIsca("Veículos fixos");			
			this.setNomeTelaUsuario("Contrato Rastreamento - Veículos Fixos");
			this.setSubNomeTelaUsuario("Cadastro de Veículos: ");
			this.setSubCampoPlace(" placa");
			this.setMostrarDadosPlaca(true);
			this.setMostrarDadosIsca(false);
			this.setMaxLengthPlacaOuIsca(8);
		}
		
		// 2 = Iscas Fixas
		if(idTipo.equals(2)){
			this.setTituloDlgVeiculoIsca("Iscas valor fixo");
			this.setNomeTelaUsuario("Contrato Rastreamento - Iscas Valor Fixo");
			this.setSubNomeTelaUsuario("Cadastro de Iscas: ");
			this.setSubCampoPlace(" isca");
			this.setMostrarDadosPlaca(false);
			this.setMostrarDadosIsca(true);
			this.setMaxLengthPlacaOuIsca(30);
		}

		try {
			//Carga tela de edição
			FacesContext.getCurrentInstance().getExternalContext().redirect("../parceiros/area_contrato_rastreamento_veiculos_iscas.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void btnVoltaContratoRastreio(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../parceiros/area_contrato_rastreamento_man.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Area " + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}
	
	public void deletarPlaca(){
				
		if(idPlaca <= 0){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Selecione as placas para serem apagadas!"));
		} else {
				
				ClienteVeiculo cliVeiculo = clienteVeiculoService.findById(idPlaca);				
				cliVeiculo.setDtExclusao(new Date());
				cliVeiculo.setUsuExclusao(checkUsuario.valid());				
				clienteVeiculoService.update(cliVeiculo);
				//getLstClienteVeiculo();
				RequestContext context = RequestContext.getCurrentInstance();			
				context.update("frmCadastroCnpj:formTable:tablePesqCli");
				
			}		
		
	}
	
	public void solicitarExclusaoPlacaVeiculo(Integer idPlacaVeiculo){
		
		findById(this.getIdPlaca());
				
	}
	
	public void solicitarTransformacaoPlacaVeiculo(Integer idPlacaVeiculo){
		
		findById(this.getIdPlaca());
		
		// Checar se placa já está no mercosul
			String checarPlaca =  cliVei.getPlacaVeiculo().substring(4,5);
		
		if( checarPlaca.matches("[A-J]*")){
			// Placa já está no formato Mercosul
			   
		   this.setAbrirTransfPlacaMercosul(false);
		   		
		} else {
			// Placa será convertida para o formato Mercosul
			this.setAbrirTransfPlacaMercosul(true);
			
			if (this.getIdPlaca() != null) {
				
				String charAlterar =  cliVei.getPlacaVeiculo().substring(5,6);
				
				if(charAlterar.equals("0")){
					charAlterar = "A";
				}
				if(charAlterar.equals("1")){
					charAlterar = "B";
				}
				if(charAlterar.equals("2")){
					charAlterar = "C";
				}
				if(charAlterar.equals("3")){
					charAlterar = "D";
				}
				if(charAlterar.equals("4")){
					charAlterar = "E";
				}
				if(charAlterar.equals("5")){
					charAlterar = "F";
				}
				if(charAlterar.equals("6")){
					charAlterar = "G";
				}
				if(charAlterar.equals("7")){
					charAlterar = "H";
				}
				if(charAlterar.equals("8")){
					charAlterar = "I";
				}			
				if(charAlterar.equals("9")){
					charAlterar = "J";
				}
				
				
				String p3Letras = cliVei.getPlacaVeiculo().substring(0,3);
				String s4Letra = cliVei.getPlacaVeiculo().substring(4,5);
				String t2Letra = ""; //cliVei.getPlacaVeiculo().substring(6,8);
				
				//String novaPlacaMercosul = p3Letras + s4Letra + charAlterar + t2Letra;
				
				String t5Letra = "";
				String t6Letra = ""; 
				
				String novaPlacaMercosul = "";
				
				int tamanhoPlaca = cliVei.getPlacaVeiculo().length();
									
				// Tamanho 7 qdo tipo Mercosul
				if(tamanhoPlaca == 7) {
					t5Letra = cliVei.getPlacaVeiculo().substring(6,7);
					novaPlacaMercosul = p3Letras + s4Letra + charAlterar + t5Letra;
				} else if (tamanhoPlaca == 8) {
					cliVei.getPlacaVeiculo().substring(7,8);
					String teste = cliVei.getPlacaVeiculo().substring(6,7);
					String a = teste;
					//t5Letra = cliVei.getPlacaVeiculo().substring(7,9);
					t2Letra = cliVei.getPlacaVeiculo().substring(6,8);
					novaPlacaMercosul = p3Letras + s4Letra + charAlterar + t2Letra;
				}
				
				
				this.setPlacaPadraoAlteradaMercosul(novaPlacaMercosul);
				
				
			}
			
		}
		

				
	}
	
	public String solicitarTransformacaoPlacaVeiculoMercosulParaPadrao(String placaVeiculo){
		
		if ( placaVeiculo != "" ){
			// Checar se placa já está no tipo padrão ABC-1111
				String checarPlaca =  placaVeiculo.substring(4,5);
			
				// Qdo a placa for tipo Mercosul
			if( checarPlaca.matches("[A-J]*")){
				// Placa já está no formato Mercosul
							
				// Se a placa pesquisada tipo Mercosul se ela já existe no campo placaVeiculo com o codigo padrao
				
				String charAlterar = placaVeiculo.substring(4, 5);
				//cliVei.getPlacaVeiculo().substring(5,6);
				
				if(charAlterar.equals("A")){
					charAlterar = "0";
				}
				if(charAlterar.equals("B")){
					charAlterar = "1";
				}
				if(charAlterar.equals("C")){
					charAlterar = "2";
				}
				if(charAlterar.equals("D")){
					charAlterar = "3";
				}
				if(charAlterar.equals("E")){
					charAlterar = "4";
				}
				if(charAlterar.equals("F")){
					charAlterar = "5";
				}
				if(charAlterar.equals("G")){
					charAlterar = "6";
				}
				if(charAlterar.equals("H")){
					charAlterar = "7";
				}
				if(charAlterar.equals("I")){
					charAlterar = "8";
				}			
				if(charAlterar.equals("J")){
					charAlterar = "9";
				}

				String p3Letras = placaVeiculo.substring(0,3);
				String s4LetraI = placaVeiculo.substring(3,4);
				String s4Letra = charAlterar;
				//String t2Letra = placaVeiculo.substring(5,7);
				
				// String novaPlacaMercosul = p3Letras +"-"+ s4LetraI + s4Letra + t2Letra;
				//
				String t5Letra = "";
				String t6Letra = ""; 
				
				String novaPlacaMercosul = "";
				
				int tamanhoPlaca = this.cliVei.getPlacaVeiculo().length(); // this.getPlacaOuIsca().length();
									
				// Tamanho 7 qdo tipo Mercosul
				if(tamanhoPlaca == 7) {
					t5Letra = placaVeiculo.substring(5,7);
					novaPlacaMercosul = p3Letras + "-" + s4LetraI + charAlterar + t5Letra;
				} else if (tamanhoPlaca == 8) {
					placaVeiculo.substring(7,8);
					String teste = placaVeiculo.substring(6,7);
					String a = teste;
					t5Letra = placaVeiculo.substring(6,8);
					novaPlacaMercosul = p3Letras + s4Letra + charAlterar + t5Letra;
				}
				
				//
				if (novaPlacaMercosul.length() == 8) {
					return novaPlacaMercosul;
				} else {
					return "";
				}

			} 		
		}
		return null;	
	}
	
	public String solicitarTransformacaoPlacaPadraoParaVeiculoMercosul(String placaVeiculo){
		
		if ( placaVeiculo != "" ){
			// Checar se placa já está no tipo padrão ABC-1111
				String checarPlaca =  placaVeiculo.substring(4,5);
			
				// Qdo a placa for tipo Mercosul
			if( checarPlaca.matches("[A-J]*")){
				// AGUARDAR SE USAREI ESSE PROCEDIMENTO...

			} else {
				// Placa será convertida para o formato Mercosul
				//this.setAbrirTransfPlacaMercosul(true);
				
							
					String charAlterar =  placaVeiculo.substring(5,6);
					
					if(charAlterar.equals("0")){
						charAlterar = "A";
					}
					if(charAlterar.equals("1")){
						charAlterar = "B";
					}
					if(charAlterar.equals("2")){
						charAlterar = "C";
					}
					if(charAlterar.equals("3")){
						charAlterar = "D";
					}
					if(charAlterar.equals("4")){
						charAlterar = "E";
					}
					if(charAlterar.equals("5")){
						charAlterar = "F";
					}
					if(charAlterar.equals("6")){
						charAlterar = "G";
					}
					if(charAlterar.equals("7")){
						charAlterar = "H";
					}
					if(charAlterar.equals("8")){
						charAlterar = "I";
					}			
					if(charAlterar.equals("9")){
						charAlterar = "J";
					}
					
					
					String p3Letras = placaVeiculo.substring(0,3);
					String s4Letra = placaVeiculo.substring(4,5);
					String t5Letra = "";
					String t6Letra = ""; 
					
					String novaPlacaMercosul = "";
					
					int tamanhoPlaca = this.cliVei.getPlacaVeiculo().length(); // this.getPlacaOuIsca().length();
										
					// Tamanho 7 qdo tipo Mercosul
					if(tamanhoPlaca == 7) {
						t5Letra = placaVeiculo.substring(6,7);
						novaPlacaMercosul = p3Letras + s4Letra + charAlterar + t5Letra;
					} else if (tamanhoPlaca == 8) {
						placaVeiculo.substring(7,8);
						String teste = placaVeiculo.substring(6,7);
						String a = teste;
						t5Letra = placaVeiculo.substring(6,8);
						novaPlacaMercosul = p3Letras + s4Letra + charAlterar + t5Letra;
					}
										
					//String t2Letra = placaVeiculo.substring(6,8);
					
					//String novaPlacaMercosul = p3Letras + s4Letra + charAlterar + t2Letra;
					
					//String novaPlacaMercosul = p3Letras + s4Letra + charAlterar + t5Letra + t6Letra;
									
					this.setPlacaPadraoAlteradaMercosul(novaPlacaMercosul);
					
					return novaPlacaMercosul;			
						
			}
	
		}
		
		return null;
	}
	
	public void salvarPlacaMercosul(){
		
		if (this.getIdPlaca() != null) {
			
			String placaAnterior = cliVei.getPlacaVeiculo();
			
			ClienteVeiculo cliVeiUp = clienteVeiculoService.findById(this.getIdPlaca());
			
			cliVeiUp.setUsuAlteraPlacaMercosul(checkUsuario.valid());
			cliVeiUp.setDtAlteracaoPlacaMercosul(new Date());
			cliVeiUp.setPlacaAnteriorMercosul(placaAnterior);
			cliVeiUp.setPlacaVeiculo(this.getPlacaPadraoAlteradaMercosul());
			
			clienteVeiculoService.update(cliVeiUp);
			
			RequestContext context = RequestContext.getCurrentInstance();
			
			//context.update("frmCadastroCnpj:frmCadastroArea:inputPlacaOuIsca");			
			context.update("frmCadastroCnpj:testeId");
			
			FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
					"Está placa foi atualizada para Mercosul!"));

			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("../contratos/ctr_monitoramento.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}							
			
		}
		
	}
	
	
	public void findById(Integer id){
		
		if( id == null ){
			throw new BusinessException("O id é obrigatório");
		}
		
		cliVei = clienteVeiculoService.crud().get(id);
		
		if( cliVei == null ) {
			throw new BusinessException("Registro não foi encontrado pelo id: " + id);
		}
	}
	
	public void onRowSelect(SelectEvent event){
		this.idPlaca = ((ClienteVeiculo) event.getObject()).getId();	
	}
    
	public void onRowUnselect(UnselectEvent event){		
		this.idPlaca = null;	
	}
		
	public void onRowSelectIsca(SelectEvent event){
		this.idIsca = ((ClienteIsca) event.getObject()).getId();	
	}
    
	public void onRowUnselectIsca(UnselectEvent event){		
		this.idIsca = null;	
	}
	
	public void copiarPlacas(){
		
		String txtPlacas = "";
		String pula = "\\n";
		
		if(this.getIdArea() != null){
			
			Criteria criteria = (Criteria) clienteVeiculoService.crud().getSession().createCriteria(ClienteVeiculo.class);
			criteria.add(Restrictions.eq("area.id", idArea));
			criteria.add(Restrictions.isNull("dtExclusao"));
			
			int result = criteria.list().size();
			
			if( result > 0) {
				qtdPlacas =  result;
				lstClienteVeiculo = criteria.list();
				
				String plcLinha = "";
				
					for(int i=0; i < criteria.list().size() ; i++){
						plcLinha = plcLinha + i + "\n";
						txtPlacas = txtPlacas + lstClienteVeiculo.get(i).getPlacaVeiculo() + "\n";
						
					}
										
					txtPlacas.replaceAll(";", "\n");
					String x = txtPlacas;
					String b = x;
					
					if(!txtPlacas.equals("")){
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Placas Copiadas", txtPlacas));
						return;
					}
				//lstClienteVeiculo = clienteVeiculoService.crud().criteria().eq("area.id", idArea ).list();
			} else {
				txtPlacas = "";
			}
				
		}
				
	}
	
	public void testeEntrada(){
		String n = "z";
		String z = n;
	}
	
	public void gerarArquivoExcel(Object document) throws ParseException{
		
		XSSFWorkbook wb = (XSSFWorkbook) document;
		XSSFSheet sheet = wb.getSheetAt(0);
		
		Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
		
		if(rowIterator.hasNext()){
			rowIterator.next();
		}
		
		while ( rowIterator.hasNext()){
			Row row = rowIterator.next();
			Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
			
			while(cellIterator.hasNext()){
				org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
				if(!cell.getStringCellValue().equals("")){
					cell.setCellValue(AppUtils.parse(cell.getStringCellValue(), Locale.FRANCE));
				}
			}
			
			
		}
		
	}
	
	/**
	 * @author Francis Bueno
	 * @return
	 * Carregado pelo clear para dar carga nos dados do cliente e contratos do tipo monitoramento
	 */
	public String ctrMonitoramento(){
		
		// Buscar Areao a qual o usuario está cadastrado
		Area areaUsuario = areaService.findById(checkUsuario.valid().getPessoa().getFuncao().getArea().getId());	

		this.setAreaNome(areaUsuario.getNome());
		this.setAreaNivelNome(areaUsuario.getNivel().getNome());
		this.setAreaCnpj(areaUsuario.getPessoaResponsavel().getDocumento1());
		
		
		// Se a area do usuario pertence a Matriz então tem acesso
		if( areaUsuario.getNivel().getId() == 2 ){
			
			// Contrato, busca o contrato de monitoramento com o status ativo
			
			Criteria criteria = (Criteria) clienteVeiculoService.crud().getSession().createCriteria(Contrato.class);
			criteria.add(Restrictions.eq("statusContrato.id", 645));
			criteria.add(Restrictions.eq("area.id", areaUsuario.getId()));
			criteria.add(Restrictions.eq("produtoTipo.id", 16));
			criteria.add(Restrictions.isNull("dtExclusao"));

			int result = criteria.list().size();
			
//			Contrato ctrMon  = contratoService.crud()
//								  .eq("statusContrato.id", 645)
//								  .eq("area.id", areaUsuario.getId())
//								  .eq("produtoTipo.id", 16).find();
			
			
			if (result == 1){
				
				Contrato ctrMon  = (Contrato) criteria.list().get(0);
//						crud()
//						.eq("id", criteria.list().get(0) )
//						.eq("statusContrato.id", 645)
//						.eq("area.id", areaUsuario.getId())
//						.eq("produtoTipo.id", 16).find();
				
				if( ctrMon.getStatusContrato().getId() == 645 ){
					
					// busca as placas dos veiculos fixos
					this.setIdContratoRastreamento(ctrMon.getId().toString());
					
					this.setIdArea(areaUsuario.getId());
					
					ctr = contratoService.findById(ctrMon.getId());
					
					Hibernate.initialize(ctr);
				
					getLstClienteVeiculo();
					
					abertoMonitoramento = true;
					
					RequestContext context = RequestContext.getCurrentInstance();							
					context.update("frmCadastroCnpj:outNome");
					context.update("frmCadastroCnpj:outNivel");
					context.update("frmCadastroCnpj:outCnpj");					
										
				}	
				
			} else {
				
				// Não tem acesso
				abertoMonitoramento = false;
			}
			
		}
		
		return "";
	}
	

	public void checkF5(){
		
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		String id = viewRoot.getViewId();		
		//previousPage != null && (
		 //if (previousPage.equals(id)) {
		       // It's a reload event
			 	mostraEdicao = true;
			 
		   // }
		
		//previousPage = id;
		
	}
	
	/**
	 * @author Francis Bueno
	 * @param s
	 * @return
	 * Formata o cnpj para o tipo padrão
	 */
    public static String formataCnpj(String s) {
        String entr = StringUtils.leftPad(s, 14, "0");
        return formata(entr, "##.###.###/####-##");
    }
	
    public static String formata(String s, String mascara) {
        MaskFormatter formatter;
        try {
            formatter = new MaskFormatter(mascara);
            formatter.setValueContainsLiteralCharacters(false);
            return formatter.valueToString(s);
        } catch (ParseException e) {
            return s;
        }
    }
    
	public ClienteVeiculo getCliVei() {
		return cliVei;
	}

	public Integer getId() {
		return id;
	}

	public void setCliVei(ClienteVeiculo cliVei) {
		this.cliVei = cliVei;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTituloDlgVeiculoIsca() {
		return tituloDlgVeiculoIsca;
	}

	public void setTituloDlgVeiculoIsca(String tituloDlgVeiculoIsca) {
		this.tituloDlgVeiculoIsca = tituloDlgVeiculoIsca;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getPlacaOuIsca() {
		return placaOuIsca;
	}

	public void setPlacaOuIsca(String placaOuIsca) {
		this.placaOuIsca = placaOuIsca;
	}

	public Integer getIdArea() {
		return idArea;
	}

	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}


	public List<String> getLstPlacasOuIscas() {
		return lstPlacasOuIscas;
	}

	public void setLstPlacasOuIscas(List<String> lstPlacasOuIscas) {
		this.lstPlacasOuIscas = lstPlacasOuIscas;
	}

	public JSONArray getArrayJsonPlacas() {
		return arrayJsonPlacas;
	}

	public void setArrayJsonPlacas(JSONArray arrayJsonPlacas) {
		this.arrayJsonPlacas = arrayJsonPlacas;
	}

	public List<ClienteVeiculo> getLstClienteVeiculo() {
		
		if(this.getIdArea() != null){
			
			Criteria criteria = (Criteria) clienteVeiculoService.crud().getSession().createCriteria(ClienteVeiculo.class);
			criteria.add(Restrictions.eq("area.id", idArea));
			criteria.add(Restrictions.isNull("dtExclusao"));
			criteria.addOrder(Order.asc("placaVeiculo"));
			
			int result = criteria.list().size();
			
			if( result > 0) {
				qtdPlacas =  result;
				lstClienteVeiculo = criteria.list();
				//lstClienteVeiculo = clienteVeiculoService.crud().criteria().eq("area.id", idArea ).list();
			} else {
				lstClienteVeiculo = null;
			}
				
		}
		
		return lstClienteVeiculo;
	}

	public void setLstClienteVeiculo(List<ClienteVeiculo> lstClienteVeiculo) {
		this.lstClienteVeiculo = lstClienteVeiculo;
	}

	public List<ClienteVeiculo> getLstClienteVeiculoSelect() {
		
		return lstClienteVeiculoSelect ;
	}

	public void setLstClienteVeiculoSelect(List<ClienteVeiculo> lstClienteVeiculoSelect) {
		this.lstClienteVeiculoSelect = lstClienteVeiculoSelect;
	}

	public String getNomeTelaUsuario() {
		return nomeTelaUsuario;
	}

	public void setNomeTelaUsuario(String nomeTelaUsuario) {
		this.nomeTelaUsuario = nomeTelaUsuario;
	}

	public String getSubNomeTelaUsuario() {
		return subNomeTelaUsuario;
	}

	public void setSubNomeTelaUsuario(String subNomeTelaUsuario) {
		this.subNomeTelaUsuario = subNomeTelaUsuario;
	}

	public Integer getQtdPlacas() {
		return qtdPlacas;
	}

	public void setQtdPlacas(Integer qtdPlacas) {
		this.qtdPlacas = qtdPlacas;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public Integer getIdPlaca() {
		return idPlaca;
	}

	public void setIdPlaca(Integer idPlaca) {
		this.idPlaca = idPlaca;
	}

	public String getSubCampoPlace() {
		return subCampoPlace;
	}

	public void setSubCampoPlace(String subCampoPlace) {
		this.subCampoPlace = subCampoPlace;
	}

	public boolean isMostrarDadosPlaca() {
		return mostrarDadosPlaca;
	}

	public boolean isMostrarDadosIsca() {
		return mostrarDadosIsca;
	}

	public void setMostrarDadosPlaca(boolean mostrarDadosPlaca) {
		this.mostrarDadosPlaca = mostrarDadosPlaca;
	}

	public void setMostrarDadosIsca(boolean mostrarDadosIsca) {
		this.mostrarDadosIsca = mostrarDadosIsca;
	}

	public ClienteIsca getCliIsca() {
		return cliIsca;
	}

	public void setCliIsca(ClienteIsca cliIsca) {
		this.cliIsca = cliIsca;
	}

	/*
	public List<ClienteIsca> getLstClienteIsca() {
		
		if(this.getIdArea() != null){
			
			//Criteria criteria = (Criteria) clienteIscaService.crud().getSession().createCriteria(ClienteIsca.class);
			criteria.add(Restrictions.eq("area.id", idArea));
			criteria.add(Restrictions.isNull("dtExclusao"));
			criteria.addOrder(Order.asc("isca"));
	
			int result = criteria.list().size();
			
			if( result > 0) {
				qtdIscas =  result;				
				lstClienteIsca = criteria.list();
				//lstClienteVeiculo = clienteVeiculoService.crud().criteria().eq("area.id", idArea ).list();
			} else {
				lstClienteIsca = null;
			}
				
		}
		
		return lstClienteIsca;
	}
	*/

	public void setLstClienteIsca(List<ClienteIsca> lstClienteIsca) {
		this.lstClienteIsca = lstClienteIsca;
	}

	public Integer getQtdIscas() {
		return qtdIscas;
	}

	public void setQtdIscas(Integer qtdIscas) {
		this.qtdIscas = qtdIscas;
	}

	public Integer getIdIsca() {
		return idIsca;
	}

	public void setIdIsca(Integer idIsca) {
		this.idIsca = idIsca;
	}

	public Integer getMaxLengthPlacaOuIsca() {
		return MaxLengthPlacaOuIsca;
	}

	public void setMaxLengthPlacaOuIsca(Integer maxLengthPlacaOuIsca) {
		MaxLengthPlacaOuIsca = maxLengthPlacaOuIsca;
	}

	public DetalheFixos getDetFixos() {
		return detFixos;
	}

	public void setDetFixos(DetalheFixos detFixos) {
		this.detFixos = detFixos;
	}

	public String getAreaNome() {
		return areaNome;
	}

	public String getAreaNivelNome() {
		return areaNivelNome;
	}

	public String getAreaCnpj() {
		return areaCnpj;
	}

	public void setAreaNome(String areaNome) {
		this.areaNome = areaNome;
	}

	public void setAreaNivelNome(String areaNivelNome) {
		this.areaNivelNome = areaNivelNome;
	}

	public void setAreaCnpj(String areaCnpj) {
		this.areaCnpj = areaCnpj;
	}

	public String getIdContratoRastreamento() {
		return idContratoRastreamento;
	}

	public void setIdContratoRastreamento(String idContratoRastreamento) {
		this.idContratoRastreamento = idContratoRastreamento;
	}

	public Contrato getCtr() {
		return ctr;
	}

	public void setCtr(Contrato ctr) {
		this.ctr = ctr;
	}

	public String getPlacaPadraoAlteradaMercosul() {
		return placaPadraoAlteradaMercosul;
	}

	public void setPlacaPadraoAlteradaMercosul(String placaPadraoAlteradaMercosul) {
		this.placaPadraoAlteradaMercosul = placaPadraoAlteradaMercosul;
	}

	public boolean isAbrirTransfPlacaMercosul() {
		return abrirTransfPlacaMercosul;
	}

	public void setAbrirTransfPlacaMercosul(boolean abrirTransfPlacaMercosul) {
		this.abrirTransfPlacaMercosul = abrirTransfPlacaMercosul;
	}

	public boolean isMostraEdicao() {
		return mostraEdicao;
	}

	public String getPreviousPage() {
		return previousPage;
	}

	public void setMostraEdicao(boolean mostraEdicao) {
		this.mostraEdicao = mostraEdicao;
	}

	public void setPreviousPage(String previousPage) {
		this.previousPage = previousPage;
	}

	public boolean isAbertoMonitoramento() {
		return abertoMonitoramento;
	}

	public void setAbertoMonitoramento(boolean abertoMonitoramento) {
		this.abertoMonitoramento = abertoMonitoramento;
	}

	public ArrayList<DetalheFixos> getLstDetalheFixos() {
		return lstDetalheFixos;
	}

	public void setLstDetalheFixos(ArrayList<DetalheFixos> lstDetalheFixos) {
		this.lstDetalheFixos = lstDetalheFixos;
	}
	
	
	
	
}
