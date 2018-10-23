package br.com.global5.showcase.model;

import java.io.Serializable;

public class Parentesco implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2030887253218272592L;
	private Integer id;
	private String  nome;
	
	public Parentesco() {
	}

	public Parentesco(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Parentesco [id=" + id + ", nome=" + nome + "]";
	}

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
