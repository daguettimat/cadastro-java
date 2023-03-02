package br.com.global5.manager.model.contrato;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Produto;
import br.com.global5.manager.model.geral.Usuario;

@Entity
@Table(name = "java.produto_servico" )
public class ProdutoServico implements BaseEntity{
	
    @Id
    @SequenceGenerator(name = "produto_servico_prodsoid_seq", sequenceName = "produto_servico_prodsoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "produto_servico_prodsoid_seq")
    @Column(name ="prodsoid")
    private Integer id;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Produto.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="prods_prodoid")
    private Produto produto;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = ServicoContrato.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="prods_servoid")
    private ServicoContrato servico;
    
    @Column(name ="prods_obrigatorio")
    private boolean obrigatorio;
    
    @Column(name ="prods_validade")
    private Integer validade;
    
    @Column(name ="prods_aut_valor")
    private String valor;
    
    
    @Column(name ="prods_dt_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao;

    @Column(name ="prods_dt_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtExclusao;

    @Column(name ="prods_dt_alteracao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAlteracao;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="prods_usuoid_criacao")
    private Usuario usuCriacao;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="prods_usuoid_alteracao")
    private Usuario usuAlteracao;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="prods_usuoid_exclusao")
    private Usuario usuExclusao;


    public ProdutoServico(){}
    
    public ProdutoServico(Integer id){this.id = id;}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public ServicoContrato getServico() {
		return servico;
	}

	public void setServico(ServicoContrato servico) {
		this.servico = servico;
	}

	public boolean isObrigatorio() {
		return obrigatorio;
	}

	public void setObrigatorio(boolean obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	public Integer getValidade() {
		return validade;
	}

	public void setValidade(Integer validade) {
		this.validade = validade;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
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

}
