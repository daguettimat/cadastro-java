package br.com.global5.rastreamento.bean.bi;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.global5.infra.Crud;
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
import br.com.global5.rastreamento.service.bi.TipoDanoService;
import br.com.global5.rastreamento.service.bi.TipoPerdaService;
import br.com.global5.rastreamento.service.bi.TipoSuspeitaService;
import br.com.global5.template.exception.BusinessException;

@Named
@ViewAccessScoped
public class BoletimInformativoViewMB implements Serializable {

	private static final long serialVersionUID = 201915041018L;

	private BoletimInformativo biV;

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
	private String tipoPerdaSel = null;
	private String tipoDanoSel = null;

	private String eventoInicial = "";
	private Integer numeroSM = null;
	private Integer numIdEvento = null;
	private Integer numIdTpSuspeita = null;
	private Integer codTipoSuspeitaClone = null;

	// Variaveis para o relatório
	private String tipoBoletimEmitido = null;
	private String diaDaSemana = null;
	private String classificacao = null;
	private String tipoSubtracao = null;
	private String tipoAbordagem = null;
	private String situacaoMotorista = null;
	private String avarias = null;
	private String tipoEventoSuspeitaRoubo = null;

	// bi.xml (sem uso por enquanto)
	private boolean painelDadosSinistro = false;
	private boolean painelDadosSuspeita = false;

	private boolean painelClassSuspeitaRoubo = false;
	private boolean painelClassSinistroDano = false;
	private boolean painelClassSinistroPerda = false;
	private boolean painelClassSinistroPerdaDano = false;

	private boolean painelBoletimFinal = false;
	private boolean painelBoletimFinalSuspeita = false;
	private boolean painelTipoSuspeitaOutros = false;

	// Variaveis Dados SM (para visualização)
	private boolean subPnDadosCarreta = true;
	private boolean subPnDadosCarreta1 = false;
	private boolean subPnDadosCarreta2 = false;
	private boolean subPnDadosCarreta3 = false;
	
	private boolean subPnDadosMotorista1 = false;
	private boolean subPnDadosMotorista2 = false;
	
	
	
	private boolean boletimSalvo = false;
	private Map<String, Object> options;

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
	TipoDanoService tipoDanoService;

	@Inject
	TipoPerdaService tipoPerdaService;

	@Inject
	private Crud<BoletimInformativoNumeracao> boletimNumeracaoCrud;

	@PostConstruct
	public void init() {

		clear();

		options = new HashMap<String, Object>();
		options.put("draggable", true);
		options.put("modal", true);
		options.put("height", "100%");

		options.put("contentWidth", "100%");
		options.put("resizable", true);
		options.put("maximizable", true);

	}

	
	private void clear() {
		biV = new BoletimInformativo();
		this.id = null;
	}

	
	public void showBi(Integer id) {		
		this.id = id;
		findById(id);

		ev = new Evento();
		eiv = new EventoInfoViagem();

		ev = evService.crud().get(biV.getEvento().getId());
		eiv = evInfoViagService.crud().get(biV.getEvento().getId());

		Hibernate.initialize(ev);
		Hibernate.initialize(eiv);

		selecaoTipoEvento();
		
		
		diaDaSemanaBI(biV.getComIniDiaSemana());
		checarEventoDadosMotorista();
		checarEventoDadosCarreta();
		checarClassificacaoBI();
		checarTipoSubtracao();
		checarTipoAbordagem();
		checarSituacaoMotorista();
		checarAvarias();
		tipoEventoSuspeitaBI();

		if (biV.getTipo() == biV.EVENTO_TIPO_SUSPEITA) {

			tipoBoletimEmitido = "Suspeita de Roubo";

		} else if (biV.getTipo() == biV.EVENTO_TIPO_PERDA) {

			tipoBoletimEmitido = "Sinistro de Perda";

		} else if (biV.getTipo() == biV.EVENTO_TIPO_DANO) {

			tipoBoletimEmitido = "Sinistro de Dano";

		}

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../bi/view/biview.xhtml?id=" + getId());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	
	public void diaDaSemanaBI(Integer numDiaSem) {
		if (biV.getTipo() != biV.EVENTO_TIPO_SUSPEITA){
		if (biV.getComIniDiaSemana() == 1) {
			diaDaSemana = "Domingo";
		} else if (biV.getComIniDiaSemana() == 2) {
			diaDaSemana = "Segunda-Feira";
		} else if (biV.getComIniDiaSemana() == 3) {
			diaDaSemana = "Terça-Feira";
		} else if (biV.getComIniDiaSemana() == 4) {
			diaDaSemana = "Quarta-Feira";
		} else if (biV.getComIniDiaSemana() == 5) {
			diaDaSemana = "Quinta-Feira";
		} else if (biV.getComIniDiaSemana() == 6) {
			diaDaSemana = "Sexta-Feira";
		} else if (biV.getComIniDiaSemana() == 7) {
			diaDaSemana = "Sábado";
		} else {
			diaDaSemana = "";			
		}
		}
	}

	
	public void tipoEventoSuspeitaBI(){
		if (biV.getTipo() == biV.EVENTO_TIPO_SUSPEITA) {

			Criteria criteriaS = tipoSuspService.crud().getSession().createCriteria(TipoSuspeita.class);

			criteriaS.add(Restrictions.eq("id", biV.getTipoSuspeita().getId()));

			int result = criteriaS.list().size();
			
			if (result == 1) {
				List<TipoSuspeita> listS = new ArrayList<>(criteriaS.list());				
				this.setTipoEventoSuspeitaRoubo(listS.get(0).getDescricao().toString());
				
			} else {
				tipoEventoSuspeitaRoubo = "Erro_Tipo_Evento_Suspeita_Roubo";
			}
			
		}
	}
	
	
	public void checarEventoDadosMotorista(){
		if(biV.getEvento().getSincViagem().getMotorista1() != null){
			subPnDadosMotorista1 = true;
		}
		
		if(biV.getEvento().getSincViagem().getMotorista2() != null){
			subPnDadosMotorista2 = true;
		}
	}
	
	public void checarEventoDadosCarreta(){
		
		if( biV.getEvento().getSincViagem().getVeiculo() != null ){
			// verifica se existe carretas ligada ao veiculo
			if (biV.getEvento().getSincViagem().getCarreta1() != null){
				// mostra dados carreta 1
				subPnDadosCarreta1 = true;
			}
			if (biV.getEvento().getSincViagem().getCarreta2() != null){
				// mostra dados carreta 2
				subPnDadosCarreta2 = true;
			}
			if (biV.getEvento().getSincViagem().getCarreta3() != null){
				// mostra dados carreta 3
				subPnDadosCarreta1 = true;
			}
			
		} else {
			// não mostra dados carretas
			subPnDadosCarreta = false;
		}
		
		if (biV.getEvento().getSincViagem().getCarreta1() == null &&
				biV.getEvento().getSincViagem().getCarreta2() == null &&
				biV.getEvento().getSincViagem().getCarreta3() == null ){
			subPnDadosCarreta = false;
		}
		
	}
	
	public void checarClassificacaoBI() {

		if (biV.getTipo() == biV.EVENTO_TIPO_DANO) {

			Criteria criteriaD = tipoDanoService.crud().getSession().createCriteria(TipoDano.class);

			criteriaD.add(Restrictions.eq("id", biV.getTipoDano().getId()));

			int result = criteriaD.list().size();

			if (result == 1) {
				List<TipoDano> listD = new ArrayList<>(criteriaD.list());
				
				this.setClassificacao(listD.get(0).getDescricao().toString());
				
			} else {
				classificacao = "Erro_Class_Dano";
			}

		} else if (biV.getTipo() == biV.EVENTO_TIPO_PERDA) {

			Criteria criteriaP = tipoPerdaService.crud().getSession().createCriteria(TipoPerda.class);

			criteriaP.add(Restrictions.eq("id", biV.getTipoPerda().getId()));

			int result = criteriaP.list().size();

			if (result == 1) {
				List<TipoPerda> listP = new ArrayList<>(criteriaP.list());

				this.setClassificacao(listP.get(0).getDescricao().toString());

			} else {
				classificacao = "Erro_Class_Perda";
			}

		}

	}

	
	public void checarTipoSubtracao() {

		if (biV.getTipo() == biV.EVENTO_TIPO_PERDA) {

			tipoSubtracao = "";

			if (biV.getPerdaTpVeiCarga() == true) {
				tipoSubtracao = "Veículo e Carga; ";
			}
			if (biV.getPerdaTpCarga() == true) {
				tipoSubtracao = tipoSubtracao + "Carga; ";
			}
			if (biV.getPerdaTpVei() == true) {
				tipoSubtracao = tipoSubtracao + "Veículo; ";
			}
			if (biV.getPerdaTpPertences() == true) {
				tipoSubtracao = tipoSubtracao + "Pertences Pessoais; ";
			}
			if (biV.getPerdaTpPneus() == true) {
				tipoSubtracao = tipoSubtracao + "Estepe/Pneus; ";
			}
			if (biV.getPerdaTpRastreador() == true) {
				tipoSubtracao = tipoSubtracao + "Rastreador; ";
			}
			if (biV.getPerdaTpTacografo() == true) {
				tipoSubtracao = tipoSubtracao + "Tacógrafo; ";
			}
			if (biV.getPerdaTpCombustivel() == true) {
				tipoSubtracao = tipoSubtracao + "Combustível; ";
			}
		}

	}

	
	public void checarTipoAbordagem() {

		tipoAbordagem = "" ;
		
		if (biV.getTipo() == biV.EVENTO_TIPO_PERDA) {
			if( biV.getPerdaAbordagemMovimento() != null ){
				if(biV.getPerdaAbordagemMovimento().booleanValue() == true ){
					tipoAbordagem = "Veículo em Movimento; ";
				} else if (biV.getPerdaAbordagemMovimento().booleanValue() == false ) {
					tipoAbordagem = "Veículo Parado; ";
				}
			} else {
				tipoAbordagem = "Não houve abordagem; ";
			}
		}

	}
	
	
	public void checarSituacaoMotorista(){
		
		if (biV.getTipo() == biV.EVENTO_TIPO_PERDA || biV.getTipo() == biV.EVENTO_TIPO_DANO) {

			situacaoMotorista = "";

			if (biV.getMotLocalizado() == true) {
				situacaoMotorista = "Localizado; ";
			} else {
				situacaoMotorista = "Não Localizado; ";
			}
			if (biV.getMotFeridoPassaBem() == true) {
				situacaoMotorista = situacaoMotorista + "Passa Bem; ";
			} 
			if (biV.getMotHospitalizado() == true) {
				situacaoMotorista = situacaoMotorista + "Hospitalizado; ";
			} 
			if (biV.getMotFeridoLevemente() == true) {
				situacaoMotorista = situacaoMotorista + "Ferido Levemento; ";
			} 
			if (biV.getMotObito() == true) {
				situacaoMotorista = situacaoMotorista + "Em Óbito; ";
			} 
			if (biV.getMotFeridoGravemente() == true) {
				situacaoMotorista = situacaoMotorista + "Ferido Gravemente; ";
			} 
			
		}
		
	}
	
	
	public void checarAvarias(){
		
		if (biV.getTipo() == biV.EVENTO_TIPO_PERDA || biV.getTipo() == biV.EVENTO_TIPO_DANO) {

			avarias = "";

			if (biV.getAvariaCavalo() == true) {
				avarias = "Cavalo; ";
			} 
			if (biV.getAvariaCarreta() == true) {
				avarias = avarias + "Carreta; ";
			} 
			if (biV.getAvariaCarga() == true) {
				avarias = avarias + "Carga; ";
			} 
			if (biV.getAvariaSaqueMercadoria() == true) {
				avarias = avarias + "Saque de Mercadoria; ";
			} 
			
			if ( biV.getAvariaCavalo() == false && biV.getAvariaCarreta() == false && biV.getAvariaCarga() == false &&biV.getAvariaSaqueMercadoria() == false ){
				avarias = "Sem Avaria";
			}
			
		}
				
	}


	public void findById(Integer id) {
		if (id == null) {
			throw new BusinessException("O id é obrigatório");
		}
		
		biV = biService.crud().get(id);

		if (biV == null) {
			throw new BusinessException("Registro não foi encontrado pelo id: " + id);
		}
	}

	
	public void selecaoTipoEvento() {

		painelBoletimFinal = false;
		painelBoletimFinalSuspeita = false;

		if (biV.getTipo() == biV.EVENTO_TIPO_DANO || biV.getTipo() == biV.EVENTO_TIPO_PERDA) {

			painelDadosSinistro = true;
			painelClassSinistroPerdaDano = true;

		}

		if (biV.getTipo() == biV.EVENTO_TIPO_SUSPEITA) {

			painelDadosSinistro = false;
			painelDadosSuspeita = true;

		}

		if (biV.getTipo() == biV.EVENTO_TIPO_PERDA) {

			painelDadosSuspeita = false;
			painelClassSinistroPerda = true;

		} else {

			painelClassSinistroPerda = false;

		}

		if (biV.getTipo() == biV.EVENTO_TIPO_DANO) {

			painelDadosSuspeita = false;
			painelClassSinistroDano = true;

		} else {

			painelClassSinistroDano = false;

		}
		
		// Se ultimo Bi do Evento é igual ao BiView e Se Evento_Tipo_Final é <> NULL -> Libera painel Final
		if (ev != null && ev.getTipoFinal() != null) {

			if (ev.getBoletimInformativo().getId() == biV.getId() && ev.getTipoFinal() == biV.EVENTO_TIPO_DANO) {
				painelBoletimFinal = true;
			}

			if (ev.getBoletimInformativo().getId() == biV.getId() && ev.getTipoFinal() == biV.EVENTO_TIPO_PERDA) {
				painelBoletimFinal = true;
			}

			if (ev.getBoletimInformativo().getId() == biV.getId() && ev.getTipoFinal() == biV.EVENTO_TIPO_SUSPEITA) {
				painelBoletimFinalSuspeita = true;
			}

		}
		
	}

	
	private void listarBiRelatos() {

		Criteria criteria = biRelService.crud().getSession().createCriteria(BoletimInformativoRelato.class);		
		
		// data menor ou igual a data do bi selecionado
		if( biV.getEvento().getId() != null){
			// id do evento
			criteria.add(Restrictions.eq("evento.id", biV.getEvento().getId()));
			
			Date dtBiRelato = null;	
			
			if(criteria.list().size() > 0){
				
				List<BoletimInformativoRelato> lstRelato = criteria.list();
				
				for(int ib = 0 ; ib < criteria.list().size() ; ib++){
					
					if (lstRelato.get(ib).getBoletimInformativo().getId() == biV.getId()){
						dtBiRelato = lstRelato.get(ib).getDtCriacao();
					}				
				}
				
			}
			if (dtBiRelato!=null){
				criteria.add(Restrictions.le("dtCriacao", dtBiRelato));
			}
			
			
		}
		
		criteria.addOrder(Order.asc("id"));
		criteria.addOrder(Order.asc("id"));

		int result = criteria.list().size();

		if (result > 0) {
			this.setListBiRelato(criteria.list());
		} else {
			this.setListBiRelato(null);
		}

	}
	
	
    public void btnBack() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../bi/bilst.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Lista de Eventos " 
                            + " não pode ser carregada. Informe ao suporte técnico.", null));
        }
    }

    
	public BoletimInformativo getBiV() {
		return biV;
	}

	public void setBiV(BoletimInformativo biV) {
		this.biV = biV;
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

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public String getTipoBoletimEmitido() {
		return tipoBoletimEmitido;
	}

	public void setTipoBoletimEmitido(String tipoBoletimEmitido) {
		this.tipoBoletimEmitido = tipoBoletimEmitido;
	}

	public String getDiaDaSemana() {
		return diaDaSemana;
	}

	public void setDiaDaSemana(String diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public String getTipoSubtracao() {
		return tipoSubtracao;
	}

	public void setTipoSubtracao(String tipoSubtracao) {
		this.tipoSubtracao = tipoSubtracao;
	}

	public String getTipoAbordagem() {
		return tipoAbordagem;
	}

	public void setTipoAbordagem(String tipoAbordagem) {
		this.tipoAbordagem = tipoAbordagem;
	}

	public String getSituacaoMotorista() {
		return situacaoMotorista;
	}

	public void setSituacaoMotorista(String situacaoMotorista) {
		this.situacaoMotorista = situacaoMotorista;
	}

	public String getAvarias() {
		return avarias;
	}

	public void setAvarias(String avarias) {
		this.avarias = avarias;
	}

	public String getTipoEventoSuspeitaRoubo() {
		return tipoEventoSuspeitaRoubo;
	}

	public void setTipoEventoSuspeitaRoubo(String tipoEventoSuspeitaRoubo) {
		this.tipoEventoSuspeitaRoubo = tipoEventoSuspeitaRoubo;
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


	public boolean isSubPnDadosCarreta() {
		return subPnDadosCarreta;
	}


	public void setSubPnDadosCarreta(boolean subPnDadosCarreta) {
		this.subPnDadosCarreta = subPnDadosCarreta;
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
