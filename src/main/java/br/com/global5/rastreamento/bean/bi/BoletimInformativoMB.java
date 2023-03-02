package br.com.global5.rastreamento.bean.bi;


import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.StoredProcedureQuery;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.RelatorioUtil;
import br.com.global5.infra.util.UtilException;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.rastreamento.infra.ViagemClient;
import br.com.global5.rastreamento.model.bi.BoletimInformativo;
import br.com.global5.rastreamento.model.bi.BoletimInformativoNumeracao;
import br.com.global5.rastreamento.model.bi.BoletimInformativoRelato;
import br.com.global5.rastreamento.model.bi.CheckBI;
import br.com.global5.rastreamento.model.bi.Evento;
import br.com.global5.rastreamento.model.bi.EventoInfoViagem;
import br.com.global5.rastreamento.model.enums.TipoDano;
import br.com.global5.rastreamento.model.enums.TipoPerda;
import br.com.global5.rastreamento.model.enums.TipoSuspeita;
import br.com.global5.rastreamento.model.trafegus.EventoInfoRastreador;
import br.com.global5.rastreamento.model.trafegus.VersaoTecnologia;
import br.com.global5.rastreamento.service.bi.BoletimInformativoRelatoService;
import br.com.global5.rastreamento.service.bi.BoletimInformativoService;
import br.com.global5.rastreamento.service.bi.EventoInfoRastreadorService;
import br.com.global5.rastreamento.service.bi.EventoInfoViagemService;
import br.com.global5.rastreamento.service.bi.EventoService;
import br.com.global5.rastreamento.service.bi.TipoSuspeitaService;
import br.com.global5.template.exception.BusinessException;

@Named
@ViewAccessScoped
public class BoletimInformativoMB implements Serializable {

	private static final long serialVersionUID = 212151315511555L;

	private BoletimInformativo bi;
	
	private Evento ev;
	private EventoInfoViagem eiv;
	private Usuario usuario;
	
	private CheckBI checkBi;
	
	private Integer id;
	private Integer idEiv;
	private Integer idBi;
	
	private List<TipoSuspeita> lstTipoSuspeita;
	private List<BoletimInformativoRelato> listBiRelato;
	
	private Character tipoSelecaoBi = null;
	private String 	  tipoPerdaSel = null;
	private String 	  tipoDanoSel = null;
	
	private String    eventoInicial = "";
	private Integer   numeroSM		= null;
	private Integer   numIdEvento= null;
	private Integer	  numIdTpSuspeita = 653; // tentativa de roubo 
	private Integer   codTipoSuspeitaClone = null;
	
	// Emails Global5
	private List<String>lstEmailGlobal5;
	private String addEmailGlobal5 = "";
	private JSONArray arrayJsonGlobal5;
	private Integer nivel2 = null;
	//private String contaEmailMultiploNfse = "";
	private String contaEmailMultiploGlobal5 = "";
	String emailListGlobal5 = "";
	String emailCcParaBoletim = "";
	
	private boolean painelDadosSinistro 		 = false;
	private boolean painelDadosSuspeita 		 = false;
	
	private boolean painelClassSuspeitaRoubo 	 = false;
	private boolean painelClassSinistroDano 	 = false;
	private boolean painelClassSinistroPerda 	 = false;
	private boolean painelClassSinistroPerdaDano = false;
	
	private boolean painelRelatoPrimProcedimento = true;
	private boolean painelBoletimFinal			 = false;
	private boolean painelBoletimFinalSuspeita 	 = false;
	private boolean painelTipoSuspeitaOutros	 = false;
	
	// Variaveis Dados SM (para visualização)
	private boolean subPnDadosCarreta = false;
	private boolean subPnDadosCarreta1 = false;
	private boolean subPnDadosCarreta2 = false;
	private boolean subPnDadosCarreta3 = false;
	
	private boolean subPnDadosMotorista1 = false;
	private boolean subPnDadosMotorista2 = false;
	
	private boolean boletimSalvo = false;
		
	@Inject
	BoletimInformativoService biService;
	
	@Inject
	BoletimInformativoRelatoService biRelService;
	
	@Inject
	EventoService evService;
	
	@Inject
	EventoInfoViagemService evInfoViagService;
	
	@Inject
	private EventoInfoRastreadorService evInfoRastreadorService;
	
	@Inject
	TipoSuspeitaService tipoSuspService;
	
	@Inject
	private Crud<BoletimInformativoNumeracao> boletimNumeracaoCrud;
		
	
	@PostConstruct
	public void init(){
		
		clear();
		
	}
	
	private void clear() {
		
		checkBi = CheckBI.getInstancia();
		
		Integer ultimoIdBiDoEvento = checkBi.checkBIRetorno();
		
		Integer idEventoSelecionado  = checkBi.checkEvRetorno();
		
		tipoSelecaoBi   = checkBi.tipoEventoBiRetorno();

			findByIdEvento(idEventoSelecionado);
		
			if (ultimoIdBiDoEvento != null){
				
				BoletimInformativo bi2 = new BoletimInformativo();
				
				bi2 = biService.crud().get(ultimoIdBiDoEvento); 
				bi =  new BoletimInformativo();
				
				try {
					bi = (BoletimInformativo) bi2.clone();

					//Francis 27/10/2020
					if(tipoSelecaoBi.equals('S')){
						bi.setTipoSuspeita(new TipoSuspeita());
					}
					/*
					if(bi.getTipoSuspeita() == null){
						bi.setTipoSuspeita(new TipoSuspeita());	
					} 
					*/
					
					
					/*if(bi.getTipoSuspeita().getId() == null){
						bi.setTipoSuspeita(new TipoSuspeita());	
					} */
					
					bi.setId(null);
					bi.setRelato("");
					
					if(ev.getNrBoletimSuspeita() != null){
						
						BoletimInformativo biSusp = new BoletimInformativo();
						biSusp = biService.crud().get(ev.getNrBoletimSuspeita());
						
						if (tipoSelecaoBi == biSusp.getTipo()){
							
							codTipoSuspeitaClone = bi.getTipoSuspeita().getId();
							bi.setTipoSuspeita(new TipoSuspeita(codTipoSuspeitaClone));
							Hibernate.initialize(bi.getTipoSuspeita().getId());
						}
							
					}
					
					
					lstTipoSuspeita = tipoSuspService.crud().isNull("exclusao").listAll();
										
					// Se tipo boletim anterior é igual a perda então carrega dados perda
					if(ev.getBoletimInformativo().getTipo() == bi.EVENTO_TIPO_PERDA) {
						cargaTipoPerdaBiAnterior();
					} 
					
					// Se tipo boletim anterior é igual a danos então carrega dados danos
					if(ev.getBoletimInformativo().getTipo() == bi.EVENTO_TIPO_DANO) {
						cargaTipoDanoBiAnterior();
					} 

					
					selecaoTipoEvento();
					checarEventoDadosCarreta();
					checarEventoDadosMotorista();
						
					
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				
				bi =  new BoletimInformativo();

				id = null;
				
				if (tipoSelecaoBi == bi.EVENTO_TIPO_SUSPEITA){
					bi.setTipoSuspeita(new TipoSuspeita());					
				}
				
				lstTipoSuspeita = tipoSuspService.crud().isNull("exclusao").listAll();	
				selecaoTipoEvento();
				checarEventoDadosCarreta();
				checarEventoDadosMotorista();
				
//				try {
//					buscarViagemBI(ev.getSincViagem());
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			}
						
			lstEmailGlobal5 = new ArrayList<String>();
			arrayJsonGlobal5 = new JSONArray();
	}

	public void findByIdEvento(Integer idEvento){
		
		if( idEvento == null ){
			throw new BusinessException("O id do evento não foi encontrado. Contate o suporte TI");
		}
		ev = new Evento();
		eiv = new EventoInfoViagem();

		ev = evService.crud().get(idEvento);
		Hibernate.initialize(ev);
		
		//Adicionado novo recurso para busca
		Criteria cri = evInfoViagService.crud().getSession().createCriteria(EventoInfoViagem.class);
		cri.add(Restrictions.eq("id", idEvento ));
		
		int result = cri.list().size();
		
		if(result == 1){
			
			EventoInfoViagem eivG = (EventoInfoViagem) cri.list().get(0);
			
			//eiv = evInfoViagService.crud().get(idEvento);
			eiv = evInfoViagService.crud().get(eivG.getId());
		}
		
		// Registro anterior
		Hibernate.initialize(eiv);
			
	}
	
	
	public void selecaoTipoEvento(){
		
		painelBoletimFinal = false;
		painelBoletimFinalSuspeita = false;
		
		if ( tipoSelecaoBi == bi.EVENTO_TIPO_DANO || tipoSelecaoBi == bi.EVENTO_TIPO_PERDA  ) {
		
			painelDadosSinistro = true;
			painelClassSinistroPerdaDano = true;
		
		} 
		
		if ( tipoSelecaoBi == bi.EVENTO_TIPO_SUSPEITA ) {
			
				painelDadosSinistro = false;
				painelDadosSuspeita = true;
			
		 }		
		
		if ( tipoSelecaoBi == bi.EVENTO_TIPO_PERDA) {
			
			painelDadosSuspeita = false;
			painelClassSinistroPerda = true;
						
		} else  {
			
			painelClassSinistroPerda = false;
			
		}
				
		if ( tipoSelecaoBi == bi.EVENTO_TIPO_DANO) {
			
			painelDadosSuspeita = false;		
			painelClassSinistroDano = true;
			
			
		} else  {
			
			painelClassSinistroDano = false;
					
		}
		
		if( checkBi.tipoBoletimFinalRetorno() == true && tipoSelecaoBi == bi.EVENTO_TIPO_DANO ){
			painelBoletimFinal = true;
			painelRelatoPrimProcedimento = false;
		}
		
		if( checkBi.tipoBoletimFinalRetorno() == true && tipoSelecaoBi == bi.EVENTO_TIPO_PERDA ){
			painelBoletimFinal = true;
			painelRelatoPrimProcedimento = false;
		}
	
		if( checkBi.tipoBoletimFinalRetorno() == true && tipoSelecaoBi == bi.EVENTO_TIPO_SUSPEITA ){
			painelBoletimFinalSuspeita = true;
			painelRelatoPrimProcedimento = false;
		}
		
	}
	
	
	public void insertBi() {
	        try {
	            clear();
	            FacesContext.getCurrentInstance().getExternalContext().redirect("../bi/bi.xhtml");
	        } catch (IOException e) {
	            FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Inserir proprietários " + getId()
	                            + " não pode ser carregado. Informe ao suporte técnico.",null));
	        }
	}
	
	
	public void checarEventoDadosMotorista(){
		/* Bloqueio em 08/10/2020
		if(ev.getSincViagem().getMotorista1() != null){
			subPnDadosMotorista1 = true;
		}
		
		if(ev.getSincViagem().getMotorista2() != null){
			subPnDadosMotorista2 = true;
		}
		*/
		
		if(eiv.getMotoristaCpf1() != null){
			subPnDadosMotorista1 = true;
		}
	
		if(eiv.getMotoristaCpf2() != null){
			subPnDadosMotorista2 = true;
		}
		
	}
			
	public void checarEventoDadosCarreta(){
		String b = "a";
		String a = b;
		
		/* Bloqueio em 08/10/2020
		if( ev.getSincViagem().getVeiculo() != null ){
			// verifica se existe carretas ligada ao veiculo
			if (ev.getSincViagem().getCarreta1() != null){
				// mostra dados carreta 1
				subPnDadosCarreta1 = true;
			}
			if (ev.getSincViagem().getCarreta2() != null){
				// mostra dados carreta 2
				subPnDadosCarreta2 = true;
			}
			if (ev.getSincViagem().getCarreta3() != null){
				// mostra dados carreta 3
				subPnDadosCarreta1 = true;
			}
			
		} else {
			// não mostra dados carretas
			subPnDadosCarreta = false;
		}
		
		if (ev.getSincViagem().getCarreta1() == null &&
				ev.getSincViagem().getCarreta2() == null &&
				ev.getSincViagem().getCarreta3() == null ){
			subPnDadosCarreta = false;
		}
		*/


			// verifica se existe carretas ligada ao veiculo
			if (eiv.getCarretaPlaca1() != null){
				// mostra dados carreta 1
				// subPnDadosCarreta = true;
				//this.subPnDadosCarreta1 = true;
				this.setSubPnDadosCarreta(true);
				this.setSubPnDadosCarreta1(true);
			}
			if (eiv.getCarretaPlaca2() != null){
				// mostra dados carreta 2
				this.setSubPnDadosCarreta(true);
				this.setSubPnDadosCarreta2(true);
			}
			if (eiv.getCarretaPlaca3() != null){
				// mostra dados carreta 3
				this.setSubPnDadosCarreta(true);				
				this.setSubPnDadosCarreta3(true);
			}

		if (eiv.getCarretaPlaca1() == null &&
				eiv.getCarretaPlaca2() == null &&
				eiv.getCarretaPlaca3() == null ){
			subPnDadosCarreta = false;
		}
		
	}
		
	public void cargaTipoPerdaBiAnterior(){
		//if (checkBi.tipoEventoBiRetorno() == 'P') {
		// Carga de dados com base no tipo do ultimo boletim informativo ligado ao evento
		if (ev.getBoletimInformativo().getTipo() == 'P') {

			if (bi.getPerdaTpFurto() == true) {
				tipoPerdaSel = "furto";
			}
			if (bi.getPerdaTpFurtoQualificado() == true) {
				tipoPerdaSel = "furtoQualificado";
			}
			if (bi.getPerdaTpRoubo() == true) {
				tipoPerdaSel = "roubo";
			}
			if (bi.getPerdaTpExtorsao() == true) {
				tipoPerdaSel = "extorsao";
			}
			if (bi.getPerdaTpApropriacaoIndebita() == true) {
				tipoPerdaSel = "apropriacaoIndebita";
			}
			if (bi.getPerdaTpEstelionato() == true) {
				tipoPerdaSel = "estelionato";
			}
			if (bi.getPerdaTpReceptacao() == true) {
				tipoPerdaSel = "receptacao";
			}
			if (bi.getPerdaTpFalsidadeIdeologica() == true) {
				tipoPerdaSel = "falsidadeIdeologica";
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Sua chamada deste evento não trouxe nada para carregar!"));
			return;
		}
	}
	
	public void tipoPerdaSelecionada(){
				
		clearTipoPerda();
		
		if ( tipoPerdaSel.equals("furto")){
			bi.setPerdaTpFurto(true);
			bi.setTipoPerda(new TipoPerda(670));		// 670 | Furto 
		} else 		
		if ( tipoPerdaSel.equals("furtoQualificado")) {
			bi.setPerdaTpFurtoQualificado(true);
			bi.setTipoPerda(new TipoPerda(671));		// 671 | Furto Qualificado 
		} else 		
		if ( tipoPerdaSel.equals("roubo")) {
			bi.setPerdaTpRoubo(true);		
			bi.setTipoPerda(new TipoPerda(672));		// 672 | Roubo 
		} else 		
		if ( tipoPerdaSel.equals("extorsao")) {
			bi.setPerdaTpExtorsao(true);	
			bi.setTipoPerda(new TipoPerda(673));		// 673 | Extorsão
		} else 		
		if ( tipoPerdaSel.equals("apropriacaoIndebita")) {
			bi.setPerdaTpApropriacaoIndebita(true);
			bi.setTipoPerda(new TipoPerda(674));		// 674 | Apropriação Indébita
		} else 		
		if ( tipoPerdaSel.equals("estelionato")) {
			bi.setPerdaTpEstelionato(true);
			bi.setTipoPerda(new TipoPerda(675));		// 675 | Estelionato   
		} else 		
		if ( tipoPerdaSel.equals("receptacao")) {
			bi.setPerdaTpReceptacao(true);
			bi.setTipoPerda(new TipoPerda(676));		// 676 | Receptação  
		} else 		
		if ( tipoPerdaSel.equals("falsidadeIdeologica")) {
			bi.setPerdaTpFalsidadeIdeologica(true);
			bi.setTipoPerda(new TipoPerda(677));		// 677 | Falsidade Ideológica
		} 
		
	}	
	
	public void clearTipoPerda(){
		if(!tipoPerdaSel.equals("")) {
			bi.setPerdaTpFurto(false);
			bi.setPerdaTpFurtoQualificado(false);
			bi.setPerdaTpRoubo(false);
			bi.setPerdaTpExtorsao(false);
			bi.setPerdaTpApropriacaoIndebita(false);
			bi.setPerdaTpEstelionato(false);
			bi.setPerdaTpReceptacao(false);
			bi.setPerdaTpFalsidadeIdeologica(false);
		}
	}
	
    public void cargaTipoDanoBiAnterior(){
		
    	//if (checkBi.tipoEventoBiRetorno() == 'D') {
		// Carga de dados com base no tipo do ultimo boletim informativo ligado ao evento
    	if (ev.getBoletimInformativo().getTipo() == 'D') {    		
    		
			if (bi.getDanoTpChoque() == true ) {
                tipoDanoSel = "choque" ; 
            }	
			if (bi.getDanoTpSaidaPista() == true ) { 
                tipoDanoSel = "saidaPista" ; 
            }
			if (bi.getDanoTpAdernamento() == true ) {
                tipoDanoSel = "adernamento" ; 
            }		
			if (bi.getDanoTpIncendio() == true ) {
                tipoDanoSel = "incendio" ; 
            }		
			if (bi.getDanoTpAmassamento() == true ) { 
                tipoDanoSel = "amassamento" ; 
            }		
			if (bi.getDanoTpVazamento() == true ) {
                tipoDanoSel = "vazamento" ; 
            }	
			if (bi.getDanoTpArranhadura() == true ) { 
                tipoDanoSel = "arranhadura" ; 
            }		
			if (bi.getDanoTpQuebra() == true ) {
                tipoDanoSel = "quebra" ; 
            }	
			if (bi.getDanoTpQuebraMercadoria() == true ) { 
                tipoDanoSel = "quebraMercadoria" ; 
            }		
			if (bi.getDanoTpColisao() == true  ) {
                tipoDanoSel = "colisao" ; 
            }	
			if (bi.getDanoTpAbalroamento() == true  ) { 
                tipoDanoSel = "abolroamento" ; 
            }		
			if (bi.getDanoTpTombamento() == true ) {
                tipoDanoSel = "tombamento" ; 
            }		
			if (bi.getDanoTpDeslocamentoCarga() == true ) { 
                tipoDanoSel = "deslocamentoCarga" ; 
            }		
			if (bi.getDanoTpContaminacao() == true ) {
                tipoDanoSel = "contaminacao" ; 
            }		
			if (bi.getDanoTpDerrame() == true ) {
                tipoDanoSel = "derrame" ; 
            }	
			if (bi.getDanoTpDerrapagem() == true ) { 
                tipoDanoSel = "derrapagem" ; 
            }		
			if (bi.getDanoTpEngavetamento() == true ) { 
                tipoDanoSel = "engavetamento" ; 
            }		
			if (bi.getDanoTpQuedaNivel() == true ) {
                tipoDanoSel = "quedaNivelVeiculo" ; 
            }		
			if (bi.getDanoTpMolhadura() == true ) {
                tipoDanoSel = "molhadura" ; 
            }		
			if (bi.getDanoTpAtolamento() == true ) {
                tipoDanoSel = "atolamento" ; 
            }		
			if (bi.getDanoTpAtropelamento() == true ) { 
                tipoDanoSel = "atropelamento" ; 
            }		
			if (bi.getDanoTpDesatreamento() == true ) {
                tipoDanoSel = "desatreamento" ; 
            }		
			if (bi.getDanoTpMaEstiva() == true ) {
                tipoDanoSel = "maEstiva" ; 
            }		
			if (bi.getDanoTpAmolgamento() == true ) { 
                tipoDanoSel = "amolgamento" ; 
            }		
			if (bi.getDanoTpCapotamento() == true ) {
                tipoDanoSel = "capotamento" ; 
            }
    	}
	}
	
	public void tipoDanoSelecionada(){
		
		clearTipoDano();
		
		if ( tipoDanoSel.equals("choque")){
			bi.setDanoTpChoque(true);
			bi.setTipoDano(new TipoDano(688));		// 688 | Choque       
		} else
		if ( tipoDanoSel.equals("saidaPista")){
			bi.setDanoTpSaidaPista(true);
			bi.setTipoDano(new TipoDano(689));		// 689 | Saída de Pista
		} else
		if ( tipoDanoSel.equals("adernamento")){
			bi.setDanoTpAdernamento(true);
			bi.setTipoDano(new TipoDano(690));		// 690 | Adernamento  
		} else
		if ( tipoDanoSel.equals("incendio")){
			bi.setDanoTpIncendio(true);
			bi.setTipoDano(new TipoDano(691));		// 691 | Incêndio   
		} else
		if ( tipoDanoSel.equals("amassamento")){
			bi.setDanoTpAmassamento(true);
			bi.setTipoDano(new TipoDano(692));		// 692 | Amassamento 	
		} else
		if ( tipoDanoSel.equals("vazamento")){
			bi.setDanoTpVazamento(true);
			bi.setTipoDano(new TipoDano(693));		//  693 | Vazamento            
		} else
		if ( tipoDanoSel.equals("arranhadura")){
			bi.setDanoTpArranhadura(true);
			bi.setTipoDano(new TipoDano(694));		// 694 | Arranhadura  
		} else
		if ( tipoDanoSel.equals("quebra")){
			bi.setDanoTpQuebra(true);
			bi.setTipoDano(new TipoDano(695));		// 695 | Quebra
		} else
		if ( tipoDanoSel.equals("quebraMercadoria")){
			bi.setDanoTpQuebraMercadoria(true);
			bi.setTipoDano(new TipoDano(696));		// 696 | Quebra de Mercadoria
		} else
		if ( tipoDanoSel.equals("colisao")){
			bi.setDanoTpColisao(true);
			bi.setTipoDano(new TipoDano(697));		// 697 | Colisão 
		} else
		if ( tipoDanoSel.equals("abolroamento")){
			bi.setDanoTpAbalroamento(true);
			bi.setTipoDano(new TipoDano(698));		// 698 | Abalroamento 
		} else
		if ( tipoDanoSel.equals("tombamento")){
			bi.setDanoTpTombamento(true);
			bi.setTipoDano(new TipoDano(699));		// 699 | Tombamento 
		} else
		if ( tipoDanoSel.equals("deslocamentoCarga")){
			bi.setDanoTpDeslocamentoCarga(true);
			bi.setTipoDano(new TipoDano(700));		// 700 | Deslocamento de Carga
		} else
		if ( tipoDanoSel.equals("contaminacao")){
			bi.setDanoTpContaminacao(true);
			bi.setTipoDano(new TipoDano(701));		// 701 | Contaminação
		} else
		if ( tipoDanoSel.equals("derrame")){
			bi.setDanoTpDerrame(true);
			bi.setTipoDano(new TipoDano(702));		// 702 | Derrame 
		} else
		if ( tipoDanoSel.equals("derrapagem")){
			bi.setDanoTpDerrapagem(true);
			bi.setTipoDano(new TipoDano(703));		// 703 | Derrapagem 
		} else
		if ( tipoDanoSel.equals("engavetamento")){
			bi.setDanoTpEngavetamento(true);
			bi.setTipoDano(new TipoDano(704));		// 704 | Engavetamento  
		} else
		if ( tipoDanoSel.equals("quedaNivelVeiculo")){
			bi.setDanoTpQuedaNivel(true);
			bi.setTipoDano(new TipoDano(705));		// 705 | Queda de Nivel ou de Veículo
		} else
		if ( tipoDanoSel.equals("molhadura")){
			bi.setDanoTpMolhadura(true);
			bi.setTipoDano(new TipoDano(706)); 		// 706 | Molhadura 
		} else
		if ( tipoDanoSel.equals("atolamento")){
			bi.setDanoTpAtolamento(true);
			bi.setTipoDano(new TipoDano(707));		// 707 | Atolamento  
		} else
		if ( tipoDanoSel.equals("atropelamento")){
			bi.setDanoTpAtropelamento(true);
			bi.setTipoDano(new TipoDano(708));		// 708 | Atropelamento
		} else
		if ( tipoDanoSel.equals("desatreamento")){
			bi.setDanoTpDesatreamento(true);
			bi.setTipoDano(new TipoDano(709));		// 709 | Desatreamento 
		} else
		if ( tipoDanoSel.equals("maEstiva")){
			bi.setDanoTpMaEstiva(true);
			bi.setTipoDano(new TipoDano(710));		// 710 | Má Estiva   
		} else
		if ( tipoDanoSel.equals("amolgamento")){
			bi.setDanoTpAmolgamento(true);
			bi.setTipoDano(new TipoDano(711));		// 711 | Amolgamento  
		} else
		if ( tipoDanoSel.equals("capotamento")){
			bi.setDanoTpCapotamento(true);
			bi.setTipoDano(new TipoDano(712));		// 712 | Capotamento 
		}				
		
	}
	
	public void clearTipoDano(){
		if(!tipoDanoSel.equals("")) {
			bi.setDanoTpChoque(false);	
			bi.setDanoTpSaidaPista(false);
			bi.setDanoTpAdernamento(false);		
			bi.setDanoTpIncendio(false);		
			bi.setDanoTpAmassamento(false);		
			bi.setDanoTpVazamento(false);	
			bi.setDanoTpArranhadura(false);		
			bi.setDanoTpQuebra(false);	
			bi.setDanoTpQuebraMercadoria(false);		
			bi.setDanoTpColisao(false);	
			bi.setDanoTpAbalroamento(false);		
			bi.setDanoTpTombamento(false);		
			bi.setDanoTpDeslocamentoCarga(false);		
			bi.setDanoTpContaminacao(false);		
			bi.setDanoTpDerrame(false);	
			bi.setDanoTpDerrapagem(false);		
			bi.setDanoTpEngavetamento(false);		
			bi.setDanoTpQuedaNivel(false);		
			bi.setDanoTpMolhadura(false);		
			bi.setDanoTpAtolamento(false);		
			bi.setDanoTpAtropelamento(false);		
			bi.setDanoTpDesatreamento(false);		
			bi.setDanoTpMaEstiva(false);		
			bi.setDanoTpAmolgamento(false);		
			bi.setDanoTpCapotamento(false);
		}
	}

	
	public void salvarBoletim() throws IOException{
		
		Integer numGlobalPorTipo = null;
		
		Integer numGlobalPorTipoDano = null;
		Integer numGlobalPorTipoPerda = null;
		Integer numGlobalPorTipoSuspeita = null;
		
			//tipoSelecaoBi
			// Se o tipo do boletim ainda não foi gerado para o evento então gera um novo numGlobalPorTipo
			if(ev.getNrBoletimDano() != null && tipoSelecaoBi == bi.EVENTO_TIPO_DANO){
				
				BoletimInformativo biDano = biService.crud().get(ev.getNrBoletimDano());				
				numGlobalPorTipo = biDano.getNrGlobal();
				
			} else 
				if (ev.getNrBoletimPerda() != null && tipoSelecaoBi == bi.EVENTO_TIPO_PERDA) {
					
					BoletimInformativo biPerda = biService.crud().get(ev.getNrBoletimPerda());				
					numGlobalPorTipo = biPerda.getNrGlobal();
				
			} else 
				if (ev.getNrBoletimSuspeita() != null && tipoSelecaoBi == bi.EVENTO_TIPO_SUSPEITA) {
					
					BoletimInformativo biSuspeita = biService.crud().get(ev.getNrBoletimSuspeita());				
					numGlobalPorTipo = biSuspeita.getNrGlobal();
				
			} else {
				
				// gera um novo numGlobalPorTipo se não existir nenhum boletim gerado 
				StoredProcedureQuery qryBINum = boletimNumeracaoCrud.getEntityManager().createNamedStoredProcedureQuery("boletim_informativo_global");
				qryBINum.setParameter("tipo", tipoSelecaoBi);
				qryBINum.execute();
								
				numGlobalPorTipo = (Integer) qryBINum.getOutputParameterValue("bin");
				
			}
				
			Long result = numGlobalPorTipo.longValue();
			
			if ( result > 0 ){
				
				// Contador de sequencia do boletim
				Integer countNrBoletim = null;
				
				ev = evService.crud().get(checkBi.checkEvRetorno());
				
				if( ev.getNrBoletim() == null || ev.getNrBoletim() == 0 ){
					
					countNrBoletim = 1;
					
				} else {
					
					countNrBoletim = ev.getNrBoletim() + 1;
					
				}
				
				// Persiste Boletim
				//String codRotulo = "BI-" + tipoSelecaoBi + " " + ev.getSincViagem().getId() +
				String codRotulo = "BI-" + tipoSelecaoBi + " " + ev.getSincViagem() +
									"-" + ev.getNrEvento() + "." + countNrBoletim + "/" + numGlobalPorTipo +
									"-" + ev.getAno();
				
				
				bi.setUsuEmissao(checkUsuario.valid());
				bi.setDtEmissao(new Date());
				bi.setNrGlobal(numGlobalPorTipo);
				bi.setNrSequencia(ev.getNrEvento());
				bi.setTipo(tipoSelecaoBi);
				bi.setRotulo(codRotulo);
				bi.setComIniDataHora(new Date());
				
				if ( ev.getBoletimInformativo() != null){
					if(ev.getBoletimInformativo().getTipo() == bi.EVENTO_TIPO_SUSPEITA){
						
						if( numIdTpSuspeita == null){
							bi.setTipoSuspeita(new TipoSuspeita(codTipoSuspeitaClone));
						}
						
					}
										
				}
				
				bi.setEvento(new Evento(ev.getId()));
											
				biService.crud().save(bi);
				
				boletimSalvo = true;
				
				// Salvar relato do boletim
				if(bi.getRelato().length() > 0){
					BoletimInformativoRelato biRel = new BoletimInformativoRelato();
				
					biRel.setEvento(bi.getEvento());
					biRel.setBoletimInformativo(bi);
					biRel.setRelato(bi.getRelato());
					//	biRel.setRelato("fsadfsadfasd");
					biRel.setDtCriacao(new Date());
					biRel.setTipo("N");
				
					biRelService.crud().save(biRel);
				}
				
				// Gravar Boletim em PDF na pasta /var/www/anexos_bi/envio/
				String nomeRelatorioJasper ="";
				// Escolhe qual é o tipo do arquivo  ( antigo modelo de teste "BoletimInformativo" )
				if(tipoSelecaoBi.equals('P')){
					nomeRelatorioJasper = "BoletimInformativoP";
				}
				
				if(tipoSelecaoBi.equals('D')){
					nomeRelatorioJasper = "BoletimInformativoD";
				}

				if(tipoSelecaoBi.equals('S')){
					nomeRelatorioJasper = "BoletimInformativoS";
				}
				
 				RelatorioUtil relatorioUtil = new RelatorioUtil();
				
				String  nomeRelatorioSaida = "BI-"+ bi.getTipo() + " " + ev.getSincViagem() +"-ev-"+ev.getId()+"-bi-"+bi.getId();  
				
				String  nomeRelatorioSaidaPdf = nomeRelatorioSaida + ".pdf" ;
				
				Integer numEvento = ev.getId();
				Integer numBoletim = bi.getId();
				
				HashMap parametrosRelatorio = new HashMap<>();
				parametrosRelatorio.put("Evento", ev.getId());
				parametrosRelatorio.put("Boletim", bi.getId());
				
				try {
					relatorioUtil.geraRelatorio(parametrosRelatorio, nomeRelatorioJasper, nomeRelatorioSaida, 1);
				} catch (UtilException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String toEmail = ev.getEmails().replace(";", ",");
				String ccEmail = emailCcParaBoletim;
				
				// Enviar email
				String localArq = "/var/www/anexos_bi/envio/" ; //"/var/www/boletos/1/envio/"
				String nomeArq = nomeRelatorioSaidaPdf;
				
				AppUtils.enviarEmail(localArq, nomeArq, toEmail, ccEmail);
				//AppUtils.preparaEmail(localArq);			
				
				//Atualização na Tabela Evento
				
				/* Set Nr Boletim: Perda|Dano|Suspeita */
				if( ev.getNrBoletimPerda() == null && tipoSelecaoBi == bi.EVENTO_TIPO_PERDA ) {
					
					ev.setNrBoletimPerda(bi.getId());
					
				} else 
					
					if ( ev.getNrBoletimDano() == null && tipoSelecaoBi == bi.EVENTO_TIPO_DANO ) {
					
						ev.setNrBoletimDano(bi.getId());
						
				} else 
						
					if ( ev.getNrBoletimSuspeita() == null && tipoSelecaoBi == bi.EVENTO_TIPO_SUSPEITA ) {
						
						ev.setNrBoletimSuspeita(bi.getId());
						
				}
				
				/* Set Tipo_inicial */
				if ( ev.getTipoInicial() == null){
					ev.setTipoInicial(tipoSelecaoBi);
				} 
				
				ev.setNrBoletim(countNrBoletim);
				ev.setBoletimInformativo(bi);
				
				if( checkBi.tipoBoletimFinalRetorno() == true ) {
					ev.setDtFinalizacao(new Date());
					ev.setTipoFinal(tipoSelecaoBi);
					ev.setUsuFinalizacao(checkUsuario.valid());
				}
				
				int qtdEmail = lstEmailGlobal5.size();
				String emailsAnterior = ev.getEmailsCc();
				
				if(qtdEmail > 0){
					for(int i = 0 ; i < qtdEmail ; i++){
						emailListGlobal5 +=  lstEmailGlobal5.get(i) + ";";
					}
				}
				
				if (emailsAnterior == null) {
					ev.setEmailsCc(emailListGlobal5);
				} else {
					ev.setEmailsCc(emailsAnterior + emailListGlobal5);	
				}
				
				
				
				evService.update(ev);
					
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Boletim gravado com sucessso!") );
				
				
			} else {
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "erro", "Não foi possível gerar o Numerador para Registro do BI!") );
				return;
			}
		
	}
	
	
	private void listarBiRelatos(){
		
		Criteria criteria = biRelService.crud().getSession().createCriteria(BoletimInformativoRelato.class);
		
		criteria.add(Restrictions.eq("evento.id", ev.getId()));
		
		criteria.addOrder(Order.asc("id"));
		criteria.addOrder(Order.asc("id"));
		
		int result = criteria.list().size();
		
		if ( result > 0 ) {
			this.setListBiRelato(criteria.list());
		} else {
			this.setListBiRelato(null);
		}
		
	}
	
	/**
	 * @author Francis Bueno
	 * @param event
	 * 
	 * Suspeita de Roubo, quando o tipo do evento de suspeita de roubo
	 * 
	 */
	public void onTipoEventoSuspeitaRoubo(AjaxBehaviorEvent event){
		//AjaxBehaviorEvent event 
		//SelectEvent event
		String a = "b";
		String b = a;
		if( ((UIOutput)event.getSource()).getValue() == null){
			this.setNumIdTpSuspeita(653); // Tentativa de Roubo 
		} else {
			//Integer idEve = (Integer) event.getSource();
			Integer idEvent = (Integer) ((UIOutput)event.getSource()).getValue();
			
			//Integer ii = idEve;
			this.setNumIdTpSuspeita(idEvent);
			//this.setNumIdTpSuspeita(this.bi.getTipoSuspeita().getId());
			
			if (this.bi.getTipoSuspeita().getId() ==  669 ){
				this.setPainelTipoSuspeitaOutros(true);
				
			} else {
				this.setPainelTipoSuspeitaOutros(false);
				
			}
		}
				
	}
	
	// Novo Método busca ws chp
	private List<VersaoTecnologia> lstVersaoTecnologia;
	
	@Inject
	private Crud<Area> areaCrud;
	
	public void buscarViagemBI(Integer idViagem) throws ParseException {
		//Hibernate.initialize(EventoInfoViagem.class);
		Criteria criEvViagem = evInfoViagService.crud().getSession().createCriteria(EventoInfoViagem.class);
		criEvViagem.add(Restrictions.eq("viagem", idViagem));
		
		int result = criEvViagem.list().size();
		
		// Não atualiza o result após a inclusão (persistencia)
		
		if( result == 0){
			ViagemClient wsV = new ViagemClient();

			getWsDataViagemBI(wsV, idViagem);
			
			
		}
		
		

	}
	
	
	private void getWsDataViagemBI(ViagemClient wsV, Integer idViagem)
			throws ParseException {
		
		if( idViagem != null){
		// Busca existencia de Viagem na tabela de eventos
		Criteria smEvento = evService.crud().getSession().createCriteria(Evento.class);
		smEvento.add(Restrictions.eq("sincViagem", idViagem));
		
		int result = smEvento.list().size();
		
		if( result > 0){
			
			// Não busca pois o ID já está no evento
			//FacesContext.getCurrentInstance().addMessage(null, 
			//		new FacesMessage(FacesMessage.SEVERITY_INFO , "Aviso", "A sm já está cadadastrada no evento!"));
			//} else {

			EventoInfoViagem eiv = new EventoInfoViagem();
			ArrayList<EventoInfoRastreador> listArray = new ArrayList<>();
			// Buscar Dados no WS CHP
			JSONArray objJsonArray = wsV.getViagemBI(idViagem);
			
			
			if( objJsonArray != null){

				// tamanho do JsonArray retornado do Ws
				int viagensCount = objJsonArray.length();
				
				for (int i = 0; i < viagensCount; i++) {
				
					String numSm = objJsonArray.getJSONObject(i).opt("codigoSM").toString();
					String docTransportador = objJsonArray.getJSONObject(i).opt("documento_transportador").toString();
					
					// Se docTransportador for diferente de null
					if (!docTransportador.equals("null")) {
						
						// Dados para Gravação do Evento
						String nomeCliente = objJsonArray.getJSONObject(i).opt("pess_nome_transportador").toString();						
						String docSeguradora = objJsonArray.getJSONObject(i).opt("documento_seguradora").toString();

						// Persistencia - Cliente
						eiv.setViagem(idViagem);
						eiv.setNomeCliente(nomeCliente);
						eiv.setNomeSeguradora("");
						eiv.setNomeCorretora("");
						eiv.setNomeTransportadora(nomeCliente);
						
						
						String statusViagemJson = objJsonArray.getJSONObject(i).optString("status_viagem").toString();						
						
						// Motoristas
						JSONArray jsonArrayMotoristas = objJsonArray.getJSONObject(i).getJSONArray("motoristas");
						
						// Var Motoristas
						int arrayMotoristasCount = 0;
						int motoristaCount = 0;
						String jsonMotoristas = "";
						
						if (jsonArrayMotoristas != null) {
							
							arrayMotoristasCount = jsonArrayMotoristas.length();
							
							for (int im = 0; im < arrayMotoristasCount; im++) {

								motoristaCount++;

								jsonMotoristas += jsonArrayMotoristas.getJSONObject(im).toString() + ",";
								
								if(im == 0){
									// Persistencia - Motorista 1
									eiv.setMotoristaNome1(jsonArrayMotoristas.getJSONObject(im).opt("nome_moto").toString());
									eiv.setMotoristaCpf1(jsonArrayMotoristas.getJSONObject(im).opt("cpf_moto").toString());
									eiv.setMotoristaVinculo1(jsonArrayMotoristas.getJSONObject(im).opt("vinculo_contratual").toString());
								}
								if( im == 1){
									// Persistencia - Motorista 2
									eiv.setMotoristaNome2(jsonArrayMotoristas.getJSONObject(im).opt("nome_moto").toString());
									eiv.setMotoristaCpf2(jsonArrayMotoristas.getJSONObject(im).opt("cpf_moto").toString());
									eiv.setMotoristaVinculo2(jsonArrayMotoristas.getJSONObject(im).opt("vinculo_contratual").toString());									
								}

							} // final for arrayMotoristaCount
							
							// Motorista Preparação de dados para Persistencia
							if (motoristaCount > 0) {
								
								int countChar = jsonMotoristas.length();
								String jsonClear = jsonMotoristas.substring(0, (countChar - 1)); 
								
								String jsonMotoristaMontado = "[" + jsonClear + "]";
								jsonMotoristas = jsonMotoristaMontado;
								
							}
							
						} // final if jsonArrayMotoristas --> Motoristas
						
						
						// Veiculos (Trativa para Cavalo, Reboque e Escolta)
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
						
						if (jsonArrayVeiculos != null) {
							
							arrayVeiculosCount = jsonArrayVeiculos.length();
							
							// Escolta
							escoltaCount = 0;
							
							for (int iv = 0; iv < arrayVeiculosCount; iv++){
								
								int tipoVeiculo = jsonArrayVeiculos.getJSONObject(iv).getInt("tipo_veiculo");
								
								// Valor Escolta para uso em criterio de regra -->
								// valor do elemento escolta para condição if
								String veiculoEscolta = jsonArrayVeiculos.getJSONObject(iv).opt("escolta").toString();
								
								// Cavalo: coleta placa onde (tipo_veiculo != 1 e
								// escolta = null)
								if (tipoVeiculo != 1 && veiculoEscolta.equals("null")) {

									cavaloCount++;
									placaCavalo += jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString() + "|";

									//Persistencia - Cavalo / Veiculo
									eiv.setVeiculoPlaca(jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString());
									eiv.setVeiculoMarca(jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString());
									eiv.setVeiculoModelo(jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString());
									eiv.setVeiculoAno(jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString());
									eiv.setVeiculoCor(jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString());
																		
								} // final if tipo_veiculo != 1 && escolta = null
								
								// Reboque: coleta placa onde (tipo_veiculo = 1 e
								// escolta = null )
								if (tipoVeiculo == 1 && veiculoEscolta.equals("null")) {
									
									reboqueCount++;

									if (reboqueCount == 1) {
										placaReboque1 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
									
										//Persistencia - Carreta1
										eiv.setCarretaPlaca1(jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim());
										eiv.setCarretaMarca1(jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString().trim());
										eiv.setCarretaAno1(Integer.parseInt(jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString()));
										eiv.setCarretaCor1(jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString().trim());
										eiv.setCarretaTipo1(jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString().trim());
										
									}
									if (reboqueCount == 2) {
										placaReboque2 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
										
										//Persistencia - Carreta2
										eiv.setCarretaPlaca2(jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim());
										eiv.setCarretaMarca2(jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString().trim());
										eiv.setCarretaAno2(Integer.parseInt(jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString()));
										eiv.setCarretaCor2(jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString().trim());
										eiv.setCarretaTipo2(jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString().trim());
										
									}
									if (reboqueCount == 3) {
										placaReboque3 = jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim();
										
										//Persistencia - Carreta3
										eiv.setCarretaPlaca3(jsonArrayVeiculos.getJSONObject(iv).opt("placa").toString().trim());
										eiv.setCarretaMarca3(jsonArrayVeiculos.getJSONObject(iv).opt("marca").toString().trim());
										eiv.setCarretaAno3(Integer.parseInt(jsonArrayVeiculos.getJSONObject(iv).opt("ano_modelo").toString()));
										eiv.setCarretaCor3(jsonArrayVeiculos.getJSONObject(iv).opt("cor").toString().trim());
										eiv.setCarretaTipo3(jsonArrayVeiculos.getJSONObject(iv).opt("modelo").toString().trim());
										
									}
																	
								} // final if tipo_veiculo = 1 e escolta = null
								
								// Escolta: (Se existir e se diferente de null )
								// (AJUSTE... deixar condição <> de null | ! )
								if (!veiculoEscolta.equals("null")) {

									escoltaCount++;
									jsonVeiculosEscolta += jsonArrayVeiculos.getJSONObject(iv).toString() + ",";

								}
															
							} // final for arrayVeiculosCount --> Verifica Veiculo
							
							// Cavalo Preparação de dados para Persistencia
							if (cavaloCount > 0) {

								int countChar = placaCavalo.length();
								String strClear = placaCavalo.substring(0, (countChar - 1));
								placaCavalo = strClear;

							} // final if cavaloCount
							
							// Escolta Preparação de dados para Persistencia
							if (escoltaCount > 0) {

								int countChar = jsonVeiculosEscolta.length();
								// tira a virgula no final do arquivo
								String jsonClear = jsonVeiculosEscolta.substring(0, (countChar - 1)); 
								String jsonEscoltaMontado = "[" + jsonClear + "]";
								jsonVeiculosEscolta = jsonEscoltaMontado;

							} // final if escoltaCount						
							
							
						} // final jsonArrayVeiculos -> Veiculos 
						
						// Tecnologia (Terminais) (Trativa para tecnologia e iscas )
						JSONArray jsonArrayTecnologia = objJsonArray.getJSONObject(i).getJSONArray("terminais");
						
						// Var Tecnologias
						int arrayTecnologiaCount = 0;
						int tecnologiaCount = 0;
						int iscaSimCount = 0;
						int iscaNaoCount = 0;
						String jsonTerminalComIsca = "";
						String numeroTerminal = ""; // para Isca = N ,
													// term_numero_terminal (mct)
						int terminalCodigo = 0; 	// para Isca = N , term_vtec_codigo
						int idTecnologia = 0;
						
						ArrayList<String> listArrayA = new ArrayList<>();
						
						
						
						if (jsonArrayTecnologia != null) {
							
							arrayTecnologiaCount = jsonArrayTecnologia.length();
							
							int contador = 1;
							String termCodigo ="";
							String termDescricao ="";
							String termTelemonitorado = "";
							String termNumTerminal = "";
							String termVtecCodigo = "";
							String termIsca = "";
							
							String terminalComIsca = "";
							
							for (int it = 0; it < arrayTecnologiaCount; it++) {
								
								// Adiciona rastreador...
								termCodigo = jsonArrayTecnologia.getJSONObject(it).opt("term_codigo").toString();
								termDescricao = jsonArrayTecnologia.getJSONObject(it).opt("vtec_descricao").toString();
								termTelemonitorado = jsonArrayTecnologia.getJSONObject(it).opt("telemonitorado").toString();
								termNumTerminal = jsonArrayTecnologia.getJSONObject(it).opt("term_numero_terminal").toString();
								termVtecCodigo = jsonArrayTecnologia.getJSONObject(it).opt("term_vtec_codigo").toString();
								termIsca = jsonArrayTecnologia.getJSONObject(it).opt("isca").toString();
								
								listArray.add(new EventoInfoRastreador( contador++ , termCodigo, termDescricao, termTelemonitorado, termNumTerminal, termVtecCodigo, termIsca ) );		
								
							} // final for arrayTecnologiaCount
							
							// Terminal com Isca,
							if (iscaSimCount > 0) {

								int countChar = jsonTerminalComIsca.length();
								// tira virgula final da linha
								String jsonClear = jsonTerminalComIsca.substring(0, (countChar - 1)); 
								String jsonTerminalComIscaMontado = "[" + jsonClear + "]";
								jsonTerminalComIsca = jsonTerminalComIscaMontado;

							} // final if iscaSimCount
							
							
							// Terminal sem Isca (Veiculo Motor)
							if (iscaNaoCount > 0) {

								int countChar = numeroTerminal.length();
								// tira virgula final da linha
								String strClear = numeroTerminal.substring(0, (countChar - 1));
								numeroTerminal = strClear;

							} // final if iscaNaoCount
							
						} // final jsonArrayTecnologia
						
						
						// Origem
						JSONObject jbOrigem =  objJsonArray.getJSONObject(i).getJSONObject("origem");
						
							if(jbOrigem != null){
								jbOrigem.opt("vloc_descricao").toString();
								jbOrigem.opt("cida_descricao_ibge").toString();
								jbOrigem.opt("sigla_estado").toString();
								jbOrigem.opt("pais").toString();
								jbOrigem.opt("refe_latitude").toString();
								jbOrigem.opt("refe_longitude").toString();
								jbOrigem.opt("previsao_chegada").toString();	
								
								// Persistencia Origem...
							
								eiv.setOrigemCidade(jbOrigem.opt("cida_descricao_ibge").toString());
								eiv.setOrigemUf(jbOrigem.opt("sigla_estado").toString());
							}
							
							
						// Destino
						JSONObject jbDestino =  objJsonArray.getJSONObject(i).getJSONObject("destino");
							
							if(jbDestino != null){
								jbDestino.opt("vloc_descricao").toString();
								jbDestino.opt("cidade").toString();
								jbDestino.opt("sigla_estado").toString();
								jbDestino.opt("pais").toString();
								jbDestino.opt("refe_latitude").toString();
								jbDestino.opt("refe_longitude").toString();
								
								//jsonArrayDestino.getJSONObject(d).opt("cidade").toString();
								jbDestino.opt("previsao_chegada").toString();	
																
								// Persistencia Destino...
								eiv.setDestinoCidade(jbDestino.opt("cidade").toString());
								eiv.setDestinoUf(jbDestino.opt("sigla_estado").toString());
								
								
							}						
									
						
						// locais
						JSONArray jsonArrayLocais = objJsonArray.getJSONObject(i).getJSONArray("locais");
						
						int arrayLocaisCount = 0;
						
						if(jsonArrayLocais != null){
							
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
								
								// persistir 
								
							}
							
						}
						
						// Valor Frete					
						JSONArray jsonArrayFrete = objJsonArray.getJSONObject(i).getJSONArray("valor_frete");
						
						int arrayFreteCount = 0;
						
						if(jsonArrayFrete != null){
							
							arrayFreteCount = jsonArrayFrete.length();
							
							for (int f=0; f < arrayFreteCount ; f++){
								// jsonArrayTecnologia.getJSONObject(it).opt("term_numero_terminal").toString().trim();
								
								//String docTransportador = objJsonArray.getJSONObject(i).opt("documento_transportador").toString();
								
								// Se docTransportador for diferente de null
								//if (!docTransportador.equals("null")) {
								
								
								//String embarcador = jsonArrayLocais.getJSONObject(f).opt("embarcador").toString();
								
								//eiv.setEmbarcadorOrigem(embarcador);
								if(jsonArrayLocais.getJSONObject(f).opt("valor_frete") != null){
									BigDecimal vlrFrete = new BigDecimal(jsonArrayLocais.getJSONObject(f).optDouble("valor_frete"));
									eiv.setValor(vlrFrete);
								}
								
								
								
								
								String t1= "a";
								String b = t1;
								// persistir 
								
							}
							
						}

						// Persiste Boletim_Informativo
						
						
						// Persiste Tabela evento_rastreador

						
					}
					
					eiv.setId(ev.getId());
					eiv.setArea(ev.getArea());
					evInfoViagService.crud().save(eiv);
					
					EventoInfoRastreador eir = new EventoInfoRastreador();
					
					if(listArray.size() >= 0){
						
						for(int ei=0; ei < listArray.size();ei++){
							
							//eir.setId(eiv.getId());
							eir.setEvInfoViagem(eiv);
							eir.setCodigo(listArray.get(ei).getCodigo());
							eir.setDescricao(listArray.get(ei).getDescricao());
							eir.setTelemonitorado(listArray.get(ei).getTelemonitorado());
							eir.setNumeroTerminal(listArray.get(ei).getNumeroTerminal());
							eir.setVtecCodigo(listArray.get(ei).getVtecCodigo());
							eir.setIsca(listArray.get(ei).getIsca());
							
							evInfoRastreadorService.crud().save(eir);
							
						}
						
					}
					
				}
				
			}			
		}
			
		}
		
		
		
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
	
	public void backListEvento(){
		try {
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("../bi/bilst.xhtml");			
			
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de Eventos" + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
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
				emailCcParaBoletim = emailCcParaBoletim + addEmailGlobal5.toLowerCase() + ',';
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
			/*
			int qtdEmail = lstEmailGlobal5.size();
			
			if(qtdEmail > 0){
				for(int i = 0 ; i < qtdEmail ; i++){
					emailListGlobal5 +=  lstEmailGlobal5.get(i) + ";";
				}
			}
			*/
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
			emailCcParaBoletim = "";
			if(qtdEmail >= 0){
				for(int i = 0 ; i < qtdEmail ; i++){
					//emailListGlobal5 = emailListGlobal5 + lstEmailGlobal5.get(i) + ";";
					emailListGlobal5 =  lstEmailGlobal5.get(i) + ";";
					emailCcParaBoletim = emailCcParaBoletim + lstEmailGlobal5.get(i) + ',';
				}
			}
			
			//arrayJsonNfse.remove(zer);
			arrayJsonGlobal5.remove(zer);
			
			this.setContaEmailMultiploGlobal5("");
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("frmRegistro:frmBoletim:frmDadosBoletim:frmDadosEmailEnvio:listEmailG5");
		
	}
	
	// Final teste novo método ws chp
	
	public void convertHtmlToPdf(){
		//Jtidy tidy = new Tidy();
	}
	
	public BoletimInformativo getBi() {
		return bi;
	}

	public void setBi(BoletimInformativo bi) {
		this.bi = bi;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdEiv() {
		return idEiv;
	}

	public void setIdEiv(Integer idEiv) {
		this.idEiv = idEiv;
	}

	public Integer getIdBi() {
		return idBi;
	}

	public void setIdBi(Integer idBi) {
		this.idBi = idBi;
	}

	public List<TipoSuspeita> getLstTipoSuspeita() {
		return lstTipoSuspeita;
	}

	public void setLstTipoSuspeita(List<TipoSuspeita> lstTipoSuspeita) {
		this.lstTipoSuspeita = lstTipoSuspeita;
	}

	public Character getTipoSelecaoBi() {
		return tipoSelecaoBi;
	}

	public void setTipoSelecaoBi(Character tipoSelecaoBi) {
		this.tipoSelecaoBi = tipoSelecaoBi;
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

	public Integer getNumIdEvento() {
		return numIdEvento;
	}

	public void setNumIdEvento(Integer numIdEvento) {
		this.numIdEvento = numIdEvento;
	}

	public boolean isPainelDadosSinistro() {
		return painelDadosSinistro;
	}

	public void setPainelDadosSinistro(boolean painelDadosSinistro) {
		this.painelDadosSinistro = painelDadosSinistro;
	}

	public boolean isPainelDadosSuspeita() {
		return painelDadosSuspeita;
	}

	public void setPainelDadosSuspeita(boolean painelDadosSuspeita) {
		this.painelDadosSuspeita = painelDadosSuspeita;
	}

	public boolean isPainelClassSuspeitaRoubo() {
		return painelClassSuspeitaRoubo;
	}

	public void setPainelClassSuspeitaRoubo(boolean painelClassSuspeitaRoubo) {
		this.painelClassSuspeitaRoubo = painelClassSuspeitaRoubo;
	}

	public boolean isPainelClassSinistroDano() {
		return painelClassSinistroDano;
	}

	public void setPainelClassSinistroDano(boolean painelClassSinistroDano) {
		this.painelClassSinistroDano = painelClassSinistroDano;
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
	
	
	public boolean isPainelRelatoPrimProcedimento() {
		return painelRelatoPrimProcedimento;
	}

	public void setPainelRelatoPrimProcedimento(boolean painelRelatoPrimProcedimento) {
		this.painelRelatoPrimProcedimento = painelRelatoPrimProcedimento;
	}

	public boolean isPainelBoletimFinal() {
		return painelBoletimFinal;
	}

	public void setPainelBoletimFinal(boolean painelBoletimFinal) {
		this.painelBoletimFinal = painelBoletimFinal;
	}

	public BoletimInformativoService getBiService() {
		return biService;
	}

	public void setBiService(BoletimInformativoService biService) {
		this.biService = biService;
	}

	public BoletimInformativoRelatoService getBiRelService() {
		return biRelService;
	}

	public void setBiRelService(BoletimInformativoRelatoService biRelService) {
		this.biRelService = biRelService;
	}

	public EventoService getEvService() {
		return evService;
	}

	public void setEvService(EventoService evService) {
		this.evService = evService;
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

	public Crud<BoletimInformativoNumeracao> getBoletimNumeracaoCrud() {
		return boletimNumeracaoCrud;
	}

	public void setBoletimNumeracaoCrud(Crud<BoletimInformativoNumeracao> boletimNumeracaoCrud) {
		this.boletimNumeracaoCrud = boletimNumeracaoCrud;
	}

	public String getTipoPerdaSel() {
		return tipoPerdaSel;
	}

	public void setTipoPerdaSel(String tipoPerdaSel) {
		this.tipoPerdaSel = tipoPerdaSel;
	}

	public String getTipoDanoSel() {
		return tipoDanoSel;
	}

	public List<BoletimInformativoRelato> getListBiRelato() {
		listarBiRelatos();
		return listBiRelato;
	}

	public void setListBiRelato(List<BoletimInformativoRelato> listBiRelato) {
		this.listBiRelato = listBiRelato;
	}

	public void setTipoDanoSel(String tipoDanoSel) {
		this.tipoDanoSel = tipoDanoSel;
	}

	public Integer getNumIdTpSuspeita() {
		return numIdTpSuspeita;
	}

	public void setNumIdTpSuspeita(Integer numIdTpSuspeita) {
		this.numIdTpSuspeita = numIdTpSuspeita;
	}

	public Integer getCodTipoSuspeitaClone() {
		return codTipoSuspeitaClone;
	}

	public void setCodTipoSuspeitaClone(Integer codTipoSuspeitaClone) {
		this.codTipoSuspeitaClone = codTipoSuspeitaClone;
	}

	public boolean isPainelTipoSuspeitaOutros() {
		return painelTipoSuspeitaOutros;
	}

	public void setPainelTipoSuspeitaOutros(boolean painelTipoSuspeitaOutros) {
		this.painelTipoSuspeitaOutros = painelTipoSuspeitaOutros;
	}

	public boolean isPainelBoletimFinalSuspeita() {
		return painelBoletimFinalSuspeita;
	}

	public void setPainelBoletimFinalSuspeita(boolean painelBoletimFinalSuspeita) {
		this.painelBoletimFinalSuspeita = painelBoletimFinalSuspeita;
	}

	public boolean isBoletimSalvo() {
		return boletimSalvo;
	}

	public void setBoletimSalvo(boolean boletimSalvo) {
		this.boletimSalvo = boletimSalvo;
	}

	public boolean isSubPnDadosCarreta() {
		return subPnDadosCarreta;
	}

	public void setSubPnDadosCarreta(boolean subPnDadosCarreta) {
		this.subPnDadosCarreta = subPnDadosCarreta;
	}

	public boolean isSubPnDadosCarreta1() {
		return subPnDadosCarreta1;
	}

	public void setSubPnDadosCarreta1(boolean subPnDadosCarreta1) {
		this.subPnDadosCarreta1 = subPnDadosCarreta1;
	}

	public boolean isSubPnDadosCarreta2() {
		return subPnDadosCarreta2;
	}

	public void setSubPnDadosCarreta2(boolean subPnDadosCarreta2) {
		this.subPnDadosCarreta2 = subPnDadosCarreta2;
	}

	public boolean isSubPnDadosCarreta3() {
		return subPnDadosCarreta3;
	}

	public void setSubPnDadosCarreta3(boolean subPnDadosCarreta3) {
		this.subPnDadosCarreta3 = subPnDadosCarreta3;
	}

	public boolean isSubPnDadosMotorista1() {
		return subPnDadosMotorista1;
	}

	public void setSubPnDadosMotorista1(boolean subPnDadosMotorista1) {
		this.subPnDadosMotorista1 = subPnDadosMotorista1;
	}

	public boolean isSubPnDadosMotorista2() {
		return subPnDadosMotorista2;
	}

	public void setSubPnDadosMotorista2(boolean subPnDadosMotorista2) {
		this.subPnDadosMotorista2 = subPnDadosMotorista2;
	}

	public List<VersaoTecnologia> getLstVersaoTecnologia() {
		return lstVersaoTecnologia;
	}

	public void setLstVersaoTecnologia(List<VersaoTecnologia> lstVersaoTecnologia) {
		this.lstVersaoTecnologia = lstVersaoTecnologia;
	}

	public List<String> getLstEmailGlobal5() {
		return lstEmailGlobal5;
	}

	public String getAddEmailGlobal5() {
		return addEmailGlobal5;
	}

	public JSONArray getArrayJsonGlobal5() {
		return arrayJsonGlobal5;
	}

	public Integer getNivel2() {
		return nivel2;
	}

	public String getContaEmailMultiploGlobal5() {
		return contaEmailMultiploGlobal5;
	}

	public String getEmailListGlobal5() {
		return emailListGlobal5;
	}

	public void setLstEmailGlobal5(List<String> lstEmailGlobal5) {
		this.lstEmailGlobal5 = lstEmailGlobal5;
	}

	public void setAddEmailGlobal5(String addEmailGlobal5) {
		this.addEmailGlobal5 = addEmailGlobal5;
	}

	public void setArrayJsonGlobal5(JSONArray arrayJsonGlobal5) {
		this.arrayJsonGlobal5 = arrayJsonGlobal5;
	}

	public void setNivel2(Integer nivel2) {
		this.nivel2 = nivel2;
	}

	public void setContaEmailMultiploGlobal5(String contaEmailMultiploGlobal5) {
		this.contaEmailMultiploGlobal5 = contaEmailMultiploGlobal5;
	}

	public void setEmailListGlobal5(String emailListGlobal5) {
		this.emailListGlobal5 = emailListGlobal5;
	}

	public String getEmailCcParaBoletim() {
		return emailCcParaBoletim;
	}

	public void setEmailCcParaBoletim(String emailCcParaBoletim) {
		this.emailCcParaBoletim = emailCcParaBoletim;
	}
	
			
}
