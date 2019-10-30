package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_pgr")
public class SincPgr {

	@Id
	@Column(name="pgroid")
	private Integer id;
	
	@Column(name="pgr_nome")
	private String nome;
	
	@ManyToOne(
				fetch = FetchType.LAZY,
				targetEntity = SincCliente.class
			)
	@JoinColumn(name="pgr_clioid")
	private SincCliente cliente;
	
	@Column(name="pgr_tipo")
	private Integer tipo;

	@ManyToOne(
				fetch = FetchType.LAZY,
				targetEntity = SincSeguradora.class
			)
	@JoinColumn(name="pgr_segoid")
	private SincSeguradora seguradora;
	
	@ManyToOne(
				fetch = FetchType.LAZY,
				targetEntity = SincCorretora.class
			)
	@JoinColumn(name="pgr_corroid")
	private SincCorretora corretora;

	public SincPgr() {}
	public SincPgr(Integer id) {this.id = id;}
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
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public SincSeguradora getSeguradora() {
		return seguradora;
	}
	public void setSeguradora(SincSeguradora seguradora) {
		this.seguradora = seguradora;
	}
	public SincCorretora getCorretora() {
		return corretora;
	}
	public void setCorretora(SincCorretora corretora) {
		this.corretora = corretora;
	}
	
}
