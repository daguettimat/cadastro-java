package br.com.global5.rastreamento.bean.bi;


import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.rastreamento.model.bi.BoletimInformativo;
import br.com.global5.rastreamento.model.bi.BoletimInformativoNumeracao;
import br.com.global5.rastreamento.model.bi.BoletimInformativoRelato;
import br.com.global5.rastreamento.model.bi.CheckBI;
import br.com.global5.rastreamento.model.bi.Evento;
import br.com.global5.rastreamento.model.bi.EventoInfoViagem;
import br.com.global5.rastreamento.model.enums.TipoDano;
import br.com.global5.rastreamento.model.enums.TipoPerda;
import br.com.global5.rastreamento.model.enums.TipoSuspeita;
import br.com.global5.rastreamento.service.bi.BoletimInformativoRelatoService;
import br.com.global5.rastreamento.service.bi.BoletimInformativoService;
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
	private Integer	  numIdTpSuspeita = null; 
	private Integer   codTipoSuspeitaClone = null;
	
	private boolean painelDadosSinistro 		 = false;
	private boolean painelDadosSuspeita 		 = false;
	
	private boolean painelClassSuspeitaRoubo 	 = false;
	private boolean painelClassSinistroDano 	 = false;
	private boolean painelClassSinistroPerda 	 = false;
	private boolean painelClassSinistroPerdaDano = false;
	
	private boolean painelBoletimFinal			 = false;
	private boolean painelBoletimFinalSuspeita 	 = false;
	private boolean painelTipoSuspeitaOutros	 = false;
	
	// Variaveis Dados SM (para visualização)
	private boolean subPnDadosCarreta = true;
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
					
			
					//bi.setTipoSuspeita(new TipoSuspeita());
					
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
				//bi.setTipoSuspeita(new TipoSuspeita(null));
				id = null;
				
				if (tipoSelecaoBi == bi.EVENTO_TIPO_SUSPEITA){
					bi.setTipoSuspeita(new TipoSuspeita());					
				}
				
				lstTipoSuspeita = tipoSuspService.crud().isNull("exclusao").listAll();	
				selecaoTipoEvento();
				checarEventoDadosCarreta();
				checarEventoDadosMotorista();
				
			}
						
	}

	public void findByIdEvento(Integer idEvento){
		
		if( idEvento == null ){
			throw new BusinessException("O id do evento não foi encontrado. Contate o suporte TI");
		}
		ev = new Evento();
		eiv = new EventoInfoViagem();

		ev = evService.crud().get(idEvento);
		eiv = evInfoViagService.crud().get(idEvento);
		
			Hibernate.initialize(ev);
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
		}
		
		if( checkBi.tipoBoletimFinalRetorno() == true && tipoSelecaoBi == bi.EVENTO_TIPO_PERDA ){
			painelBoletimFinal = true;
		}
	
		if( checkBi.tipoBoletimFinalRetorno() == true && tipoSelecaoBi == bi.EVENTO_TIPO_SUSPEITA ){
			painelBoletimFinalSuspeita = true;
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
		if(ev.getSincViagem().getMotorista1() != null){
			subPnDadosMotorista1 = true;
		}
		
		if(ev.getSincViagem().getMotorista2() != null){
			subPnDadosMotorista2 = true;
		}
	}
	
	
	public void checarEventoDadosCarreta(){
		String b = "a";
		String a = b;
		
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

	
	public void salvarBoletim(){
		
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
			
			//StoredProcedureQuery qryBINum = boletimNumeracaoCrud.getEntityManager().createNamedStoredProcedureQuery("boletim_informativo_global");
			//qryBINum.setParameter("tipo", tipoSelecaoBi);
			//qryBINum.execute();
						
			//numGlobalPorTipo = (Integer) qryBINum.getOutputParameterValue("bin");
			
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
				String codRotulo = "BI-" + tipoSelecaoBi + " " + ev.getSincViagem().getId() +
									"-" + ev.getNrEvento() + "." + countNrBoletim + "/" + numGlobalPorTipo +
									"-" + ev.getAno();
				
				
				bi.setUsuEmissao(checkUsuario.valid());
				bi.setDtEmissao(new Date());
				bi.setNrGlobal(numGlobalPorTipo);
				bi.setNrSequencia(ev.getNrEvento());
				bi.setTipo(tipoSelecaoBi);
				bi.setRotulo(codRotulo);
				
				
				if ( ev.getBoletimInformativo() != null){
					if(ev.getBoletimInformativo().getTipo() == bi.EVENTO_TIPO_SUSPEITA){
						
						if( numIdTpSuspeita == null){
							bi.setTipoSuspeita(new TipoSuspeita(codTipoSuspeitaClone));
						}
						
					}
										
				}
				
				bi.setEvento(new Evento(ev.getId()));
				
//				if(this.getNumIdTpSuspeita() != null){
//					bi.setTipoSuspeita(new TipoSuspeita(this.getNumIdTpSuspeita()));
//				} 
								
				biService.crud().save(bi);
				
				boletimSalvo = true;
				
				// Salvar relato do boletim
				BoletimInformativoRelato biRel = new BoletimInformativoRelato();
				biRel.setEvento(bi.getEvento());
				biRel.setBoletimInformativo(bi);
				biRel.setRelato(bi.getRelato());
				biRel.setDtCriacao(new Date());
				biRel.setTipo("N");
				
				biRelService.crud().save(biRel);
				
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
	
	
	public void onTipoEventoSuspeitaRoubo(AjaxBehaviorEvent event){
		
		this.setNumIdTpSuspeita(this.bi.getTipoSuspeita().getId());
		
		if (this.bi.getTipoSuspeita().getId() ==  669 ){
			this.setPainelTipoSuspeitaOutros(true);
			
		} else {
			this.setPainelTipoSuspeitaOutros(false);
			
		}
				
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
	
			
}
