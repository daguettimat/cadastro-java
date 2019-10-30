package br.com.global5.rastreamento.model.sinc;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name="sinc_embarcador")
public class SincEmbarcador {
	
	@Id
	@Column(name="emboid")
	private Integer id;
	
	@Column(name="emb_nome")
	private String nome;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCliente.class
			)
	@JoinColumn(name="emb_clioid")
	private SincCliente cliente;
	
	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincPgr.class
			)
	@JoinColumn(name="emb_pgroid")
	private SincPgr pgr;
	
	@Column(name="emb_cnpj")
	private String cnpj;

	@Column(name="emb_latitude")
	private BigDecimal latitude;

	@Column(name="emb_longitude")
	private BigDecimal longitude;

	@Column(name="emb_ativo")
	private Integer ativo;

	@Column(name="emb_uf")
	private String uf;
	
	@Column(name="emb_cidade")
	private String cidade;

	@Column(name="emb_bairro")
	private String bairro;

	@Column(name="emb_tipo")
	private Integer tipo;

	public SincEmbarcador() {}
	
	public SincEmbarcador(Integer id) {this.id = id;}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public SincCliente getCliente() {
		return cliente;
	}

	public void setCliente(SincCliente cliente) {
		this.cliente = cliente;
	}

	public SincPgr getPgr() {
		return pgr;
	}

	public void setPgr(SincPgr pgr) {
		this.pgr = pgr;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public Integer getAtivo() {
		return ativo;
	}

	public void setAtivo(Integer ativo) {
		this.ativo = ativo;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	
}
