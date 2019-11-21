package br.com.global5.manager.model.areacliente;

import java.math.BigDecimal;

public class CheckListCliente{
	
	private Integer id;
		
	private String data;
	
	private String cavalo;
	
	private String status;
		
	private BigDecimal valor;
		
	public CheckListCliente(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCavalo() {
		return cavalo;
	}

	public void setCavalo(String cavalo) {
		this.cavalo = cavalo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}	

}
