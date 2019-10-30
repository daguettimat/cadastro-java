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
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Hibernate;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

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

/**
 * 
 * @author francis
 * @date 2019-03-19 
 * 
 */

@Named
@ViewAccessScoped
public class EventoMB2 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Evento ev;
	private EventoInfoViagem eiv;
	private BoletimInformativo bi;
	
	private Usuario usuario;
	
	private Integer id;
	private Integer idEiv;
	private Integer idBi;
	
	private List<TipoSuspeita> lstTipoSuspeita;
	
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
	
	
	/*** Variaveis tela bi_add ***/
	
	
	//public static final char EVENTO_TIPO_SUSPEITA 	= 'S';
	//public static final char EVENTO_TIPO_PERDA		= 'P';
	//public static final char EVENTO_TIPO_DANO		= 'D';
	
	@Inject
	EventoService evService;
	
	@Inject
	EventoInfoViagemService evInfoViagService;

	@Inject
	BoletimInformativoService biService;
	
	@Inject
	TipoSuspeitaService tipoSuspService;
	
	@Inject
	private Crud<Evento> eventoCrud;
	
	@Inject
	private Crud<BoletimInformativoNumeracao> boletimNumeracaoCrud;
	
	@PostConstruct
	public void init(){
		
		clear();		

	}

	private void clear() {
		
		id = null;
		idEiv = null;
		idBi = null;		
		
		ev = new Evento();		
		bi = new BoletimInformativo();
		
		lstTipoSuspeita = tipoSuspService.crud().isNull("exclusao").listAll();
		
		
	}
	
	
	
	public void findById(Integer id){
		
		if( id == null ){
			throw new BusinessException("O id do evento não foi encontrado. Contate o suporte TI");
		}
		
		ev = evService.crud().get(id);
		eiv = evInfoViagService.crud().get(id);
		
		bi = new BoletimInformativo();
		
		Hibernate.initialize(ev);
		Hibernate.initialize(eiv);
		
		bi.setUsuEmissao(checkUsuario.valid());
		bi.setDtEmissao(new Date());
		
		Hibernate.initialize(bi);
		
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
	
	public void selecaoTipoEventoNovoBi(){
		
		String teste = tipoSelecaoBi.toString();
		
		String b = "a";
		String e = b;

	}
	

	
	public void selecaoNumeroSM(){
		
		Integer teste = numeroSM;
		
		String b = "a";
		String e = b;

	}
	
	public void gerarNovoBI(){
		
		EntityManager emE = evService.crud().getEntityManager();
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		Integer idEventoReturn = null;
		
		
		try {

			StoredProcedureQuery qryNovoBI = eventoCrud.getEntityManager().createNamedStoredProcedureQuery("evento_i");
			qryNovoBI.setParameter("sm", numeroSM);
			qryNovoBI.setParameter("usuoid", checkUsuario.valid().getId());			
			qryNovoBI.execute();
						
			idEventoReturn = (Integer) qryNovoBI.getOutputParameterValue("evento");
			
			Long result = idEventoReturn.longValue();
			
			if(result > 1){
				numIdEvento = idEventoReturn;				
				selecaoTipoEvento();
				
			} else {
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "erro", "Não foi possível gerar o BI!") );
				
			}
			
		} catch (Exception e) {
			e.printStackTrace(pw);
			
			//String msg = ExceptionUtils.getCause(e);
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "erro", msg) );
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
		
		showBI();
		findById(numIdEvento);
		
		
		
		context.update("form0");
		

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
				
				//EntityManager eBI = biService.crud().getEntityManager();
				
				// Persiste Boletim
				String codRotulo = "BI-" + tipoSelecaoBi + " " + ev.getSincViagem().getId() +
									"-" + ev.getNrEvento() + "." + ev.getNrEvento() + "/" + numGlobalPorTipo +
									"-" + ev.getAno();
				
				
				
				bi.setNrGlobal(numGlobalPorTipo);
				bi.setNrSequencia(ev.getNrEvento());
				bi.setTipo(tipoSelecaoBi);
				bi.setRotulo(codRotulo);
				bi.setTipoSuspeita(new TipoSuspeita(669));
				bi.setEvento(ev);
			
				biService.crud().save(bi);
					
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Boletim gravado com sucessso!") );
				
				
			} else {
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "erro", "Não foi possível gerar o Numerador para Registro do BI!") );
				return;
			}
			
			
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

	public BoletimInformativo getBi() {
		return bi;
	}

	public void setBi(BoletimInformativo bi) {
		this.bi = bi;
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
	
	
}
