package br.com.global5.rastreamento.bean.bi;


import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.StoredProcedureQuery;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;


import br.com.global5.infra.Crud;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.rastreamento.model.bi.BoletimInformativo;
import br.com.global5.rastreamento.model.bi.BoletimInformativoNumeracao;
import br.com.global5.rastreamento.model.bi.Evento;
import br.com.global5.rastreamento.model.bi.EventoInfoViagem;
import br.com.global5.rastreamento.model.enums.TipoSuspeita;
import br.com.global5.rastreamento.service.bi.BoletimInformativoService;
import br.com.global5.rastreamento.service.bi.EventoInfoViagemService;
import br.com.global5.rastreamento.service.bi.EventoService;
import br.com.global5.rastreamento.service.bi.TipoSuspeitaService;
import br.com.global5.template.exception.BusinessException;

@Named
@ViewAccessScoped
public class BoletimInformativoMB2_Clone implements Serializable {

	private static final long serialVersionUID = 212151315511555L;

	private BoletimInformativo bi;
	
	private Evento ev;
	private EventoInfoViagem eiv;
	private Usuario usuario;
	
	private Integer id;
	private Integer idEiv;
	private Integer idBi;
	
	private List<TipoSuspeita> lstTipoSuspeita;
	
	private Character tipoSelecaoBi = null;
	private String 	  tipoPerdaSel = null;
	
	private String    eventoInicial = "";
	private Integer   numeroSM		= null;
	private Integer   numIdEvento= null;
	
	private boolean painelDadosSinistro 		 = false;
	private boolean painelDadosSuspeita 		 = false;
	
	private boolean painelClassSuspeitaRoubo 	 = false;
	private boolean painelClassSinistroDano 	 = false;
	private boolean painelClassSinistroPerda 	 = false;
	private boolean painelClassSinistroPerdaDano = false;
		
	@Inject
	BoletimInformativoService biService;
	
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
		
		
		
		bi =  new BoletimInformativo();
		id = null;
		bi.setTipoSuspeita(new TipoSuspeita());

		lstTipoSuspeita = tipoSuspService.crud().isNull("exclusao").listAll();
		
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

	
	public void abrirNovoBID(Integer idEvento, Character tpBoletim){

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		if(idEvento == null || tpBoletim == null ) {

			if ( idEvento == null ){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Número da SM não carregou dados para o BI. Informe a TI!"));
			}
			
			if ( tpBoletim == null ) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "O tipo do boletim não foi informado. Selecione para continuar!"));
			}
			
		}
		
		

		if(idEvento != null ) {
			
			try {
				
				//clear();
				tipoSelecaoBi = tpBoletim;
				numIdEvento = idEvento;
				
				//findByIdEvento(idEvento);

				selecaoTipoEvento();
				
			} catch (Exception e) {
				e.printStackTrace(pw);
			}
			
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Número da SM não gerou dados para BI!"));
		}
		
	}
	

	
	public void abrirNovoBI(Integer idEvento, Character tpBoletim, boolean boletimFinal){

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		if(idEvento == null || tpBoletim == null ) {

			if ( idEvento == null ){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Número da SM não carregou dados para o BI. Informe a TI!"));
			}
			
			if ( tpBoletim == null ) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "O tipo do boletim não foi informado. Selecione para continuar!"));
			}
			
		}
		
		

		if(idEvento != null ) {
			
			try {
				
				clear();
				tipoSelecaoBi = tpBoletim;
				numIdEvento = idEvento;
				
				//==findByIdEvento(idEvento);

				selecaoTipoEvento();
				
			} catch (Exception e) {
				e.printStackTrace(pw);
			}
			
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Número da SM não gerou dados para BI!"));
		}
		
	}
	
	public void showBI(){

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		if(numIdEvento != null ) {
			
			try {
				clear();
				FacesContext.getCurrentInstance().getExternalContext().redirect("../bi/bi.xhtml");
				
			} catch (Exception e) {
				e.printStackTrace(pw);
			}
			
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Número da SM não gerou dados para BI!"));
		}
		
	}
	
	public void findById(Integer id){
	        if(id == null){
	            throw new BusinessException("O id é obrigatório");
	        }
	        
	        bi = biService.crud().get(id);

	        if(bi == null){
	            throw new BusinessException("Registro não foi encontrado pelo id: " + id);
	        }
	}
	
	public void showBICopia(){

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		if(numIdEvento != null ) {
			
			try {				
				FacesContext.getCurrentInstance().getExternalContext().redirect("../bi/bi.xhtml");
				
			} catch (Exception e) {
				e.printStackTrace(pw);
			}
			
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Número da SM não gerou dados para BI!"));
		}
		
	}
	
	public void selecaoTipoEvento(){
		
		RequestContext context = RequestContext.getCurrentInstance();
		
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
		
		findByIdEvento(numIdEvento);
		
		showBI();
				
		//context.update("form0");
		

	}
	
	public void salvarTeste(){
		
		
		bi.setNrGlobal(1);
		bi.setNrSequencia(2);
		bi.setTipo(tipoSelecaoBi);
		bi.setRotulo("RTL");
		bi.setTipoSuspeita(new TipoSuspeita(669));
		bi.setEvento(new Evento(88));
		bi.setUsuEmissao(checkUsuario.valid());
		bi.setDtEmissao(new Date());
		bi.setTipo(tipoSelecaoBi);
		
		
		biService.crud().save(bi);
		
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Boletim gravado com sucessso!") );
			
	
	}
	
	public void tipoPerdaSelecionada(){
		clearTipoPerda();
		if ( tipoPerdaSel.equals("furto")){
			bi.setPerdaTpFurto(true);
		} else 		
		if ( tipoPerdaSel.equals("furtoQualificado")) {
			bi.setPerdaTpFurtoQualificado(true);
		} else 		
		if ( tipoPerdaSel.equals("roubo")) {
			bi.setPerdaTpRoubo(true);			
		} else 		
		if ( tipoPerdaSel.equals("extorsao")) {
			bi.setPerdaTpExtorsao(true);			
		} else 		
		if ( tipoPerdaSel.equals("apropriacaoIndebita")) {
			bi.setPerdaTpApropriacaoIndebita(true);
		} else 		
		if ( tipoPerdaSel.equals("estelionato")) {
			bi.setPerdaTpEstelionato(true);
		} else 		
		if ( tipoPerdaSel.equals("receptacao")) {
			bi.setPerdaTpReceptacao(true);
		} else 		
		if ( tipoPerdaSel.equals("falsidadeIdeologica")) {
			bi.setPerdaTpReceptacao(true);
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
			bi.setPerdaTpReceptacao(false);
		}
	}
	
	public void salvarBoletim(){
		
		Integer numGlobalPorTipo = null;
		
		if ( tipoSelecaoBi == bi.EVENTO_TIPO_DANO || tipoSelecaoBi == bi.EVENTO_TIPO_PERDA  || tipoSelecaoBi == bi.EVENTO_TIPO_SUSPEITA ) {
			
			// nrGlobal: bi_nr_global int4 not null, >> número global do tipo (Tabela boletim_informativo_numeracao | campo: bin_contador )
				//	será atualizado apos a tabela de evento no campo eve_nr_boletim_suspeita/perda/dano
			
			// nrSequencia: bi_nr_sequencia int4 not null, << sequencia dentro do evento ( Tabela evento  | campo:  eve_nr_evento )
			
			// id: bioid (key), >>  (Tabela boletim_informativo_numeracao | campo: eve_nr_boletim)
			
			// TipoSuspeita , boletim_informativo campo bi_susp_tp_enumoid esta not null mas não deve ser pois temos os casos de sinistro
			
			// 
			StoredProcedureQuery qryBINum = boletimNumeracaoCrud.getEntityManager().createNamedStoredProcedureQuery("boletim_informativo_global");
			qryBINum.setParameter("tipo", tipoSelecaoBi);
			qryBINum.execute();
			
			
			numGlobalPorTipo = (Integer) qryBINum.getOutputParameterValue("bin");
			
			Long result = numGlobalPorTipo.longValue();
			
			if ( result > 0 ){
				
				// Contador de sequencia do boletim
				Integer countNrBoletim = null;
				
				if( ev.getNrBoletim() == null || ev.getNrBoletim() == 0 ){
					
					countNrBoletim = 1;
					
				} else {
					
					countNrBoletim = ev.getNrBoletim() + 1;
					
				}
				
				//EntityManager eBI = biService.crud().getEntityManager();
				
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
				bi.setTipoSuspeita(new TipoSuspeita(669));
				bi.setEvento(ev);
			
			
				biService.crud().save(bi);
				
				
				//Atualização na Tabela Evento
				
				/* Set Nr Boletim: Perda|Dano|Suspeita */
				if( tipoSelecaoBi == bi.EVENTO_TIPO_PERDA ) {
					
					ev.setNrBoletimPerda(bi.getId());
					
				} else 
					
					if ( tipoSelecaoBi == bi.EVENTO_TIPO_DANO ) {
					
						ev.setNrBoletimDano(bi.getId());
						
				} else 
						
					if ( tipoSelecaoBi == bi.EVENTO_TIPO_SUSPEITA ) {
						
						ev.setNrBoletimSuspeita(bi.getId());
						
				}
				
				/* Set Tipo_inicial */
				if ( ev.getTipoInicial() == null){
					ev.setTipoInicial(tipoSelecaoBi);
				} 
				
				ev.setNrBoletim(countNrBoletim);
				ev.setBoletimInformativo(bi);
				
				evService.update(ev);
					
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Boletim gravado com sucessso!") );
				
				
			} else {
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "erro", "Não foi possível gerar o Numerador para Registro do BI!") );
				return;
			}
			
			
		}
		
	}
	
	public void adicionarNovaSeqBoletim(Integer idEvento, Integer idUltimoBi){
		
		//
		
		numIdEvento = idEvento;
		id = idUltimoBi;
		
		if ( idEvento != null && idUltimoBi != null) {
			
			
			
			ev = new Evento();
			eiv = new EventoInfoViagem();

			ev = evService.crud().get(idEvento);
			eiv = evInfoViagService.crud().get(idEvento);
			
			BoletimInformativo bi2 = new BoletimInformativo();
			
			bi2 = ev.getBoletimInformativo(); 
			
			bi2.setId(null);
			
			//clear();
			
			try {
				bi = (BoletimInformativo) bi2.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			
				Hibernate.initialize(ev);
				Hibernate.initialize(eiv);
				//Hibernate.initialize(bi);
			
				showBICopia();
				tipoSelecaoBi = bi.getTipo();
				painelDadosSinistro = true;
				painelClassSinistroPerda = true;
				
				//RequestContext context = RequestContext.getCurrentInstance();
    			//context.update("");

    		
			
		} else {
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Evento não consta boletim anterior gerado!"));
			
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

	public BoletimInformativoService getBiService() {
		return biService;
	}

	public void setBiService(BoletimInformativoService biService) {
		this.biService = biService;
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


	
		
}
