package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_uf")
public class SincUf {
	
	@Id
	@Column(name="ufoid")
	private Integer id;

	@Column(name="uf_sigla")
	private String sigla;
	
	@Column(name="uf_nome")
	private String nome;

	@Column(name="uf_pais")
	private String pais;

	public SincUf () {}
	public SincUf (Integer id) {this.id = id;}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	
	
	
}
