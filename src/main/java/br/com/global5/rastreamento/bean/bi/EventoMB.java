package br.com.global5.rastreamento.bean.bi;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.io.FilenameUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.areas.AreaPorPessoaDocumento;
import br.com.global5.manager.model.areas.AreaQry;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.rastreamento.bean.ws.ViagemMB;
import br.com.global5.rastreamento.infra.ViagemClient;
import br.com.global5.rastreamento.model.bi.BoletimInformativo;
import br.com.global5.rastreamento.model.bi.BoletimInformativoNumeracao;
import br.com.global5.rastreamento.model.bi.CheckBI;
import br.com.global5.rastreamento.model.bi.Evento;
import br.com.global5.rastreamento.model.bi.EventoAnexo;
import br.com.global5.rastreamento.model.bi.EventoInfoViagem;
import br.com.global5.rastreamento.model.enums.TipoAnexo;
import br.com.global5.rastreamento.model.enums.TipoSuspeita;
import br.com.global5.rastreamento.model.trafegus.EventoInfoRastreador;
import br.com.global5.rastreamento.model.trafegus.VersaoTecnologia;
import br.com.global5.rastreamento.service.bi.BoletimInformativoNumeracaoService;
import br.com.global5.rastreamento.service.bi.BoletimInformativoService;
import br.com.global5.rastreamento.service.bi.EventoAnexoService;
import br.com.global5.rastreamento.service.bi.EventoInfoRastreadorService;
import br.com.global5.rastreamento.service.bi.EventoInfoViagemService;
import br.com.global5.rastreamento.service.bi.EventoService;
import br.com.global5.rastreamento.service.bi.TipoAnexoService;
import br.com.global5.rastreamento.service.bi.TipoSuspeitaService;
import br.com.global5.template.exception.BusinessException;
import net.bootsfaces.render.E;

/**
 * 
 * @author francis
 * @date 2019-03-19 
 * 
 */

@Named
@ViewAccessScoped
public class EventoMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Evento ev;
	private EventoInfoViagem eiv;
	
	private EventoAnexo eva;
	
	private CheckBI checkBi;
	
	private Usuario usuario;
	
	private Integer id;
	private Integer idEiv;
	private Integer idBi;
	
	private List<Evento> listEventos;
	private List<TipoSuspeita> lstTipoSuspeita;
	private List<BoletimInformativo> listBoletimPorEvento;
	private List<EventoAnexo> listEventoAnexo;
	private List<TipoAnexo> lstTipoAnexo;
	private List<AreaPorPessoaDocumento> lstAreaPorPessoaDoc;
	// Emails Global5
	private List<String>lstEmailGlobal5;
	private String addEmailGlobal5 = "";
	private JSONArray arrayJsonGlobal5;
	private Integer nivel2 = null;
	//private String contaEmailMultiploNfse = "";
	private String contaEmailMultiploGlobal5 = "";
	String emailListGlobal5="";
	
	private Character tipoSelecaoBi = null;
	private String    eventoInicial = "";
	private Integer   numeroSM		= null;
	private Integer   numIdEvento= null;
	private String    statusTabelaEvento;
	
	// Var Consulta SM
	private String nomeClienteChp = "";
	private String cnpjClienteChp = "";
	private String nomeMotorista1Chp = "";
	private String cpfMotorista1Chp = "";
	private String vinculoMotorista1Chp = "";
	private String nomeMotorista2Chp = "";
	private String cpfMotorista2Chp = "";
	private String vinculoMotorista2Chp = "";
	private String placaCavaloChp = "";
	private String corCavaloChp = "";
	private String marcaCavaloChp = "";	
	private String modeloCavaloChp = "";
	private String anoCavaloChp = "";
	private String placaReboque1Chp = "";
	private String corReboque1Chp = "";
	private String modeloReboque1Chp = "";
	private String placaReboque2Chp = "";
	private String corReboque2Chp = "";
	private String modeloReboque2Chp = "";
	private String placaReboque3Chp = "";
	private String corReboque3Chp = "";
	private String modeloReboque3Chp = "";	
	private boolean chpNaoRetornaInformacao = false;
	private List<AreaQry> lstAreaQry;
	
	// Var pesquisar
	private boolean ativarPesquisarEventosPorCriterios = false;
	
	// Var Cancelamento
	private String cancelaRetorno = "";
	private String nomeClienteCancel = "";
	private Integer idEventoCancel = null;
	private Integer smViagemCancel = null;
	
	private String emailsParaEnvioBIRastreamento="";
	
	private boolean painelDadosSinistro 		 = false;
	private boolean painelDadosSuspeita 		 = false;
	
	private boolean painelClassSuspeitaRoubo 	 = false;
	private boolean painelClassSinistroDano 	 = false;
	private boolean painelClassSinistroPerda 	 = false;
	private boolean painelClassSinistroPerdaDano = false;
	
	
	private boolean habilitarBtnGerarEvento = false;
	private boolean habilitarBtnAbrirGerarBoletim = false;
	
	private boolean habilitaDivBoletimFinal		 = false; 
	
	private boolean eventoAdicionado = false;
	private boolean boletimFinal = false;
	
	private int anoVigente;
	private int qtdBiPerdas = 0;
	private int qtdBiDanos = 0;
	private int qtdBiSuspeitas = 0;
	
	private String nomeCliente = "";
	private String placaPesq = "";
	private String motoristaPesq = "";
	private boolean eventoFinalizado = false;
	private Date   pesqDtInicio = null;
	private Date   pesqDtFinal  = null;
	private char tipoEventoPesq = 'T';
	private char tipoEventoFinalPesq = 'E';
	
	private String msgNomeTransportadorChp;
	private String msgCnpjTransportadorChp;
	
	private Map<String, Object> options;
	
	// Anexo
	private Integer idTipoAnexo = null;
	private String  descArquivoAnexo = "";
	private String  descArquivoAnexoFront = "";
	private String  msgArquivoUpload = "";
	private List<UploadedFile> uploadedFiles;
	private boolean btnRegistrarAnexoLiberado = false;
	private String  arquivoTipoExtensao = "";
	private String  arquivoNome = "";
	
	@Inject
	EventoService evService;
	
	@Inject
	EventoInfoViagemService evInfoViagService;

	@Inject
	private EventoInfoRastreadorService evInfoRastreadorService;
	
	@Inject
	EventoAnexoService eventoAnexoService;
	
	@Inject
	BoletimInformativoService biService;
	
	@Inject
	BoletimInformativoNumeracaoService biNumService;
	
	@Inject
	TipoSuspeitaService tipoSuspService;
	
	@Inject
	TipoAnexoService    TipoAnexoService;
	
	@Inject
	private Crud<Evento> eventoCrud;
	
	@Inject
	private Crud<BoletimInformativoNumeracao> boletimNumeracaoCrud;
	
	@PostConstruct
	public void init(){
		
		clear();		
				
		// Atribuição Ano Atual, para contadores de boletim	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		anoVigente = calendar.get(Calendar.YEAR);   
		
		consultarNumeracaoBI();
		
	}

	private void clear() {
		
		id = null;
		idEiv = null;
		idBi = null;		
		
		ev = new Evento();
		//ev.setBoletimInformativo(new BoletimInformativo());
		
		lstTipoSuspeita = tipoSuspService.crud().isNull("exclusao").listAll();
		lstTipoAnexo = TipoAnexoService.crud().isNull("exclusao").listAll();
		 
		uploadedFiles = new ArrayList<UploadedFile>();
		descArquivoAnexo = "";
		
		lstEmailGlobal5 = new ArrayList<String>();
		arrayJsonGlobal5 = new JSONArray();
		
		//tipoEventoPesq = "D";
		
	}
	
	public void insert(){
		try {
			clear();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					" Inserir Eventos não pode ser carregado. Informe ao suporte técnico. ", null));
		}
	}
	
	public void findById(Integer id){
		
		if( id == null ){
			throw new BusinessException("O id do evento não foi encontrado. Contate o suporte TI");
		}
		
		ev = evService.crud().get(id);
		eiv = evInfoViagService.crud().get(id);

		Hibernate.initialize(ev);
		Hibernate.initialize(eiv);
		
	}
	
	public void findByIdEv(Integer id){
		
		if( id == null ){
			throw new BusinessException("O id do evento não foi encontrado. Contate o suporte TI");
		}
		
		ev = evService.crud().get(id);
		eiv = evInfoViagService.crud().get(id);
		
		Hibernate.initialize(ev);
		Hibernate.initialize(eiv);
		
	}
	
	public void eventoSelecionadoEmissaoBoletim(Integer idEvento ){

		numIdEvento = idEvento;
			tipoSelecaoBi = null;
			habilitarBtnAbrirGerarBoletim = false;
			this.id = idEvento;
			ev = evService.crud().get(idEvento);
			
			if( ev.getBoletimInformativo() == null) {
				checkBi = CheckBI.getInstancia();
				checkBi.recebeIdBI(null);
				checkBi.recebeIdEv(idEvento);				
				this.setHabilitaDivBoletimFinal(false);
				//RequestContext context = RequestContext.getCurrentInstance();
				//context.update("formListaEventos:dlgAddBoletim");
			} else {

				if (ev.getBoletimInformativo().getId() != null) {
					// Chamada da Singleton CheckBI para reservar de variaveis
					checkBi = CheckBI.getInstancia();
					checkBi.recebeIdEv(idEvento);
					checkBi.recebeIdBI(ev.getBoletimInformativo().getId());
					checkBi.recebeUltimoTipoBoletimClone(ev.getBoletimInformativo().getTipo());
					this.setHabilitaDivBoletimFinal(true);					
					RequestContext context = RequestContext.getCurrentInstance();
					context.update("formListaEventos:dlgAddBoletim");
				}				
			}				
	}
	
	
	public void eventoSelecionadoViewBoletim(Integer idEvento ){

		numIdEvento = idEvento;
		tipoSelecaoBi = null;
		this.id = idEvento;
		ev = evService.crud().get(getId());

	}
	
	public void listarEventosAberto(){
		try{
									
			//init();
			//numIdEvento = null;
			limparCamposPesquisa();
			statusTabelaEvento = " Abertos ";
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Eventos", "Esses são os eventos em aberto."));
			this.getListEventos();
			RequestContext context = RequestContext.getCurrentInstance();
			context.update(":formListaEventos:tbListEvento");
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("../bi/bilst.xhtml");
			
		}catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção","Nenhum evento em aberto disponível para ser apresentado."));
		}
	}
	
	/**
	 * @author Francis Bueno
	 * 
	 */
	
	@SuppressWarnings("deprecation")
	public void checarSmParaAbrirEvento(){
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		Integer numSMDigitado = numeroSM;

		// Verificar a existência da SM registrada na tabela de evento
		Criteria criteria = evService.crud().getSession().createCriteria(Evento.class);
		criteria.add(Restrictions.eq("sincViagem", numSMDigitado));
		criteria.add(Restrictions.isNull("dtFinalizacao"));
		criteria.add(Restrictions.isNull("dtCancelamento"));
		
		int result = criteria.list().size();
		
		try {
			// Busca dados da SM para mostrar ao usuario
			buscarViagemBIArea(numSMDigitado);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Se encontrou	SM no Evento em aberto ...
		if (result > 0) {

			Evento evCheck = (Evento) criteria.uniqueResult();
			
			// Por causa da retirada da Classe SincViagem transformando em Integer
			//if (numSMDigitado == evCheck.getSincViagem().getId().intValue()){ 
			
			if (numSMDigitado == evCheck.getSincViagem()){
				
				// verificar se a SM está aberta
				if ( evCheck.getDtFinalizacao() == null ){
					
					findById(evCheck.getId());
					numSMDigitado = null;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção", "Consta um evento aberto para está SM informada!"));
					
				} else {
					
					RequestContext context = RequestContext.getCurrentInstance();
					habilitarBtnGerarEvento = true;				
					context.update("formListaEventos:btnAddEvento");	
				}
			} else {
				RequestContext context = RequestContext.getCurrentInstance();
				habilitarBtnGerarEvento = false;				
				context.update("formListaEventos:btnAddEvento");	
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção", "Consta um evento aberto para está SM informada!"));
				return;
			}
			
			// Para número de SM não existente ou com status não permitido para abertura de evento será tratada na chamada da procedure
					
		} 
			else
			
				// Se não encontrado SM aberto (isto é está fechada) será liberado o botão para gerar o Evento ,
				// Temos que colocar uma boolean que checa se veio algo ou não do ws para apos entrar nessa condição
			{
				if( chpNaoRetornaInformacao != true){
					RequestContext context = RequestContext.getCurrentInstance();
					habilitarBtnGerarEvento = true;				
					context.update("formListaEventos:pnDdSMS");
					context.update("formListaEventos:btnAddEvento");									
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Sistema Trafegus/CHP não está retornado resposta da SM informada!"));
					chpNaoRetornaInformacao = false;
					
					RequestContext context = RequestContext.getCurrentInstance();
								
					context.update("formListaEventos:growlAddEvento");
				}

		}
		
	}
	
	private int salvarEvento(int idArea){
		
		try {
			// Se idArea veio preenchida
			if(idArea != 0) {

				Criteria checarNumMaxEvento = evService.crud().getSession().createCriteria(Evento.class);
				String a ="b";
				//Contador do checarNumMaxEvento					
				int resultChecar = checarNumMaxEvento.list().size();
				
				// Apropria o numero do evento
				if(resultChecar == 0){
					//Se = 0 Então = 1
					ev.setNrEvento(1);
				} else {
					//Se != 0 Então soma 1 ao contador
					ev.setNrEvento(resultChecar + 1);
				}
				
				//Adicionar a variavel o valor do nrEvento 
				numIdEvento = ev.getNrEvento();
				// habilita que o evento foi adicionado
				eventoAdicionado = true;
				habilitarBtnGerarEvento = false;	
				
				// chama a lista de eventos
				this.getListEventos();

				// busca as contas de emails na area
				Area areaEmail = areaService.findById(idArea);
				String emailGlobal5 = "filipe.aira@global5.com.br;alfredo.peracetta@global5.com.br";
				
				Evento nev = new Evento();
				
				// Preparação para a Persistência
				ev.setSincViagem(numeroSM);
				ev.setNrBoletim(0);
				ev.setNrEvento(numIdEvento);
				ev.setArea(new Area(idArea));
				ev.setEmails(emailsParaEnvioBIRastreamento);
				ev.setPlaca(placaCavaloChp);
				ev.setMotorista(nomeMotorista1Chp);
				//ev.setEmailsCc(emailGlobal5);
				//ev.setEmails(areaEmail.getEmails());
				ev.setAno(AppUtils.getYear(new Date()));
				//ev.setBoletimInformativo(new BoletimInformativo(null));
				ev.setDtCriacao(new Date());
				ev.setDtAlteracao(new Date());
				ev.setUsuCriacao(checkUsuario.valid());
				ev.setUsuAlteracao(checkUsuario.valid());
				
				// Persiste os dados - Salva o evento
				evService.crud().save(ev);			

				return 1;
		}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return 0;

	}
	
	/***
	 * @author Francis Bueno
	 * @param numeroSM
	 * Pesquisa ...
	 */
	public void update(int numeroSM){
		int resultEvento = 0;
		// Consulta SM na tabela de eventos
		Criteria smEvento = evService.crud().getSession().createCriteria(Evento.class);
		//smEvento.add(Restrictions.eq("id", getId()));		
		smEvento.add(Restrictions.eq("sincViagem", numeroSM));
		
		int result = smEvento.list().size();
		
		// Se não encontrou a SM registrada na tabela evento
		if (result == 0){
			
			// Prepara dados para registro
			
			try {

				// Armazena o id da Area encontrada no WS
				int idArea = buscarViagemBIArea(numeroSM);			
				
				if(	idArea != 0 ) {

					resultEvento =  salvarEvento(idArea);
					
					if (resultEvento == 1){
						// Atualiza a tabela de eventos
						RequestContext context = RequestContext.getCurrentInstance();
						context.update("formListaEventos:tbListEvento");
									
						update(numeroSM);
						
					} else {
						// Caso idArea seja 0 (isto é não foi encontrado)
						// responde ao usuario informando que não encontrou a area do cliente pela SM
						limparVariaveis();
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Cliente não encontrado na base de dados!\n" + " Cliente: " + msgNomeTransportadorChp + "\n Cnpj: " + msgCnpjTransportadorChp));
					} // final if idArea != 0
					
				} 				
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			
		} else
			// final if (result == 0)
			// Se encontrou a SM registrada na tabela evento então
			 // buscar novamente a SM no WS CHP (conforme solicitação do Filipe) e 
			 // vai registrar a tabela de evento_info_viagem 
			{		
			
			try {
				
				int idArea = buscarViagemBIArea(numeroSM);
				// Retirado em 20/11/2020 pois não precisada adicionar novo evento
				//int resultEvento =  salvarEvento(idArea);
				//int aaa = resultEvento;
				// Tabela EventoInfoViagem: registra a tela do ws chp, informações que não se altera
				// ResultadoString = EventoInfoViagemOK e AcessouWs
				// altera o id do evento evev_eveoid = id
				
				//Inicio teste...
				if (idArea != 0){
					resultEvento =  salvarEvento(idArea);
					
					if (resultEvento == 1){
						// Atualiza a tabela de eventos
						RequestContext context = RequestContext.getCurrentInstance();
						context.update("formListaEventos:tbListEvento");
									
						//update(numeroSM);
						
					}
				}
				// .. Final teste
				
				String resultStg = buscarViagemBI(numeroSM).toString();
				
				// Resultado = EventoInfoViagemOK encontrou dados na tabela evento_info_viagem 
				if(resultStg == "EventoInfoViagemOK"){
					
					// Procura da tabela eventos onde n SM e dtFinalização que não está nulo
					Criteria criEve = evService.crud().getSession().createCriteria(Evento.class);
					
						criEve.add(Restrictions.eq("sincViagem", numeroSM));
						criEve.add(Restrictions.isNotNull("dtFinalizacao"));
					
						// Contador resultado
						int resultEve = criEve.list().size();
						
						// Se encontrou 1 resultado
						if (resultEve >= 1 ){
							
								// Faz uma nova inclusão de um novo evento
							 	if(ev.getId() != null){

									FacesContext.getCurrentInstance().addMessage(null, new 
											FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Evento " + numeroSM +" foi reaberto em um novo evento!"));

									
							 	}	
								
							 	/*
								// Então busque os dados da SM Finalizada
								Evento eve = (Evento) criEve.list().get(0);
								
								// Reserve nas variaveis
								String id = eve.getId().toString();
								String sm = eve.getSincViagem().toString();
								
								// Forme a msg
								String msg = " id: " + id + " SM: " + sm;

								// Responda ao usuario 
								FacesContext.getCurrentInstance().addMessage(null, new 
										FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Evento " + msg +" já está finalizado!"));
								
								*/
							
						} else 
							
							// Se não encontrou então a SM está aberta
							
							{
							
							// Procura da tabela eventos onde n SM e dtFinalização está nulo (aberta)
							Criteria criEveA = evService.crud().getSession().createCriteria(Evento.class);							
							criEveA.add(Restrictions.eq("sincViagem", numeroSM));
							criEveA.add(Restrictions.isNull("dtFinalizacao"));
							
							// Contador resultado
							int resultEveA = criEveA.list().size();
							
							// Encontrou 1 resultado
							if (resultEveA == 1){
								
								// Então busque os dados da SM aberta
								Evento eveA = (Evento) criEveA.list().get(0);
								
								// Reserve nas variaveis
								String id = eveA.getId().toString();
								String sm = eveA.getSincViagem().toString();
								
								// Forme a msg
								String msg = " id: " + id + " SM: " + sm;

								// Responda ao usuario 
								limparVariaveis();
								FacesContext.getCurrentInstance().addMessage(null, new 
										FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção", "Evento " + msg +" já está aberto!"));													
								
							}

						}
					
					

				} else if(resultStg == "AcessouWs"){
					// Caso a resposta seja AcessouWs quer dizer que os dados foram inclusos na tabela evento_info_viagem
					FacesContext.getCurrentInstance().addMessage(null, new 
							FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Evento liberado para adicionar o Boletim. Clique em Eventos Abertos para atualizar a lista!"));
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Atualiza a tela 
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("formListaEventos:btnAddEvento");
			context.update("formListaEventos:inptNumSM");

			
			
		}
		
	}
	
	

	public Integer abrirNovoEventoSM(Integer numeroSM){
		
		if( numeroSM != 0){
		
		try {
			// Armazena o id da Area encontrada no WS
			int idArea = buscarViagemBIArea(numeroSM);
			
			// Se idArea veio preenchida
			if(idArea != 0) {

				Criteria checarNumMaxEvento = evService.crud().getSession().createCriteria(Evento.class);
				
				//Contador do checarNumMaxEvento					
				int resultChecar = checarNumMaxEvento.list().size();
				
				// Apropria o numero do evento
				if(resultChecar == 0){
					//Se = 0 Então = 1
					ev.setNrEvento(1);
				} else {
					//Se != 0 Então soma 1 ao contador
					ev.setNrEvento(resultChecar + 1);
				}
				
				//Adicionar a variavel o valor do nrEvento 
				numIdEvento = ev.getNrEvento();
				// habilita que o evento foi adicionado
				eventoAdicionado = true;
				habilitarBtnGerarEvento = false;	
				
				// chama a lista de eventos
				this.getListEventos();

				// busca as contas de emails na area
				Area areaEmail = areaService.findById(idArea);
				String emailGlobal5 = "filipe.aira@global5.com.br;alfredo.peracetta@global5.com.br";
				
				// Preparação para a Persistência
				ev.setSincViagem(numeroSM);
				ev.setNrBoletim(0);
				ev.setArea(new Area(idArea));
				ev.setEmails(emailsParaEnvioBIRastreamento);
				//ev.setEmailsCc(emailGlobal5);
				//ev.setEmailsCc(emailListGlobal5);
				//ev.setEmails(areaEmail.getEmails());
				ev.setAno(AppUtils.getYear(new Date()));
				//ev.setBoletimInformativo(new BoletimInformativo(null));
				ev.setDtCriacao(new Date());
				ev.setDtAlteracao(new Date());
				ev.setUsuCriacao(checkUsuario.valid());
				ev.setUsuAlteracao(checkUsuario.valid());
				
				// Persiste os dados - Salva o evento
				evService.crud().save(ev);
				
				// Atualiza a tabela de eventos
				RequestContext context = RequestContext.getCurrentInstance();
				context.update("formListaEventos:tbListEvento");
				
				this.id = ev.getId();
				
				int idTabEvento = ev.getId();
				
				update(numeroSM);					
								
				
				return ev.getId(); 
				
			} else {
				// Caso idArea seja 0 (isto é não foi encontrado)
				// responde ao usuario informando que não encontrou a area do cliente pela SM
				
				limparVariaveis();
				
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Cliente não encontrado na base de dados!\n" + " Cliente: " + msgNomeTransportadorChp + "\n Cnpj: " + msgCnpjTransportadorChp));
			} // final if idArea != 0				
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return null;
		}
		return null;
		
	}
	
	public void gerarNovoEvento(){
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		 
		
		Integer idEventoReturn = null;	
		// Novo
			
			// Buscar a existencia da SM na tabela de eventos...
			// Caso exista a SM o que deve ser feito???????
			// Caso NÃO EXISTA a SM será cadastrada 
			Criteria smEvento = evService.crud().getSession().createCriteria(Evento.class);
			smEvento.add(Restrictions.eq("sincViagem", numeroSM));
			
			int result = smEvento.list().size();
			
			if (result == 0){
				// Adicionar SM na tabela de Evento
				//numIdEvento = idEventoReturn;	
				
				// Gerar NrEvento
				Criteria checarNumMaxEvento = evService.crud().getSession().createCriteria(Evento.class);
				
				int resultChecar = checarNumMaxEvento.list().size();
				
				if(resultChecar == 0){
					ev.setNrEvento(1);
				} else {
					ev.setNrEvento(resultChecar + 1);
				}
				// Aplicações
				numIdEvento = ev.getNrEvento();
				eventoAdicionado = true;
				habilitarBtnGerarEvento = false;	
				
				this.getListEventos();
				// Adicionado 19/10/20
				ev.setDtCriacao(new Date());
				ev.setDtAlteracao(new Date());
				ev.setUsuCriacao(checkUsuario.valid());
				ev.setUsuAlteracao(checkUsuario.valid());
				
				// Salva o evento
				evService.crud().save(ev);
				

				
			} else {
				// Avisar que a SM não existe
				habilitarBtnGerarEvento = false;									
				numeroSM = null;
				
				RequestContext context = RequestContext.getCurrentInstance();
				context.update("formListaEventos:btnAddEvento");
				context.update("formListaEventos:inptNumSM");

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "SM informada não foi encontrada!") );
				
			}
			
			
		// Antigo abaixo
		
//		try {
//
//			StoredProcedureQuery qryNovoEv = eventoCrud.getEntityManager().createNamedStoredProcedureQuery("evento_i");
//			qryNovoEv.setParameter("sm", numeroSM);
//			qryNovoEv.setParameter("usuoid", checkUsuario.valid().getId());			
//			qryNovoEv.execute();
//						
//			idEventoReturn = (Integer) qryNovoEv.getOutputParameterValue("evento");
//			
//			Long resulta = idEventoReturn.longValue();
//			
//			if(resulta > 1){
//				numIdEvento = idEventoReturn;	
//				eventoAdicionado = true;
//				habilitarBtnGerarEvento = false;	
//				this.getListEventos();
//				
//			} else {
//
//				habilitarBtnGerarEvento = false;									
//				numeroSM = null;
//				
//				RequestContext context = RequestContext.getCurrentInstance();
//				context.update("formListaEventos:btnAddEvento");
//				context.update("formListaEventos:inptNumSM");
//
//				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "SM informada não foi encontrada!") );		
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace(pw);
//			
//		}	
		
	}
	
	/**
	 * @author Francis Bueno
	 * Processo de marcação do tipo de boletim selecionado dialog dlgAddBoletim
	 * Quando seleciona o tipo Boletim Tipo (P,D,S)
	 * 
	 */
	public void selecaoTipoBoletim(){
		// Tipo Boletim que será emitido
		checkBi.recebeCharTpBoletim(tipoSelecaoBi);
		
		RequestContext context = RequestContext.getCurrentInstance();
		
			if ( !tipoSelecaoBi.equals("")){
				
				habilitarBtnAbrirGerarBoletim = true;				
				context.update("formListaEventos:btnAddBoletim");

			} else {
				
				habilitarBtnAbrirGerarBoletim = false;
				context.update("formListaEventos:btnAddBoletim");
			}
			
			if(boletimFinal == false){
				checkBi.recebeBoletimFinal(false);
	
			}
	}
	
	/**
	 * @author Francis Bueno
	 * Processo do Botão SIM se Deseja Gerar um Boletim para SM informada dialog dlgAddBoletim
	 * 
	 */
	public void checarTipoSelecionado(){
		if(tipoSelecaoBi == null){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Defina uma opção para o Boletim Tipo."));
			return;
		} 
	}
		
	/**
	 * @author Francis Bueno
	 * Processo do Botão se Deseja Finalizar o boletim dialog dlgAddBoletim
	 * 
	 */
	public void selecaoBoletimFinal(){
		checkBi.recebeBoletimFinal(boletimFinal);				
	}
	
	private void listarAnexosPorEvento(){
		Criteria criteria = eventoAnexoService.crud().getSession().createCriteria(EventoAnexo.class);
		criteria.addOrder(Order.asc("id"));
		criteria.addOrder(Order.asc("dtCriacao"));
		
		if( numIdEvento != null ){
			criteria.add(Restrictions.eq("evento.id", numIdEvento));
			criteria.add(Restrictions.isNull("dtExclusao"));
		}
		
		if(criteria.setMaxResults(0) != null) {

			int result = criteria.list().size();
			
			if ( result > 0 ) {
				this.setListEventoAnexo(criteria.list());
			} else {
				this.setListEventoAnexo(null);
			}
			

			
		} else {

			this.setListEventoAnexo(null);
			
		}
			
		
		

		
	}
	
	private void listarBoletimPorEvento() {
	
		Criteria criteria = biService.crud().getSession().createCriteria(BoletimInformativo.class);
		criteria.addOrder(Order.asc("id"));
		criteria.addOrder(Order.asc("comIniDataHora"));
		
		if ( numIdEvento != null ) {

			criteria.add(Restrictions.eq("evento.id", numIdEvento));

		}
		
		if(criteria.setMaxResults(0)!=null){
			int result = criteria.list().size();
			
			if ( result > 0) {
				
				this.setListBoletimPorEvento(criteria.list());			
				
			} else {
				this.setListBoletimPorEvento(null);
			}	
			
			
		} else {
			this.setListBoletimPorEvento(null);
		}
		
	}
	
	public void limparCamposPesquisa(){
		/*
		numeroSM = null;
		nomeCliente = null;
		eventoFinalizado = false;
		pesqDtInicio = null;
		pesqDtFinal = null;
		placaPesq = "";
		motoristaPesq = "";
		*/
		this.setNumeroSM(null);
		this.setNomeCliente("");
		this.setEventoFinalizado(false);
		this.setPesqDtInicio(null);
		this.setPesqDtFinal(null);
		this.setPlacaPesq("");
		this.setMotoristaPesq("");
		char x = 'T';
		this.setTipoEventoPesq(x);
		//tipoEventoPesq
		//clear();
		listarEventos();
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("frmFiltroPesq");
		//context.update("frmListaEventos:tbListEvento");
		
	}
	public void limparVariaveis(){
		numeroSM = null;
	    nomeClienteChp = "";
		cnpjClienteChp = "";
		nomeMotorista1Chp = "";
		cpfMotorista1Chp = "";
		vinculoMotorista1Chp = "";
		nomeMotorista2Chp = "";
		cpfMotorista2Chp = "";
		vinculoMotorista2Chp = "";
		placaCavaloChp = "";
		corCavaloChp = "";
		marcaCavaloChp = "";	
		modeloCavaloChp = "";
		anoCavaloChp = "";
		placaReboque1Chp = "";
		corReboque1Chp = "";
		modeloReboque1Chp = "";
		placaReboque2Chp = "";
		corReboque2Chp = "";
		modeloReboque2Chp = "";
		placaReboque3Chp = "";
		corReboque3Chp = "";
		modeloReboque3Chp = "";	
		RequestContext context = RequestContext.getCurrentInstance();
		habilitarBtnGerarEvento = false;				
		context.update("formListaEventos:btnAddEvento");	
	}
	
	public void cancelarEvento(){
		if(ev.getId()!= null){
			
			Evento nev = evService.findById(ev.getId());
			// Tirado pois ainda não consegui identificar a causa do cancelamento
			//EventoInfoViagem eiv = evInfoViagService.findById(ev.getId());
			//evInfoViagService.crud().delete(eiv);
			
			nev.setDtCancelamento(new Date());
			nev.setUsuCancelamento(checkUsuario.valid());
			evService.crud().update(nev);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção", "Registro foi cancelado com sucesso!"));
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("formListaEventos:tbListEvento");
			context.update("formListaEventos:dlgCancel");
						
			listarEventosAberto();
			
			
		}
	}
	
	public void buscarEventoPCancelar(Integer idEvento){
		
		if(!idEvento.equals(null)){
			cancelaRetorno = "";
			// Cheque se o evento pode ser cancelado
			Criteria criEven = evService.crud().getSession().createCriteria(Evento.class);
			criEven.add(Restrictions.eq("id", idEvento));
			criEven.add(Restrictions.eq("nrBoletim", 0));
			
			int result = criEven.list().size();
			
			if(result == 1){					
				
				
				/* Chamada para o cancelamento
				Evento getEvento = evService.findById(idEvento);
				getEvento.setDtCancelamento(new Date());
				getEvento.setUsuCancelamento(checkUsuario.valid());
				
				evService.crud().update(getEvento);
				*/
				/*
				idEventoCancel = idEvento;
				smViagemCancel = smViagem;
				nomeClienteCancel = nomeCliente;
				*/
				
				findByIdEv(idEvento);
				listarAnexosPorEvento();
				//cancelaRetorno = "PF('dlgCancel').show();onTop('dlgCancel')";
				
				RequestContext context = RequestContext.getCurrentInstance();
				//context.update("formListaEventos:tbListEvento");
				context.update("formListaEventos:dlgCancel");
				
				
			} else 
				if (result < 1){
					cancelaRetorno = "";
					FacesContext.getCurrentInstance().addMessage(null, 
							new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Você não pode cancelar um evento onde já há um Boletim Gerado!"));
					return;
				}
			
			
		} else {
			// Não faça nada
			String a = "a";
			String b = a;
		}
		
	}
	
	private void pesquisarEventosPorCriterios(){
			
		Criteria criteria = evService.crud().getSession().createCriteria(Evento.class);
		criteria.add(Restrictions.isNull("dtCancelamento"));
		criteria.addOrder(Order.asc("id"));
		criteria.addOrder(Order.asc("sincViagem"));
		criteria.addOrder(Order.asc("dtCriacao"));
		
		
		// Tipo Evento
		/*
		if(tipoEventoPesq != null){
			criteria.add(Restrictions.eq("tipoInicial", tipoEventoPesq));
		}
		*/
		String isNull = "is null";
		
/*		if(tipoEventoPesq == 'T'){
			criteria.add(Restrictions.in("tipoInicial", 'D','P','S'));
			//criteria.add(Restrictions.isNull("tipoInicial"));
		} else 	*/	
		if(tipoEventoPesq == 'D'){
			//Character x = new Character(x);
			char cd = 'D';
			String sCd = String.valueOf(cd);			
			criteria.add(Restrictions.eq("tipoInicial", 'D'));
		} else 
		if(tipoEventoPesq == 'P'){
			char cp = 'P';
			criteria.add(Restrictions.eq("tipoInicial", 'P'));
		} else
		if(tipoEventoPesq == 'S'){
			char cs = 'S';
			criteria.add(Restrictions.eq("tipoInicial", 'S'));
		}
		
		
		if(tipoEventoFinalPesq == 'T'){
			criteria.add(Restrictions.in("tipoFinal", 'D','P','S'));
			//criteria.add(Restrictions.isNull("tipoInicial"));
		} else 		
		if(tipoEventoFinalPesq == 'D'){
			//Character x = new Character(x);
			char cd = 'D';
			String sCd = String.valueOf(cd);			
			criteria.add(Restrictions.eq("tipoFinal", 'D'));
		} else 
		if(tipoEventoFinalPesq == 'P'){
			char cp = 'P';
			criteria.add(Restrictions.eq("tipoFinal", 'P'));
		} else
		if(tipoEventoFinalPesq == 'S'){
			char cs = 'S';
			criteria.add(Restrictions.eq("tipoFinal", 'S'));
		}
		
		// Motorista
		if(!motoristaPesq.equals("")){
			criteria.add(Restrictions.ilike("motorista", motoristaPesq, MatchMode.ANYWHERE));
		}
		
		// Placa
		if(!placaPesq.equals("")){
			criteria.add(Restrictions.ilike("placa", placaPesq, MatchMode.ANYWHERE));
		}
			
		// Data 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fmtInicial = "";
		String fmtFinal = "";
		Date dtInicio = null;
		Date dtFinal = null;
		
		if(pesqDtInicio != null){
			fmtInicial = formatter.format(pesqDtInicio);
			try {
				dtInicio = new SimpleDateFormat("yyyy-MM-dd").parse(fmtInicial);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(pesqDtFinal != null){
			fmtFinal = formatter.format(pesqDtFinal);
			try {
				dtFinal = new SimpleDateFormat("yyyy-MM-dd").parse(fmtFinal);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (pesqDtInicio == null || pesqDtFinal  == null) {
			
			if (pesqDtInicio != null && pesqDtFinal == null) {
				criteria.add(Restrictions.ge("dtCriacao", dtInicio));
			} else 
				if (pesqDtInicio == null && pesqDtFinal != null) {
				criteria.add(Restrictions.le("dtCriacao", dtFinal));
			}  
			
		} else 
			if(pesqDtInicio != null && pesqDtFinal != null){
				criteria.add(Restrictions.between("dtCriacao",dtInicio , dtFinal));
			}
			
		
		
		if (numeroSM != null) {
			
			// Verificar a existência da SM registrada na tabela de evento
			
			Criteria criSM = evService.crud().getSession().createCriteria(Evento.class);
			
			if(numeroSM != null){
				criSM.add(Restrictions.eq("sincViagem", numeroSM));
			}
			
			int resultSM = criSM.list().size();
			
			// Se encontrou	SM no Evento em aberto ...
			if (resultSM >= 0) {
				criteria.add(Restrictions.eq("sincViagem", numeroSM));
			} 
			
			if ( resultSM == 0 ){
				//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção", "SM não encontrada no cadastro de eventos!"));
				//criteria.add(Restrictions.eq("sincViagem.id", null)); 				
			}

		}
		
		if ( eventoFinalizado == true ) {
			
			criteria.add(Restrictions.isNotNull("dtFinalizacao"));
			statusTabelaEvento = " Finalizados ";
		}
		
		if ( eventoFinalizado == false ) {
			
			criteria.add(Restrictions.isNull("dtFinalizacao"));
			statusTabelaEvento = " Abertos ";
		}
		
		//!nomeCliente.equals("")
		if(!nomeCliente.equals("")){
			
			String parametrosSql = "";
			parametrosSql += " area_nome ~* '" + nomeCliente + "' group by  idcliente";
			
			EntityManager em = areaService.crud().getEntityManager();
			
			String query = 	" Select area.areaoid as idcliente, area.area_nome as razao " +
					" from java.area , evento " +
					" where " +
					"     areaoid = eve_areaoid and " +
					"     eve_dt_cancelamento is null and "
					+ parametrosSql ;
			
			lstAreaQry =  em.createNativeQuery(query,"ListAreaClienteMapping").getResultList();
			
			int resultA = lstAreaQry.size();
			
			if (resultA > 0){
				ArrayList<Integer> listIdArea = new ArrayList<>();
				
				for(int i=0; i < resultA ; i++){
					listIdArea.add(lstAreaQry.get(i).getIdcliente());
					//
					//listIdArea.get(i).
					//Area area1 = (Area) listIdArea;
					//listIdArea.add(area1.getId());
				}
			
				criteria.add(Restrictions.in("area.id", listIdArea));
			}
			

		}
	
		/*
		if( criteria.setMaxResults(0) != null){
			this.setListEventos(null);
		} else {
		
		}
		*/
		
		int result = criteria.list().size();
		
		if ( result > 0) {
			
			this.setListEventos(criteria.list());			
			RequestContext context = RequestContext.getCurrentInstance();
			
			context.update("formListaEventos:tbListEvento");
		 
		} else {
			this.setListEventos(null);
		}
		/*
		else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Sua pesquisa não retornou nenhum evento registrado!"));
		}
		*/
		
	}
	
	private void listarEventos() {
		
		Criteria criteria = evService.crud().getSession().createCriteria(Evento.class);
		criteria.add(Restrictions.isNull("dtCancelamento"));
		criteria.add(Restrictions.isNull("dtFinalizacao"));
		criteria.addOrder(Order.asc("id"));
		criteria.addOrder(Order.asc("sincViagem"));
		criteria.addOrder(Order.asc("dtCriacao"));
		
		if (numIdEvento != null) {

			criteria.add(Restrictions.eq("id", numIdEvento));

		}
		
		int result = criteria.list().size();
		
		if ( result > 0) {
			
			this.setListEventos(criteria.list());
			
			if(eventoAdicionado == true ){
				eventoAdicionado = false;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Evento registrado com sucesso! Prosiga com o registro do boletim informativo."));
			}
			
		} 
		
	}
	
	public Integer consultarNumeracaoBI(){
		
		Integer anoAtual = new Integer(anoVigente);

		Number sizeP = 0;
		Number sizeD = 0;
		Number sizeS = 0;
		
		if ( anoVigente > 0 ) {
			
			// Qtde de Boletim emitidos para Perdas
			Criteria criteriaP = biNumService.crud().getSession().createCriteria(BoletimInformativoNumeracao.class);
			criteriaP.add(Restrictions.eq("ano" , anoAtual));
			criteriaP.add(Restrictions.eq("tipo" , 'P'));
			
			BoletimInformativoNumeracao biNumP = (BoletimInformativoNumeracao) criteriaP.uniqueResult();
			
			if(biNumP == null){
				sizeP = 0;
			}  else {
		
				sizeP = biNumP.getContador();								
			}
		
			this.setQtdBiPerdas((int) sizeP);
			
			
			// Qtde de Boletim emitidos para Danos
			Criteria criteriaD = biNumService.crud().getSession().createCriteria(BoletimInformativoNumeracao.class);
			criteriaD.add(Restrictions.eq("ano" , anoAtual));
			criteriaD.add(Restrictions.eq("tipo" , 'D'));
			
			BoletimInformativoNumeracao biNumD = (BoletimInformativoNumeracao) criteriaD.uniqueResult();
			
			if ( biNumD == null){
				sizeD = 0;	
			} else {
				sizeD = biNumD.getContador();	
			}
			
			
			
			this.setQtdBiDanos((int) sizeD);
			
			
			// Qtde de Boletim emitidos para Suspeitas
			Criteria criteriaS = biNumService.crud().getSession().createCriteria(BoletimInformativoNumeracao.class);
			criteriaS.add(Restrictions.eq("ano" , anoAtual));
			criteriaS.add(Restrictions.eq("tipo" , 'S'));
			
			BoletimInformativoNumeracao biNumS = (BoletimInformativoNumeracao) criteriaS.uniqueResult();
			
			if( biNumS == null){
				sizeS = 0;	
			} else {
				sizeS = biNumS.getContador();
			}
						
			this.setQtdBiSuspeitas((int) sizeS);
			
			
		}
		
		return null;
	}
	
	public void uploadArqAnexo(FileUploadEvent event){
		uploadedFiles.add(event.getFile());
		if(event.getFile()!=null){
			 msgArquivoUpload = "O arquivo " + event.getFile().getFileName() + " está pronto para ser salvo para registro do anexo.";
			 this.setBtnRegistrarAnexoLiberado(true);
			 
			 FacesMessage message = new FacesMessage("Sucesso na carga do arquivo! ", event.getFile().getFileName() + " arquivo pronto para ser salvo.");
	         FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
	}
	
	public void limparUpload(){
		msgArquivoUpload = "";
		idTipoAnexo = null;
		descArquivoAnexo = "";
		descArquivoAnexoFront = "";
		//lstTipoAnexo = TipoAnexoService.crud().isNull("exclusao").listAll();
		uploadedFiles.clear();
		this.setBtnRegistrarAnexoLiberado(false);
	}
	
	public String salvarArqAnexoUpload(){
		
		// Local que será armazenado o arquivo no servidor (/var/www/anexos_bi/nr_SM/nr_Evento/ )
		
		// Por causa da retirada da Classe SincViagem transformando em Integer
		//String dir = AppUtils.dirAnexosBI + ev.getSincViagem().getId() + "/" + ev.getNrEvento() + "/" ;

		String dir = AppUtils.dirAnexosBI + ev.getSincViagem() + "/" + ev.getNrEvento() + "/" ;
		
		
		// Criar diretório no servidor
		File files = new File(dir);
		
		if( files.mkdirs()){
			System.out.println("Diretórios Criados com Sucesso! ( " + dir + " )");
		} else {
			System.out.println("Não foi possível criar o diretório ( " + dir + " )");
		}
		
		// Salvar o arquivo no Servidor
		for( UploadedFile uploadedFile : uploadedFiles){
			AppUtils.saveFile(dir, uploadedFile);
			arquivoTipoExtensao=FilenameUtils.getExtension(uploadedFile.getFileName());
			arquivoNome = uploadedFile.getFileName();
		}
		
		uploadedFiles.clear();
		
		return "salvoAnexo";
		
	}
	
	public void onTipoAnexoSelecionado(SelectEvent event){
		idTipoAnexo = (Integer) event.getObject();
	}
	
	public void onDescArquivoAnexo(AjaxBehaviorEvent event){
		String descParaArquivoAnexo  = (String) ( (UIOutput) event.getSource()).getValue();
		this.setDescArquivoAnexo(descParaArquivoAnexo);
	}
	
	
	public void salvarRegistroDeAnexo(){
		
		if (idTipoAnexo == null) {
			
			 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção","Informe o tipo do anexo para que possa ser registrado o anexo!");
	         FacesContext.getCurrentInstance().addMessage(null, message);
			
	         return;
		}
		
		EventoAnexo evA = new EventoAnexo();
		evA.setEvento(ev);
		evA.setNumEvento(ev.getNrEvento());
		evA.setSincViagem(ev.getSincViagem());
		evA.setTipoAnexo(new TipoAnexo(idTipoAnexo));
		evA.setDescricao(descArquivoAnexo);
		evA.setDtCriacao(new Date());
		evA.setUsuCriacao(checkUsuario.valid());
		
		String responseAnexo = salvarArqAnexoUpload(); 
		
		if (responseAnexo == "salvoAnexo"){
			
			evA.setArquivoTipo(arquivoTipoExtensao);
			evA.setArquivoNome(arquivoNome);
			
			eventoAnexoService.crud().save(evA);
			 
			 listarAnexosPorEvento();
			 
			 RequestContext context = RequestContext.getCurrentInstance();
			 context.update("formListaEventos:frmTbAnexoBoletim:tabAnexosDoEvento");
			 
			 limparUpload();
			 
			 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Registro do Anexo realizado com sucesso!");
	         FacesContext.getCurrentInstance().addMessage(null, message);
	         
		}
			

	}
	
	public void excluirAnexo(Integer idAnexo){
		
		if ( idAnexo != null) {
			
			EventoAnexo evAnexoDel = eventoAnexoService.findById(idAnexo);
			
			evAnexoDel.setDtExclusao(new Date());
			evAnexoDel.setUsuExclusao(checkUsuario.valid());
			
			eventoAnexoService.update(evAnexoDel);
			
			// Apagando arquivo fisico
			// Local que será armazenado o arquivo no servidor (/var/www/anexos_bi/nr_SM/nr_Evento/ )
			
			// Por causa da retirada da Classe EventoAnexo SincViagem transformando em Integer			
			//String dir = AppUtils.dirAnexosBI + evAnexoDel.getSincViagem().getId() + "/" + evAnexoDel.getNumEvento()  + "/" ;			
			String dir = AppUtils.dirAnexosBI + evAnexoDel.getSincViagem() + "/" + evAnexoDel.getNumEvento()  + "/" ;
		
			File arquivoParaExcluir = new File(dir + evAnexoDel.getArquivoNome() );
			
			if ( arquivoParaExcluir != null) {
				arquivoParaExcluir.delete();
			}
			
			listarAnexosPorEvento();
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("formListaEventos:frmTbAnexoBoletim:tabAnexosDoEvento");
		
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Registro de Anexo foi removido deste Evento com sucesso!");
	        FacesContext.getCurrentInstance().addMessage(null, message);
			 
		}
		
		
	}
	
	// Novo Método busca ws chp
	private List<VersaoTecnologia> lstVersaoTecnologia;
	
	@Inject
	private Crud<Area> areaCrud;
	
	@Inject
	private AreaService areaService;
	
	/***
	 * @author Francis Bueno
	 * @param idViagem
	 * @return
	 * @throws ParseException
	 * Retorna o idArea ou 0 (zero)
	 */
	public int buscarViagemBIArea(Integer idViagem) throws ParseException {

		// Consulta a existencia da viagem na tabela evento_info_viagem
		Criteria criEvViagem = evInfoViagService.crud().getSession().createCriteria(EventoInfoViagem.class);
		criEvViagem.add(Restrictions.eq("viagem", idViagem));
		
		// Conta quantia de registros encontrado
		int result = criEvViagem.list().size();
		
		// Instancia o Ws Chp para passar como parametro 
		ViagemClient wsV = new ViagemClient();

		// Se não encontrou nada no contador
		if( result == 0){

			// Instancia o Ws Chp para passar como parametro 
			//ViagemClient wsV = new ViagemClient();

			// idRetorno Area, chamado a consulta no WS CHP 
			int idArea = getWsDataViagemBuscaArea(wsV, idViagem);
			
			if (idArea != 0){
				// se encontrou devolve idArea
				return idArea;
			} else {
				// caso contrario retorna Zero
				chpNaoRetornaInformacao = true;
				return 0;
			}
			
		} else {
			// idRetorno Area, chamado a consulta no WS CHP 
			int idArea = getWsDataViagemBuscaArea(wsV, idViagem);
			
			if (idArea != 0){
				// se encontrou devolve idArea
				return idArea;
			} else {
				// caso contrario retorna Zero
				return 0;
			}
		}
		//return 0;		

	}

	/***
	 * @author Francis Bueno
	 * @param wsV
	 * @param idViagem
	 * @return idArea || 0
	 * @throws ParseException
	 * busca no WS o cnpj do dono da viagem e caso encontre retorna o idArea
	 */
	private int getWsDataViagemBuscaArea(ViagemClient wsV, Integer idViagem)
			throws ParseException {
		
		// Caso a viagem sejá diferente de null
		if( idViagem != null){
			
			// Buscar Dados no WS CHP com idViagem	(sempre retorna apenas uma viagem ou nada)	
			JSONArray objJsonArray = wsV.getViagemBI(idViagem);			
			
			// Encontrou a viagem então da inicio a coleta do CNPJ
			if( objJsonArray != null){				
				
				// coleta o tamanho do JsonArray retornado do Ws
				int viagensCount = objJsonArray.length();
				
				// laço for para a coleta de dados
				for (int i = 0; i < viagensCount; i++) {
					
					// Cnpj da Area, reserva para a consulta da query
					String nomeTransportador = objJsonArray.getJSONObject(i).opt("pess_nome_transportador").toString();
					String docTransportador = objJsonArray.getJSONObject(i).opt("documento_transportador").toString().trim();
					
					nomeClienteChp = nomeTransportador;
					cnpjClienteChp =  objJsonArray.getJSONObject(i).opt("documento_transportador").toString().trim();
					// Motorista
					JSONArray jsonArrayMotoristas = objJsonArray.getJSONObject(i).getJSONArray("motoristas");
					
					// Var Motoristas
					int arrayMotoristasCount = 0;
					int motoristaCount = 0;
					String jsonMotoristas = "";
					
					// Encontrou json motorista
					if (jsonArrayMotoristas != null) {
						
						// Contador de quantidada de chave motorista
						arrayMotoristasCount = jsonArrayMotoristas.length();
					
						// for para motorista
						for (int im = 0; im < arrayMotoristasCount; im++) {

							motoristaCount++;

							jsonMotoristas += jsonArrayMotoristas.getJSONObject(im).toString() + ",";
							
							if(im == 0){
								// Persistencia - Motorista 1
								nomeMotorista1Chp 	 = jsonArrayMotoristas.getJSONObject(im).opt("nome_moto").toString();
								cpfMotorista1Chp  	 = jsonArrayMotoristas.getJSONObject(im).opt("cpf_moto").toString();
								vinculoMotorista1Chp = jsonArrayMotoristas.getJSONObject(im).opt("vinculo_contratual").toString();

							}
							if( im == 1){
								// Persistencia - Motorista 2
								nomeMotorista2Chp	 = jsonArrayMotoristas.getJSONObject(im).opt("nome_moto").toString();
								cpfMotorista2Chp	 = jsonArrayMotoristas.getJSONObject(im).opt("cpf_moto").toString();
								vinculoMotorista2Chp = jsonArrayMotoristas.getJSONObject(im).opt("vinculo_contratual").toString();
						
							}

						} // final for arrayMotoristaCount
						
						
					} // final if jsonArrayMotoristas --> Motoristas

					
					// Acesso json, veiculos (Trativa para Cavalo, Reboque e Escolta)
					
					JSONArray jsonArrayVeiculos = objJsonArray.getJSONObject(i).getJSONArray("veiculos");
					
					// Var Veiculos
					int arrayVeiculosCount = 0;
					int cavaloCount = 0;
					String placaCavalo = "";
					int reboqueCount = 0;
					String placaReboque1 = "";
					String placaReboque2 = "";
					String placaReboque3 = "";
					int escoltaCount = 0;
					String jsonVeiculosEscolta = "";
					String codAutotracMct = "";
					
					// Encontrou json veiculos
					if (jsonArrayVeiculos != null) {
						
						// Contador de veiculos
						arrayVeiculosCount = jsonArrayVeiculos.length();
						
						// Escolta
						escoltaCount = 0;
						
						// for para contador de veiculos
						for (int iv = 0; iv < arrayVeiculosCount; iv++){
							
							int tipoVeiculo = jsonArrayVeiculos.getJSONObject(iv).getInt("tipo_veiculo");
							
							// Valor Escolta para uso em criterio de regra --> valor do elemento escolta para condição if
							String veiculoEscolta = jsonArrayVeiculos.getJSONObject(iv).opt("escolta").toString();
							
							// Cavalo: coleta placa onde (tipo_veiculo != 1 e escolta = null)
							if (tipoVeiculo != 1 && veiculoEscolta.equals("null")) {

								cavaloCount++;
								//placaCavalo += jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString() + "|";

								//Persistencia - Cavalo / Veiculo
								placaCavaloChp  = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString();
								corCavaloChp    = jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString();
								marcaCavaloChp  = jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString();
								modeloCavaloChp =jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString();
								String veicAno = jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString().trim();
								if(!veicAno.equals("")){
									anoCavaloChp = 	veicAno;
								} else {
									
								}								
								
																	
							} // final if tipo_veiculo != 1 && escolta = null
							
							// Reboque: coleta placa onde (tipo_veiculo = 1 e escolta = null )
							if (tipoVeiculo == 1 && veiculoEscolta.equals("null")) {
								
								reboqueCount++;

								if (reboqueCount == 1) {
									//placaReboque1 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
								
									//Persistencia - Carreta1
									placaReboque1Chp = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
									jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString().trim();										
									//eiv.setCarretaAno1(Integer.parseInt(jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString()));
									String carr1Ano = jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString().trim();
									if(!carr1Ano.equals("")){
										Integer.parseInt(carr1Ano);	
									}										
									corReboque1Chp = jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString().trim();
									modeloReboque1Chp = jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString().trim();
									
								}
								if (reboqueCount == 2) {
									//placaReboque2 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
									
									//Persistencia - Carreta2
									placaReboque2Chp = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
									jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString().trim();
									//eiv.setCarretaAno2(Integer.parseInt(jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString()));
									String carr2Ano = jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString().trim();
									if(!carr2Ano.equals("")){
										Integer.parseInt(carr2Ano);	
									}												
									corReboque2Chp = jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString().trim();
									modeloReboque2Chp = jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString().trim();
									
								}
								if (reboqueCount == 3) {
									//placaReboque3 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
									
									//Persistencia - Carreta3
									placaReboque3Chp = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
									jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString().trim();
									//eiv.setCarretaAno3(Integer.parseInt(jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString()));
									String carr3Ano = jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString().trim();
									if(!carr3Ano.equals("")){
										Integer.parseInt(carr3Ano);	
									}												
									corReboque3Chp = jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString().trim();
									modeloReboque3Chp = jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString().trim();
									
								}
																
							} // final if tipo_veiculo = 1 e escolta = null
																				
						} // final for arrayVeiculosCount --> Verifica Veiculo
											
						
					} // final jsonArrayVeiculos -> Veiculos 					
					
					// buscar emails para envio do boletim informativo
					String emailsCliente = wsV.getTransportador(docTransportador).toString();
					
					emailsParaEnvioBIRastreamento = emailsCliente;
					
/*					String jArray = "";
					
					JSONArray arrayEmail = new JSONArray(jArray);
					int listEmail = arrayEmail.length();
					
					for(int a = 0; a < listEmail; a++ ){
						
						JSONObject obj = arrayEmail.getJSONObject(a);
						
						obj.opt("texto").toString();
						if (!obj.opt("email").toString().equals("null")){
							emailsParaEnvioBIRastreamento = emailsParaEnvioBIRastreamento +  obj.opt("email").toString().toLowerCase()+";";
						}
						
					}*/
					
					EntityManager em = areaService.crud().getEntityManager();

					// Query de pesquisa com busca do CNPJ, nivel 2(cliente) e dt exclusão nula
					String query = "select a.areaoid as id from java.area a  " +
								   " where a.area_anvloid = 2 " + 
								   " and a.area_dt_exclusao is null" +
								   " and a.area_pesoid_responsavel in (select p.pesoid from java.pessoa p where p.pes_documento1 = '" + docTransportador + "')" ;
					
					// alimenta o list com os dados encontrado
					lstAreaPorPessoaDoc =  em.createNativeQuery(query,"ListAreaPorPessoaDocumentoMapping").getResultList();
					
					// contador de registros
					int res = lstAreaPorPessoaDoc.size();
										
					// se encontrou registro retorna o id da area
					if( res > 0){						
						return lstAreaPorPessoaDoc.get(0).getId();	
					 } else {
						 msgCnpjTransportadorChp = docTransportador;
						 msgNomeTransportadorChp = nomeTransportador;
						 // Caso contrario retorna 0
						 return 0;
					 }
					
					}				
				} else {
					// Caso não encontre nada no ws retorna 0
					return 0;
				}
		}		
		return 0;			
	}

	/***
	 * @author Francis Bueno
	 * @param idViagem
	 * @return
	 * @throws ParseException
	 * Consulta se a viagem existe na tabela evento_info_viagem, se sim retorna
	 * EventoInfoViagemOK caso contrario AcessouWs
	 * 
	 * Tabela EventoInfoViagem: registra a tela do ws chp, informações que não se altera
	 * 
	 */
	public String buscarViagemBI(Integer idViagem) throws ParseException {
		
		// Criteria de Consulta na tabela evento_info_viagem
		Criteria criEvViagem = evInfoViagService.crud().getSession().createCriteria(EventoInfoViagem.class);
		//criEvViagem.add(Restrictions.eq("id", ev.getId()));
		criEvViagem.add(Restrictions.eq("viagem", idViagem));
		
		// Contador dos evento_info_viagem encontrado
		int result = criEvViagem.list().size();
		
		// Se não encontrou
		
		if( result >= 0){

			// Instancia a classe ws chp
			ViagemClient wsV = new ViagemClient();
			
			// Chama o método de Busca acessando o ws passando ws e idViagem
			getWsDataViagemBI(wsV, idViagem);
			
			// Se não encontrou retorna AcessouWs
			return "EventoInfoViagemOK";
			//return  "AcessouWs";
			
		} else {
			// Se encontrou retorna EventoInfoViagemOK 
			//return "EventoInfoViagemOK";
			return "EventoInfoViagemOK";
		}
		

	}
	
	/***
	 * @author Francis Bueno
	 * @param wsV
	 * @param idViagem
	 * @throws ParseException
	 * 
	 */
	private void getWsDataViagemBI(ViagemClient wsV, Integer idViagem)
			throws ParseException {
		// Inicio Francis
		if( idViagem != null){
			
			// Instancia eiv (tabela evento_info_viagem)
			EventoInfoViagem eiv = new EventoInfoViagem();
			
			// Instancia listArray para persistencia (tabela evento_info_rastreador) 
			ArrayList<EventoInfoRastreador> listArray = new ArrayList<>();
			
			// Buscar Dados no WS CHP com informação da viagem(SM)
			JSONArray objJsonArray = wsV.getViagemBI(idViagem);
			
			// Encontrou dados no WS
			if( objJsonArray != null){				
				
				// tamanho do JsonArray retornado do Ws (forma de acesso)
				int viagensCount = objJsonArray.length();
				
				// for para contador de viagem
				for (int i = 0; i < viagensCount; i++) {
					
					// Variaveis Inicias
					String numSm = objJsonArray.getJSONObject(i).opt("codigoSM").toString();
					String docTransportador = objJsonArray.getJSONObject(i).opt("documento_transportador").toString().trim();
										
					// Se docTransportador for diferente de null
					if (!docTransportador.equals("null")) {
						
						// Variaveis para persistencia no Evento
						String nomeCliente = objJsonArray.getJSONObject(i).opt("pess_nome_transportador").toString();						
						String docSeguradora = objJsonArray.getJSONObject(i).opt("documento_seguradora").toString();
						String tipoMercadoria = objJsonArray.getJSONObject(i).opt("viag_descricao_carga").toString();
						String tipoOperacao = objJsonArray.getJSONObject(i).opt("tipo_viagem").toString();						
						String valorMercadoria = objJsonArray.getJSONObject(i).opt("viag_valor_merc_especifica").toString();
						String valorCarga = objJsonArray.getJSONObject(i).opt("viag_valor_carga").toString();
												
						// Encontrou valorCarga
						if(!valorCarga.equals("null")){									
							//viag_valor_carga	
							BigDecimal vlrCarga = new BigDecimal(objJsonArray.getJSONObject(i).optDouble("viag_valor_carga"));

							// Persistencia vlrFrete
							eiv.setValor(vlrCarga);
							
						} else {
							eiv.setValor(BigDecimal.ZERO);
						}
						
						// Persistencia - (tabela evento_info_viagem)
						eiv.setViagem(idViagem);
						eiv.setNomeCliente(nomeCliente);
						eiv.setNomeSeguradora("WS ND CST");
						eiv.setNomeCorretora("WS ND CST");
						eiv.setNomeTransportadora(nomeCliente);
						eiv.setMercadoria(tipoMercadoria);
						eiv.setTipoOperacao(tipoOperacao);
						/*
						if(!valorMercadoria.equals("null") ){
							eiv.setValor(new BigDecimal(valorMercadoria));
						} else {
							eiv.setValor(BigDecimal.ZERO);
						}
						*/
						eiv.setEmbarcadorDestino("WS ND CST");
						eiv.setEmbarcadorOrigem("WS ND CST");
						
						// Acesso Json, status_viagem
						String statusViagemJson = objJsonArray.getJSONObject(i).optString("status_viagem").toString();						
						
						// Acesso Json, motoristas
						JSONArray jsonArrayMotoristas = objJsonArray.getJSONObject(i).getJSONArray("motoristas");
						
						// Var Motoristas
						int arrayMotoristasCount = 0;
						int motoristaCount = 0;
						String jsonMotoristas = "";
						
						// Encontrou json motorista
						if (jsonArrayMotoristas != null) {
							
							// Contador de quantidada de chave motorista
							arrayMotoristasCount = jsonArrayMotoristas.length();
							
							// for para motorista
							for (int im = 0; im < arrayMotoristasCount; im++) {

								motoristaCount++;

								jsonMotoristas += jsonArrayMotoristas.getJSONObject(im).toString() + ",";
								
								if(im == 0){
									// Persistencia - Motorista 1
									eiv.setMotoristaNome1(jsonArrayMotoristas.getJSONObject(im).opt("nome_moto").toString());
									eiv.setMotoristaCpf1(jsonArrayMotoristas.getJSONObject(im).opt("cpf_moto").toString());
									eiv.setMotoristaVinculo1(jsonArrayMotoristas.getJSONObject(im).opt("vinculo_contratual").toString());
									eiv.setMotoristaFone1("WS ND CST");
								}
								if( im == 1){
									// Persistencia - Motorista 2
									eiv.setMotoristaNome2(jsonArrayMotoristas.getJSONObject(im).opt("nome_moto").toString());
									eiv.setMotoristaCpf2(jsonArrayMotoristas.getJSONObject(im).opt("cpf_moto").toString());
									eiv.setMotoristaVinculo2(jsonArrayMotoristas.getJSONObject(im).opt("vinculo_contratual").toString());
									eiv.setMotoristaFone2("WS ND CST");
								}

							} // final for arrayMotoristaCount
							
						} // final if jsonArrayMotoristas --> Motoristas
						
						
						// Acesso json, veiculos (Trativa para Cavalo, Reboque e Escolta)
						JSONArray jsonArrayVeiculos = objJsonArray.getJSONObject(i).getJSONArray("veiculos");
						
						// Var Veiculos
						int arrayVeiculosCount = 0;
						int cavaloCount = 0;
						String placaCavalo = "";
						int reboqueCount = 0;
						String placaReboque1 = "";
						String placaReboque2 = "";
						String placaReboque3 = "";
						int escoltaCount = 0;
						String jsonVeiculosEscolta = "";
						String codAutotracMct = "";
						
						// Encontrou json veiculos
						if (jsonArrayVeiculos != null) {
							
							// Contador de veiculos
							arrayVeiculosCount = jsonArrayVeiculos.length();
							
							// Escolta
							escoltaCount = 0;
							
							// for para contador de veiculos
							for (int iv = 0; iv < arrayVeiculosCount; iv++){
								
								int tipoVeiculo = jsonArrayVeiculos.getJSONObject(iv).getInt("tipo_veiculo");
								
								// Valor Escolta para uso em criterio de regra --> valor do elemento escolta para condição if
								String veiculoEscolta = jsonArrayVeiculos.getJSONObject(iv).opt("escolta").toString();
								
								// Cavalo: coleta placa onde (tipo_veiculo != 1 e escolta = null)
								if (tipoVeiculo != 1 && veiculoEscolta.equals("null")) {

									cavaloCount++;
									placaCavalo += jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString() + "|";

									//Persistencia - Cavalo / Veiculo
									eiv.setVeiculoPlaca(jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString());
									eiv.setVeiculoMarca(jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString());
									eiv.setVeiculoModelo(jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString());
									String veicAno = jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString().trim();
									if(!veicAno.equals("")){
										eiv.setVeiculoAno(veicAno);	
									} else {
										eiv.setVeiculoAno("");
									}
									
									eiv.setVeiculoCor(jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString());
																		
								} // final if tipo_veiculo != 1 && escolta = null
								
								// Reboque: coleta placa onde (tipo_veiculo = 1 e escolta = null )
								if (tipoVeiculo == 1 && veiculoEscolta.equals("null")) {
									
									reboqueCount++;

									if (reboqueCount == 1) {
										placaReboque1 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
									
										//Persistencia - Carreta1
										eiv.setCarretaPlaca1(jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim());
										eiv.setCarretaMarca1(jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString().trim());										
										//eiv.setCarretaAno1(Integer.parseInt(jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString()));
										String carr1Ano = jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString().trim();
										if(!carr1Ano.equals("")){
											eiv.setCarretaAno1(Integer.parseInt(carr1Ano));	
										}										
										eiv.setCarretaCor1(jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString().trim());
										eiv.setCarretaTipo1(jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString().trim());
										
									}
									if (reboqueCount == 2) {
										placaReboque2 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
										
										//Persistencia - Carreta2
										eiv.setCarretaPlaca2(jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim());
										eiv.setCarretaMarca2(jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString().trim());
										//eiv.setCarretaAno2(Integer.parseInt(jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString()));
										String carr2Ano = jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString().trim();
										if(!carr2Ano.equals("")){
											eiv.setCarretaAno2(Integer.parseInt(carr2Ano));	
										}												
										eiv.setCarretaCor2(jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString().trim());
										eiv.setCarretaTipo2(jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString().trim());
										
									}
									if (reboqueCount == 3) {
										placaReboque3 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
										
										//Persistencia - Carreta3
										eiv.setCarretaPlaca3(jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim());
										eiv.setCarretaMarca3(jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString().trim());
										//eiv.setCarretaAno3(Integer.parseInt(jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString()));
										String carr3Ano = jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString().trim();
										if(!carr3Ano.equals("")){
											eiv.setCarretaAno3(Integer.parseInt(carr3Ano));	
										}												
										eiv.setCarretaCor3(jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString().trim());
										eiv.setCarretaTipo3(jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString().trim());
										
									}
																	
								} // final if tipo_veiculo = 1 e escolta = null
								
								// Escolta: (Se existir e se diferente de null ) (AJUSTE... deixar condição <> de null | ! )
								if (!veiculoEscolta.equals("null")) {

									escoltaCount++;
									jsonVeiculosEscolta += jsonArrayVeiculos.getJSONObject(iv).toString() + ",";

								}
															
							} // final for arrayVeiculosCount --> Verifica Veiculo
												
							
						} // final jsonArrayVeiculos -> Veiculos 
						
						// Acesso json, tecnologia (Terminais) (Trativa para tecnologia e iscas )
						JSONArray jsonArrayTecnologia = objJsonArray.getJSONObject(i).getJSONArray("terminais");
						
						// Var Tecnologias
						int arrayTecnologiaCount = 0;
						int tecnologiaCount = 0;
						int iscaSimCount = 0;
						int iscaNaoCount = 0;
						String jsonTerminalComIsca = "";
						String numeroTerminal = ""; // para Isca = N , term_numero_terminal (mct)
						int terminalCodigo = 0; 	// para Isca = N , term_vtec_codigo
						int idTecnologia = 0;
						
						// Se encontrou json tecnologia
						if (jsonArrayTecnologia != null) {
							
							// Contador de tecnologia
							arrayTecnologiaCount = jsonArrayTecnologia.length();
							
							int contador = 1;
							String termCodigo ="";
							String termDescricao ="";
							String termTelemonitorado = "";
							String termNumTerminal = "";
							String termVtecCodigo = "";
							String termIsca = "";
							
							String terminalComIsca = "";
							
							// for para contador de tecnologia
							for (int it = 0; it < arrayTecnologiaCount; it++) {
								
								// Adiciona rastreador...
								termCodigo = jsonArrayTecnologia.getJSONObject(it).opt("term_codigo").toString();
								termDescricao = jsonArrayTecnologia.getJSONObject(it).opt("vtec_descricao").toString();
								termTelemonitorado = jsonArrayTecnologia.getJSONObject(it).opt("telemonitorado").toString();
								termNumTerminal = jsonArrayTecnologia.getJSONObject(it).opt("term_numero_terminal").toString();
								termVtecCodigo = jsonArrayTecnologia.getJSONObject(it).opt("term_vtec_codigo").toString();
								termIsca = jsonArrayTecnologia.getJSONObject(it).opt("isca").toString();
								
								// Adiciona os dados dos rastreadores a listArray
								listArray.add(new EventoInfoRastreador( contador++ , termCodigo, termDescricao, termTelemonitorado, termNumTerminal, termVtecCodigo, termIsca ) );		
								
							} // final for arrayTecnologiaCount						
							
						} // final jsonArrayTecnologia
						
						
						// Acesso json, origem
						JSONObject jbOrigem =  objJsonArray.getJSONObject(i).getJSONObject("origem");

							// se encontrou json origem 
							if(jbOrigem != null){
								jbOrigem.opt("vloc_descricao").toString();
								jbOrigem.opt("cida_descricao_ibge").toString();
								jbOrigem.opt("sigla_estado").toString();
								jbOrigem.opt("pais").toString();
								jbOrigem.opt("refe_latitude").toString();
								jbOrigem.opt("refe_longitude").toString();
								jbOrigem.opt("previsao_chegada").toString();	
								
								// Persistencia Origem   							
								eiv.setOrigemCidade(jbOrigem.opt("cida_descricao_ibge").toString());
								eiv.setOrigemUf(jbOrigem.opt("sigla_estado").toString());
							}
							
							
						// Acesso json, destino
						JSONObject jbDestino =  objJsonArray.getJSONObject(i).getJSONObject("destino");
							
							// se encontrou json destino
							if(jbDestino != null){
								jbDestino.opt("vloc_descricao").toString();
								jbDestino.opt("cidade").toString();
								jbDestino.opt("sigla_estado").toString();
								jbDestino.opt("pais").toString();
								jbDestino.opt("refe_latitude").toString();
								jbDestino.opt("refe_longitude").toString();								
								//jsonArrayDestino.getJSONObject(d).opt("cidade").toString();
								jbDestino.opt("previsao_chegada").toString();	
																
								// Persistencia Destino
								eiv.setDestinoCidade(jbDestino.opt("cidade").toString());
								eiv.setDestinoUf(jbDestino.opt("sigla_estado").toString());
								
								
							}						
									
						
						// Acesso json, locais
						JSONArray jsonArrayLocais = objJsonArray.getJSONObject(i).getJSONArray("locais");
						
						int arrayLocaisCount = 0;
						
						 // se encontrou json locais
						if(jsonArrayLocais != null){

							// contador json locais
							arrayLocaisCount = jsonArrayLocais.length();
							
							Integer seqParada = null;
							String tipoParada = "";
							String locDescricao = "";
							String cidadeLocal = "";
							String ufLocal = "";
							String paisLocal = "";
							String latitudeLocal = "";
							String longitudeLocal = "";
							String refe_Km_Local = "";
							String previsao_checada_local = "";
							
							// for para contador locais
							for (int l=0; l < arrayLocaisCount ; l++){
								jsonArrayLocais.getJSONObject(l).opt("sequencia_parada").toString();
								jsonArrayLocais.getJSONObject(l).opt("tipo_parada").toString();
								jsonArrayLocais.getJSONObject(l).opt("vloc_descricao").toString();
								jsonArrayLocais.getJSONObject(l).opt("cida_descricao_ibge").toString();
								jsonArrayLocais.getJSONObject(l).opt("sigla_estado").toString();
								jsonArrayLocais.getJSONObject(l).opt("pais").toString();
								jsonArrayLocais.getJSONObject(l).opt("refe_latitude").toString();
								jsonArrayLocais.getJSONObject(l).opt("refe_longitude").toString();
								jsonArrayLocais.getJSONObject(l).opt("refe_km").toString();
								jsonArrayLocais.getJSONObject(l).opt("previsao_chegada").toString();								
								
							}
							
						}
						
						// Acesso json,  valor_frete	
						// Filipe em 30/10/2020 - utilizar viag_valor_carga
						JSONArray jsonArrayFrete = objJsonArray.getJSONObject(i).getJSONArray("valor_frete");
						
						int arrayFreteCount = 0;
						
						// se encontrou json valor_frete --- Aqui foi desativado pois não trazia o valor do frete corretamente (05/11/2020)
						if(jsonArrayFrete != null){
							
							// contador json valor_frete
							arrayFreteCount = jsonArrayFrete.length();
							
							// for para json valor_frete
							for (int f=0; f < arrayFreteCount ; f++){

								String vlrFreteStg = jsonArrayFrete.getJSONObject(f).opt("valor_frete").toString();
								
								// Encontrou valor_frete no json
								if(!vlrFreteStg.equals("null")){									
									//viag_valor_carga	
									//BigDecimal vlrFrete = new BigDecimal(jsonArrayLocais.getJSONObject(f).optDouble("valor_frete"));

									// Persistencia vlrFrete
									//eiv.setValor(vlrFrete);
									
								} else {
									//eiv.setValor(BigDecimal.ZERO);
								}
																
							} // final for arrayFreteCount
							
						} // final if jsonArrayFrete
						
					} // final if docTransportador
					
					
					// Buscar Area pelo docTransportador
					EntityManager em = areaService.crud().getEntityManager();
					
					String query = "select a.areaoid as id from java.area a  " +
								   " where a.area_anvloid = 2 " + 
								   " and a.area_dt_exclusao is null" +
								   " and a.area_pesoid_responsavel in (select p.pesoid from java.pessoa p where p.pes_documento1 = '" + docTransportador + "')" ;
					
					lstAreaPorPessoaDoc =  em.createNativeQuery(query,"ListAreaPorPessoaDocumentoMapping").getResultList();
					
					int res = lstAreaPorPessoaDoc.size();
					
					if( res > 0){						

						// Persistencia 
						eiv.setArea(new Area(lstAreaPorPessoaDoc.get(0).getId()));
						
					}
					/// linha do ERRO
					// Final persistencia EventoInfoViagem
					eiv.setId(ev.getId());				
					evInfoViagService.crud().save(eiv);
					
					// Tecnologia (tabela evento_info_rastreador)
					ArrayList<String> listaIdRastreador = new ArrayList<>();
					ArrayList<String> listaIdTecnologia = new ArrayList<>();
					
					// Abre a listArray para iniciar o processo de persistencia
					if(listArray.size() >= 0){
						
						// for do listArray
						for(int ei=0; ei < listArray.size();ei++){

							// Instancia  evento_info_rastreador
							EventoInfoRastreador eir = new EventoInfoRastreador();
							
							eir.setEvInfoViagem(eiv);
							eir.setCodigo(listArray.get(ei).getCodigo());
							eir.setDescricao(listArray.get(ei).getDescricao());
							eir.setTelemonitorado(listArray.get(ei).getTelemonitorado());
							eir.setNumeroTerminal(listArray.get(ei).getNumeroTerminal());
							eir.setVtecCodigo(listArray.get(ei).getVtecCodigo());
							eir.setIsca(listArray.get(ei).getIsca());
							
							// Salvar a instancia
							evInfoRastreadorService.crud().save(eir);
							
							//Lista Rastreador e Tecnologia para xml
							listaIdRastreador.add(listArray.get(ei).getCodigo());
							listaIdTecnologia.add(listArray.get(ei).getDescricao());
							
						}
						
					} // final if listaArray
					
					// Campos para atualização 
					eiv.setVeiculoIdRastreador(listaIdRastreador.toString());
					eiv.setVeiculoIdTecnologia(listaIdTecnologia.toString());					
					eiv.setRastreador(new EventoInfoRastreador(eiv.getId()));
					
					// Update evento_info_viagem
					evInfoViagService.crud().update(eiv);
					
				} // final for contador de viagem
				
			} // Final if consulta WS			
		
			
		} // Final if idViagem
				
	}

	
	private String interpretarMCT(String codMctViagem) {

		char[] codM = codMctViagem.toCharArray();

		int sizeString = codMctViagem.length();
		int countAntesPipe = 0;
		int countPosPipe = 0;

		String codMct = "";

		for (int i = 0; i < sizeString; ++i) {

			if (!String.valueOf(codM[i]).equals("|")) {
				++countAntesPipe;
			}
		}

		codMct = codMctViagem.substring(0, 7);
		return codMct;

	}

	
	public void meuEmailGlobal5(String emailGlobal5){
		
		if(!emailGlobal5.equals("")){
			this.setAddEmailGlobal5(emailGlobal5);
		}
		
	}
	
	public void addEmailGlobal5NaLista(Integer nivel){
		
		Integer idNivel = nivel;
		
		JSONObject obj = new JSONObject();
		
	    String EMAIL_REGEX = "^[_a-z0-9-\\+]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9]+)*(\\.[a-z]{2,})$";
	    
	    Pattern pattern;

		Matcher matcher = null;

		pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
		
		if ( idNivel == 2 ) {
			matcher = pattern.matcher(addEmailGlobal5.trim());
			this.setNivel2(2);
		}
		
		boolean resultEmail = matcher.matches();
		
		if (resultEmail == true){
			
			if ( idNivel == 2) {
				
				lstEmailGlobal5.add(addEmailGlobal5.toLowerCase());
				
				/*
				lstEmailNfse.add(emailNfse.toLowerCase());
				
				AreaEmailNfseJson emailNfseJ = new AreaEmailNfseJson();							
				
				emailNfseJ.setEmail(emailNfse.toLowerCase());
				obj.put("email", emailNfseJ.getEmail());
			   */
				obj.put("email", addEmailGlobal5.toLowerCase());
				
				arrayJsonGlobal5.put(obj);
				
				this.setAddEmailGlobal5("");
				
			}
		
			// Teste para coletar emails para ser atualizada na tabela de eventos
			int qtdEmail = lstEmailGlobal5.size();
			
			if(qtdEmail > 0){
				for(int i = 0 ; i < qtdEmail ; i++){
					emailListGlobal5 +=  lstEmailGlobal5.get(i) + ";";
				}
			}

			// Atualizar página	
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmRegistro:frmBoletim:frmDadosBoletim:frmDadosEmailEnvio:inputEmailNfse");
			//context.update("frmRegistro:frmBoletim:frmDadosBoletim:frmDadosEmailEnvio:listEmailNfs");
			//context.update("frmCadastroCnpj:frmCadastroArea:inputEmailNfse");				
			
			
		}
	}
	
	public void coletarEmailGlobal5(SelectEvent event){
		
		if ( nivel2 == 2 ) {
			
			this.setContaEmailMultiploGlobal5((String) event.getObject());
			
			//int zer = lstEmailNfse.indexOf(contaEmailMultiploNfse);
			int zer = lstEmailGlobal5.indexOf(contaEmailMultiploGlobal5);
			
			//lstEmailNfse.remove(contaEmailMultiploNfse);
			lstEmailGlobal5.remove(contaEmailMultiploGlobal5);
			
			int qtdEmail = lstEmailGlobal5.size();
			
			if(qtdEmail >= 0){
				for(int i = 0 ; i < qtdEmail ; i++){
					emailListGlobal5 = emailListGlobal5 + lstEmailGlobal5.get(i) + ";";
				}
			}
			
			//arrayJsonNfse.remove(zer);
			arrayJsonGlobal5.remove(zer);
			
			this.setContaEmailMultiploGlobal5("");
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("frmRegistro:frmBoletim:frmDadosBoletim:frmDadosEmailEnvio:listEmailG5");
		
	}
	
	public void checarBotaoPesquisar(Integer numPesquisa){
		
		if(numPesquisa == 1){
			ativarPesquisarEventosPorCriterios = true;
			getListEventos();
		} else {
			ativarPesquisarEventosPorCriterios = false;
		}
	}

	// Final teste novo método ws chp
	
	public Evento getEv() {
		return ev;
	}

	public void setEv(Evento ev) {
		this.ev = ev;
	}

	public EventoInfoViagem getEiv() {
		return eiv;
	}

	public void setEiv(EventoInfoViagem eiv) {
		this.eiv = eiv;
	}

	public Integer getIdEiv() {
		return idEiv;
	}

	public void setIdEiv(Integer idEiv) {
		this.idEiv = idEiv;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdBi() {
		return idBi;
	}

	public void setIdBi(Integer idBi) {
		this.idBi = idBi;
	}

	public boolean isPainelDadosSinistro() {
		return painelDadosSinistro;
	}

	public void setPainelDadosSinistro(boolean painelDadosSinistro) {
		this.painelDadosSinistro = painelDadosSinistro;
	}

	public Character getTipoSelecaoBi() {
		return tipoSelecaoBi;
	}

	public void setTipoSelecaoBi(Character tipoSelecaoBi) {
		this.tipoSelecaoBi = tipoSelecaoBi;
	}

	public boolean isPainelClassSinistroPerda() {
		return painelClassSinistroPerda;
	}

	public void setPainelClassSinistroPerda(boolean painelClassSinistroPerda) {
		this.painelClassSinistroPerda = painelClassSinistroPerda;
	}

	public boolean isPainelClassSinistroPerdaDano() {
		return painelClassSinistroPerdaDano;
	}

	public void setPainelClassSinistroPerdaDano(boolean painelClassSinistroPerdaDano) {
		this.painelClassSinistroPerdaDano = painelClassSinistroPerdaDano;
	}

	public boolean isPainelClassSinistroDano() {
		return painelClassSinistroDano;
	}

	public void setPainelClassSinistroDano(boolean painelClassSinistroDano) {
		this.painelClassSinistroDano = painelClassSinistroDano;
	}

	public boolean isPainelClassSuspeitaRoubo() {
		return painelClassSuspeitaRoubo;
	}

	public void setPainelClassSuspeitaRoubo(boolean painelClassSuspeitaRoubo) {
		this.painelClassSuspeitaRoubo = painelClassSuspeitaRoubo;
	}

	public boolean isPainelDadosSuspeita() {
		return painelDadosSuspeita;
	}

	public void setPainelDadosSuspeita(boolean painelDadosSuspeita) {
		this.painelDadosSuspeita = painelDadosSuspeita;
	}

	public List<TipoSuspeita> getLstTipoSuspeita() {
		return lstTipoSuspeita;
	}

	public void setLstTipoSuspeita(List<TipoSuspeita> lstTipoSuspeita) {
		this.lstTipoSuspeita = lstTipoSuspeita;
	}

	public EventoService getEvService() {
		return evService;
	}

	public void setEvService(EventoService evService) {
		this.evService = evService;
	}

	public BoletimInformativoService getBiService() {
		return biService;
	}

	public void setBiService(BoletimInformativoService biService) {
		this.biService = biService;
	}

	public EventoInfoViagemService getEvInfoViagService() {
		return evInfoViagService;
	}

	public void setEvInfoViagService(EventoInfoViagemService evInfoViagService) {
		this.evInfoViagService = evInfoViagService;
	}

	public TipoSuspeitaService getTipoSuspService() {
		return tipoSuspService;
	}

	public void setTipoSuspService(TipoSuspeitaService tipoSuspService) {
		this.tipoSuspService = tipoSuspService;
	}

	public String getEventoInicial() {
		return eventoInicial;
	}

	public void setEventoInicial(String eventoInicial) {
		this.eventoInicial = eventoInicial;
	}

	public Integer getNumeroSM() {
		return numeroSM;
	}

	public void setNumeroSM(Integer numeroSM) {
		this.numeroSM = numeroSM;
	}

	public Crud<Evento> getEventoCrud() {
		return eventoCrud;
	}

	public void setEventoCrud(Crud<Evento> eventoCrud) {
		this.eventoCrud = eventoCrud;
	}

	public Integer getNumIdEvento() {
		return numIdEvento;
	}

	public void setNumIdEvento(Integer numIdEvento) {
		this.numIdEvento = numIdEvento;
	}

	public List<Evento> getListEventos() {
		if(ativarPesquisarEventosPorCriterios == false) {
			listarEventos();
		} else {
			pesquisarEventosPorCriterios();	
		}
		return listEventos;
	}

	public void setListEventos(List<Evento> listEventos) {
		this.listEventos = listEventos;
	}
	
	public List<BoletimInformativo> getListBoletimPorEvento() {
		listarBoletimPorEvento();
		return listBoletimPorEvento;
	}

	public void setListBoletimPorEvento(List<BoletimInformativo> listBoletimPorEvento) {
		this.listBoletimPorEvento = listBoletimPorEvento;
	}

	public boolean isEventoAdicionado() {
		return eventoAdicionado;
	}

	public void setEventoAdicionado(boolean eventoAdicionado) {
		this.eventoAdicionado = eventoAdicionado;
	}

	public boolean isBoletimFinal() {
		return boletimFinal;
	}

	public void setBoletimFinal(boolean boletimFinal) {
		this.boletimFinal = boletimFinal;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public boolean isHabilitaDivBoletimFinal() {
		return habilitaDivBoletimFinal;
	}

	public void setHabilitaDivBoletimFinal(boolean habilitaDivBoletimFinal) {
		this.habilitaDivBoletimFinal = habilitaDivBoletimFinal;
	}

	public int getAnoVigente() {
		return anoVigente;
	}

	public void setAnoVigente(int anoVigente) {
		this.anoVigente = anoVigente;
	}

	public int getQtdBiPerdas() {
		return qtdBiPerdas;
	}

	public void setQtdBiPerdas(int qtdBiPerdas) {
		this.qtdBiPerdas = qtdBiPerdas;
	}

	public int getQtdBiDanos() {
		return qtdBiDanos;
	}

	public void setQtdBiDanos(int qtdBiDanos) {
		this.qtdBiDanos = qtdBiDanos;
	}

	public int getQtdBiSuspeitas() {
		return qtdBiSuspeitas;
	}

	public void setQtdBiSuspeitas(int qtdBiSuspeitas) {
		this.qtdBiSuspeitas = qtdBiSuspeitas;
	}
	
	public boolean isHabilitarBtnGerarEvento() {
		return habilitarBtnGerarEvento;
	}

	public void setHabilitarBtnGerarEvento(boolean habilitarBtnGerarEvento) {
		this.habilitarBtnGerarEvento = habilitarBtnGerarEvento;
	}

	public boolean isHabilitarBtnAbrirGerarBoletim() {
		return habilitarBtnAbrirGerarBoletim;
	}

	public void setHabilitarBtnAbrirGerarBoletim(boolean habilitarBtnAbrirGerarBoletim) {
		this.habilitarBtnAbrirGerarBoletim = habilitarBtnAbrirGerarBoletim;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public boolean isEventoFinalizado() {
		return eventoFinalizado;
	}

	public void setEventoFinalizado(boolean eventoFinalizado) {
		this.eventoFinalizado = eventoFinalizado;
	}

	public List<EventoAnexo> getListEventoAnexo() {
		listarAnexosPorEvento();
		return listEventoAnexo;
	}

	public void setListEventoAnexo(List<EventoAnexo> listEventoAnexo) {
		this.listEventoAnexo = listEventoAnexo;
	}

	public List<TipoAnexo> getLstTipoAnexo() {
		return lstTipoAnexo;
	}

	public void setLstTipoAnexo(List<TipoAnexo> lstTipoAnexo) {
		this.lstTipoAnexo = lstTipoAnexo;
	}

	public Integer getIdTipoAnexo() {
		return idTipoAnexo;
	}

	public void setIdTipoAnexo(Integer idTipoAnexo) {
		this.idTipoAnexo = idTipoAnexo;
	}

	public String getDescArquivoAnexo() {
		return descArquivoAnexo;
	}

	public void setDescArquivoAnexo(String descArquivoAnexo) {
		this.descArquivoAnexo = descArquivoAnexo;
	}

	public String getMsgArquivoUpload() {
		return msgArquivoUpload;
	}

	public void setMsgArquivoUpload(String msgArquivoUpload) {
		this.msgArquivoUpload = msgArquivoUpload;
	}

	public boolean isBtnRegistrarAnexoLiberado() {
		return btnRegistrarAnexoLiberado;
	}

	public void setBtnRegistrarAnexoLiberado(boolean btnRegistrarAnexoLiberado) {
		this.btnRegistrarAnexoLiberado = btnRegistrarAnexoLiberado;
	}

	public String getArquivoTipoExtensao() {
		return arquivoTipoExtensao;
	}

	public void setArquivoTipoExtensao(String arquivoTipoExtensao) {
		this.arquivoTipoExtensao = arquivoTipoExtensao;
	}

	public String getArquivoNome() {
		return arquivoNome;
	}

	public void setArquivoNome(String arquivoNome) {
		this.arquivoNome = arquivoNome;
	}

	public String getDescArquivoAnexoFront() {
		return descArquivoAnexoFront;
	}

	public void setDescArquivoAnexoFront(String descArquivoAnexoFront) {
		this.descArquivoAnexoFront = descArquivoAnexoFront;
	}

	public List<AreaPorPessoaDocumento> getLstAreaPorPessoaDoc() {
		return lstAreaPorPessoaDoc;
	}

	public void setLstAreaPorPessoaDoc(List<AreaPorPessoaDocumento> lstAreaPorPessoaDoc) {
		this.lstAreaPorPessoaDoc = lstAreaPorPessoaDoc;
	}

	public String getStatusTabelaEvento() {
		return statusTabelaEvento;
	}

	public void setStatusTabelaEvento(String statusTabelaEvento) {
		this.statusTabelaEvento = statusTabelaEvento;
	}

	public String getMsgNomeTransportadorChp() {
		return msgNomeTransportadorChp;
	}

	public String getMsgCnpjTransportadorChp() {
		return msgCnpjTransportadorChp;
	}

	public void setMsgNomeTransportadorChp(String msgNomeTransportadorChp) {
		this.msgNomeTransportadorChp = msgNomeTransportadorChp;
	}

	public void setMsgCnpjTransportadorChp(String msgCnpjTransportadorChp) {
		this.msgCnpjTransportadorChp = msgCnpjTransportadorChp;
	}

	public String getEmailsParaEnvioBIRastreamento() {
		return emailsParaEnvioBIRastreamento;
	}

	public void setEmailsParaEnvioBIRastreamento(String emailsParaEnvioBIRastreamento) {
		this.emailsParaEnvioBIRastreamento = emailsParaEnvioBIRastreamento;
	}

	public List<String> getLstEmailGlobal5() {
		return lstEmailGlobal5;
	}

	public void setLstEmailGlobal5(List<String> lstEmailGlobal5) {
		this.lstEmailGlobal5 = lstEmailGlobal5;
	}

	public String getAddEmailGlobal5() {
		return addEmailGlobal5;
	}

	public void setAddEmailGlobal5(String addEmailGlobal5) {
		this.addEmailGlobal5 = addEmailGlobal5;
	}

	public JSONArray getArrayJsonGlobal5() {
		return arrayJsonGlobal5;
	}

	public void setArrayJsonGlobal5(JSONArray arrayJsonGlobal5) {
		this.arrayJsonGlobal5 = arrayJsonGlobal5;
	}

	public Integer getNivel2() {
		return nivel2;
	}

	public void setNivel2(Integer nivel2) {
		this.nivel2 = nivel2;
	}	
	
	public String getContaEmailMultiploGlobal5() {
		return contaEmailMultiploGlobal5;
	}

	public void setContaEmailMultiploGlobal5(String contaEmailMultiploGlobal5) {
		this.contaEmailMultiploGlobal5 = contaEmailMultiploGlobal5;
	}

	public String getEmailListGlobal5() {
		return emailListGlobal5;
	}

	public void setEmailListGlobal5(String emailListGlobal5) {
		this.emailListGlobal5 = emailListGlobal5;
	}

	public String getNomeClienteChp() {
		return nomeClienteChp;
	}

	public String getNomeMotorista1Chp() {
		return nomeMotorista1Chp;
	}

	public String getCpfMotorista1Chp() {
		return cpfMotorista1Chp;
	}

	public String getNomeMotorista2Chp() {
		return nomeMotorista2Chp;
	}

	public String getCpfMotorista2Chp() {
		return cpfMotorista2Chp;
	}

	public String getPlacaCavaloChp() {
		return placaCavaloChp;
	}

	public String getCorCavaloChp() {
		return corCavaloChp;
	}

	public String getModeloCavaloChp() {
		return modeloCavaloChp;
	}

	public String getPlacaReboque1Chp() {
		return placaReboque1Chp;
	}

	public String getCorReboque1Chp() {
		return corReboque1Chp;
	}

	public String getModeloReboque1Chp() {
		return modeloReboque1Chp;
	}

	public String getPlacaReboque2Chp() {
		return placaReboque2Chp;
	}

	public String getCorReboque2Chp() {
		return corReboque2Chp;
	}

	public String getModeloReboque2Chp() {
		return modeloReboque2Chp;
	}

	public String getPlacaReboque3Chp() {
		return placaReboque3Chp;
	}

	public String getCorReboque3Chp() {
		return corReboque3Chp;
	}

	public String getModeloReboque3Chp() {
		return modeloReboque3Chp;
	}

	public void setNomeClienteChp(String nomeClienteChp) {
		this.nomeClienteChp = nomeClienteChp;
	}

	public void setNomeMotorista1Chp(String nomeMotorista1Chp) {
		this.nomeMotorista1Chp = nomeMotorista1Chp;
	}

	public void setCpfMotorista1Chp(String cpfMotorista1Chp) {
		this.cpfMotorista1Chp = cpfMotorista1Chp;
	}

	public void setNomeMotorista2Chp(String nomeMotorista2Chp) {
		this.nomeMotorista2Chp = nomeMotorista2Chp;
	}

	public void setCpfMotorista2Chp(String cpfMotorista2Chp) {
		this.cpfMotorista2Chp = cpfMotorista2Chp;
	}

	public void setPlacaCavaloChp(String placaCavaloChp) {
		this.placaCavaloChp = placaCavaloChp;
	}

	public void setCorCavaloChp(String corCavaloChp) {
		this.corCavaloChp = corCavaloChp;
	}

	public void setModeloCavaloChp(String modeloCavaloChp) {
		this.modeloCavaloChp = modeloCavaloChp;
	}

	public void setPlacaReboque1Chp(String placaReboque1Chp) {
		this.placaReboque1Chp = placaReboque1Chp;
	}

	public void setCorReboque1Chp(String corReboque1Chp) {
		this.corReboque1Chp = corReboque1Chp;
	}

	public void setModeloReboque1Chp(String modeloReboque1Chp) {
		this.modeloReboque1Chp = modeloReboque1Chp;
	}

	public void setPlacaReboque2Chp(String placaReboque2Chp) {
		this.placaReboque2Chp = placaReboque2Chp;
	}

	public void setCorReboque2Chp(String corReboque2Chp) {
		this.corReboque2Chp = corReboque2Chp;
	}

	public void setModeloReboque2Chp(String modeloReboque2Chp) {
		this.modeloReboque2Chp = modeloReboque2Chp;
	}

	public void setPlacaReboque3Chp(String placaReboque3Chp) {
		this.placaReboque3Chp = placaReboque3Chp;
	}

	public void setCorReboque3Chp(String corReboque3Chp) {
		this.corReboque3Chp = corReboque3Chp;
	}

	public void setModeloReboque3Chp(String modeloReboque3Chp) {
		this.modeloReboque3Chp = modeloReboque3Chp;
	}

	public String getVinculoMotorista1Chp() {
		return vinculoMotorista1Chp;
	}

	public String getVinculoMotorista2Chp() {
		return vinculoMotorista2Chp;
	}

	public void setVinculoMotorista1Chp(String vinculoMotorista1Chp) {
		this.vinculoMotorista1Chp = vinculoMotorista1Chp;
	}

	public void setVinculoMotorista2Chp(String vinculoMotorista2Chp) {
		this.vinculoMotorista2Chp = vinculoMotorista2Chp;
	}

	public String getMarcaCavaloChp() {
		return marcaCavaloChp;
	}

	public void setMarcaCavaloChp(String marcaCavaloChp) {
		this.marcaCavaloChp = marcaCavaloChp;
	}

	public String getAnoCavaloChp() {
		return anoCavaloChp;
	}

	public void setAnoCavaloChp(String anoCavaloChp) {
		this.anoCavaloChp = anoCavaloChp;
	}

	public String getCnpjClienteChp() {
		return cnpjClienteChp;
	}

	public void setCnpjClienteChp(String cnpjClienteChp) {
		this.cnpjClienteChp = cnpjClienteChp;
	}

	public String getCancelaRetorno() {
		return cancelaRetorno;
	}

	public void setCancelaRetorno(String cancelaRetorno) {
		this.cancelaRetorno = cancelaRetorno;
	}

	public String getNomeClienteCancel() {
		return nomeClienteCancel;
	}

	public Integer getIdEventoCancel() {
		return idEventoCancel;
	}

	public Integer getSmViagemCancel() {
		return smViagemCancel;
	}

	public void setNomeClienteCancel(String nomeClienteCancel) {
		this.nomeClienteCancel = nomeClienteCancel;
	}

	public void setIdEventoCancel(Integer idEventoCancel) {
		this.idEventoCancel = idEventoCancel;
	}

	public void setSmViagemCancel(Integer smViagemCancel) {
		this.smViagemCancel = smViagemCancel;
	}

	public boolean isChpNaoRetornaInformacao() {
		return chpNaoRetornaInformacao;
	}

	public void setChpNaoRetornaInformacao(boolean chpNaoRetornaInformacao) {
		this.chpNaoRetornaInformacao = chpNaoRetornaInformacao;
	}

	public List<AreaQry> getLstAreaQry() {
		return lstAreaQry;
	}

	public void setLstAreaQry(List<AreaQry> lstAreaQry) {
		this.lstAreaQry = lstAreaQry;
	}

	public String getPlacaPesq() {
		return placaPesq;
	}

	public String getMotoristaPesq() {
		return motoristaPesq;
	}

	public Date getPesqDtInicio() {
		return pesqDtInicio;
	}

	public Date getPesqDtFinal() {
		return pesqDtFinal;
	}

	public void setPlacaPesq(String placaPesq) {
		this.placaPesq = placaPesq;
	}

	public void setMotoristaPesq(String motoristaPesq) {
		this.motoristaPesq = motoristaPesq;
	}

	public void setPesqDtInicio(Date pesqDtInicio) {
		this.pesqDtInicio = pesqDtInicio;
	}

	public void setPesqDtFinal(Date pesqDtFinal) {
		this.pesqDtFinal = pesqDtFinal;
	}



	public char getTipoEventoPesq() {
		return tipoEventoPesq;
	}

	public void setTipoEventoPesq(char tipoEventoPesq) {
		this.tipoEventoPesq = tipoEventoPesq;
	}

	public boolean isAtivarPesquisarEventosPorCriterios() {
		return ativarPesquisarEventosPorCriterios;
	}

	public void setAtivarPesquisarEventosPorCriterios(boolean ativarPesquisarEventosPorCriterios) {
		this.ativarPesquisarEventosPorCriterios = ativarPesquisarEventosPorCriterios;
	}

	public char getTipoEventoFinalPesq() {
		return tipoEventoFinalPesq;
	}

	public void setTipoEventoFinalPesq(char tipoEventoFinalPesq) {
		this.tipoEventoFinalPesq = tipoEventoFinalPesq;
	}	
	
	
	
}
