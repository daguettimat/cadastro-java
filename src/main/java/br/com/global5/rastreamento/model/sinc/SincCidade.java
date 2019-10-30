package br.com.global5.rastreamento.model.sinc;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name="sinc_cidade")
public class SincCidade {

	@Id
	@Column(name="cidoid")
	private Integer id;

	@Column(name="cid_nome")
	private String nome;

	@Column(name="cid_uf")
	private String uf;

	@Column(name="cid_pais")
	private String pais;

	@Column(name="cid_cep")
	private String cep;

	@Column(name="cid_latitude")
	private BigDecimal latitude;

	@Column(name="cid_longitude")
	private BigDecimal longitude;

	public SincCidade(){}
	
	public SincCidade(Integer id){ this.id = id;}

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

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
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
	
	
}
