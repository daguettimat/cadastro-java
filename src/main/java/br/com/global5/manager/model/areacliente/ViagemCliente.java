package br.com.global5.manager.model.areacliente;

import java.math.BigDecimal;


public class ViagemCliente{
	
	private Integer id;
	
	private Integer sm;
	
	private String dt_inicio;

	private String dt_termino;
	
	private String cavalo;
	
	private String cid_origem;
	
	private String cid_destino;
	
	private String tecnologia;
	
	private String produto;
	
	private BigDecimal valor;
		
	public ViagemCliente(){}

	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSm() {
		return sm;
	}

	public void setSm(Integer sm) {
		this.sm = sm;
	}
	
	public String getDt_inicio() {
		return dt_inicio;
	}

	public void setDt_inicio(String dt_inicio) {
		this.dt_inicio = dt_inicio;
	}

	public String getDt_termino() {
		return dt_termino;
	}

	public void setDt_termino(String dt_termino) {
		this.dt_termino = dt_termino;
	}

	public String getCavalo() {
		return cavalo;
	}

	public void setCavalo(String cavalo) {
		this.cavalo = cavalo;
	}

	public String getCid_origem() {
		return cid_origem;
	}

	public void setCid_origem(String cid_origem) {
		this.cid_origem = cid_origem;
	}

	public String getCid_destino() {
		return cid_destino;
	}

	public void setCid_destino(String cid_destino) {
		this.cid_destino = cid_destino;
	}

	public String getTecnologia() {
		return tecnologia;
	}

	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
