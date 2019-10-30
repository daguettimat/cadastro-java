package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_seguradora")
public class SincSeguradora {

	@Id
	@Column(name="segoid")
	private Integer id;

	@Column(name="seg_nome")
	private String nome;

	@Column(name="seg_fone")
	private String fone;

	@Column(name="seg_contato")
	private String contato;

	@Column(name="seg_cidade")
	private String cidade;

	@Column(name="seg_uf")
	private String uf;

	@Column(name="seg_observacao")
	private String observacao;

	@Column(name="seg_email")
	private String email;

	public SincSeguradora() {}
	
	public SincSeguradora(Integer id) {this.id = id;}

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

	public String getFone() {
		return fone;
	}

	public void setFone(String fone) {
		this.fone = fone;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
