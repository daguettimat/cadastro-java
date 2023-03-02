package br.com.global5.manager.model.areacliente;

public class DetalheFixos {
	
	private Integer id;
	private Integer total;
	private Integer inclusoes;
	private Integer exclusoes;
	private String placas;
	
	public DetalheFixos(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getInclusoes() {
		return inclusoes;
	}

	public void setInclusoes(Integer inclusoes) {
		this.inclusoes = inclusoes;
	}

	public Integer getExclusoes() {
		return exclusoes;
	}

	public void setExclusoes(Integer exclusoes) {
		this.exclusoes = exclusoes;
	}

	public String getPlacas() {
		return placas;
	}

	public void setPlacas(String placas) {
		this.placas = placas;
	}

	
}
