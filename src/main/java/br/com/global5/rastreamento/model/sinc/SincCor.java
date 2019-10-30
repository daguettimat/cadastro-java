package br.com.global5.rastreamento.model.sinc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sinc_cidade")
public class SincCor {

	@Id
	@Column(name="coroid")
	private Integer id;

	@Column(name="cor_nome")
	private String nome;
	
	public SincCor(){}
	
	public SincCor(Integer id){}

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
