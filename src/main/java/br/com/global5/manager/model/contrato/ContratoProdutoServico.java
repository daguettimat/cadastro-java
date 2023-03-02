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
import br.com.global5.manager.model.geral.ContratoProduto;


@Entity
@Table(name = "java.contrato_produto_servico" )
public class ContratoProdutoServico implements BaseEntity{
	
    @Id
    @SequenceGenerator(name = "contrato_produto_servico_conpsoid_seq", sequenceName = "contrato_produto_servico_conpsoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "contrato_produto_servico_conpsoid_seq")
    @Column(name ="conpsoid")
    private Integer id;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Produto.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="conps_prodoid")
    private Produto produto;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = ContratoProduto.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="conps_conpoid")
    private ContratoProduto contratoProduto;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = ServicoContrato.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="conps_servoid")
    private ServicoContrato servico;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = ProdutoServico.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="conps_prods_servoid")
    private ProdutoServico produtoServico;

    @Column(name ="conps_dtexclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtExclusao;
    
    @Column(name ="conps_validade")
    private Integer validade;
    
    @Column(name ="conps_aut_valor")
    private String valor;

    public ContratoProdutoServico (){}
    
    public ContratoProdutoServico (Integer id){this.id=id;}

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

	public ContratoProduto getContratoProduto() {
		return contratoProduto;
	}

	public void setContratoProduto(ContratoProduto contratoProduto) {
		this.contratoProduto = contratoProduto;
	}

	public ServicoContrato getServico() {
		return servico;
	}

	public void setServico(ServicoContrato servico) {
		this.servico = servico;
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}

	public Date getDtExclusao() {
		return dtExclusao;
	}

	public void setDtExclusao(Date dtExclusao) {
		this.dtExclusao = dtExclusao;
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
    
}
