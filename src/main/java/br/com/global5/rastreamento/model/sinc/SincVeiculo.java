package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_veiculo")
public class SincVeiculo {
	
	@Id
	@Column(name="veioid")
	private Integer id;

	@Column(name="vei_placa")
	private String placa;

	@ManyToOne(
				fetch = FetchType.LAZY,
				targetEntity = SincCliente.class
			)
	@JoinColumn(name="vei_clioid")
	private SincCliente cliente;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincUf.class
		)
	@JoinColumn(name="vei_ufoid")
	private SincUf uf;
	
	@Column(name="vei_uf")
	private String descUf;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCidade.class
		)
	@JoinColumn(name="vei_cidoid")
	private SincCidade cidade;
	
	@Column(name="vei_cidade")
	private String nomeCidade;

	@Column(name="vei_chassi")
	private String chassi;

	@Column(name="vei_renavam")
	private String renavam;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincMarcaVeiculo.class
		)
	@JoinColumn(name="vei_marvoid")
	private SincMarcaVeiculo marcaVeiculo;

	@Column(name="vei_modelo")
	private String modelo;	

	@Column(name="vei_ano_modelo")
	private Integer anoModelo;
	
	@Column(name="vei_ano_fabricacao")
	private Integer anoFabricacao;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCor.class
		)
	@JoinColumn(name="vei_coroid")
	private SincCor cor;

	@Column(name="vei_cor")
	private String nomeCor;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincTipoVeiculo.class
		)
	@JoinColumn(name="vei_tpveioid")
	private SincTipoVeiculo tipoVeiculo;
	
	@Column(name="vei_ativo")
	private Integer ativo;

	@Column(name="vei_id")
	private Integer numId;

	@Column(name="vei_id_tecnologia")
	private Integer idTecnologia;

	@Column(name="vei_tipo_rastreador")
	private Integer tipoRastreador;
	
	@Column(name="vei_dispositivo")
	private Integer dispositivo;
	
	@Column(name="vei_localizador1")
	private Integer localizador1;

	@Column(name="vei_localizador2")
	private Integer localizador2;

	@Column(name="vei_vinculo")
	private Integer vinculo;

	@Column(name="vei_nr_frota")
	private String nrFrota;
	
	@Column(name="vei_antt")
	private String antt;

	@Column(name="vei_tipo_proprietario")
	private Integer tipoProprietario;

	@Column(name="vei_dados_proprietário")
	private String dadosProprietário;

	@Column(name="vei_proprietario_f")
	private String proprietarioF;

	@Column(name="vei_comunicacao_rastreador")
	private Integer comunicacaoRastreador;

	@Column(name="vei_conta")
	private Integer conta;

	public SincVeiculo() {}
	public SincVeiculo(Integer id) {this.id = id;}
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
	public String getNomeCidade() {
		return nomeCidade;
	}
	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
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
	public SincMarcaVeiculo getMarcaVeiculo() {
		return marcaVeiculo;
	}
	public void setMarcaVeiculo(SincMarcaVeiculo marcaVeiculo) {
		this.marcaVeiculo = marcaVeiculo;
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
	public String getNomeCor() {
		return nomeCor;
	}
	public void setNomeCor(String nomeCor) {
		this.nomeCor = nomeCor;
	}
	public SincTipoVeiculo getTipoVeiculo() {
		return tipoVeiculo;
	}
	public void setTipoVeiculo(SincTipoVeiculo tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}
	public Integer getAtivo() {
		return ativo;
	}
	public void setAtivo(Integer ativo) {
		this.ativo = ativo;
	}
	public Integer getNumId() {
		return numId;
	}
	public void setNumId(Integer numId) {
		this.numId = numId;
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
	public String getNrFrota() {
		return nrFrota;
	}
	public void setNrFrota(String nrFrota) {
		this.nrFrota = nrFrota;
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
	public Integer getComunicacaoRastreador() {
		return comunicacaoRastreador;
	}
	public void setComunicacaoRastreador(Integer comunicacaoRastreador) {
		this.comunicacaoRastreador = comunicacaoRastreador;
	}
	public Integer getConta() {
		return conta;
	}
	public void setConta(Integer conta) {
		this.conta = conta;
	}

}
