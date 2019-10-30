package br.com.global5.rastreamento.bean.bi;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.io.FilenameUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.rastreamento.model.bi.BoletimInformativo;
import br.com.global5.rastreamento.model.bi.BoletimInformativoNumeracao;
import br.com.global5.rastreamento.model.bi.CheckBI;
import br.com.global5.rastreamento.model.bi.Evento;
import br.com.global5.rastreamento.model.bi.EventoAnexo;
import br.com.global5.rastreamento.model.bi.EventoInfoViagem;
import br.com.global5.rastreamento.model.enums.TipoAnexo;
import br.com.global5.rastreamento.model.enums.TipoSuspeita;
import br.com.global5.rastreamento.service.bi.BoletimInformativoNumeracaoService;
import br.com.global5.rastreamento.service.bi.BoletimInformativoService;
import br.com.global5.rastreamento.service.bi.EventoAnexoService;
import br.com.global5.rastreamento.service.bi.EventoInfoViagemService;
import br.com.global5.rastreamento.service.bi.EventoService;
import br.com.global5.rastreamento.service.bi.TipoAnexoService;
import br.com.global5.rastreamento.service.bi.TipoSuspeitaService;
import br.com.global5.template.exception.BusinessException;

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
	
	private Character tipoSelecaoBi = null;
	private String    eventoInicial = "";
	private Integer   numeroSM		= null;
	private Integer   numIdEvento= null;
	
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
	private boolean eventoFinalizado = false;
	
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
		ev.setBoletimInformativo(new BoletimInformativo());
		
		lstTipoSuspeita = tipoSuspService.crud().isNull("exclusao").listAll();
		lstTipoAnexo = TipoAnexoService.crud().isNull("exclusao").listAll();
		 
		uploadedFiles = new ArrayList<UploadedFile>();
		descArquivoAnexo = "";
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
	
	
	public void eventoSelecionadoEmissaoBoletim(Integer idEvento ){

		numIdEvento = idEvento;
			tipoSelecaoBi = null;
			habilitarBtnAbrirGerarBoletim = false;
			this.id = idEvento;
			ev = evService.crud().get(getId());
			
			if( ev.getBoletimInformativo() == null) {
				checkBi = CheckBI.getInstancia();
				checkBi.recebeIdBI(null);
				checkBi.recebeIdEv(idEvento);				
				this.setHabilitaDivBoletimFinal(false);
				RequestContext context = RequestContext.getCurrentInstance();
				context.update("formListaEventos:dlgAddBoletim");
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
			limparCamposPesquisa();
			numIdEvento = null;
			init();
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
	
	
	public void checarSmParaAbrirEvento(){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		Integer numSMDigitado = numeroSM;

		// Verificar a existência da SM registrada na tabela de evento
		Criteria criteria = evService.crud().getSession().createCriteria(Evento.class);
		criteria.add(Restrictions.eq("sincViagem.id", numSMDigitado));
		criteria.add(Restrictions.isNull("dtFinalizacao"));
		
		int result = criteria.list().size();
		
		// Se encontrou	SM no Evento em aberto ...
		if (result > 0) {

			Evento evCheck = (Evento) criteria.uniqueResult();
			
			if (numSMDigitado == evCheck.getSincViagem().getId().intValue()){
				
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
			} 
			
			// Para número de SM não existente ou com status não permitido para abertura de evento será tratada na chamada da procedure
					
		} 
			else
			
				// Se não encontrado SM aberto será liberado o botão para gerar o Evento 			
			{
						
				RequestContext context = RequestContext.getCurrentInstance();
				habilitarBtnGerarEvento = true;				
				context.update("formListaEventos:btnAddEvento");				
		}
		
	}
	
	
	public void gerarNovoEvento(){
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		Integer idEventoReturn = null;	
		
		try {

			StoredProcedureQuery qryNovoEv = eventoCrud.getEntityManager().createNamedStoredProcedureQuery("evento_i");
			qryNovoEv.setParameter("sm", numeroSM);
			qryNovoEv.setParameter("usuoid", checkUsuario.valid().getId());			
			qryNovoEv.execute();
						
			idEventoReturn = (Integer) qryNovoEv.getOutputParameterValue("evento");
			
			Long result = idEventoReturn.longValue();
			
			if(result > 1){
				numIdEvento = idEventoReturn;	
				eventoAdicionado = true;
				habilitarBtnGerarEvento = false;	
				this.getListEventos();
				
			} else {

				habilitarBtnGerarEvento = false;									
				numeroSM = null;
				
				RequestContext context = RequestContext.getCurrentInstance();
				context.update("formListaEventos:btnAddEvento");
				context.update("formListaEventos:inptNumSM");

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "SM informada não foi encontrada!") );		
			}
			
		} catch (Exception e) {
			e.printStackTrace(pw);
			
		}	
		
	}
	
	
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
	
	public void checarTipoSelecionado(){
		if(tipoSelecaoBi == null){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Defina uma opção para o Boletim Tipo."));
			return;
		} 
	}
		
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
		
		int result = criteria.list().size();
		
		if ( result > 0 ) {
			this.setListEventoAnexo(criteria.list());
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
				
		int result = criteria.list().size();
		
		if ( result > 0) {
			
			this.setListBoletimPorEvento(criteria.list());			
			
		} else {
			this.setListBoletimPorEvento(null);
		}
		
	}
	
	public void limparCamposPesquisa(){
		numeroSM = null;
		eventoFinalizado = false;
	}
	
	public void pesquisarEventosPorCriterios(){
			
		Criteria criteria = evService.crud().getSession().createCriteria(Evento.class);
		criteria.addOrder(Order.asc("id"));
		criteria.addOrder(Order.asc("sincViagem.id"));
		criteria.addOrder(Order.asc("dtCriacao"));
		
		if (numeroSM != null) {
			
			// Verificar a existência da SM registrada na tabela de evento
			
			Criteria criSM = evService.crud().getSession().createCriteria(Evento.class);
			criSM.add(Restrictions.eq("sincViagem.id", numeroSM));
			
			int resultSM = criSM.list().size();
			
			// Se encontrou	SM no Evento em aberto ...
			if (resultSM > 0) {
				criteria.add(Restrictions.eq("sincViagem.id", numeroSM));
			} 
			
			if ( resultSM == 0 ){
				//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção", "SM não encontrada no cadastro de eventos!"));
				//criteria.add(Restrictions.eq("sincViagem.id", null)); 				
			}

		}
		
//		if ( !nomeCliente.equals("") ) {
//			
//			String a = nomeCliente;
//			String b = a;
//			
//			criteria.add(Restrictions.ilike("sincViagem.cliente1.razaoSocial", nomeCliente, MatchMode.ANYWHERE ));
//			
//		}
		
		if ( eventoFinalizado == true ) {
			
			criteria.add(Restrictions.isNotNull("dtFinalizacao"));
			
		}
		
		if ( eventoFinalizado == false ) {
			
			criteria.add(Restrictions.isNull("dtFinalizacao"));
			
		}
		
		int result = criteria.list().size();
		
		if ( result > 0) {
			
			this.setListEventos(criteria.list());			
			
		} 
		
	}
	
	private void listarEventos() {
		
		Criteria criteria = evService.crud().getSession().createCriteria(Evento.class);
		criteria.addOrder(Order.asc("id"));
		criteria.addOrder(Order.asc("sincViagem.id"));
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
			
			sizeP = biNumP.getContador();
			
			this.setQtdBiPerdas((int) sizeP);
			
			
			// Qtde de Boletim emitidos para Danos
			Criteria criteriaD = biNumService.crud().getSession().createCriteria(BoletimInformativoNumeracao.class);
			criteriaD.add(Restrictions.eq("ano" , anoAtual));
			criteriaD.add(Restrictions.eq("tipo" , 'D'));
			
			BoletimInformativoNumeracao biNumD = (BoletimInformativoNumeracao) criteriaD.uniqueResult();
			
			sizeD = biNumD.getContador();
			
			this.setQtdBiDanos((int) sizeD);
			
			
			// Qtde de Boletim emitidos para Suspeitas
			Criteria criteriaS = biNumService.crud().getSession().createCriteria(BoletimInformativoNumeracao.class);
			criteriaS.add(Restrictions.eq("ano" , anoAtual));
			criteriaS.add(Restrictions.eq("tipo" , 'S'));
			
			BoletimInformativoNumeracao biNumS = (BoletimInformativoNumeracao) criteriaS.uniqueResult();
			
			sizeS = biNumS.getContador();
			
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
		String dir = AppUtils.dirAnexosBI + ev.getSincViagem().getId() + "/" + ev.getNrEvento() + "/" ;
		
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
			String dir = AppUtils.dirAnexosBI + evAnexoDel.getSincViagem().getId() + "/" + evAnexoDel.getNumEvento()  + "/" ;
		
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
		//listarEventos();
		pesquisarEventosPorCriterios();
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


	
	
			
}
