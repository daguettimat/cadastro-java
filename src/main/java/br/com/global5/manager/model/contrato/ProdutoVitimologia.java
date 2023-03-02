package br.com.global5.manager.model.contrato;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Produto;
import br.com.global5.manager.model.geral.Usuario;

@Entity
@Table(name = "java.produto_vitimologia")
public class ProdutoVitimologia implements BaseEntity {
	
    @Id
    @SequenceGenerator(name = "produto_vitimologia_prdvoid_seq", sequenceName = "produto_vitimologia_prdvoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_vitimologia_prdvoid_seq")
	@Column(name="prdvoid")
    private Integer id;
    
    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Produto.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="prdv_prodoid")
	private Produto produto;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Contrato.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="prdv_conoid")    
	private Contrato contrato;
    
    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=UfVitimologia.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="prdv_ufvoid")       
    private UfVitimologia ufVitimologia;
    
    @Column(name="prdv_valor")
    private BigDecimal valor;
    
	@Column(name="prdv_ativo")
	private boolean ativo;
	
	@Column(name="prdv_dt_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao;
	
	@Column(name="prdv_dt_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtExclusao;
		
	@Column(name="prdv_dt_alteracao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="prdv_usuoid_criacao")
	private Usuario usuCriacao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="prdv_usuoid_alteracao")
	private Usuario usuAlteracao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="prdv_usuoid_exclusao")    
	private Usuario usuExclusao;
    
    
    public ProdutoVitimologia () {}
    
    public ProdutoVitimologia (Integer id) {this.id = id;}

	public Integer getId() {
		return id;
	}

	public Produto getProduto() {
		return produto;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public UfVitimologia getUfVitimologia() {
		return ufVitimologia;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public Date getDtExclusao() {
		return dtExclusao;
	}

	public Date getDtAlteracao() {
		return dtAlteracao;
	}

	public Usuario getUsuCriacao() {
		return usuCriacao;
	}

	public Usuario getUsuAlteracao() {
		return usuAlteracao;
	}

	public Usuario getUsuExclusao() {
		return usuExclusao;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public void setUfVitimologia(UfVitimologia ufVitimologia) {
		this.ufVitimologia = ufVitimologia;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public void setDtExclusao(Date dtExclusao) {
		this.dtExclusao = dtExclusao;
	}

	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}

	public void setUsuCriacao(Usuario usuCriacao) {
		this.usuCriacao = usuCriacao;
	}

	public void setUsuAlteracao(Usuario usuAlteracao) {
		this.usuAlteracao = usuAlteracao;
	}

	public void setUsuExclusao(Usuario usuExclusao) {
		this.usuExclusao = usuExclusao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
