package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_pais")
public class SincPais {

	@Id
	@Column(name="paisoid")
	private Integer id;

	@Column(name="pais_nome")
	private String nome;

	public SincPais() {}
	public SincPais(Integer id) {this.id = id;}
	
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
	
	
}
