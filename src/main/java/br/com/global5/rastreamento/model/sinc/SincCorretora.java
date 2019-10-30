package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_corretora")
public class SincCorretora {

	@Id
	@Column(name="corroid")
	private Integer id;

	@Column(name="corr_nome")
	private String nome;

	@Column(name="corr_fone")
	private String fone;

	@Column(name="corr_contato")
	private String contato;

	@Column(name="corr_cidade")
	private String cidade;
	
	@Column(name="corr_uf")
	private String uf;

	@Column(name="corr_endereco")
	private String endereco;

	@Column(name="corr_email")
	private String email;
	
	public SincCorretora() {}
	
	public SincCorretora(Integer id) {this.id  = id;}

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

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
