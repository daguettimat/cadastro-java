package br.com.global5.manager.bean.areacliente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.StoredProcedureQuery;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.areacliente.AutotracCliente;
import br.com.global5.manager.model.areacliente.CadastroCliente;
import br.com.global5.manager.model.areacliente.CheckListCliente;
import br.com.global5.manager.model.areacliente.ViagemCliente;
import br.com.global5.manager.model.areacliente.Virtual;
import br.com.global5.manager.model.cadastro.PessoaQry;
import br.com.global5.manager.model.geral.Usuario;
//import br.com.global5.manager.service.areacliente.CadastroClienteService;
import br.com.global5.manager.service.areacliente.VirtualService;

@Named
@ViewAccessScoped
public class AreaClienteMB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Virtual virtual;
	private Integer id;
	
	private Usuario usuario;
	
	// Dados da Pesquisa
	private Date dtPesquisa = null;
	private Integer numContratoPesq = null;
	private String dtPesqMesAno = "";
	
	// Dados do Relatório Faturamento Rastreamento
	private String codContrato = "";
	private String areaCliente = "";
	private String pessoaCliente = "";
	private String nomeCliente = "";
	private String numeroNfse = "";
	private String numeroPrevisao = "";
	private String numeroCiclo = "";
	private String totalGeral = "";
	private String vlrViagFixo = "";
	private String qtdViagFixo = "";
	private String vlrViagAvulsa = "";
	private String qtdViagAvulsa = "";
	private String vlrCheckList = "";
	private String qtdCheckList = "";
	private String vlrIscaFixo = "";
	private String qtdIscaFixo = "";
	private String vlrIscaAvulsa = "";
	private String qtdIscaAvulsa = "";
	private String vlrEscolta = "";
	private String qtdEscolta = "";
	private String taxaADE = "";
	private String consumoSatelital = "";
	private String vlrControleJornada = "";
	private String vlrModuloLogistico = "";
	private String vlrBi = "";
	private BigDecimal vlrGerContaCompart = BigDecimal.ZERO;
	private BigDecimal vlrGerContaDedic = BigDecimal.ZERO;
	private String vlrResultadoSomaGerente = "";
	private BigDecimal vlrPaRemota = BigDecimal.ZERO;
	private String vlrResultadoPaRemota = "";
	private BigDecimal vlrAdicionais = BigDecimal.ZERO;
	private String vlrResultadoAdicionais = "";
	private String vlrConsumoViagem = "";
	private BigDecimal gerVlrConsumo = BigDecimal.ZERO;
	private String vlrFaturaMinima = "";
	private BigDecimal gerVlrFaturamMinima = BigDecimal.ZERO;
	private String vlrTotalOutros = "";
	private BigDecimal gerVlrTotalOutros = BigDecimal.ZERO;
	private String vlrAjustes = "";
	private BigDecimal gerVlrAjustes = BigDecimal.ZERO;
	private String vlrTotalFinal = "";
	private BigDecimal gerVlrTotalFinal = BigDecimal.ZERO;
	private String diaVencimento = "";
	
	
	// Para uso do fragment
	private Integer colspanServicosAdicionais = null;
	private boolean liberadoServicosAdicionais = false;
	private boolean liberadoServicosAdicionaisGerente = false;
	private boolean liberadoServicosAdicionaisPARemota = false;
	private boolean liberadoServicosAdicionaisAdicionais = false;	
	private boolean liberadoBotoesRastreamento = false;
	private boolean liberadoBotoesCadastro = false;
	
	private String msgNfseRastreamento = "";
	
	private Integer colspanServicosResumo = null;
	private boolean liberadoResumoVlrConsumo 		= false;
	private boolean liberadoResumoVlrFaturaMinima	= false;
	private boolean liberadoResumoVlrAjustes 	  	= false;
	
	// Dados do Relatório Faturamento Cadastro
	private String cdPrevisao 		= "";
	private String cdCadastro 		= "";
	private String cdCiclo    		= "";
	private String cdTotal	   		= "";
	private String cdAdicionais		= "";
	private String cdPessoa   		= "";
	private String cdTotalFinal 	= "";
	private String cdFaturaMinima 	= "";
	private String cdAreaoid		= "";
	private String cdNome 			= "";
	private String cdVencimento 	= "";
	private String cdConsumoFichas 	= "";
	private String cdConsulta 		= "";
	private String cdContrato 		= "";
	private String cdNotaFiscal 	= "";
	private String cdRenovacao 		= "";
	private String cdAjustes 		= "";
	private String cdTotalOutros 	= "";
	private String cdCadastroQtd	= "";
	private String cdConsultaQtd	= "";
	private String cdRenovacaoQtd 	= "";
	
	private BigDecimal gerCdVlrConsumoFichas 			= BigDecimal.ZERO;
	private BigDecimal gerCdVlrFaturamMinima 			= BigDecimal.ZERO;
	private BigDecimal gerCdVlrOutros 					= BigDecimal.ZERO;
	private BigDecimal gerCdVlrAjustes 					= BigDecimal.ZERO;
	private BigDecimal gerCdVlrTotalFinal 				= BigDecimal.ZERO;
	
	private String msgNfseCadastro = "";
	
	private Integer colspanServicosCadResumo 			= null;
	private boolean liberadoResumoCadVlrConsumoFichas 	= false;
	private boolean liberadoResumoCadVlrFaturaMinima	= false;
	private boolean liberadoResumoCadVlrOutros 	  		= false;
	private boolean liberadoResumoCadVlrAjustes 	  	= false;

	
	// relatório Consumo Cadastro
	
	private BigDecimal relCadValor = BigDecimal.ZERO;
	private BigDecimal vlrRelatorioCadastro = BigDecimal.ZERO;
	ArrayList<CadastroCliente> lstRelCadastro = new ArrayList<CadastroCliente>();
	
	// relatório Consumo Viagem
	private BigDecimal relViagemValor = BigDecimal.ZERO;
	private BigDecimal vlrRelatorioViagem = BigDecimal.ZERO;
	ArrayList<ViagemCliente> lstRelViagem = new ArrayList<ViagemCliente>();
	

	// relatório Consumo CheckList
	private BigDecimal relCheckListValor = BigDecimal.ZERO;
	private BigDecimal vlrRelatorioCheckList = BigDecimal.ZERO;
	ArrayList<CheckListCliente> lstRelCheckList = new ArrayList<CheckListCliente>();

	// relatório Consumo Autotrac
	private BigDecimal relAutotracValor = BigDecimal.ZERO;
	private BigDecimal relAutotracComunicacaoValor = BigDecimal.ZERO;
	private BigDecimal relTotalAutotracValor = BigDecimal.ZERO;
	private BigDecimal vlrRelatorioAutotrac = BigDecimal.ZERO;
	private BigDecimal vlrRelatorioAutotracComunicacao = BigDecimal.ZERO;
	ArrayList<AutotracCliente> lstRelAutotrac = new ArrayList<AutotracCliente>();
	
	
	// Variaveis para Dlg
	private Map<String, Object> options;
	
	
	// Variaveis para Cliente
	private String cnpjCliente = "";
	private String nomeClientePesquisado = "";
	private Integer numCodPessoaSac = null;
	private Integer idClientePesquisado = null;
	private Integer idAreaCliente = null;
	
	@Inject
	VirtualService virtualService;
	
	//@Inject
	//CadastroClienteService cadClienteService;
	
	
	@Inject
	private Crud<Virtual> virtualCrud;

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
		virtual = new Virtual();
		id = null;
		dtPesqMesAno = "";
		msgNfseCadastro = "";
		msgNfseRastreamento = "";
		usuario = checkUsuario.valid();
		colspanServicosResumo = 3;
		colspanServicosCadResumo = 2;
		vlrRelatorioCadastro = BigDecimal.ZERO;
		lstRelCadastro = new ArrayList<CadastroCliente>();
		
	}
	

	public void acesso(){
	
	 SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
	 if ( this.getDtPesquisa() != null) {
		 dtPesqMesAno = formatter.format(this.getDtPesquisa());

	 } else {
		 limparVariaveis();
		 limparVariaveisCadastro();
		 dtPesqMesAno = "";
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Por gentileza escolha uma data no calendário para seja possível continuar."));
		 return;
	 }
	// Processo do Monitoramento
		String nomeProcedureChamada = "usuario_mensal_monitoramento";

		Date dtMes = null;	

		Calendar now = Calendar.getInstance();
		now.setTime(this.getDtPesquisa());
		now.set(Calendar.HOUR, 23);
		now.set(Calendar.MINUTE, 00);
		now.set(Calendar.SECOND, 00);

		dtMes = now.getTime();

		
		StoredProcedureQuery queryPv = virtualCrud.getEntityManager()
				.createNamedStoredProcedureQuery(nomeProcedureChamada);
		
		Integer numIDArea = null;
		
		if (  idAreaCliente != null  ) {
			numIDArea = idAreaCliente;
		} else {
			 numIDArea = checkUsuario.valid().getPessoa().getFuncao().getArea().getId();//Integer.valueOf(this.getNumContratoPesq());
		}
		
				
		
		queryPv.setParameter("dt_mes", dtMes);
		queryPv.setParameter("id_area"  , numIDArea);
		queryPv.execute();
		
		String textb = queryPv.getResultList().get(0).toString();
		
		if (!textb.equals("{}")){
			
			JSONObject obj = new  JSONObject(textb);
			
			// Nao foi encontrado relação com esses campos
				if (!obj.isNull("conoid")) {
					codContrato 	=	obj.opt("conoid").toString();				
				}

				if (!obj.isNull("areaoid")) {
					areaCliente 	=	obj.opt("areaoid").toString();				
				}

				if (!obj.isNull("pesoid")) {
					pessoaCliente 	=	obj.opt("pesoid").toString();				
				}
				
				if (!obj.isNull("nome")) {
					nomeCliente 	=	obj.opt("nome").toString();				
				}

				if (!obj.isNull("nf_nr_nf")) {
					numeroNfse		= 	obj.opt("nf_nr_nf").toString();
				}
				
				if (!obj.isNull("prevoid")) {
					numeroPrevisao	=	obj.opt("prevoid").toString();				
				}

				if (!obj.isNull("ciclo")) {
					numeroCiclo 	=	obj.opt("ciclo").toString();				
				}

				if (!obj.isNull("total")) {
					totalGeral		= 	obj.opt("total").toString();				
				}

				// Nfse - Rastreamento	
				if (!obj.isNull("nf_nr_nf")) {
					
					numeroNfse = obj.opt("nf_nr_nf").toString();
				
					Integer numNfse = Integer.valueOf(numeroNfse);
					
					if (numNfse > 1 ){
						msgNfseRastreamento = numeroNfse;
					} else if (numNfse < 1) {
						msgNfseRastreamento = "Nota Fiscal Modificada";
					} 
					
				} else {
					msgNfseRastreamento = "Nota Fiscal será Gerada";
				}
				
				
			// Viagens	
				if (!obj.isNull("viag_fixo")) {
					vlrViagFixo		=	obj.opt("viag_fixo").toString();	
				}
				if (!obj.isNull("viag_fixo_qtd")) {
					qtdViagFixo		=	obj.opt("viag_fixo_qtd").toString();				
				}
				if (!obj.isNull("viag_viag")) {
					vlrViagAvulsa	=	obj.opt("viag_viag").toString();				
				}
				if (!obj.isNull("viag_viag_qtd")) {				
					qtdViagAvulsa	=	obj.opt("viag_viag_qtd").toString();
				}


			
			// CheckList
				if (!obj.isNull("check_vlr")) {	
					vlrCheckList	=	obj.opt("check_vlr").toString();				
				}
				if (!obj.isNull("check_qtd")) {
					qtdCheckList	=	obj.opt("check_qtd").toString();				
				}

			
			// Monitoramento Adicional
				if (!obj.isNull("isca_fixo")) {
					vlrIscaFixo		=	obj.opt("isca_fixo").toString();				
				}
				if (!obj.isNull("isca_fixo_qtd")) {
					qtdIscaFixo		=	obj.opt("isca_fixo_qtd").toString();				
				}
				if (!obj.isNull("isca_viagem")) {
					vlrIscaAvulsa	= 	obj.opt("isca_viagem").toString();	
				}
				if (!obj.isNull("isca_viagem_qtd")) {
					qtdIscaAvulsa 	=	obj.opt("isca_viagem_qtd").toString();				
				}
				if (!obj.isNull("escolta")) {
					vlrEscolta		=	obj.opt("escolta").toString();				
				}
				if (!obj.isNull("escolta_qtd")) {
					qtdEscolta		= 	obj.opt("escolta_qtd").toString();	
				}
				

			// Consumo Satelital
				if (!obj.isNull("taxa_ade")) {
					taxaADE			=	obj.opt("taxa_ade").toString();				
				}
				if (!obj.isNull("consumo_satelital")) {
					consumoSatelital=	obj.opt("consumo_satelital").toString();				
				}


			// Logística
				if (!obj.isNull("controle_jornada")) {
					if ( obj.opt("controle_jornada").toString().equals("-1.0")) {
						vlrControleJornada = "Não Contratado";
					} else {
						vlrControleJornada	=	obj.opt("controle_jornada").toString();
					}				
				}

				if (!obj.isNull("modulo_logistico")) {
					if(obj.opt("modulo_logistico").toString().equals("-1.0") || obj.opt("modulo_logistico").toString().equals("0.0")){
						vlrModuloLogistico =	"Não Contratado";
					} else {
						vlrModuloLogistico =	obj.opt("modulo_logistico").toString();
					}				
				}
				
				if (!obj.isNull("bi")) {
					if(obj.opt("bi").toString().equals("-1.0")){
						vlrBi =	"Não Contratado";
					} else {
						vlrBi				= 	obj.opt("bi").toString();
					}				
				}
						

			// Serviços Adicionais	--> Apenas mostrar se algum deste for apresentado	
				
				Integer contaColspanServAdic = 0;
				
				String gerContaCompart = "";
				int resultContaCompart = 0;
				
				if (!obj.isNull("ger_conta_compart")) {
					
					gerContaCompart = obj.opt("ger_conta_compart").toString();
					vlrGerContaCompart = new BigDecimal(gerContaCompart);
					resultContaCompart 	= vlrGerContaCompart.compareTo(BigDecimal.ZERO);
					
				}
				
				String gerContaDedic = "";
				int resultContaDedic = 0;
				
				if (!obj.isNull("ger_conta_dedic")) {
					gerContaDedic = obj.opt("ger_conta_dedic").toString();
					vlrGerContaDedic   = new BigDecimal(gerContaDedic);
					resultContaDedic   = vlrGerContaDedic.compareTo(BigDecimal.ZERO);
				}
										
				// Quando os valores forem iguais a Zero
				if(resultContaCompart == 0 && resultContaDedic == 0){
					this.setVlrResultadoSomaGerente("Não Contratado");

				} 
				
				// Quando os valores forem maiores que Zero
				if (resultContaCompart == 1 && resultContaDedic == 1) {
					
					BigDecimal vlrSoma = BigDecimal.ZERO;
					vlrGerContaCompart = new BigDecimal(gerContaCompart);
					vlrGerContaDedic   = new BigDecimal(gerContaDedic);
					
					vlrSoma = vlrSoma.add(vlrGerContaCompart).add(vlrGerContaDedic);
					
					this.setLiberadoServicosAdicionaisGerente(true);
					
					this.setVlrResultadoSomaGerente(vlrSoma.toString());
					
					contaColspanServAdic++;
					
				}
				if (resultContaCompart == 1 && resultContaDedic == 0) {
					
					this.setLiberadoServicosAdicionaisGerente(true);
					this.setVlrResultadoSomaGerente(gerContaCompart.toString());
					contaColspanServAdic++;
					
				}
				if (resultContaCompart == 0 && resultContaDedic == 1) {
					
					this.setLiberadoServicosAdicionaisGerente(true);
					this.setVlrResultadoSomaGerente(gerContaDedic.toString());
					contaColspanServAdic++;
					
				}
				
				
				// Tratamento para Valor PA Remota
				String gerPaRemota = "";
				int resultVlrPaRemota = 0;
				
				if (!obj.isNull("pa_remota")) {
					gerPaRemota = obj.opt("pa_remota").toString();
					vlrPaRemota	   = new BigDecimal(gerPaRemota);
					resultVlrPaRemota = vlrPaRemota.compareTo(BigDecimal.ZERO);
				}
				
				if(resultVlrPaRemota == 0){
					this.setVlrResultadoPaRemota("Não Contratado");				
					this.setLiberadoServicosAdicionaisPARemota(false);				
					
				} else {
					this.setVlrResultadoPaRemota(obj.opt("pa_remota").toString());
					this.setLiberadoServicosAdicionaisPARemota(true);
					contaColspanServAdic++;
				}
				
				// Tratamento para Valor Adicionais
				String gerAdicionais = "";
				int resultVlrAdicionais = 0;
				if (!obj.isNull("adicionais")) {
					gerAdicionais = obj.opt("adicionais").toString();
					vlrAdicionais	   = new BigDecimal(gerAdicionais);
					resultVlrAdicionais = vlrAdicionais.compareTo(BigDecimal.ZERO);
				}			
				
				if(resultVlrAdicionais == 0){
					this.setVlrResultadoAdicionais("Não Contratado");
					this.setLiberadoServicosAdicionaisAdicionais(false);
				} else {
					this.setVlrResultadoAdicionais(obj.opt("adicionais").toString());
					this.setLiberadoServicosAdicionaisAdicionais(true);
					contaColspanServAdic++;
				}			

				if ( contaColspanServAdic > 1) {
					this.setColspanServicosAdicionais(contaColspanServAdic);
					this.setLiberadoServicosAdicionais(true);
					contaColspanServAdic++;
				} else {
					
					this.setLiberadoServicosAdicionais(false);				
					
				}
				
				
				
			// Resumo Rastreamento				
				
				Integer contaColspanResumo = 0;
				
				String gerConsumoViagem = obj.opt("consumo_viagens").toString();
				String gerFaturaMinima  = obj.opt("fat_minima").toString();						
				
				gerVlrConsumo 			= new BigDecimal(gerConsumoViagem);
				gerVlrFaturamMinima   	= new BigDecimal(gerFaturaMinima);			  
						
				int resultVlrConsumoVlrFaturaMinima 	= gerVlrConsumo.compareTo(gerVlrFaturamMinima);
				
				if ( resultVlrConsumoVlrFaturaMinima == 0 ) {
					
					//vlrConsumoViagem	=	obj.opt("consumo_viagens").toString();
					vlrFaturaMinima		=	obj.opt("fat_minima").toString();
		
					//this.setLiberadoResumoVlrConsumo(true);
					this.setLiberadoResumoVlrFaturaMinima(true);
					
					//contaColspanResumo++;
					contaColspanResumo++;
				} 
				
				// Se VlrConsumo for > que VlrFaturaMinima
				if ( resultVlrConsumoVlrFaturaMinima == 1 ) {
					// Mostrar VlrConsumo
					this.setLiberadoResumoVlrConsumo(true);
					gerVlrConsumo	= new BigDecimal(obj.opt("consumo_viagens").toString());
					
					// Ocultar VlrFaturaMinima
					this.setLiberadoResumoVlrFaturaMinima(false);
					contaColspanResumo++;
					
				} 
				
				// Se VlrConsumo for < que VlrFaturaMinima
				if ( resultVlrConsumoVlrFaturaMinima == -1 ) {
					// Mostrar VlrFaturaMinima
					this.setLiberadoResumoVlrFaturaMinima(true);
					gerVlrFaturamMinima		=	new BigDecimal(obj.opt("fat_minima").toString());
					
					// Ocultar VlrConsumo
					this.setLiberadoResumoVlrConsumo(false);
					contaColspanResumo++;
				} 


				// VlrAjustes

				String gerAjustes		= obj.opt("ajustes").toString();

				gerVlrAjustes	 		= new BigDecimal(gerAjustes);
				
				int resultVlrAjustes = gerVlrAjustes.compareTo(BigDecimal.ZERO);
				
				
				if(resultVlrAjustes == 0){
					// Ocultar VlrAjustes
					this.setLiberadoResumoVlrAjustes(false);
					
				} else {
					// Mostrar VlrAjustes
					this.setLiberadoResumoVlrAjustes(true);
					gerVlrAjustes	= 	new BigDecimal(obj.opt("ajustes").toString());
					contaColspanResumo++;
				}
				
				this.setLiberadoBotoesRastreamento(true);
				
			
				gerVlrTotalOutros		=	new BigDecimal(obj.opt("total_outros").toString());		
				gerVlrTotalFinal		=	new BigDecimal(obj.opt("total_final").toString());	
				diaVencimento			= 	obj.opt("vencimento").toString();

				contaColspanResumo++;
				contaColspanResumo++;
				contaColspanResumo++;

				this.setColspanServicosResumo(contaColspanResumo);

				
				RequestContext context = RequestContext.getCurrentInstance();
				context.update("formRelatorioCliente:tbView:tbView01");
							
			
		} else {
			limparVariaveis();
			 msgNfseRastreamento = "Não há Faturamento para o Período Selecionado!";
			 setLiberadoBotoesRastreamento(false);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Não há apuração para esse período selecionado para Rastreamento!"));
		}
		
		// Processo do Faturamento Cadastro
		String nomeProcedureChamadaCadastro = "usuario_mensal_cadastro";
		
		StoredProcedureQuery queryPvc = virtualCrud.getEntityManager()
				.createNamedStoredProcedureQuery(nomeProcedureChamadaCadastro);
		
		Integer numIDAreaC = null;
		
		if (  idAreaCliente != null  ) {
			numIDAreaC = idAreaCliente;
		} else {
			 numIDAreaC = checkUsuario.valid().getPessoa().getFuncao().getArea().getId();//Integer.valueOf(this.getNumContratoPesq());
		}
		
		//Integer numIDAreaC = checkUsuario.valid().getPessoa().getFuncao().getArea().getId(); //Integer.valueOf(this.getNumContratoPesq());
		
		queryPvc.setParameter("dt_mes", dtMes);
		queryPvc.setParameter("id_area"  , numIDAreaC);
		queryPvc.execute();
		
		String textbc = queryPvc.getResultList().get(0).toString();
		
		if (!textbc.equals("{}")){
			
			JSONObject obj = new  JSONObject(textbc);
			
			// Nao foi encontrado relação com esses campos
				if (!obj.isNull("conoid")) {
					codContrato 	=	obj.opt("conoid").toString();				
				}
			
			// Nfse - Cadastro	
				if (!obj.isNull("nf_nr_nf")) {
					
					cdNotaFiscal = obj.opt("nf_nr_nf").toString();
				
					Integer numNfse = Integer.valueOf(cdNotaFiscal);
					
					if (numNfse > 1 ){
						msgNfseCadastro = cdNotaFiscal;
					} else if (numNfse < 1) {
						msgNfseCadastro = "Nota Fiscal Modificada";
					} 
					
				} else {
					msgNfseCadastro = "Nota Fiscal será Gerada";
				}
			
			// Ficha Cadastro , Consulta e Renovação	
				if (!obj.isNull("cadastro")) {
					cdCadastro		= obj.opt("cadastro").toString();					
				}
				if (!obj.isNull("cadastro_qtd")) {
					cdCadastroQtd 	= obj.opt("cadastro_qtd").toString();					
				}
				if (!obj.isNull("consulta")) {
					cdConsulta		= obj.opt("consulta").toString();					
				}
				if (!obj.isNull("consulta_qtd")) {
					cdConsultaQtd 	= obj.opt("consulta_qtd").toString();					
				}
				if (!obj.isNull("renovacao")) {
					cdRenovacao 	= obj.opt("renovacao").toString();					
				}
				if (!obj.isNull("renovacao_qtd")) {
					cdRenovacaoQtd 	= obj.opt("renovacao_qtd").toString();					
				}

				
			// Ficha Resumo Cadastro								
								
				Integer contaColspanResumo = 0;
				
				String gerConsumoFichas = obj.opt("consumo_fichas").toString();
				String gerFaturaMinima  = obj.opt("fat_minima").toString();						
				
				gerCdVlrConsumoFichas	= new BigDecimal(gerConsumoFichas);
				gerCdVlrFaturamMinima   = new BigDecimal(gerFaturaMinima);			  
						
				int resultVlrConsumoFichasVlrFaturaMinima 	= gerCdVlrConsumoFichas.compareTo(gerCdVlrFaturamMinima);
				
				if ( resultVlrConsumoFichasVlrFaturaMinima == 0 ) {
					
					//cdConsumoFichas = obj.opt("consumo_fichas").toString();				
					cdFaturaMinima  = obj.opt("fat_minima").toString();					
					
					//contaColspanResumo++;
					contaColspanResumo++;
				} 
				
				// Se VlrConsumoFichas for > que VlrFaturaMinima
				if ( resultVlrConsumoFichasVlrFaturaMinima == 1 ) {
					// Mostrar VlrConsumoFichas
					this.setLiberadoResumoCadVlrConsumoFichas(true);
					cdConsumoFichas = obj.opt("consumo_fichas").toString();	
					
					// Ocultar VlrFaturaMinima
					//this.setLiberadoResumoCadVlrFaturaMinima(false);
					contaColspanResumo++;
					
				} 
				
				// Se VlrConsumo for < que VlrFaturaMinima
				if ( resultVlrConsumoFichasVlrFaturaMinima == -1 ) {
					// Mostrar VlrFaturaMinima
					this.setLiberadoResumoCadVlrFaturaMinima(true);
					cdFaturaMinima  = obj.opt("fat_minima").toString();
					
					// Ocultar VlrConsumo
					//this.setLiberadoResumoCadVlrConsumoFichas(false);
					contaColspanResumo++;
				} 

				// Outros
					int resultVlrOutros = 0;
					if (!obj.isNull("adicionais")) {
						cdAdicionais  	= obj.opt("adicionais").toString();
						gerCdVlrOutros = new BigDecimal(cdAdicionais);
						resultVlrOutros = gerCdVlrOutros.compareTo(BigDecimal.ZERO);
						//this.setLiberadoResumoCadVlrOutros(true);
					}
					
					if ( resultVlrOutros > 0) {
						// Mostrar VlrAjustes
						this.setLiberadoResumoCadVlrOutros(true);
						cdAdicionais  	= obj.opt("adicionais").toString();
						contaColspanResumo++;
					}					

				// VlrAjustes
					int resultVlrAjustes = 0;
					if (!obj.isNull("ajustes")) {
						cdAjustes 		= obj.opt("ajustes").toString();	
						gerCdVlrAjustes = new BigDecimal(cdAjustes);
						resultVlrAjustes = gerCdVlrAjustes.compareTo(BigDecimal.ZERO);
					}
								
						if(resultVlrAjustes > 0){
							// Mostrar VlrAjustes
							this.setLiberadoResumoCadVlrAjustes(true);
							cdAjustes 		= obj.opt("ajustes").toString();
							contaColspanResumo++;
						}
				
				// VlrTotal
					int resultVlrTotalFinal = 0;
					if (!obj.isNull("total_final")) {	
						cdTotalFinal = obj.opt("total_final").toString();
						gerCdVlrTotalFinal = new BigDecimal(cdTotalFinal);
						resultVlrTotalFinal = gerCdVlrTotalFinal.compareTo(BigDecimal.ZERO);
					}
					
						if(resultVlrTotalFinal > 0){
							// Mostrar VlrAjustes
							cdTotalFinal = obj.opt("total_final").toString();
							gerCdVlrTotalFinal = new BigDecimal(cdTotalFinal);							
							contaColspanResumo++;
						}					

				// Vencimento
						if (!obj.isNull("vencimento")) {	
							cdVencimento = obj.opt("vencimento").toString();	
							contaColspanResumo++;
						}
												
				
				this.setColspanServicosCadResumo(contaColspanResumo);
				
				this.setLiberadoBotoesCadastro(true);				
				
				RequestContext context = RequestContext.getCurrentInstance();
				context.update("formRelatorioCliente:tbView:tbView02");
				
				
		} else {
			limparVariaveisCadastro();
			setLiberadoBotoesCadastro(false);
		    msgNfseCadastro = "Não há Faturamento para o Período Selecionado!";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Não há apuração para esse período selecionado para Cadastro!"));
		}

	}

	public void verConsumoCadastro(){
		// Processo do Faturamento Cadastro
		String nomeProcedureChamadaCadastro = "usuario_detalhe_cadastro";
		
		Date dtMes = null;	

		Calendar now = Calendar.getInstance();
		now.setTime(this.getDtPesquisa());
		now.set(Calendar.HOUR, 23);
		now.set(Calendar.MINUTE, 00);
		now.set(Calendar.SECOND, 00);

		dtMes = now.getTime();

		
		this.setVlrRelatorioCadastro(BigDecimal.ZERO);
		
		StoredProcedureQuery queryPvCad = virtualCrud.getEntityManager()
				.createNamedStoredProcedureQuery(nomeProcedureChamadaCadastro);
		
		Integer numIDAreaC = null;
		
		if (  idAreaCliente != null  ) {
			numIDAreaC = idAreaCliente;
		} else {
			 numIDAreaC = checkUsuario.valid().getPessoa().getFuncao().getArea().getId();//Integer.valueOf(this.getNumContratoPesq());
		}
		
		//Integer numIDAreaC = checkUsuario.valid().getPessoa().getFuncao().getArea().getId(); //Integer.valueOf(this.getNumContratoPesq());
		
		queryPvCad.setParameter("dt_mes", dtMes);
		queryPvCad.setParameter("id_area"  , numIDAreaC);
		queryPvCad.execute();
		
		String textbCad = queryPvCad.getResultList().get(0).toString();
		
		lstRelCadastro = new ArrayList<CadastroCliente>();
		
		if (!textbCad.equals("[{")){
			
			
			JSONArray arrayJson = new JSONArray(textbCad);
			
			if ( arrayJson != null ){
				
				int listCountArray = arrayJson.length();

		
				
				for( int i = 0; i < listCountArray ; i++){
					
					JSONObject objCad = arrayJson.getJSONObject(i);
					
					CadastroCliente cad = new CadastroCliente();

					// Tipo Motorista
					// !obj.isNull("conoid")
					if(!objCad.isNull("mottipo")){
						cad.setMottipo(objCad.optString("mottipo"));
					} else {
						cad.setMottipo("");
					}
					
					// Vencimento
					if(!objCad.opt("vencimento").toString().equals(null)){
						cad.setVencimento(objCad.optString("vencimento"));
					}
					
					// Tipo
					if(!objCad.opt("tipo").toString().equals(null)){
						cad.setTipo(objCad.optString("tipo"));
					}
					
					// Cpf
					if(!objCad.optString("motcpf").toString().equals(null)){
						cad.setMotcpf(objCad.optString("motcpf"));
					}

					// Data_termino
					if(!objCad.optString("dt_termino").toString().equals(null)){
						cad.setDt_termino(objCad.optString("dt_termino"));
					}

					// cavalo
					if(!objCad.optString("cavalo").toString().equals(null)){
						cad.setCavalo(objCad.optString("cavalo"));
					}
					
					// motorista
					if(!objCad.optString("motorista").toString().equals(null)){
						cad.setMotorista(objCad.optString("motorista"));
					}					
					
					// cliente
					if(!objCad.optString("unidade").toString().equals(null)){
						cad.setUnidade(objCad.optString("unidade"));
					}
					
					// ficha
					if(!objCad.optString("ficha").toString().equals(null)){
						cad.setFicha(objCad.optString("ficha"));
					}
					
					// reboque1
					if(!objCad.optString("reboque1").toString().equals(null)){
						cad.setReboque1(objCad.optString("reboque1"));
					}
					
					// reboque2
					if(!objCad.optString("reboque2").toString().equals(null)){
						cad.setReboque2(objCad.optString("reboque2"));
					}
					// reboque3
					if(!objCad.optString("reboque3").toString().equals(null)){
						cad.setReboque3(objCad.optString("reboque3"));
					}
					
					// valor
					if(!objCad.optString("valor").toString().equals(null)){
						relCadValor = new BigDecimal(objCad.opt("valor").toString());
						
						cad.setValor(relCadValor.setScale(2, BigDecimal.ROUND_CEILING));
						vlrRelatorioCadastro = vlrRelatorioCadastro.add(relCadValor.setScale(2, BigDecimal.ROUND_CEILING));
					}					
					
					cad.setId(i + 1);
					
					lstRelCadastro.add(cad);
					
					
				}				
				
			} else {
				//TODO Nada consta no Json
			}
			
			String b = "a";
			String c = b;
			
			openDlgRelatorioCadastro();
			
			//JSONObject obj = new  JSONObject(textbCad);
			
			// Nao foi encontrado relação com esses campos
		//	if (!obj.isNull("valor")) {
		//		codContrato 	=	obj.opt("valor").toString();				
		//	}
			
			
		}
		
	}
		
	public void openDlgRelatorioCadastro() {
		options.put("width", "96%");
		options.put("height", "80%");

		RequestContext.getCurrentInstance().openDialog("dlg_relatorioCadastro", options, null);
		RequestContext.getCurrentInstance().execute("onTop('dlg_relatorioCadastro')");
	}

	public void verConsumoViagem(){
		// Processo do Faturamento Cadastro
		String nomeProcedureChamadaViagem = "usuario_detalhe_viagens";
		
		Date dtMes = null;	

		Calendar now = Calendar.getInstance();
		now.setTime(this.getDtPesquisa());
		now.set(Calendar.HOUR, 23);
		now.set(Calendar.MINUTE, 00);
		now.set(Calendar.SECOND, 00);

		dtMes = now.getTime();
		
		this.setVlrRelatorioViagem(BigDecimal.ZERO);
		
		StoredProcedureQuery queryPvViagem = virtualCrud.getEntityManager()
				.createNamedStoredProcedureQuery(nomeProcedureChamadaViagem);
		
		Integer numIDAreaC = null;
		
		if (  idAreaCliente != null  ) {
			numIDAreaC = idAreaCliente;
		} else {
			 numIDAreaC = checkUsuario.valid().getPessoa().getFuncao().getArea().getId();//Integer.valueOf(this.getNumContratoPesq());
		}
		
		//Integer numIDAreaC = checkUsuario.valid().getPessoa().getFuncao().getArea().getId(); 
		
		queryPvViagem.setParameter("dt_mes", dtMes);
		queryPvViagem.setParameter("id_area"  , numIDAreaC);
		queryPvViagem.execute();
		
		String textViagem = queryPvViagem.getResultList().get(0).toString();
		
		lstRelViagem = new ArrayList<ViagemCliente>();
		
		if (!textViagem.equals("[{")){
			
			
			JSONArray arrayJson = new JSONArray(textViagem);
			
			if ( arrayJson != null ){
				
				int listCountArray = arrayJson.length();
				
				for( int i = 0; i < listCountArray ; i++){
					
					JSONObject objViagem = arrayJson.getJSONObject(i);
					
					ViagemCliente viag = new ViagemCliente();

					// SM
					if(!objViagem.isNull("sm")){
						viag.setSm( Integer.valueOf(objViagem.optString("sm")));
					} else {
						viag.setSm(null);
					}
					
					// Data Inicio
					if(!objViagem.opt("dt_inicio").toString().equals(null)){
						viag.setDt_inicio(objViagem.optString("dt_inicio"));
					}
					
					// Data Termino
					if(!objViagem.opt("dt_termino").toString().equals(null)){
						viag.setDt_termino(objViagem.optString("dt_termino"));
					}
					
					// cavalo
					if(!objViagem.optString("cavalo").toString().equals(null)){
						viag.setCavalo(objViagem.optString("cavalo"));
					}
	
					// cidade origem
					if(!objViagem.optString("cid_origem").toString().equals(null)){
						viag.setCid_origem(objViagem.optString("cid_origem"));
					}
					
					// cidade destino
					if(!objViagem.optString("cid_destino").toString().equals(null)){
						viag.setCid_destino(objViagem.optString("cid_destino"));
					}

					// tecnologia
					if(!objViagem.optString("tecnologia").toString().equals(null)){
						viag.setTecnologia(objViagem.optString("tecnologia"));
					}

					// produto
					if(!objViagem.optString("produto").toString().equals(null)){
						viag.setProduto(objViagem.optString("produto"));
					}					
										
					// valor
					if(!objViagem.optString("valor").toString().equals(null)){
						
						relViagemValor = new BigDecimal(objViagem.opt("valor").toString());
						
						viag.setValor(relViagemValor.setScale(2, BigDecimal.ROUND_CEILING));
						
						vlrRelatorioViagem = vlrRelatorioViagem.add(relViagemValor.setScale(2, BigDecimal.ROUND_CEILING));
						
					}					
					
					viag.setId(i + 1);
					
					lstRelViagem.add(viag);					
					
				}
				
				
			} else {
				//TODO Nada consta no Json
			}
						
			openDlgRelatorioViagem();
						
		}
		
	}
		
	public void openDlgRelatorioViagem() {
		options.put("width", "96%");
		options.put("height", "80%");

		RequestContext.getCurrentInstance().openDialog("dlg_relatorioViagem", options, null);
		RequestContext.getCurrentInstance().execute("onTop('dlg_relatorioViagem')");
	}
	
	public void verConsumoCheckList(){
		// Processo do Faturamento Cadastro
		String nomeProcedureChamadaCheckList = "usuario_detalhe_checklists";

		Date dtMes = null;	

		Calendar now = Calendar.getInstance();
		now.setTime(this.getDtPesquisa());
		now.set(Calendar.HOUR, 23);
		now.set(Calendar.MINUTE, 00);
		now.set(Calendar.SECOND, 00);

		dtMes = now.getTime();
				
		
		this.setVlrRelatorioCheckList(BigDecimal.ZERO);
		
		StoredProcedureQuery queryPvCheck = virtualCrud.getEntityManager()
				.createNamedStoredProcedureQuery(nomeProcedureChamadaCheckList);
		
		Integer numIDAreaC = null;
		
		if (  idAreaCliente != null  ) {
			numIDAreaC = idAreaCliente;
		} else {
			 numIDAreaC = checkUsuario.valid().getPessoa().getFuncao().getArea().getId();//Integer.valueOf(this.getNumContratoPesq());
		}
		
		//Integer numIDAreaC = checkUsuario.valid().getPessoa().getFuncao().getArea().getId(); 
		
		queryPvCheck.setParameter("dt_mes", dtMes);
		queryPvCheck.setParameter("id_area"  , numIDAreaC);
		queryPvCheck.execute();
		
		String textCheck = queryPvCheck.getResultList().get(0).toString();
		
		lstRelCheckList = new ArrayList<CheckListCliente>();
		
		if (!textCheck.equals("[{")){
			
			
			JSONArray arrayJson = new JSONArray(textCheck);
			
			if ( arrayJson != null ){
				
				int listCountArray = arrayJson.length();
				
				for( int i = 0; i < listCountArray ; i++){
					
					JSONObject objCheck = arrayJson.getJSONObject(i);
					
					CheckListCliente check = new CheckListCliente();

					// ID
					if(!objCheck.isNull("id")){
						check.setId(Integer.valueOf(objCheck.optString("id")));
					} else {
						check.setId(null);
					}
					
					// Data 
					if(!objCheck.opt("data").toString().equals(null)){
						check.setData(objCheck.optString("data"));
					}
					
					// cavalo
					if(!objCheck.optString("cavalo").toString().equals(null)){
						check.setCavalo(objCheck.optString("cavalo"));
					}

					// status
					if(!objCheck.optString("status").toString().equals(null)){
						check.setStatus(objCheck.optString("status"));
					}
										
					// valor
					if(!objCheck.optString("valor").toString().equals(null)){
						
						relCheckListValor = new BigDecimal(objCheck.opt("valor").toString());
						
						check.setValor(relCheckListValor.setScale(2, BigDecimal.ROUND_CEILING));
						
						vlrRelatorioCheckList = vlrRelatorioCheckList.add(relCheckListValor.setScale(2, BigDecimal.ROUND_CEILING));
						
					}					
										
					lstRelCheckList.add(check);
										
				}
				
				
			} else {
				//TODO Nada consta no Json
			}
						
			openDlgRelatorioChecklist();
						
		}
		
	}
		
	public void openDlgRelatorioChecklist() {
		options.put("width", "96%");
		options.put("height", "80%");

		RequestContext.getCurrentInstance().openDialog("dlg_relatorioCheckList", options, null);
		RequestContext.getCurrentInstance().execute("onTop('dlg_relatorioCheckList')");
	}
	
	public void verConsumoAutotrac(){
		// Processo do Faturamento Cadastro
		String nomeProcedureChamadaAutotrac = "usuario_detalhe_autotrac";

		Date dtMes = null;	

		Calendar now = Calendar.getInstance();
		now.setTime(this.getDtPesquisa());
		now.set(Calendar.HOUR, 23);
		now.set(Calendar.MINUTE, 00);
		now.set(Calendar.SECOND, 00);

		dtMes = now.getTime();
		
		this.setVlrRelatorioAutotrac(BigDecimal.ZERO);
		this.setVlrRelatorioAutotracComunicacao(BigDecimal.ZERO);
		this.setRelTotalAutotracValor(BigDecimal.ZERO);
		
		StoredProcedureQuery queryPvAuto = virtualCrud.getEntityManager()
				.createNamedStoredProcedureQuery(nomeProcedureChamadaAutotrac);
		
		Integer numIDAreaC = null;
		
		if (  idAreaCliente != null  ) {
			numIDAreaC = idAreaCliente;
		} else {
			 numIDAreaC = checkUsuario.valid().getPessoa().getFuncao().getArea().getId();//Integer.valueOf(this.getNumContratoPesq());
		}
		
		//Integer numIDAreaC = checkUsuario.valid().getPessoa().getFuncao().getArea().getId(); 
		
		queryPvAuto.setParameter("dt_mes", dtMes);
		queryPvAuto.setParameter("id_area"  , numIDAreaC);
		queryPvAuto.execute();
		
		String textAuto = queryPvAuto.getResultList().get(0).toString();
		
		lstRelAutotrac = new ArrayList<AutotracCliente>();
		
		if (!textAuto.equals("[{")){
			
			
			JSONArray arrayJson = new JSONArray(textAuto);
			
			if ( arrayJson != null ){
				
				int listCountArray = arrayJson.length();
				
				for( int i = 0; i < listCountArray ; i++){
					
					JSONObject objAuto = arrayJson.getJSONObject(i);
					
					AutotracCliente auto = new AutotracCliente();

					// mct
					if(!objAuto.isNull("mct")){
						auto.setMct(Integer.valueOf(objAuto.optString("mct")));
					} else {
						auto.setMct(null);
					}
					
					// cavalo
					if(!objAuto.optString("cavalo").toString().equals(null)){
						auto.setCavalo(objAuto.optString("cavalo"));
					}

					// sm
					if(!objAuto.optString("sm").toString().equals(null)){
						auto.setSm(objAuto.optString("sm"));
					}
										
					// valor_ade
					if(!objAuto.optString("valor_ade").toString().equals(null)){
						
						relAutotracValor = new BigDecimal(objAuto.opt("valor_ade").toString());
						
						auto.setValor_ade(relAutotracValor.setScale(2, BigDecimal.ROUND_CEILING));
						
						vlrRelatorioAutotrac = vlrRelatorioAutotrac.add(relAutotracValor.setScale(2, BigDecimal.ROUND_CEILING));
						
						relTotalAutotracValor = relTotalAutotracValor.add(relAutotracValor.setScale(2, BigDecimal.ROUND_CEILING));
						
					}		
					
					// valor_comunicacao
					if(!objAuto.optString("valor_comunicacao").toString().equals(null)){
						
						relAutotracComunicacaoValor = new BigDecimal(objAuto.opt("valor_comunicacao").toString());
						
						auto.setValor_comunicacao(relAutotracComunicacaoValor.setScale(2, BigDecimal.ROUND_CEILING));
						
						vlrRelatorioAutotracComunicacao = vlrRelatorioAutotracComunicacao.add(relAutotracComunicacaoValor.setScale(2, BigDecimal.ROUND_CEILING));
						
						relTotalAutotracValor = relTotalAutotracValor.add(relAutotracComunicacaoValor.setScale(2, BigDecimal.ROUND_CEILING));
						
					}						
					
					auto.setId(i + 1);
					
					lstRelAutotrac.add(auto);
										
				}
				
				
			} else {
				//TODO Nada consta no Json
			}
						
			openDlgRelatorioAutotrac();
						
		}
		
	}
		
	public void openDlgRelatorioAutotrac() {
		options.put("width", "96%");
		options.put("height", "80%");

		RequestContext.getCurrentInstance().openDialog("dlg_relatorioAutotrac", options, null);
		RequestContext.getCurrentInstance().execute("onTop('dlg_relatorioAutotrac')");
	}
	
	
	public void limparVariaveis(){
		
		codContrato = "";
		areaCliente = "";
		pessoaCliente = "";
		nomeCliente = "";
		numeroNfse = "";
		numeroPrevisao = "";
		numeroCiclo = "";
		totalGeral = "";
		vlrViagFixo = "";
		qtdViagFixo = "";
		vlrViagAvulsa = "";
		qtdViagAvulsa = "";
		vlrCheckList = "";
		qtdCheckList = "";
		vlrIscaFixo = "";
		qtdIscaFixo = "";
		vlrIscaAvulsa = "";
		qtdIscaAvulsa = "";
		vlrEscolta = "";
		qtdEscolta = "";
		taxaADE = "";
		consumoSatelital = "";
		vlrControleJornada = "";
		vlrModuloLogistico = "";
		vlrBi = "";
		vlrGerContaCompart = null;
		vlrGerContaDedic = null;
		vlrResultadoSomaGerente = "";
		vlrPaRemota = null;
		vlrResultadoPaRemota = "";
		vlrAdicionais = null;
		vlrResultadoAdicionais = "";
		vlrConsumoViagem = "";
		gerVlrConsumo = null;
		vlrFaturaMinima = "";
		gerVlrFaturamMinima = null;
		vlrTotalOutros = "";
		vlrAjustes = "";
		gerVlrAjustes = null;
		vlrTotalFinal = "";
		diaVencimento = "";
		msgNfseRastreamento = "";
		gerVlrTotalOutros = null;
		gerVlrTotalFinal = null;
		
	}
	
	public void limparVariaveisCadastro(){
		
		cdPrevisao 		= "";
		cdCadastro 		= "";
		cdCiclo    		= "";
		cdTotal	   		= "";
		cdAdicionais	= "";
		cdPessoa   		= "";
		cdTotalFinal 	= "";
		cdFaturaMinima 	= "";
		cdAreaoid		= "";
		cdNome 			= "";
		cdVencimento 	= "";
		cdConsumoFichas = "";
		cdConsulta 		= "";
		cdContrato 		= "";
		cdNotaFiscal 	= "";
		cdRenovacao 	= "";
		cdAjustes 		= "";
		cdTotalOutros 	= "";
		cdCadastroQtd	= "";
		cdConsultaQtd	= "";
		cdRenovacaoQtd 	= "";
		gerCdVlrConsumoFichas 	= null;
		gerCdVlrFaturamMinima   = null;
		gerCdVlrOutros 			= null;
		gerCdVlrAjustes 		= null;
		gerCdVlrTotalFinal 		= null;
		msgNfseCadastro = "";
		
	}
	
	
	// Cliente
	
	public void clienteSelPesq(PessoaQry cli) {
		RequestContext.getCurrentInstance().closeDialog(cli);
	}	
	
	public void clienteSelecionado(SelectEvent event) {
		PessoaQry pessoaqry = (PessoaQry) event.getObject();
		
		nomeClientePesquisado =  "  - " + pessoaqry.getRazao();
		
		String cnpjCheio = pessoaqry.getCnpj();
		String cnpjMask  = "";
		
		cnpjMask =  cnpjCheio.substring(0, 2) + "." + cnpjCheio.substring(2, 5) + "." + cnpjCheio.substring(5, 8) + "/"
					+ cnpjCheio.substring(8, 12) + "-" + cnpjCheio.substring(12, 14);

		cnpjCliente = " CNPJ : " + cnpjMask;
		numCodPessoaSac = pessoaqry.getIdcliente();
		
		idClientePesquisado = pessoaqry.getIdcliente();
		idAreaCliente = pessoaqry.getIdArea();
		
	}
	
	public void openDlgPesqCliente() {
		options.put("width", "70%");
		options.put("height", "60%");

		RequestContext.getCurrentInstance().openDialog("dlg_pesqCliente", options, null);
		RequestContext.getCurrentInstance().execute("onTop('dlg_pesqCliente')");
	}
	
	
	public Virtual getVirtual() {
		return virtual;
	}

	public void setVirtual(Virtual virtual) {
		this.virtual = virtual;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getDtPesquisa() {
		return dtPesquisa;
	}

	public void setDtPesquisa(Date dtPesquisa) {
		this.dtPesquisa = dtPesquisa;
	}

	public String getCodContrato() {
		return codContrato;
	}

	public void setCodContrato(String codContrato) {
		this.codContrato = codContrato;
	}

	public String getAreaCliente() {
		return areaCliente;
	}

	public void setAreaCliente(String areaCliente) {
		this.areaCliente = areaCliente;
	}

	public String getPessoaCliente() {
		return pessoaCliente;
	}

	public void setPessoaCliente(String pessoaCliente) {
		this.pessoaCliente = pessoaCliente;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getNumeroNfse() {
		return numeroNfse;
	}

	public void setNumeroNfse(String numeroNfse) {
		this.numeroNfse = numeroNfse;
	}

	public String getNumeroPrevisao() {
		return numeroPrevisao;
	}

	public void setNumeroPrevisao(String numeroPrevisao) {
		this.numeroPrevisao = numeroPrevisao;
	}

	public String getNumeroCiclo() {
		return numeroCiclo;
	}

	public void setNumeroCiclo(String numeroCiclo) {
		this.numeroCiclo = numeroCiclo;
	}

	public String getTotalGeral() {
		return totalGeral;
	}

	public void setTotalGeral(String totalGeral) {
		this.totalGeral = totalGeral;
	}

	public String getVlrViagFixo() {
		return vlrViagFixo;
	}

	public void setVlrViagFixo(String vlrViagFixo) {
		this.vlrViagFixo = vlrViagFixo;
	}

	public String getQtdViagFixo() {
		return qtdViagFixo;
	}

	public void setQtdViagFixo(String qtdViagFixo) {
		this.qtdViagFixo = qtdViagFixo;
	}

	public String getVlrViagAvulsa() {
		return vlrViagAvulsa;
	}

	public void setVlrViagAvulsa(String vlrViagAvulsa) {
		this.vlrViagAvulsa = vlrViagAvulsa;
	}

	public String getQtdViagAvulsa() {
		return qtdViagAvulsa;
	}

	public void setQtdViagAvulsa(String qtdViagAvulsa) {
		this.qtdViagAvulsa = qtdViagAvulsa;
	}

	public String getVlrCheckList() {
		return vlrCheckList;
	}

	public void setVlrCheckList(String vlrCheckList) {
		this.vlrCheckList = vlrCheckList;
	}

	public String getQtdCheckList() {
		return qtdCheckList;
	}

	public void setQtdCheckList(String qtdCheckList) {
		this.qtdCheckList = qtdCheckList;
	}

	public String getVlrIscaFixo() {
		return vlrIscaFixo;
	}

	public void setVlrIscaFixo(String vlrIscaFixo) {
		this.vlrIscaFixo = vlrIscaFixo;
	}

	public String getQtdIscaFixo() {
		return qtdIscaFixo;
	}

	public void setQtdIscaFixo(String qtdIscaFixo) {
		this.qtdIscaFixo = qtdIscaFixo;
	}

	public String getVlrIscaAvulsa() {
		return vlrIscaAvulsa;
	}

	public void setVlrIscaAvulsa(String vlrIscaAvulsa) {
		this.vlrIscaAvulsa = vlrIscaAvulsa;
	}

	public String getQtdIscaAvulsa() {
		return qtdIscaAvulsa;
	}

	public void setQtdIscaAvulsa(String qtdIscaAvulsa) {
		this.qtdIscaAvulsa = qtdIscaAvulsa;
	}

	public String getVlrEscolta() {
		return vlrEscolta;
	}

	public void setVlrEscolta(String vlrEscolta) {
		this.vlrEscolta = vlrEscolta;
	}

	public String getQtdEscolta() {
		return qtdEscolta;
	}

	public void setQtdEscolta(String qtdEscolta) {
		this.qtdEscolta = qtdEscolta;
	}

	public String getTaxaADE() {
		return taxaADE;
	}

	public void setTaxaADE(String taxaADE) {
		this.taxaADE = taxaADE;
	}

	public String getConsumoSatelital() {
		return consumoSatelital;
	}

	public void setConsumoSatelital(String consumoSatelital) {
		this.consumoSatelital = consumoSatelital;
	}

	public String getVlrControleJornada() {
		return vlrControleJornada;
	}

	public void setVlrControleJornada(String vlrControleJornada) {
		this.vlrControleJornada = vlrControleJornada;
	}

	public String getVlrModuloLogistico() {
		return vlrModuloLogistico;
	}

	public void setVlrModuloLogistico(String vlrModuloLogistico) {
		this.vlrModuloLogistico = vlrModuloLogistico;
	}

	public String getVlrBi() {
		return vlrBi;
	}

	public void setVlrBi(String vlrBi) {
		this.vlrBi = vlrBi;
	}

	public BigDecimal getVlrGerContaCompart() {
		return vlrGerContaCompart;
	}

	public void setVlrGerContaCompart(BigDecimal vlrGerContaCompart) {
		this.vlrGerContaCompart = vlrGerContaCompart;
	}

	public BigDecimal getVlrGerContaDedic() {
		return vlrGerContaDedic;
	}

	public void setVlrGerContaDedic(BigDecimal vlrGerContaDedic) {
		this.vlrGerContaDedic = vlrGerContaDedic;
	}

	

	public BigDecimal getVlrPaRemota() {
		return vlrPaRemota;
	}

	public void setVlrPaRemota(BigDecimal vlrPaRemota) {
		this.vlrPaRemota = vlrPaRemota;
	}

	public BigDecimal getVlrAdicionais() {
		return vlrAdicionais;
	}

	public void setVlrAdicionais(BigDecimal vlrAdicionais) {
		this.vlrAdicionais = vlrAdicionais;
	}

	public String getVlrConsumoViagem() {
		return vlrConsumoViagem;
	}

	public void setVlrConsumoViagem(String vlrConsumoViagem) {
		this.vlrConsumoViagem = vlrConsumoViagem;
	}

	public String getVlrFaturaMinima() {
		return vlrFaturaMinima;
	}

	public void setVlrFaturaMinima(String vlrFaturaMinima) {
		this.vlrFaturaMinima = vlrFaturaMinima;
	}

	public String getVlrTotalOutros() {
		return vlrTotalOutros;
	}

	public void setVlrTotalOutros(String vlrTotalOutros) {
		this.vlrTotalOutros = vlrTotalOutros;
	}

	public String getVlrAjustes() {
		return vlrAjustes;
	}

	public void setVlrAjustes(String vlrAjustes) {
		this.vlrAjustes = vlrAjustes;
	}

	public String getVlrTotalFinal() {
		return vlrTotalFinal;
	}

	public void setVlrTotalFinal(String vlrTotalFinal) {
		this.vlrTotalFinal = vlrTotalFinal;
	}

	public String getDiaVencimento() {
		return diaVencimento;
	}

	public void setDiaVencimento(String diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	public String getVlrResultadoSomaGerente() {
		return vlrResultadoSomaGerente;
	}

	public void setVlrResultadoSomaGerente(String vlrResultadoSomaGerente) {
		this.vlrResultadoSomaGerente = vlrResultadoSomaGerente;
	}

	public String getVlrResultadoPaRemota() {
		return vlrResultadoPaRemota;
	}

	public void setVlrResultadoPaRemota(String vlrResultadoPaRemota) {
		this.vlrResultadoPaRemota = vlrResultadoPaRemota;
	}

	public String getVlrResultadoAdicionais() {
		return vlrResultadoAdicionais;
	}

	public void setVlrResultadoAdicionais(String vlrResultadoAdicionais) {
		this.vlrResultadoAdicionais = vlrResultadoAdicionais;
	}

	public boolean isLiberadoServicosAdicionais() {
		return liberadoServicosAdicionais;
	}

	public void setLiberadoServicosAdicionais(boolean liberadoServicosAdicionais) {
		this.liberadoServicosAdicionais = liberadoServicosAdicionais;
	}

	public boolean isLiberadoServicosAdicionaisGerente() {
		return liberadoServicosAdicionaisGerente;
	}

	public void setLiberadoServicosAdicionaisGerente(boolean liberadoServicosAdicionaisGerente) {
		this.liberadoServicosAdicionaisGerente = liberadoServicosAdicionaisGerente;
	}

	public Integer getColspanServicosAdicionais() {
		return colspanServicosAdicionais;
	}

	public void setColspanServicosAdicionais(Integer colspanServicosAdicionais) {
		this.colspanServicosAdicionais = colspanServicosAdicionais;
	}

	public Integer getNumContratoPesq() {
		return numContratoPesq;
	}

	public void setNumContratoPesq(Integer numContratoPesq) {
		this.numContratoPesq = numContratoPesq;
	}

	public boolean isLiberadoServicosAdicionaisPARemota() {
		return liberadoServicosAdicionaisPARemota;
	}

	public void setLiberadoServicosAdicionaisPARemota(boolean liberadoServicosAdicionaisPARemota) {
		this.liberadoServicosAdicionaisPARemota = liberadoServicosAdicionaisPARemota;
	}

	public boolean isLiberadoServicosAdicionaisAdicionais() {
		return liberadoServicosAdicionaisAdicionais;
	}

	public void setLiberadoServicosAdicionaisAdicionais(boolean liberadoServicosAdicionaisAdicionais) {
		this.liberadoServicosAdicionaisAdicionais = liberadoServicosAdicionaisAdicionais;
	}

	public BigDecimal getGerVlrConsumo() {
		return gerVlrConsumo;
	}

	public void setGerVlrConsumo(BigDecimal gerVlrConsumo) {
		this.gerVlrConsumo = gerVlrConsumo;
	}

	public BigDecimal getGerVlrFaturamMinima() {
		return gerVlrFaturamMinima;
	}

	public void setGerVlrFaturamMinima(BigDecimal gerVlrFaturamMinima) {
		this.gerVlrFaturamMinima = gerVlrFaturamMinima;
	}

	public boolean isLiberadoResumoVlrConsumo() {
		return liberadoResumoVlrConsumo;
	}

	public void setLiberadoResumoVlrConsumo(boolean liberadoResumoVlrConsumo) {
		this.liberadoResumoVlrConsumo = liberadoResumoVlrConsumo;
	}

	public boolean isLiberadoResumoVlrFaturaMinima() {
		return liberadoResumoVlrFaturaMinima;
	}

	public void setLiberadoResumoVlrFaturaMinima(boolean liberadoResumoVlrFaturaMinima) {
		this.liberadoResumoVlrFaturaMinima = liberadoResumoVlrFaturaMinima;
	}

	public BigDecimal getGerVlrAjustes() {
		return gerVlrAjustes;
	}

	public void setGerVlrAjustes(BigDecimal gerVlrAjustes) {
		this.gerVlrAjustes = gerVlrAjustes;
	}

	public boolean isLiberadoResumoVlrAjustes() {
		return liberadoResumoVlrAjustes;
	}

	public void setLiberadoResumoVlrAjustes(boolean liberadoResumoVlrAjustes) {
		this.liberadoResumoVlrAjustes = liberadoResumoVlrAjustes;
	}

	public Integer getColspanServicosResumo() {
		return colspanServicosResumo;
	}

	public void setColspanServicosResumo(Integer colspanServicosResumo) {
		this.colspanServicosResumo = colspanServicosResumo;
	}

	public String getCdPrevisao() {
		return cdPrevisao;
	}

	public void setCdPrevisao(String cdPrevisao) {
		this.cdPrevisao = cdPrevisao;
	}

	public String getCdCadastro() {
		return cdCadastro;
	}

	public void setCdCadastro(String cdCadastro) {
		this.cdCadastro = cdCadastro;
	}

	public String getCdCiclo() {
		return cdCiclo;
	}

	public void setCdCiclo(String cdCiclo) {
		this.cdCiclo = cdCiclo;
	}

	public String getCdTotal() {
		return cdTotal;
	}

	public void setCdTotal(String cdTotal) {
		this.cdTotal = cdTotal;
	}

	public String getCdAdicionais() {
		return cdAdicionais;
	}

	public void setCdAdicionais(String cdAdicionais) {
		this.cdAdicionais = cdAdicionais;
	}

	public String getCdPessoa() {
		return cdPessoa;
	}

	public void setCdPessoa(String cdPessoa) {
		this.cdPessoa = cdPessoa;
	}

	public String getCdTotalFinal() {
		return cdTotalFinal;
	}

	public void setCdTotalFinal(String cdTotalFinal) {
		this.cdTotalFinal = cdTotalFinal;
	}

	public String getCdFaturaMinima() {
		return cdFaturaMinima;
	}

	public void setCdFaturaMinima(String cdFaturaMinima) {
		this.cdFaturaMinima = cdFaturaMinima;
	}

	public String getCdAreaoid() {
		return cdAreaoid;
	}

	public void setCdAreaoid(String cdAreaoid) {
		this.cdAreaoid = cdAreaoid;
	}

	public String getCdNome() {
		return cdNome;
	}

	public void setCdNome(String cdNome) {
		this.cdNome = cdNome;
	}

	public String getCdVencimento() {
		return cdVencimento;
	}

	public void setCdVencimento(String cdVencimento) {
		this.cdVencimento = cdVencimento;
	}

	public String getCdConsumoFichas() {
		return cdConsumoFichas;
	}

	public void setCdConsumoFichas(String cdConsumoFichas) {
		this.cdConsumoFichas = cdConsumoFichas;
	}

	public String getCdConsulta() {
		return cdConsulta;
	}

	public void setCdConsulta(String cdConsulta) {
		this.cdConsulta = cdConsulta;
	}

	public String getCdContrato() {
		return cdContrato;
	}

	public void setCdContrato(String cdContrato) {
		this.cdContrato = cdContrato;
	}

	public String getCdNotaFiscal() {
		return cdNotaFiscal;
	}

	public void setCdNotaFiscal(String cdNotaFiscal) {
		this.cdNotaFiscal = cdNotaFiscal;
	}

	public String getCdRenovacao() {
		return cdRenovacao;
	}

	public void setCdRenovacao(String cdRenovacao) {
		this.cdRenovacao = cdRenovacao;
	}

	public String getCdAjustes() {
		return cdAjustes;
	}

	public void setCdAjustes(String cdAjustes) {
		this.cdAjustes = cdAjustes;
	}

	public String getCdTotalOutros() {
		return cdTotalOutros;
	}

	public void setCdTotalOutros(String cdTotalOutros) {
		this.cdTotalOutros = cdTotalOutros;
	}

	public String getCdCadastroQtd() {
		return cdCadastroQtd;
	}

	public void setCdCadastroQtd(String cdCadastroQtd) {
		this.cdCadastroQtd = cdCadastroQtd;
	}

	public String getCdConsultaQtd() {
		return cdConsultaQtd;
	}

	public void setCdConsultaQtd(String cdConsultaQtd) {
		this.cdConsultaQtd = cdConsultaQtd;
	}

	public String getCdRenovacaoQtd() {
		return cdRenovacaoQtd;
	}

	public void setCdRenovacaoQtd(String cdRenovacaoQtd) {
		this.cdRenovacaoQtd = cdRenovacaoQtd;
	}

	public BigDecimal getGerCdVlrConsumoFichas() {
		return gerCdVlrConsumoFichas;
	}

	public void setGerCdVlrConsumoFichas(BigDecimal gerCdVlrConsumoFichas) {
		this.gerCdVlrConsumoFichas = gerCdVlrConsumoFichas;
	}

	public BigDecimal getGerCdVlrFaturamMinima() {
		return gerCdVlrFaturamMinima;
	}

	public void setGerCdVlrFaturamMinima(BigDecimal gerCdVlrFaturamMinima) {
		this.gerCdVlrFaturamMinima = gerCdVlrFaturamMinima;
	}

	public BigDecimal getGerCdVlrOutros() {
		return gerCdVlrOutros;
	}

	public void setGerCdVlrOutros(BigDecimal gerCdVlrOutros) {
		this.gerCdVlrOutros = gerCdVlrOutros;
	}

	public BigDecimal getGerCdVlrAjustes() {
		return gerCdVlrAjustes;
	}

	public void setGerCdVlrAjustes(BigDecimal gerCdVlrAjustes) {
		this.gerCdVlrAjustes = gerCdVlrAjustes;
	}

	public BigDecimal getGerCdVlrTotalFinal() {
		return gerCdVlrTotalFinal;
	}

	public void setGerCdVlrTotalFinal(BigDecimal gerCdVlrTotalFinal) {
		this.gerCdVlrTotalFinal = gerCdVlrTotalFinal;
	}

	public Integer getColspanServicosCadResumo() {
		return colspanServicosCadResumo;
	}

	public void setColspanServicosCadResumo(Integer colspanServicosCadResumo) {
		this.colspanServicosCadResumo = colspanServicosCadResumo;
	}

	public boolean isLiberadoResumoCadVlrConsumoFichas() {
		return liberadoResumoCadVlrConsumoFichas;
	}

	public void setLiberadoResumoCadVlrConsumoFichas(boolean liberadoResumoCadVlrConsumoFichas) {
		this.liberadoResumoCadVlrConsumoFichas = liberadoResumoCadVlrConsumoFichas;
	}

	public boolean isLiberadoResumoCadVlrFaturaMinima() {
		return liberadoResumoCadVlrFaturaMinima;
	}

	public void setLiberadoResumoCadVlrFaturaMinima(boolean liberadoResumoCadVlrFaturaMinima) {
		this.liberadoResumoCadVlrFaturaMinima = liberadoResumoCadVlrFaturaMinima;
	}

	public boolean isLiberadoResumoCadVlrOutros() {
		return liberadoResumoCadVlrOutros;
	}

	public void setLiberadoResumoCadVlrOutros(boolean liberadoResumoCadVlrOutros) {
		this.liberadoResumoCadVlrOutros = liberadoResumoCadVlrOutros;
	}

	public boolean isLiberadoResumoCadVlrAjustes() {
		return liberadoResumoCadVlrAjustes;
	}

	public void setLiberadoResumoCadVlrAjustes(boolean liberadoResumoCadVlrAjustes) {
		this.liberadoResumoCadVlrAjustes = liberadoResumoCadVlrAjustes;
	}

	public String getDtPesqMesAno() {
		return dtPesqMesAno;
	}

	public void setDtPesqMesAno(String dtPesqMesAno) {
		this.dtPesqMesAno = dtPesqMesAno;
	}

	public String getMsgNfseRastreamento() {
		return msgNfseRastreamento;
	}

	public void setMsgNfseRastreamento(String msgNfseRastreamento) {
		this.msgNfseRastreamento = msgNfseRastreamento;
	}

	public String getMsgNfseCadastro() {
		return msgNfseCadastro;
	}

	public void setMsgNfseCadastro(String msgNfseCadastro) {
		this.msgNfseCadastro = msgNfseCadastro;
	}

	public BigDecimal getGerVlrTotalOutros() {
		return gerVlrTotalOutros;
	}

	public void setGerVlrTotalOutros(BigDecimal gerVlrTotalOutros) {
		this.gerVlrTotalOutros = gerVlrTotalOutros;
	}

	public BigDecimal getGerVlrTotalFinal() {
		return gerVlrTotalFinal;
	}

	public void setGerVlrTotalFinal(BigDecimal gerVlrTotalFinal) {
		this.gerVlrTotalFinal = gerVlrTotalFinal;
	}

	public BigDecimal getRelCadValor() {
		return relCadValor;
	}

	public void setRelCadValor(BigDecimal relCadValor) {
		this.relCadValor = relCadValor;
	}

	public ArrayList<CadastroCliente> getLstRelCadastro() {
		return lstRelCadastro;
	}

	public void setLstRelCadastro(ArrayList<CadastroCliente> lstRelCadastro) {
		this.lstRelCadastro = lstRelCadastro;
	}

	public BigDecimal getVlrRelatorioCadastro() {
		return vlrRelatorioCadastro;
	}

	public void setVlrRelatorioCadastro(BigDecimal vlrRelatorioCadastro) {
		this.vlrRelatorioCadastro = vlrRelatorioCadastro;
	}

	public BigDecimal getRelViagemValor() {
		return relViagemValor;
	}

	public void setRelViagemValor(BigDecimal relViagemValor) {
		this.relViagemValor = relViagemValor;
	}

	public BigDecimal getVlrRelatorioViagem() {
		return vlrRelatorioViagem;
	}

	public void setVlrRelatorioViagem(BigDecimal vlrRelatorioViagem) {
		this.vlrRelatorioViagem = vlrRelatorioViagem;
	}

	public ArrayList<ViagemCliente> getLstRelViagem() {
		return lstRelViagem;
	}

	public void setLstRelViagem(ArrayList<ViagemCliente> lstRelViagem) {
		this.lstRelViagem = lstRelViagem;
	}

	public BigDecimal getRelCheckListValor() {
		return relCheckListValor;
	}

	public void setRelCheckListValor(BigDecimal relCheckListValor) {
		this.relCheckListValor = relCheckListValor;
	}

	public BigDecimal getVlrRelatorioCheckList() {
		return vlrRelatorioCheckList;
	}

	public void setVlrRelatorioCheckList(BigDecimal vlrRelatorioCheckList) {
		this.vlrRelatorioCheckList = vlrRelatorioCheckList;
	}

	public ArrayList<CheckListCliente> getLstRelCheckList() {
		return lstRelCheckList;
	}

	public void setLstRelCheckList(ArrayList<CheckListCliente> lstRelCheckList) {
		this.lstRelCheckList = lstRelCheckList;
	}

	public BigDecimal getRelAutotracValor() {
		return relAutotracValor;
	}

	public void setRelAutotracValor(BigDecimal relAutotracValor) {
		this.relAutotracValor = relAutotracValor;
	}

	public BigDecimal getVlrRelatorioAutotrac() {
		return vlrRelatorioAutotrac;
	}

	public void setVlrRelatorioAutotrac(BigDecimal vlrRelatorioAutotrac) {
		this.vlrRelatorioAutotrac = vlrRelatorioAutotrac;
	}

	public ArrayList<AutotracCliente> getLstRelAutotrac() {
		return lstRelAutotrac;
	}

	public void setLstRelAutotrac(ArrayList<AutotracCliente> lstRelAutotrac) {
		this.lstRelAutotrac = lstRelAutotrac;
	}

	public BigDecimal getRelAutotracComunicacaoValor() {
		return relAutotracComunicacaoValor;
	}

	public void setRelAutotracComunicacaoValor(BigDecimal relAutotracComunicacaoValor) {
		this.relAutotracComunicacaoValor = relAutotracComunicacaoValor;
	}

	public BigDecimal getRelTotalAutotracValor() {
		return relTotalAutotracValor;
	}

	public void setRelTotalAutotracValor(BigDecimal relTotalAutotracValor) {
		this.relTotalAutotracValor = relTotalAutotracValor;
	}

	public BigDecimal getVlrRelatorioAutotracComunicacao() {
		return vlrRelatorioAutotracComunicacao;
	}

	public void setVlrRelatorioAutotracComunicacao(BigDecimal vlrRelatorioAutotracComunicacao) {
		this.vlrRelatorioAutotracComunicacao = vlrRelatorioAutotracComunicacao;
	}

	public boolean isLiberadoBotoesRastreamento() {
		return liberadoBotoesRastreamento;
	}

	public void setLiberadoBotoesRastreamento(boolean liberadoBotoesRastreamento) {
		this.liberadoBotoesRastreamento = liberadoBotoesRastreamento;
	}

	public boolean isLiberadoBotoesCadastro() {
		return liberadoBotoesCadastro;
	}

	public void setLiberadoBotoesCadastro(boolean liberadoBotoesCadastro) {
		this.liberadoBotoesCadastro = liberadoBotoesCadastro;
	}

	public String getCnpjCliente() {
		return cnpjCliente;
	}

	public void setCnpjCliente(String cnpjCliente) {
		this.cnpjCliente = cnpjCliente;
	}

	public Integer getNumCodPessoaSac() {
		return numCodPessoaSac;
	}

	public void setNumCodPessoaSac(Integer numCodPessoaSac) {
		this.numCodPessoaSac = numCodPessoaSac;
	}

	public Integer getIdClientePesquisado() {
		return idClientePesquisado;
	}

	public void setIdClientePesquisado(Integer idClientePesquisado) {
		this.idClientePesquisado = idClientePesquisado;
	}

	public String getNomeClientePesquisado() {
		return nomeClientePesquisado;
	}

	public void setNomeClientePesquisado(String nomeClientePesquisado) {
		this.nomeClientePesquisado = nomeClientePesquisado;
	}
	

}
