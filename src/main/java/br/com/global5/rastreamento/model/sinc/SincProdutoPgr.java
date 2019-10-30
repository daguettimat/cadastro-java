package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_produto_pgr")
public class SincProdutoPgr {

	@Id
	@Column(name="prodoid")
	private Integer id;
	
	@Column(name="prod_nome")
	private String nome;

	@ManyToOne(
				fetch = FetchType.LAZY,
				targetEntity = SincCliente.class
			)
	@JoinColumn(name="prod_clioid")
	private SincCliente cliente;

	public SincProdutoPgr() {}
	public SincProdutoPgr(Integer id) {this.id = id;}
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
	
	
}
