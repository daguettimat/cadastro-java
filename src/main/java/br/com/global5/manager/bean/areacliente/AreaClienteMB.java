package br.com.global5.manager.bean.areacliente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.areacliente.Virtual;
import br.com.global5.manager.model.geral.Usuario;
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

	@Inject
	VirtualService virtualService;
	
	@Inject
	private Crud<Virtual> virtualCrud;

	@PostConstruct
	public void init() {

		clear();

	}
	
	public void clear() {
		virtual = new Virtual();
		id = null;
		dtPesqMesAno = "";
		msgNfseCadastro = "";
		msgNfseRastreamento = "";
		usuario = checkUsuario.valid();
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

        
		StoredProcedureQuery queryPv = virtualCrud.getEntityManager()
				.createNamedStoredProcedureQuery(nomeProcedureChamada);
		
		Integer numIDArea = checkUsuario.valid().getPessoa().getFuncao().getArea().getId();//Integer.valueOf(this.getNumContratoPesq());
		
		queryPv.setParameter("dt_mes", this.getDtPesquisa());
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
				
				Integer contaColspanServAdic = 1;
				
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
					resultContaDedic   = vlrGerContaCompart.compareTo(BigDecimal.ZERO);
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
					contaColspanServAdic = 0;
				} else {
					
					this.setLiberadoServicosAdicionais(false);
					contaColspanServAdic = 0;
					
				}
				
				
				
			// Resumo Rastreamento				
				
				Integer contaColspanResumo = 4;
				
				String gerConsumoViagem = obj.opt("consumo_viagens").toString();
				String gerFaturaMinima  = obj.opt("fat_minima").toString();						
				
				gerVlrConsumo 			= new BigDecimal(gerConsumoViagem);
				gerVlrFaturamMinima   	= new BigDecimal(gerFaturaMinima);			  
						
				int resultVlrConsumoVlrFaturaMinima 	= gerVlrConsumo.compareTo(gerVlrFaturamMinima);
				
				if ( resultVlrConsumoVlrFaturaMinima == 0 ) {
					
					vlrConsumoViagem	=	obj.opt("consumo_viagens").toString();
					vlrFaturaMinima		=	obj.opt("fat_minima").toString();
		
					this.setLiberadoResumoVlrConsumo(true);
					this.setLiberadoResumoVlrFaturaMinima(true);
					
					contaColspanResumo++;
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
				
				contaColspanResumo++;
				contaColspanResumo++;
				contaColspanResumo++;
				
				this.setColspanServicosResumo(contaColspanResumo);
			
				gerVlrTotalOutros		=	new BigDecimal(obj.opt("total_outros").toString());		
				gerVlrTotalFinal		=	new BigDecimal(obj.opt("total_final").toString());	
				diaVencimento			= 	obj.opt("vencimento").toString();
			
				RequestContext context = RequestContext.getCurrentInstance();
				context.update("formRelatorioCliente:tbView:tbView01");
							
			
		} else {
			limparVariaveis();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Não há apuração para esse período selecionado para Rastreamento!"));
		}
		
		// Processo do Faturamento Cadastro
		String nomeProcedureChamadaCadastro = "usuario_mensal_cadastro";
        
		StoredProcedureQuery queryPvc = virtualCrud.getEntityManager()
				.createNamedStoredProcedureQuery(nomeProcedureChamadaCadastro);
		
		Integer numIDAreaC = checkUsuario.valid().getPessoa().getFuncao().getArea().getId(); //Integer.valueOf(this.getNumContratoPesq());
		
		queryPvc.setParameter("dt_mes", this.getDtPesquisa());
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
				
				//vlrTotalOutros		=	obj.opt("total_outros").toString();		
				//vlrTotalFinal		=	obj.opt("total_final").toString();	
				//diaVencimento		= 	obj.opt("vencimento").toString();				
				
				//cdConsumoFichas = obj.opt("consumo_fichas").toString();				
				//cdFaturaMinima  = obj.opt("fat_minima").toString();
				//cdAdicionais  	= obj.opt("adicionais").toString();
				//cdAjustes 		= obj.opt("ajustes").toString();
				//cdTotalFinal	= obj.opt("total_final").toString();
				//cdVencimento	= obj.opt("vencimento").toString();
				
				Integer contaColspanResumo = 0;
				
				String gerConsumoFichas = obj.opt("consumo_fichas").toString();
				String gerFaturaMinima  = obj.opt("fat_minima").toString();						
				
				gerCdVlrConsumoFichas	= new BigDecimal(gerConsumoFichas);
				gerCdVlrFaturamMinima   = new BigDecimal(gerFaturaMinima);			  
						
				int resultVlrConsumoFichasVlrFaturaMinima 	= gerCdVlrConsumoFichas.compareTo(gerCdVlrFaturamMinima);
				
				if ( resultVlrConsumoFichasVlrFaturaMinima == 0 ) {
					
					cdConsumoFichas = obj.opt("consumo_fichas").toString();				
					cdFaturaMinima  = obj.opt("fat_minima").toString();					
					
					contaColspanResumo++;
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
						}
												
				
				this.setColspanServicosCadResumo(contaColspanResumo);
				
				
				
				RequestContext context = RequestContext.getCurrentInstance();
				context.update("formRelatorioCliente:tbView:tbView02");
				
				
		} else {
			limparVariaveisCadastro();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Não há apuração para esse período selecionado para Cadastro!"));
		}

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
		
	
}
