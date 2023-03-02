package br.com.global5.manager.model.geral;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.global5.infra.model.BaseEntity;

@Entity
@Table(name="java.contrato_produto")
public class ContratoProduto implements BaseEntity {
	
	@Id
    @SequenceGenerator(name = "contrato_produto_conpoid_seq", sequenceName = "contrato_produto_conpoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "contrato_produto_conpoid_seq")
    @Column(name = "conpoid")
	private Integer id;
	
	@Column(name = "conp_prodoid")
	private Integer produto;
	
	@Column(name = "conp_fatura_minima")
	private BigDecimal faturaMinima;
	
	@Column(name = "conp_fatura_maxima")
	private BigDecimal faturaMaxima;
	
	@Column(name = "conp_valor1_unitario")
	private BigDecimal vlr1Unitario;
	
	@Column(name = "conp_param1")
	private Integer    parametro1;
	
	@Column(name = "conp_valor2_unitario")
	private BigDecimal vlr2Unitario;
	
	@Column(name = "conp_param2")
	private Integer    parametro2;
	
	@Column(name = "conp_valor3_unitario")
	private BigDecimal vlr3Unitario;
	
	@Column(name = "conp_param3")
	private Integer    parametro3;
	
	@Column(name = "conp_valor4_unitario")
	private BigDecimal vlr4Unitario;
	
	@Column(name = "conp_param4")
	private Integer    parametro4;
	
	@Column(name = "conp_valor5_unitario")
	private BigDecimal vlr5Unitario;
	
	@Column(name = "conp_param5")
	private Integer    parametro5;
	
	@Column(name = "conp_valor6_unitario")
	private BigDecimal vlr6Unitario;
	
	@Column(name = "conp_param6")
	private Integer    parametro6;

	@Column(name = "conp_dt_exclusao")
	private Date dtExclusao;
	
	@Column(name = "conp_valor1_unidade")
	private Integer  vlr1Unidade;
	
	@Column(name = "conp_valor2_unidade")
	private Integer  vlr2Unidade;
	
	@Column(name = "conp_valor3_unidade")
	private Integer  vlr3Unidade;
	
	@Column(name = "conp_valor4_unidade")
	private Integer  vlr4Unidade;
	
	@Column(name = "conp_valor5_unidade")
	private Integer  vlr5Unidade;
	
	@Column(name = "conp_valor6_unidade")
	private Integer  vlr6Unidade;
	
	@Column(name = "conp_conoid")
	private Integer  contrato;
	
	@Column(name = "conp_produto_ativo")
	private boolean  produtoAtivo;
	
	public ContratoProduto(){}

	public Integer getId() {
		return id;
	}

	public Integer getProduto() {
		return produto;
	}

	public BigDecimal getFaturaMinima() {
		return faturaMinima;
	}

	public BigDecimal getFaturaMaxima() {
		return faturaMaxima;
	}

	public BigDecimal getVlr1Unitario() {
		return vlr1Unitario;
	}

	public Integer getParametro1() {
		return parametro1;
	}

	public BigDecimal getVlr2Unitario() {
		return vlr2Unitario;
	}

	public Integer getParametro2() {
		return parametro2;
	}

	public BigDecimal getVlr3Unitario() {
		return vlr3Unitario;
	}

	public Integer getParametro3() {
		return parametro3;
	}

	public BigDecimal getVlr4Unitario() {
		return vlr4Unitario;
	}

	public Integer getParametro4() {
		return parametro4;
	}

	public BigDecimal getVlr5Unitario() {
		return vlr5Unitario;
	}

	public Integer getParametro5() {
		return parametro5;
	}

	public BigDecimal getVlr6Unitario() {
		return vlr6Unitario;
	}

	public Integer getParametro6() {
		return parametro6;
	}

	public Date getDtExclusao() {
		return dtExclusao;
	}

	public Integer getVlr1Unidade() {
		return vlr1Unidade;
	}

	public Integer getVlr2Unidade() {
		return vlr2Unidade;
	}

	public Integer getVlr3Unidade() {
		return vlr3Unidade;
	}

	public Integer getVlr4Unidade() {
		return vlr4Unidade;
	}

	public Integer getVlr5Unidade() {
		return vlr5Unidade;
	}

	public Integer getVlr6Unidade() {
		return vlr6Unidade;
	}

	public Integer getContrato() {
		return contrato;
	}

	public boolean isProdutoAtivo() {
		return produtoAtivo;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setProduto(Integer produto) {
		this.produto = produto;
	}

	public void setFaturaMinima(BigDecimal faturaMinima) {
		this.faturaMinima = faturaMinima;
	}

	public void setFaturaMaxima(BigDecimal faturaMaxima) {
		this.faturaMaxima = faturaMaxima;
	}

	public void setVlr1Unitario(BigDecimal vlr1Unitario) {
		this.vlr1Unitario = vlr1Unitario;
	}

	public void setParametro1(Integer parametro1) {
		this.parametro1 = parametro1;
	}

	public void setVlr2Unitario(BigDecimal vlr2Unitario) {
		this.vlr2Unitario = vlr2Unitario;
	}

	public void setParametro2(Integer parametro2) {
		this.parametro2 = parametro2;
	}

	public void setVlr3Unitario(BigDecimal vlr3Unitario) {
		this.vlr3Unitario = vlr3Unitario;
	}

	public void setParametro3(Integer parametro3) {
		this.parametro3 = parametro3;
	}

	public void setVlr4Unitario(BigDecimal vlr4Unitario) {
		this.vlr4Unitario = vlr4Unitario;
	}

	public void setParametro4(Integer parametro4) {
		this.parametro4 = parametro4;
	}

	public void setVlr5Unitario(BigDecimal vlr5Unitario) {
		this.vlr5Unitario = vlr5Unitario;
	}

	public void setParametro5(Integer parametro5) {
		this.parametro5 = parametro5;
	}

	public void setVlr6Unitario(BigDecimal vlr6Unitario) {
		this.vlr6Unitario = vlr6Unitario;
	}

	public void setParametro6(Integer parametro6) {
		this.parametro6 = parametro6;
	}

	public void setDtExclusao(Date dtExclusao) {
		this.dtExclusao = dtExclusao;
	}

	public void setVlr1Unidade(Integer vlr1Unidade) {
		this.vlr1Unidade = vlr1Unidade;
	}

	public void setVlr2Unidade(Integer vlr2Unidade) {
		this.vlr2Unidade = vlr2Unidade;
	}

	public void setVlr3Unidade(Integer vlr3Unidade) {
		this.vlr3Unidade = vlr3Unidade;
	}

	public void setVlr4Unidade(Integer vlr4Unidade) {
		this.vlr4Unidade = vlr4Unidade;
	}

	public void setVlr5Unidade(Integer vlr5Unidade) {
		this.vlr5Unidade = vlr5Unidade;
	}

	public void setVlr6Unidade(Integer vlr6Unidade) {
		this.vlr6Unidade = vlr6Unidade;
	}

	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}

	public void setProdutoAtivo(boolean produtoAtivo) {
		this.produtoAtivo = produtoAtivo;
	}
	
	
	
}                                          

