package br.com.global5.rastreamento.model.bi;

import javax.persistence.*;

import org.apache.commons.lang.SerializationUtils;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.rastreamento.model.enums.TipoPerda;
import br.com.global5.rastreamento.model.enums.TipoDano;
import br.com.global5.rastreamento.model.enums.TipoSuspeita;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name="boletim_informativo")
public class BoletimInformativo implements BaseEntity{
	@Transient	
	public static final char EVENTO_TIPO_SUSPEITA 	= 'S';
	
	@Transient
	public static final char EVENTO_TIPO_PERDA		= 'P';
	
	@Transient	
	public static final char EVENTO_TIPO_DANO		= 'D';

	@Id 
	@SequenceGenerator(name = "boletim_informativo_bioid_seq", sequenceName = "boletim_informativo_bioid_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boletim_informativo_bioid_seq")
	@Column(name="bioid")
	private Integer id;

	@ManyToOne(	fetch = FetchType.LAZY,
				targetEntity = Evento.class,
				cascade={CascadeType.DETACH, CascadeType.MERGE})
	@JoinColumn(name="bi_eveoid")
	private Evento evento;

	@Column(name="bi_tipo", nullable = false, precision = 1)
	private Character tipo;
	
	@Column(name="bi_nr_global")
	private Integer nrGlobal;

	@Column(name="bi_nr_sequencia")
	private Integer nrSequencia;	

	@Column(name="bi_rotulo")
	private String rotulo;
	
	@ManyToOne(	
			fetch = FetchType.EAGER,
			targetEntity = TipoSuspeita.class
			)
	@JoinColumn(name="bi_susp_tp_enumoid")
	private TipoSuspeita tipoSuspeita;
	
	@Column(name="bi_susp_detalhe_outros")
	private String suspDetalheOutros;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bi_com_ini_datahora")
	private Date comIniDataHora;

	@Column(name="bi_com_ini_rodovia")
	private String comIniRodovia;
	
	@Column(name="bi_com_ini_cidade")
	private String comIniCidade;
	
	@Column(name="bi_com_ini_uf")
	private String comIniUf;
	
	@Column(name="bi_com_ini_km")
	private String comIniKm;

	@Column(name="bi_com_ini_rua")
	private String comIniRua;

	@Column(name="bi_com_ini_numero")
	private Integer comIniNumero;

	@Column(name="bi_com_ini_latitude")
	private BigDecimal comIniLatitude;

	@Column(name="bi_com_ini_longitude")
	private BigDecimal comIniLongitude;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bi_com_ini_dt_comunicado")
	private Date comDtComunicado;
	
	@Column(name="bi_com_ini_nome")
	private String comIniNome;

	@Column(name="bi_com_ini_fone")
	private String comIniFone;

	@Column(name="bi_com_ini_dia_semana")
	private Integer comIniDiaSemana;
	
	@Column(name="bi_com_ini_ponto_referencia")
	private String comIniPontoReferencia;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bi_susp_fim_datahora")
	private Date suspFimDataHora;
	
	@Column(name="bi_susp_fim_rodovia")
	private String suspFimRodovia;
	
	@Column(name="bi_susp_fim_cidade")
	private String suspFimCidade;
	
	@Column(name="bi_susp_fim_uf")
	private String suspFimUf;
	
	@Column(name="bi_susp_fim_km")
	private String suspFimKm;

	@Column(name="bi_susp_fim_rua")
	private String suspFimRua;
	
	@Column(name="bi_susp_fim_numero")
	private Integer suspFimNumero;

	@Column(name="bi_susp_fim_latitude")
	private BigDecimal suspFimLatitude;

	@Column(name="bi_susp_fim_longitude")
	private BigDecimal suspFimLongitude;
	
	@Column(name="bi_perda_tp_furto")
	private Boolean perdaTpFurto;
	
	@Column(name="bi_perda_tp_furto_qualificado")
	private Boolean perdaTpFurtoQualificado;
	
	@Column(name="bi_perda_tp_roubo")
	private Boolean perdaTpRoubo;

	@Column(name="bi_perda_tp_extorsao")
	private Boolean perdaTpExtorsao;

	@Column(name="bi_perda_tp_apropriacao_indebita")
	private Boolean perdaTpApropriacaoIndebita;

	@Column(name="bi_perda_tp_estelionato")
	private Boolean perdaTpEstelionato;

	@Column(name="bi_perda_tp_receptacao")
	private Boolean perdaTpReceptacao;

	@Column(name="bi_perda_tp_falsidade_ideologica")
	private Boolean perdaTpFalsidadeIdeologica;

	@ManyToOne(	
			fetch = FetchType.EAGER,
			targetEntity = TipoPerda.class			
			)
	@JoinColumn(name="bi_perda_tp_enumoid")
	private TipoPerda tipoPerda;	
	
	@Column(name="bi_perda_abordagem_movimento")
	private Boolean perdaAbordagemMovimento;

	@Column(name="bi_perda_tp_vei_carga")
	private Boolean perdaTpVeiCarga;

	@Column(name="bi_perda_tp_carga")
	private Boolean perdaTpCarga;

	@Column(name="bi_perda_tp_vei")
	private Boolean perdaTpVei;

	@Column(name="bi_perda_tp_pertences")
	private Boolean perdaTpPertences;

	@Column(name="bi_perda_tp_pneus")
	private Boolean perdaTpPneus;

	@Column(name="bi_perda_tp_rastreador")
	private Boolean perdaTpRastreador;

	@Column(name="bi_perda_tp_tacografo")
	private Boolean perdaTpTacografo;

	@Column(name="bi_perda_tp_combustivel")
	private Boolean perdaTpCombustivel;
	
	@Column(name="bi_dano_tp_choque")
	private Boolean danoTpChoque;

	@Column(name="bi_dano_tp_saida_pista")
	private Boolean danoTpSaidaPista;

	@Column(name="bi_dano_tp_adernamento")
	private Boolean danoTpAdernamento;
	
	@Column(name="bi_dano_tp_incendio")
	private Boolean danoTpIncendio;

	@Column(name="bi_dano_tp_amassamento")
	private Boolean danoTpAmassamento;

	@Column(name="bi_dano_tp_vazamento")
	private Boolean danoTpVazamento;

	@Column(name="bi_dano_tp_arranhadura")
	private Boolean danoTpArranhadura;

	@Column(name="bi_dano_tp_quebra")
	private Boolean danoTpQuebra;

	@Column(name="bi_dano_tp_quebra_mercadoria")
	private Boolean danoTpQuebraMercadoria;

	@Column(name="bi_dano_tp_colisao")
	private Boolean danoTpColisao;

	@Column(name="bi_dano_tp_abalroamento")
	private Boolean danoTpAbalroamento;

	@Column(name="bi_dano_tp_tombamento")
	private Boolean danoTpTombamento;

	@Column(name="bi_dano_tp_deslocamento_carga")
	private Boolean danoTpDeslocamentoCarga;	
	
	@Column(name="bi_dano_tp_contaminacao")
	private Boolean danoTpContaminacao;

	@Column(name="bi_dano_tp_derrame")
	private Boolean danoTpDerrame;

	@Column(name="bi_dano_tp_derrapagem")
	private Boolean danoTpDerrapagem;
	
	@Column(name="bi_dano_tp_engavetamento")
	private Boolean danoTpEngavetamento;

	@Column(name="bi_dano_tp_queda_nivel")
	private Boolean danoTpQuedaNivel;

	@Column(name="bi_dano_tp_molhadura")
	private Boolean danoTpMolhadura;
	
	@Column(name="bi_dano_tp_atolamento")
	private Boolean danoTpAtolamento;

	@Column(name="bi_dano_tp_atropelamento")
	private Boolean danoTpAtropelamento;

	@Column(name="bi_dano_tp_desatreamento")
	private Boolean danoTpDesatreamento;

	@Column(name="bi_dano_tp_ma_estiva")
	private Boolean danoTpMaEstiva;

	@Column(name="bi_dano_tp_amolgamento")
	private Boolean danoTpAmolgamento;

	@Column(name="bi_dano_tp_capotamento")
	private Boolean danoTpCapotamento;
	
	@ManyToOne(	
			fetch = FetchType.EAGER,
			targetEntity = TipoDano.class 		
			)
	@JoinColumn(name="bi_dano_tp_enumoid")
	private TipoDano tipoDano;	

	@Column(name="bi_mot_obito")
	private Boolean motObito;

	@Column(name="bi_mot_ferido_gravemente")
	private Boolean motFeridoGravemente;

	@Column(name="bi_mot_ferido_levemente")
	private Boolean motFeridoLevemente;

	@Column(name="bi_mot_ferido_passa_bem")
	private Boolean motFeridoPassaBem;

	@Column(name="bi_mot_hospitalizado")
	private Boolean motHospitalizado;

	@Column(name="bi_mot_localizado")
	private Boolean motLocalizado;

	@Column(name="bi_avaria_cavalo")
	private Boolean avariaCavalo;

	@Column(name="bi_avaria_carreta")
	private Boolean avariaCarreta;

	@Column(name="bi_avaria_carga")
	private Boolean avariaCarga;

	@Column(name="bi_avaria_saque_mercadoria")
	private Boolean avariaSaqueMercadoria;

	@Column(name="bi_pronta_empresa_nome")
	private String prontaEmpresaNome;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bi_pronta_dthora")
	private Date prontaDthora;

	@Column(name="bi_pronta_atendente_nome")
	private String prontaAtendenteNome;
	
	@Column(name="bi_pronta_atendente_fone")
	private String prontaAtendenteFone;
	
	@Column(name="bi_pronta_agente_nome")
	private String prontaAgenteNome;

	@Column(name="bi_pronta_agente_fone")
	private String prontaAgenteFone; 

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bi_pronta_previsao_chegada")
	private Date prontaPrevisaoChegada;

	@Column(name="bi_pronta_nr_processo")
	private String prontaNrProcesso;
	
	@Column(name="bi_seg_nome")
	private String segNome;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bi_seg_dthora")
	private Date segDthora;
	
	@Column(name="bi_seg_nome_responsavel")
	private String segNomeResponsavel;

	@Column(name="bi_seg_fone")
	private String segFone;
	
	@Column(name="bi_seg_nr_processo")
	private String segNrProcesso;

	@Column(name="bi_reg_empresa_nome")
	private String regEmpresaNome;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bi_reg_dthora")
	private Date regDthora;

	@Column(name="bi_reg_atendente_nome")
	private String regAtendenteNome;
	
	@Column(name="bi_reg_atendente_fone")
	private String regAtendenteFone;

	@Column(name="bi_reg_perito_nome")
	private String regPeritoNome;
	
	@Column(name="bi_reg_perito_fone")
	private String regPeritoFone;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bi_reg_previsao_chegada")
	private Date regPrevisaoChegada;
		
	@Column(name="bi_reg_nr_processo")
	private String regNrProcesso;	

	@Column(name="bi_relato")
	private String relato;
	
	@Column(name="bi_fim_relato")
	private String fimRelato;

	@Column(name="bi_fim_valor_prejuizo")
	private BigDecimal fimValorPrejuizo;

	@Column(name="bi_fim_percentual_estimado")
	private Integer fimPercentualEstimado;
	
	@ManyToOne(	
			fetch = FetchType.LAZY,
			targetEntity = Usuario.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}			
			)
	@JoinColumn(name="bi_usuoid_emissao")
	private Usuario usuEmissao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bi_dt_emissao")
	private Date dtEmissao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bi_dt_reenvio")
	private Date dtReenvio;	
	
	@ManyToOne(	
			fetch = FetchType.LAZY,
			targetEntity = Usuario.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}			
			)
	@JoinColumn(name="bi_emails_usuoid_alteracao")
	private Usuario usuEmailsAlteracao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bi_emails_dt_alteracao")
	private Date emailsDtAlteracao;

	
	public Object clone() throws CloneNotSupportedException {
		
		Object dest = null;
		
		try {
			dest = SerializationUtils.clone(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return dest;
	}
	
	public BoletimInformativo(){}
	
	public BoletimInformativo(Integer id){ this.id = id;}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Character getTipo() {
		return tipo;
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}

	public Integer getNrGlobal() {
		return nrGlobal;
	}

	public void setNrGlobal(Integer nrGlobal) {
		this.nrGlobal = nrGlobal;
	}

	public Integer getNrSequencia() {
		return nrSequencia;
	}

	public void setNrSequencia(Integer nrSequencia) {
		this.nrSequencia = nrSequencia;
	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public TipoSuspeita getTipoSuspeita() {
		return tipoSuspeita;
	}

	public void setTipoSuspeita(TipoSuspeita tipoSuspeita) {
		this.tipoSuspeita = tipoSuspeita;
	}

	public String getSuspDetalheOutros() {
		return suspDetalheOutros;
	}

	public void setSuspDetalheOutros(String suspDetalheOutros) {
		this.suspDetalheOutros = suspDetalheOutros;
	}

	public Date getComIniDataHora() {
		return comIniDataHora;
	}

	public void setComIniDataHora(Date comIniDataHora) {
		this.comIniDataHora = comIniDataHora;
	}

	public String getComIniRodovia() {
		return comIniRodovia;
	}

	public void setComIniRodovia(String comIniRodovia) {
		this.comIniRodovia = comIniRodovia;
	}

	public String getComIniCidade() {
		return comIniCidade;
	}

	public void setComIniCidade(String comIniCidade) {
		this.comIniCidade = comIniCidade;
	}

	public String getComIniUf() {
		return comIniUf;
	}

	public void setComIniUf(String comIniUf) {
		this.comIniUf = comIniUf;
	}

	public String getComIniKm() {
		return comIniKm;
	}

	public void setComIniKm(String comIniKm) {
		this.comIniKm = comIniKm;
	}

	public String getComIniRua() {
		return comIniRua;
	}

	public void setComIniRua(String comIniRua) {
		this.comIniRua = comIniRua;
	}

	public Integer getComIniNumero() {
		return comIniNumero;
	}

	public void setComIniNumero(Integer comIniNumero) {
		this.comIniNumero = comIniNumero;
	}

	public BigDecimal getComIniLatitude() {
		return comIniLatitude;
	}

	public void setComIniLatitude(BigDecimal comIniLatitude) {
		this.comIniLatitude = comIniLatitude;
	}

	public BigDecimal getComIniLongitude() {
		return comIniLongitude;
	}

	public void setComIniLongitude(BigDecimal comIniLongitude) {
		this.comIniLongitude = comIniLongitude;
	}

	public Date getComDtComunicado() {
		return comDtComunicado;
	}

	public void setComDtComunicado(Date comDtComunicado) {
		this.comDtComunicado = comDtComunicado;
	}

	public String getComIniNome() {
		return comIniNome;
	}

	public void setComIniNome(String comIniNome) {
		this.comIniNome = comIniNome;
	}

	public String getComIniFone() {
		return comIniFone;
	}

	public void setComIniFone(String comIniFone) {
		this.comIniFone = comIniFone;
	}

	public Integer getComIniDiaSemana() {
		return comIniDiaSemana;
	}

	public void setComIniDiaSemana(Integer comIniDiaSemana) {
		this.comIniDiaSemana = comIniDiaSemana;
	}

	public String getComIniPontoReferencia() {
		return comIniPontoReferencia;
	}

	public void setComIniPontoReferencia(String comIniPontoReferencia) {
		this.comIniPontoReferencia = comIniPontoReferencia;
	}

	public Date getSuspFimDataHora() {
		return suspFimDataHora;
	}

	public void setSuspFimDataHora(Date suspFimDataHora) {
		this.suspFimDataHora = suspFimDataHora;
	}

	public String getSuspFimRodovia() {
		return suspFimRodovia;
	}

	public void setSuspFimRodovia(String suspFimRodovia) {
		this.suspFimRodovia = suspFimRodovia;
	}

	public String getSuspFimCidade() {
		return suspFimCidade;
	}

	public void setSuspFimCidade(String suspFimCidade) {
		this.suspFimCidade = suspFimCidade;
	}

	public String getSuspFimUf() {
		return suspFimUf;
	}

	public void setSuspFimUf(String suspFimUf) {
		this.suspFimUf = suspFimUf;
	}

	public String getSuspFimKm() {
		return suspFimKm;
	}

	public void setSuspFimKm(String suspFimKm) {
		this.suspFimKm = suspFimKm;
	}

	public String getSuspFimRua() {
		return suspFimRua;
	}

	public void setSuspFimRua(String suspFimRua) {
		this.suspFimRua = suspFimRua;
	}

	public Integer getSuspFimNumero() {
		return suspFimNumero;
	}

	public void setSuspFimNumero(Integer suspFimNumero) {
		this.suspFimNumero = suspFimNumero;
	}

	public BigDecimal getSuspFimLatitude() {
		return suspFimLatitude;
	}

	public void setSuspFimLatitude(BigDecimal suspFimLatitude) {
		this.suspFimLatitude = suspFimLatitude;
	}

	public BigDecimal getSuspFimLongitude() {
		return suspFimLongitude;
	}

	public void setSuspFimLongitude(BigDecimal suspFimLongitude) {
		this.suspFimLongitude = suspFimLongitude;
	}

	public Boolean getPerdaTpFurto() {
		return perdaTpFurto;
	}

	public void setPerdaTpFurto(Boolean perdaTpFurto) {
		this.perdaTpFurto = perdaTpFurto;
	}

	public Boolean getPerdaTpFurtoQualificado() {
		return perdaTpFurtoQualificado;
	}

	public void setPerdaTpFurtoQualificado(Boolean perdaTpFurtoQualificado) {
		this.perdaTpFurtoQualificado = perdaTpFurtoQualificado;
	}

	public Boolean getPerdaTpRoubo() {
		return perdaTpRoubo;
	}

	public void setPerdaTpRoubo(Boolean perdaTpRoubo) {
		this.perdaTpRoubo = perdaTpRoubo;
	}

	public Boolean getPerdaTpExtorsao() {
		return perdaTpExtorsao;
	}

	public void setPerdaTpExtorsao(Boolean perdaTpExtorsao) {
		this.perdaTpExtorsao = perdaTpExtorsao;
	}

	public Boolean getPerdaTpApropriacaoIndebita() {
		return perdaTpApropriacaoIndebita;
	}

	public void setPerdaTpApropriacaoIndebita(Boolean perdaTpApropriacaoIndebita) {
		this.perdaTpApropriacaoIndebita = perdaTpApropriacaoIndebita;
	}

	public Boolean getPerdaTpEstelionato() {
		return perdaTpEstelionato;
	}

	public void setPerdaTpEstelionato(Boolean perdaTpEstelionato) {
		this.perdaTpEstelionato = perdaTpEstelionato;
	}

	public Boolean getPerdaTpReceptacao() {
		return perdaTpReceptacao;
	}

	public void setPerdaTpReceptacao(Boolean perdaTpReceptacao) {
		this.perdaTpReceptacao = perdaTpReceptacao;
	}

	public Boolean getPerdaTpFalsidadeIdeologica() {
		return perdaTpFalsidadeIdeologica;
	}

	public void setPerdaTpFalsidadeIdeologica(Boolean perdaTpFalsidadeIdeologica) {
		this.perdaTpFalsidadeIdeologica = perdaTpFalsidadeIdeologica;
	}

	public TipoPerda getTipoPerda() {
		return tipoPerda;
	}

	public void setTipoPerda(TipoPerda tipoPerda) {
		this.tipoPerda = tipoPerda;
	}

	public Boolean getPerdaAbordagemMovimento() {
		return perdaAbordagemMovimento;
	}

	public void setPerdaAbordagemMovimento(Boolean perdaAbordagemMovimento) {
		this.perdaAbordagemMovimento = perdaAbordagemMovimento;
	}

	public Boolean getPerdaTpVeiCarga() {
		return perdaTpVeiCarga;
	}

	public void setPerdaTpVeiCarga(Boolean perdaTpVeiCarga) {
		this.perdaTpVeiCarga = perdaTpVeiCarga;
	}

	public Boolean getPerdaTpCarga() {
		return perdaTpCarga;
	}

	public void setPerdaTpCarga(Boolean perdaTpCarga) {
		this.perdaTpCarga = perdaTpCarga;
	}

	public Boolean getPerdaTpVei() {
		return perdaTpVei;
	}

	public void setPerdaTpVei(Boolean perdaTpVei) {
		this.perdaTpVei = perdaTpVei;
	}

	public Boolean getPerdaTpPertences() {
		return perdaTpPertences;
	}

	public void setPerdaTpPertences(Boolean perdaTpPertences) {
		this.perdaTpPertences = perdaTpPertences;
	}

	public Boolean getPerdaTpPneus() {
		return perdaTpPneus;
	}

	public void setPerdaTpPneus(Boolean perdaTpPneus) {
		this.perdaTpPneus = perdaTpPneus;
	}

	public Boolean getPerdaTpRastreador() {
		return perdaTpRastreador;
	}

	public void setPerdaTpRastreador(Boolean perdaTpRastreador) {
		this.perdaTpRastreador = perdaTpRastreador;
	}

	public Boolean getPerdaTpTacografo() {
		return perdaTpTacografo;
	}

	public void setPerdaTpTacografo(Boolean perdaTpTacografo) {
		this.perdaTpTacografo = perdaTpTacografo;
	}

	public Boolean getPerdaTpCombustivel() {
		return perdaTpCombustivel;
	}

	public void setPerdaTpCombustivel(Boolean perdaTpCombustivel) {
		this.perdaTpCombustivel = perdaTpCombustivel;
	}

	public Boolean getDanoTpChoque() {
		return danoTpChoque;
	}

	public void setDanoTpChoque(Boolean danoTpChoque) {
		this.danoTpChoque = danoTpChoque;
	}

	public Boolean getDanoTpSaidaPista() {
		return danoTpSaidaPista;
	}

	public void setDanoTpSaidaPista(Boolean danoTpSaidaPista) {
		this.danoTpSaidaPista = danoTpSaidaPista;
	}

	public Boolean getDanoTpAdernamento() {
		return danoTpAdernamento;
	}

	public void setDanoTpAdernamento(Boolean danoTpAdernamento) {
		this.danoTpAdernamento = danoTpAdernamento;
	}

	public Boolean getDanoTpIncendio() {
		return danoTpIncendio;
	}

	public void setDanoTpIncendio(Boolean danoTpIncendio) {
		this.danoTpIncendio = danoTpIncendio;
	}

	public Boolean getDanoTpAmassamento() {
		return danoTpAmassamento;
	}

	public void setDanoTpAmassamento(Boolean danoTpAmassamento) {
		this.danoTpAmassamento = danoTpAmassamento;
	}

	public Boolean getDanoTpVazamento() {
		return danoTpVazamento;
	}

	public void setDanoTpVazamento(Boolean danoTpVazamento) {
		this.danoTpVazamento = danoTpVazamento;
	}

	public Boolean getDanoTpArranhadura() {
		return danoTpArranhadura;
	}

	public void setDanoTpArranhadura(Boolean danoTpArranhadura) {
		this.danoTpArranhadura = danoTpArranhadura;
	}

	public Boolean getDanoTpQuebra() {
		return danoTpQuebra;
	}

	public void setDanoTpQuebra(Boolean danoTpQuebra) {
		this.danoTpQuebra = danoTpQuebra;
	}

	public Boolean getDanoTpQuebraMercadoria() {
		return danoTpQuebraMercadoria;
	}

	public void setDanoTpQuebraMercadoria(Boolean danoTpQuebraMercadoria) {
		this.danoTpQuebraMercadoria = danoTpQuebraMercadoria;
	}

	public Boolean getDanoTpColisao() {
		return danoTpColisao;
	}

	public void setDanoTpColisao(Boolean danoTpColisao) {
		this.danoTpColisao = danoTpColisao;
	}

	public Boolean getDanoTpAbalroamento() {
		return danoTpAbalroamento;
	}

	public void setDanoTpAbalroamento(Boolean danoTpAbalroamento) {
		this.danoTpAbalroamento = danoTpAbalroamento;
	}

	public Boolean getDanoTpTombamento() {
		return danoTpTombamento;
	}

	public void setDanoTpTombamento(Boolean danoTpTombamento) {
		this.danoTpTombamento = danoTpTombamento;
	}

	public Boolean getDanoTpDeslocamentoCarga() {
		return danoTpDeslocamentoCarga;
	}

	public void setDanoTpDeslocamentoCarga(Boolean danoTpDeslocamentoCarga) {
		this.danoTpDeslocamentoCarga = danoTpDeslocamentoCarga;
	}

	public Boolean getDanoTpContaminacao() {
		return danoTpContaminacao;
	}

	public void setDanoTpContaminacao(Boolean danoTpContaminacao) {
		this.danoTpContaminacao = danoTpContaminacao;
	}

	public Boolean getDanoTpDerrame() {
		return danoTpDerrame;
	}

	public void setDanoTpDerrame(Boolean danoTpDerrame) {
		this.danoTpDerrame = danoTpDerrame;
	}

	public Boolean getDanoTpDerrapagem() {
		return danoTpDerrapagem;
	}

	public void setDanoTpDerrapagem(Boolean danoTpDerrapagem) {
		this.danoTpDerrapagem = danoTpDerrapagem;
	}

	public Boolean getDanoTpEngavetamento() {
		return danoTpEngavetamento;
	}

	public void setDanoTpEngavetamento(Boolean danoTpEngavetamento) {
		this.danoTpEngavetamento = danoTpEngavetamento;
	}

	public Boolean getDanoTpQuedaNivel() {
		return danoTpQuedaNivel;
	}

	public void setDanoTpQuedaNivel(Boolean danoTpQuedaNivel) {
		this.danoTpQuedaNivel = danoTpQuedaNivel;
	}

	public Boolean getDanoTpMolhadura() {
		return danoTpMolhadura;
	}

	public void setDanoTpMolhadura(Boolean danoTpMolhadura) {
		this.danoTpMolhadura = danoTpMolhadura;
	}

	public Boolean getDanoTpAtolamento() {
		return danoTpAtolamento;
	}

	public void setDanoTpAtolamento(Boolean danoTpAtolamento) {
		this.danoTpAtolamento = danoTpAtolamento;
	}

	public Boolean getDanoTpAtropelamento() {
		return danoTpAtropelamento;
	}

	public void setDanoTpAtropelamento(Boolean danoTpAtropelamento) {
		this.danoTpAtropelamento = danoTpAtropelamento;
	}

	public Boolean getDanoTpDesatreamento() {
		return danoTpDesatreamento;
	}

	public void setDanoTpDesatreamento(Boolean danoTpDesatreamento) {
		this.danoTpDesatreamento = danoTpDesatreamento;
	}

	public Boolean getDanoTpMaEstiva() {
		return danoTpMaEstiva;
	}

	public void setDanoTpMaEstiva(Boolean danoTpMaEstiva) {
		this.danoTpMaEstiva = danoTpMaEstiva;
	}

	public Boolean getDanoTpAmolgamento() {
		return danoTpAmolgamento;
	}

	public void setDanoTpAmolgamento(Boolean danoTpAmolgamento) {
		this.danoTpAmolgamento = danoTpAmolgamento;
	}

	public Boolean getDanoTpCapotamento() {
		return danoTpCapotamento;
	}

	public void setDanoTpCapotamento(Boolean danoTpCapotamento) {
		this.danoTpCapotamento = danoTpCapotamento;
	}

	public TipoDano getTipoDano() {
		return tipoDano;
	}

	public void setTipoDano(TipoDano tipoDano) {
		this.tipoDano = tipoDano;
	}

	public Boolean getMotObito() {
		return motObito;
	}

	public void setMotObito(Boolean motObito) {
		this.motObito = motObito;
	}

	public Boolean getMotFeridoGravemente() {
		return motFeridoGravemente;
	}

	public void setMotFeridoGravemente(Boolean motFeridoGravemente) {
		this.motFeridoGravemente = motFeridoGravemente;
	}

	public Boolean getMotFeridoLevemente() {
		return motFeridoLevemente;
	}

	public void setMotFeridoLevemente(Boolean motFeridoLevemente) {
		this.motFeridoLevemente = motFeridoLevemente;
	}

	public Boolean getMotFeridoPassaBem() {
		return motFeridoPassaBem;
	}

	public void setMotFeridoPassaBem(Boolean motFeridoPassaBem) {
		this.motFeridoPassaBem = motFeridoPassaBem;
	}

	public Boolean getMotHospitalizado() {
		return motHospitalizado;
	}

	public void setMotHospitalizado(Boolean motHospitalizado) {
		this.motHospitalizado = motHospitalizado;
	}

	public Boolean getMotLocalizado() {
		return motLocalizado;
	}

	public void setMotLocalizado(Boolean motLocalizado) {
		this.motLocalizado = motLocalizado;
	}

	public Boolean getAvariaCavalo() {
		return avariaCavalo;
	}

	public void setAvariaCavalo(Boolean avariaCavalo) {
		this.avariaCavalo = avariaCavalo;
	}

	public Boolean getAvariaCarreta() {
		return avariaCarreta;
	}

	public void setAvariaCarreta(Boolean avariaCarreta) {
		this.avariaCarreta = avariaCarreta;
	}

	public Boolean getAvariaCarga() {
		return avariaCarga;
	}

	public void setAvariaCarga(Boolean avariaCarga) {
		this.avariaCarga = avariaCarga;
	}

	public Boolean getAvariaSaqueMercadoria() {
		return avariaSaqueMercadoria;
	}

	public void setAvariaSaqueMercadoria(Boolean avariaSaqueMercadoria) {
		this.avariaSaqueMercadoria = avariaSaqueMercadoria;
	}

	public String getProntaEmpresaNome() {
		return prontaEmpresaNome;
	}

	public void setProntaEmpresaNome(String prontaEmpresaNome) {
		this.prontaEmpresaNome = prontaEmpresaNome;
	}

	public Date getProntaDthora() {
		return prontaDthora;
	}

	public void setProntaDthora(Date prontaDthora) {
		this.prontaDthora = prontaDthora;
	}

	public String getProntaAtendenteNome() {
		return prontaAtendenteNome;
	}

	public void setProntaAtendenteNome(String prontaAtendenteNome) {
		this.prontaAtendenteNome = prontaAtendenteNome;
	}

	public String getProntaAtendenteFone() {
		return prontaAtendenteFone;
	}

	public void setProntaAtendenteFone(String prontaAtendenteFone) {
		this.prontaAtendenteFone = prontaAtendenteFone;
	}

	public String getProntaAgenteNome() {
		return prontaAgenteNome;
	}

	public void setProntaAgenteNome(String prontaAgenteNome) {
		this.prontaAgenteNome = prontaAgenteNome;
	}

	public String getProntaAgenteFone() {
		return prontaAgenteFone;
	}

	public void setProntaAgenteFone(String prontaAgenteFone) {
		this.prontaAgenteFone = prontaAgenteFone;
	}

	public Date getProntaPrevisaoChegada() {
		return prontaPrevisaoChegada;
	}

	public void setProntaPrevisaoChegada(Date prontaPrevisaoChegada) {
		this.prontaPrevisaoChegada = prontaPrevisaoChegada;
	}

	public String getProntaNrProcesso() {
		return prontaNrProcesso;
	}

	public void setProntaNrProcesso(String prontaNrProcesso) {
		this.prontaNrProcesso = prontaNrProcesso;
	}

	public String getSegNome() {
		return segNome;
	}

	public void setSegNome(String segNome) {
		this.segNome = segNome;
	}

	public Date getSegDthora() {
		return segDthora;
	}

	public void setSegDthora(Date segDthora) {
		this.segDthora = segDthora;
	}

	public String getSegNomeResponsavel() {
		return segNomeResponsavel;
	}

	public void setSegNomeResponsavel(String segNomeResponsavel) {
		this.segNomeResponsavel = segNomeResponsavel;
	}

	public String getSegFone() {
		return segFone;
	}

	public void setSegFone(String segFone) {
		this.segFone = segFone;
	}

	public String getSegNrProcesso() {
		return segNrProcesso;
	}

	public void setSegNrProcesso(String segNrProcesso) {
		this.segNrProcesso = segNrProcesso;
	}

	public String getRegEmpresaNome() {
		return regEmpresaNome;
	}

	public void setRegEmpresaNome(String regEmpresaNome) {
		this.regEmpresaNome = regEmpresaNome;
	}

	public Date getRegDthora() {
		return regDthora;
	}

	public void setRegDthora(Date regDthora) {
		this.regDthora = regDthora;
	}

	public String getRegAtendenteNome() {
		return regAtendenteNome;
	}

	public void setRegAtendenteNome(String regAtendenteNome) {
		this.regAtendenteNome = regAtendenteNome;
	}

	public String getRegAtendenteFone() {
		return regAtendenteFone;
	}

	public void setRegAtendenteFone(String regAtendenteFone) {
		this.regAtendenteFone = regAtendenteFone;
	}

	public String getRegPeritoNome() {
		return regPeritoNome;
	}

	public void setRegPeritoNome(String regPeritoNome) {
		this.regPeritoNome = regPeritoNome;
	}

	public String getRegPeritoFone() {
		return regPeritoFone;
	}

	public void setRegPeritoFone(String regPeritoFone) {
		this.regPeritoFone = regPeritoFone;
	}

	public Date getRegPrevisaoChegada() {
		return regPrevisaoChegada;
	}

	public void setRegPrevisaoChegada(Date regPrevisaoChegada) {
		this.regPrevisaoChegada = regPrevisaoChegada;
	}

	public String getRegNrProcesso() {
		return regNrProcesso;
	}

	public void setRegNrProcesso(String regNrProcesso) {
		this.regNrProcesso = regNrProcesso;
	}

	public String getRelato() {
		return relato;
	}

	public void setRelato(String relato) {
		this.relato = relato;
	}

	public String getFimRelato() {
		return fimRelato;
	}

	public void setFimRelato(String fimRelato) {
		this.fimRelato = fimRelato;
	}

	public BigDecimal getFimValorPrejuizo() {
		return fimValorPrejuizo;
	}

	public void setFimValorPrejuizo(BigDecimal fimValorPrejuizo) {
		this.fimValorPrejuizo = fimValorPrejuizo;
	}

	public Integer getFimPercentualEstimado() {
		return fimPercentualEstimado;
	}

	public void setFimPercentualEstimado(Integer fimPercentualEstimado) {
		this.fimPercentualEstimado = fimPercentualEstimado;
	}

	public Usuario getUsuEmissao() {
		return usuEmissao;
	}

	public void setUsuEmissao(Usuario usuEmissao) {
		this.usuEmissao = usuEmissao;
	}

	public Date getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public Date getDtReenvio() {
		return dtReenvio;
	}

	public void setDtReenvio(Date dtReenvio) {
		this.dtReenvio = dtReenvio;
	}

	public Usuario getUsuEmailsAlteracao() {
		return usuEmailsAlteracao;
	}

	public void setUsuEmailsAlteracao(Usuario usuEmailsAlteracao) {
		this.usuEmailsAlteracao = usuEmailsAlteracao;
	}

	public Date getEmailsDtAlteracao() {
		return emailsDtAlteracao;
	}

	public void setEmailsDtAlteracao(Date emailsDtAlteracao) {
		this.emailsDtAlteracao = emailsDtAlteracao;
	}

	
}
