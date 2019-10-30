package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_carreta")
public class SincCarreta {
	
	@Id
	@Column(name="caroid")
	private Integer id;
	
	@Column(name="car_placa")
	private String placa;

	@ManyToOne(
				fetch = FetchType.LAZY,
				targetEntity = SincCliente.class
			)
	@JoinColumn(name="car_clioid")
	private SincCliente cliente;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincUf.class
		)
	@JoinColumn(name="car_ufoid")
	private SincUf uf;
	
	@Column(name="car_uf")
	private String descUf;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCidade.class
		)
	@JoinColumn(name="car_cidoid")
	private SincCidade cidade;
	
	@Column(name="car_cidade")
	private String descCidade;
	
	@Column(name="car_chassi")
	private String chassi;
	
	@Column(name="car_renavam")
	private String renavam;
	
	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincMarcaCarreta.class
		)
	@JoinColumn(name="car_marcoid")
	private SincMarcaCarreta marcaCarreta;
	
	@Column(name="car_modelo")
	private String modelo;
	
	@Column(name="car_ano_modelo")
	private Integer anoModelo;
	
	@Column(name="car_ano_fabricacao")
	private Integer anoFabricacao;
	
	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCor.class
		)
	@JoinColumn(name="car_coroid")
	private SincCor cor;

	@Column(name="car_cor")
	private String descCor;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincTipoCarroceria.class
		)
	@JoinColumn(name="car_tpcaroid")
	private SincTipoCarroceria tipoCarroceria;

	@Column(name="car_ativo")
	private Integer ativo;
	
	@Column(name="car_id")
	private Integer idRastreador;

	@Column(name="car_id_tecnologia")
	private Integer idTecnologia;
	
	@Column(name="car_tipo_rastreador")
	private Integer tipoRastreador;

	@Column(name="car_dispositivo")
	private Integer dispositivo;

	@Column(name="car_localizador1")
	private Integer localizador1;

	@Column(name="car_localizador2")
	private Integer localizador2;
	
	@Column(name="car_vinculo")
	private Integer vinculo;

	@Column(name="car_antt")
	private String antt;
	
	@Column(name="car_tipo_proprietario")
	private Integer tipoProprietario;

	@Column(name="car_dados_proprietário")
	private String dadosProprietário;

	@Column(name="car_proprietario_f")
	private String proprietarioF;

	public SincCarreta() {}
	public SincCarreta(Integer id) {this.id = id;}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public SincCliente getCliente() {
		return cliente;
	}
	public void setCliente(SincCliente cliente) {
		this.cliente = cliente;
	}
	public SincUf getUf() {
		return uf;
	}
	public void setUf(SincUf uf) {
		this.uf = uf;
	}
	public String getDescUf() {
		return descUf;
	}
	public void setDescUf(String descUf) {
		this.descUf = descUf;
	}
	public SincCidade getCidade() {
		return cidade;
	}
	public void setCidade(SincCidade cidade) {
		this.cidade = cidade;
	}
	public String getDescCidade() {
		return descCidade;
	}
	public void setDescCidade(String descCidade) {
		this.descCidade = descCidade;
	}
	public String getChassi() {
		return chassi;
	}
	public void setChassi(String chassi) {
		this.chassi = chassi;
	}
	public String getRenavam() {
		return renavam;
	}
	public void setRenavam(String renavam) {
		this.renavam = renavam;
	}
	public SincMarcaCarreta getMarcaCarreta() {
		return marcaCarreta;
	}
	public void setMarcaCarreta(SincMarcaCarreta marcaCarreta) {
		this.marcaCarreta = marcaCarreta;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public Integer getAnoModelo() {
		return anoModelo;
	}
	public void setAnoModelo(Integer anoModelo) {
		this.anoModelo = anoModelo;
	}
	public Integer getAnoFabricacao() {
		return anoFabricacao;
	}
	public void setAnoFabricacao(Integer anoFabricacao) {
		this.anoFabricacao = anoFabricacao;
	}
	public SincCor getCor() {
		return cor;
	}
	public void setCor(SincCor cor) {
		this.cor = cor;
	}
	public String getDescCor() {
		return descCor;
	}
	public void setDescCor(String descCor) {
		this.descCor = descCor;
	}
	public SincTipoCarroceria getTipoCarroceria() {
		return tipoCarroceria;
	}
	public void setTipoCarroceria(SincTipoCarroceria tipoCarroceria) {
		this.tipoCarroceria = tipoCarroceria;
	}
	public Integer getAtivo() {
		return ativo;
	}
	public void setAtivo(Integer ativo) {
		this.ativo = ativo;
	}
	public Integer getIdRastreador() {
		return idRastreador;
	}
	public void setIdRastreador(Integer idRastreador) {
		this.idRastreador = idRastreador;
	}
	public Integer getIdTecnologia() {
		return idTecnologia;
	}
	public void setIdTecnologia(Integer idTecnologia) {
		this.idTecnologia = idTecnologia;
	}
	public Integer getTipoRastreador() {
		return tipoRastreador;
	}
	public void setTipoRastreador(Integer tipoRastreador) {
		this.tipoRastreador = tipoRastreador;
	}
	public Integer getDispositivo() {
		return dispositivo;
	}
	public void setDispositivo(Integer dispositivo) {
		this.dispositivo = dispositivo;
	}
	public Integer getLocalizador1() {
		return localizador1;
	}
	public void setLocalizador1(Integer localizador1) {
		this.localizador1 = localizador1;
	}
	public Integer getLocalizador2() {
		return localizador2;
	}
	public void setLocalizador2(Integer localizador2) {
		this.localizador2 = localizador2;
	}
	public Integer getVinculo() {
		return vinculo;
	}
	public void setVinculo(Integer vinculo) {
		this.vinculo = vinculo;
	}
	public String getAntt() {
		return antt;
	}
	public void setAntt(String antt) {
		this.antt = antt;
	}
	public Integer getTipoProprietario() {
		return tipoProprietario;
	}
	public void setTipoProprietario(Integer tipoProprietario) {
		this.tipoProprietario = tipoProprietario;
	}
	public String getDadosProprietário() {
		return dadosProprietário;
	}
	public void setDadosProprietário(String dadosProprietário) {
		this.dadosProprietário = dadosProprietário;
	}
	public String getProprietarioF() {
		return proprietarioF;
	}
	public void setProprietarioF(String proprietarioF) {
		this.proprietarioF = proprietarioF;
	}
	
}
