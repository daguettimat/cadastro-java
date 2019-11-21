package br.com.global5.manager.model.areacliente;

import java.math.BigDecimal;


public class AutotracCliente{
	
	private Integer id;
	
	private Integer mct;
		
	private String cavalo;
	
	private String sm;
		
	private BigDecimal valor_ade;
	
	private BigDecimal valor_comunicacao;
		
	public AutotracCliente(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMct() {
		return mct;
	}

	public void setMct(Integer mct) {
		this.mct = mct;
	}

	public String getCavalo() {
		return cavalo;
	}

	public void setCavalo(String cavalo) {
		this.cavalo = cavalo;
	}

	public String getSm() {
		return sm;
	}

	public void setSm(String sm) {
		this.sm = sm;
	}

	public BigDecimal getValor_ade() {
		return valor_ade;
	}

	public void setValor_ade(BigDecimal valor_ade) {
		this.valor_ade = valor_ade;
	}

	public BigDecimal getValor_comunicacao() {
		return valor_comunicacao;
	}

	public void setValor_comunicacao(BigDecimal valor_comunicacao) {
		this.valor_comunicacao = valor_comunicacao;
	}


}
