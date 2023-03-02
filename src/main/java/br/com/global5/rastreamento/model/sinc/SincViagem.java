package br.com.global5.rastreamento.model.sinc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="rastreamento.sinc_viagem")
public class SincViagem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="viagoid")
	private Integer id;
	
	@ManyToOne(
				fetch = FetchType.LAZY,
				targetEntity = SincCliente.class
			)
	@JoinColumn(name="viag_clioid")
	private SincCliente cliente1;

	@Column(name="viag_vl_total")
	private BigDecimal vlrTotal;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincVeiculo.class
		)
	@JoinColumn(name="viag_veioid")
	private SincVeiculo veiculo;

	@Column(name="viag_veioid_vinculo")
	private Integer veicIdVinculo;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCarreta.class
		)
	@JoinColumn(name="viag_caroid1")
	private SincCarreta carreta1;
	
	@Column(name="viag_caroid1_vinculo")
	private Integer carretaVinculo1;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCarreta.class
		)
	@JoinColumn(name="viag_caroid2")
	private SincCarreta carreta2;
	
	@Column(name="viag_caroid2_vinculo")
	private Integer carretaVinculo2;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCarreta.class
		)
	@JoinColumn(name="viag_caroid3")
	private SincCarreta carreta3;	
	
	@Column(name="viag_caroid3_vinculo")
	private Integer carretaVinculo3;
	
	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincMotorista.class
		)
	@JoinColumn(name="viag_motoid1")
	private SincMotorista motorista1;
	
	@Column(name="viag_motoid1_vinculo")
	private Integer motoristaVinculo1;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincMotorista.class
		)
	@JoinColumn(name="viag_motoid2")
	private SincMotorista motorista2;

	@Column(name="viag_motoid2_vinculo")
	private Integer motoristaVinculo2;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincUf.class
		)
	@JoinColumn(name="viag_origem_ufoid")
	private SincUf origemUf;

	@Column(name="viag_origem_uf")
	private String descOrigemUf;
	
	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCidade.class
		)
	@JoinColumn(name="viag_origem_cidoid")
	private SincCidade origemCidade;

	@Column(name="viag_origem_cidade")
	private String nomeOrigemCidade;
	
	@Column(name="viag_origem_local")
	private String origemLocal;
	
	@Column(name="viag_origem_latitude")
	private BigDecimal origemLatitude;

	@Column(name="viag_origem_longitude")
	private BigDecimal origemLongitude;
	
	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincUf.class
		)
	@JoinColumn(name="viag_destino_ufoid")
	private SincUf destinoUf;

	@Column(name="viag_destino_uf")
	private String descDestinoUf;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCidade.class
		)
	@JoinColumn(name="viag_destino_cidoid")
	private SincCidade destinoCidade;

	@Column(name="viag_destino_cidade")
	private String nomeDestinoCidade;

	@Column(name="viag_destino_local")
	private String destinoLocal;
	
	@Column(name="viag_destino_latitude")
	private BigDecimal destinoLatitude;

	@Column(name="viag_destino_longitude")
	private BigDecimal destinoLongitude;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincPgr.class
		)
	@JoinColumn(name="viag_pgroid")
	private SincPgr pgr;
	
	@Column(name="viag_rota")
	private String rota;

	@Column(name="viag_observacao")
	private String observacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="viag_inicio_prev")
	private Date inicioPrev;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="viag_inicio_real")
	private Date inicioReal;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="viag_fim_prev")
	private Date fimPrev;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="viag_fim_real")
	private Date fimReal;

	@Column(name="viag_situacao")
	private Integer situacao;

	@Column(name="viag_produto")
	private Integer produto;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCliente.class
		)
	@JoinColumn(name="viag_clioid_transportadora")
	private SincCliente cliente;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="viag_dt_criacao")
	private Date dtCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="viag_dt_alteracao")
	private Date dtAlteracao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="viag_dt_cancelamento")
	private Date dtCancelamento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="viag_dt_iniciou")
	private Date dtIniciou;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="viag_dt_finalizou")
	private Date dtFinalizou;

	@Column(name="viag_tipo_viagem")
	private Integer tipoViagem;

	@Column(name="viag_peso")
	private BigDecimal peso;

	@Column(name="viag_manifesto")
	private String manifesto;

	public SincViagem() {}
	
	public SincViagem(Integer id) {this.id = id;}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SincCliente getCliente1() {
		return cliente1;
	}

	public void setCliente1(SincCliente cliente1) {
		this.cliente1 = cliente1;
	}

	public BigDecimal getVlrTotal() {
		return vlrTotal;
	}

	public void setVlrTotal(BigDecimal vlrTotal) {
		this.vlrTotal = vlrTotal;
	}

	public SincVeiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(SincVeiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Integer getVeicIdVinculo() {
		return veicIdVinculo;
	}

	public void setVeicIdVinculo(Integer veicIdVinculo) {
		this.veicIdVinculo = veicIdVinculo;
	}

	public SincCarreta getCarreta1() {
		return carreta1;
	}

	public void setCarreta1(SincCarreta carreta1) {
		this.carreta1 = carreta1;
	}

	public Integer getCarretaVinculo1() {
		return carretaVinculo1;
	}

	public void setCarretaVinculo1(Integer carretaVinculo1) {
		this.carretaVinculo1 = carretaVinculo1;
	}

	public SincCarreta getCarreta2() {
		return carreta2;
	}

	public void setCarreta2(SincCarreta carreta2) {
		this.carreta2 = carreta2;
	}

	public Integer getCarretaVinculo2() {
		return carretaVinculo2;
	}

	public void setCarretaVinculo2(Integer carretaVinculo2) {
		this.carretaVinculo2 = carretaVinculo2;
	}

	public SincCarreta getCarreta3() {
		return carreta3;
	}

	public void setCarreta3(SincCarreta carreta3) {
		this.carreta3 = carreta3;
	}

	public Integer getCarretaVinculo3() {
		return carretaVinculo3;
	}

	public void setCarretaVinculo3(Integer carretaVinculo3) {
		this.carretaVinculo3 = carretaVinculo3;
	}

	public SincMotorista getMotorista1() {
		return motorista1;
	}

	public void setMotorista1(SincMotorista motorista1) {
		this.motorista1 = motorista1;
	}

	public Integer getMotoristaVinculo1() {
		return motoristaVinculo1;
	}

	public void setMotoristaVinculo1(Integer motoristaVinculo1) {
		this.motoristaVinculo1 = motoristaVinculo1;
	}

	public SincMotorista getMotorista2() {
		return motorista2;
	}

	public void setMotorista2(SincMotorista motorista2) {
		this.motorista2 = motorista2;
	}

	public Integer getMotoristaVinculo2() {
		return motoristaVinculo2;
	}

	public void setMotoristaVinculo2(Integer motoristaVinculo2) {
		this.motoristaVinculo2 = motoristaVinculo2;
	}

	public SincUf getOrigemUf() {
		return origemUf;
	}

	public void setOrigemUf(SincUf origemUf) {
		this.origemUf = origemUf;
	}

	public String getDescOrigemUf() {
		return descOrigemUf;
	}

	public void setDescOrigemUf(String descOrigemUf) {
		this.descOrigemUf = descOrigemUf;
	}

	public SincCidade getOrigemCidade() {
		return origemCidade;
	}

	public void setOrigemCidade(SincCidade origemCidade) {
		this.origemCidade = origemCidade;
	}

	public String getNomeOrigemCidade() {
		return nomeOrigemCidade;
	}

	public void setNomeOrigemCidade(String nomeOrigemCidade) {
		this.nomeOrigemCidade = nomeOrigemCidade;
	}

	public String getOrigemLocal() {
		return origemLocal;
	}

	public void setOrigemLocal(String origemLocal) {
		this.origemLocal = origemLocal;
	}

	public BigDecimal getOrigemLatitude() {
		return origemLatitude;
	}

	public void setOrigemLatitude(BigDecimal origemLatitude) {
		this.origemLatitude = origemLatitude;
	}

	public BigDecimal getOrigemLongitude() {
		return origemLongitude;
	}

	public void setOrigemLongitude(BigDecimal origemLongitude) {
		this.origemLongitude = origemLongitude;
	}

	public SincUf getDestinoUf() {
		return destinoUf;
	}

	public void setDestinoUf(SincUf destinoUf) {
		this.destinoUf = destinoUf;
	}

	public String getDescDestinoUf() {
		return descDestinoUf;
	}

	public void setDescDestinoUf(String descDestinoUf) {
		this.descDestinoUf = descDestinoUf;
	}

	public SincCidade getDestinoCidade() {
		return destinoCidade;
	}

	public void setDestinoCidade(SincCidade destinoCidade) {
		this.destinoCidade = destinoCidade;
	}

	public String getNomeDestinoCidade() {
		return nomeDestinoCidade;
	}

	public void setNomeDestinoCidade(String nomeDestinoCidade) {
		this.nomeDestinoCidade = nomeDestinoCidade;
	}

	public String getDestinoLocal() {
		return destinoLocal;
	}

	public void setDestinoLocal(String destinoLocal) {
		this.destinoLocal = destinoLocal;
	}

	public BigDecimal getDestinoLatitude() {
		return destinoLatitude;
	}

	public void setDestinoLatitude(BigDecimal destinoLatitude) {
		this.destinoLatitude = destinoLatitude;
	}

	public BigDecimal getDestinoLongitude() {
		return destinoLongitude;
	}

	public void setDestinoLongitude(BigDecimal destinoLongitude) {
		this.destinoLongitude = destinoLongitude;
	}

	public SincPgr getPgr() {
		return pgr;
	}

	public void setPgr(SincPgr pgr) {
		this.pgr = pgr;
	}

	public String getRota() {
		return rota;
	}

	public void setRota(String rota) {
		this.rota = rota;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getInicioPrev() {
		return inicioPrev;
	}

	public void setInicioPrev(Date inicioPrev) {
		this.inicioPrev = inicioPrev;
	}

	public Date getInicioReal() {
		return inicioReal;
	}

	public void setInicioReal(Date inicioReal) {
		this.inicioReal = inicioReal;
	}

	public Date getFimPrev() {
		return fimPrev;
	}

	public void setFimPrev(Date fimPrev) {
		this.fimPrev = fimPrev;
	}

	public Date getFimReal() {
		return fimReal;
	}

	public void setFimReal(Date fimReal) {
		this.fimReal = fimReal;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public Integer getProduto() {
		return produto;
	}

	public void setProduto(Integer produto) {
		this.produto = produto;
	}

	public SincCliente getCliente() {
		return cliente;
	}

	public void setCliente(SincCliente cliente) {
		this.cliente = cliente;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public Date getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}

	public Date getDtCancelamento() {
		return dtCancelamento;
	}

	public void setDtCancelamento(Date dtCancelamento) {
		this.dtCancelamento = dtCancelamento;
	}

	public Date getDtIniciou() {
		return dtIniciou;
	}

	public void setDtIniciou(Date dtIniciou) {
		this.dtIniciou = dtIniciou;
	}

	public Date getDtFinalizou() {
		return dtFinalizou;
	}

	public void setDtFinalizou(Date dtFinalizou) {
		this.dtFinalizou = dtFinalizou;
	}

	public Integer getTipoViagem() {
		return tipoViagem;
	}

	public void setTipoViagem(Integer tipoViagem) {
		this.tipoViagem = tipoViagem;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public String getManifesto() {
		return manifesto;
	}

	public void setManifesto(String manifesto) {
		this.manifesto = manifesto;
	}
		
}
