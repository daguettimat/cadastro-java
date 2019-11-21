package br.com.global5.manager.model.areacliente;

import java.math.BigDecimal;

public class CadastroCliente{
	
	private Integer id;
	
	private String ficha;

	private BigDecimal valor;
	
	private String mottipo;
	
	private String vencimento;
	
	private String tipo;
	
	private String motcpf;
	
	private String dt_termino;
	
	private String cavalo;
	
	private String motorista;
	
	private String unidade;

	private String reboque1;
	
	private String reboque2;
	
	private String reboque3;
	
	public CadastroCliente(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFicha() {
		return ficha;
	}

	public void setFicha(String ficha) {
		this.ficha = ficha;
	}
	
	

	public BigDecimal getValor() {
		return valor;
	}

//	public Integer getFicha() {
//		return ficha;
//	}
//
//	public void setFicha(Integer ficha) {
//		this.ficha = ficha;
//	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getMottipo() {
		return mottipo;
	}

	public void setMottipo(String mottipo) {
		this.mottipo = mottipo;
	}

	public String getVencimento() {
		return vencimento;
	}

	public void setVencimento(String vencimento) {
		this.vencimento = vencimento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getMotcpf() {
		return motcpf;
	}

	public void setMotcpf(String motcpf) {
		this.motcpf = motcpf;
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

	public String getMotorista() {
		return motorista;
	}

	public void setMotorista(String motorista) {
		this.motorista = motorista;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getReboque1() {
		return reboque1;
	}

	public void setReboque1(String reboque1) {
		this.reboque1 = reboque1;
	}

	public String getReboque2() {
		return reboque2;
	}

	public void setReboque2(String reboque2) {
		this.reboque2 = reboque2;
	}

	public String getReboque3() {
		return reboque3;
	}

	public void setReboque3(String reboque3) {
		this.reboque3 = reboque3;
	}


}
