package br.com.global5.showcase.model;

import java.io.Serializable;

public class Gerenciadoras implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7445239470410816706L;
	private long id;
	private String nome;
	
	public Gerenciadoras() {
	}
	
	public Gerenciadoras(long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		return "Gerenciadoras [id=" + id + ", nome=" + nome + "]";
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
