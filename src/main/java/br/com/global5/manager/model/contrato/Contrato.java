package br.com.global5.manager.model.contrato;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.cadastro.Pessoa;
import br.com.global5.manager.model.enums.FaturaAgrupamento;
import br.com.global5.manager.model.enums.FormaPagamento;
import br.com.global5.manager.model.enums.ProdutoTipo;
import br.com.global5.manager.model.enums.StatusContrato;
import br.com.global5.manager.model.geral.Usuario;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "java.contrato" )
public class Contrato implements BaseEntity{

    @Id
    @SequenceGenerator(name = "contrato_conoid_seq", sequenceName = "contrato_conoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "contrato_conoid_seq")
    @Column(name ="conoid")
    private Integer id;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Area.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="con_areaoid")
    private Area area;


    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Pessoa.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="con_pesoid")
    private Pessoa pessoa;

    @Column(name ="con_dt_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao;

    @Column(name ="con_dt_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtExclusao;

    @Column(name ="con_dt_alteracao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAlteracao;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="con_usuoid_criacao")
    private Usuario usuCriacao;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="con_usuoid_alteracao")
    private Usuario usuAlteracao;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="con_usuoid_exclusao")
    private Usuario usuExclusao;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = FormaPagamento.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="con_forma_pagamento")
    private FormaPagamento formaPagamento;


    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = FaturaAgrupamento.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="con_agrupar_faturas")
    private FaturaAgrupamento faturaAgrupamento;

    @Column(name ="con_fatura_minima")
    private BigDecimal faturaMin;
    
    @Column(name ="con_fatura_maxima")
    private BigDecimal faturaMax;
    
    @Column(name ="con_pfatoid")
    private Integer periodoFaturamento;
    
    @Column(name ="con_dia_vencimento")
    private int diaVencimento;
      
    @Column(name ="con_dia_semana_vencimento")
    private int diaSemanaVencimento;
	
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Pessoa.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="con_pesoid_sacado")
    private Pessoa pessoaSacado;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = ProdutoTipo.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="con_enumoid_produto_tipo")
    private ProdutoTipo produtoTipo;


    
    
    @Column( name = "con_dt_contrato")
    private Date dtContrato;
    
    
    @Column( name = "con_dt_aviso_previo")
    private Date dtAvisoPrevio;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = StatusContrato.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="con_enumoid_status")
    private StatusContrato statusContrato;
    
    @Column( name = "con_obs")
    private String observacao;
    
    @Temporal(TemporalType.DATE)
    @Column( name = "con_inicio_vigencia")
    private Date inicioVigencia;    
    
    public Contrato (){}

    public Contrato (Integer id){this.id = id;}

   
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public Date getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Date dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }

    public Usuario getUsuCriacao() {
        return usuCriacao;
    }

    public void setUsuCriacao(Usuario usuCriacao) {
        this.usuCriacao = usuCriacao;
    }

    public Usuario getUsuAlteracao() {
        return usuAlteracao;
    }

    public void setUsuAlteracao(Usuario usuAlteracao) {
        this.usuAlteracao = usuAlteracao;
    }

    public Usuario getUsuExclusao() {
        return usuExclusao;
    }

    public void setUsuExclusao(Usuario usuExclusao) {
        this.usuExclusao = usuExclusao;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public FaturaAgrupamento getFaturaAgrupamento() {
        return faturaAgrupamento;
    }

    public void setFaturaAgrupamento(FaturaAgrupamento faturaAgrupamento) {
        this.faturaAgrupamento = faturaAgrupamento;
    }

    public BigDecimal getFaturaMin() {
        return faturaMin;
    }

    public void setFaturaMin(BigDecimal faturaMin) {
        this.faturaMin = faturaMin;
    }

    public BigDecimal getFaturaMax() {
        return faturaMax;
    }

    public void setFaturaMax(BigDecimal faturaMax) {
        this.faturaMax = faturaMax;
    }

    public Integer getPeriodoFaturamento() {
        return periodoFaturamento;
    }

    public void setPeriodoFaturamento(Integer periodoFaturamento) {
        this.periodoFaturamento = periodoFaturamento;
    }

    public Pessoa getPessoaSacado() {
        return pessoaSacado;
    }

    public void setPessoaSacado(Pessoa pessoaSacado) {
        this.pessoaSacado = pessoaSacado;
    }

    public ProdutoTipo getProdutoTipo() {
        return produtoTipo;
    }

    public void setProdutoTipo(ProdutoTipo produtoTipo) {
        this.produtoTipo = produtoTipo;
    }

	public Date getDtContrato() {
		return dtContrato;
	}

	public void setDtContrato(Date dtContrato) {
		this.dtContrato = dtContrato;
	}

	public int getDiaVencimento() {
		return diaVencimento;
	}

	public void setDiaVencimento(int diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	public int getDiaSemanaVencimento() {
		return diaSemanaVencimento;
	}

	public void setDiaSemanaVencimento(int diaSemanaVencimento) {
		this.diaSemanaVencimento = diaSemanaVencimento;
	}

	public Date getDtAvisoPrevio() {
		return dtAvisoPrevio;
	}

	public void setDtAvisoPrevio(Date dtAvisoPrevio) {
		this.dtAvisoPrevio = dtAvisoPrevio;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getInicioVigencia() {
		return inicioVigencia;
	}

	public void setInicioVigencia(Date inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}

	public StatusContrato getStatusContrato() {
		return statusContrato;
	}

	public void setStatusContrato(StatusContrato statusContrato) {
		this.statusContrato = statusContrato;
	}    		
    
	
}
