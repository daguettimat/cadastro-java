package br.com.global5.manager.model.gerencial;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.rastreamento.model.enums.Tecnologia;

import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@SqlResultSetMapping (
        name = "ListaMonViagCtrFixoMapping",
        entities = @EntityResult(
                entityClass = MonitoramentoViagensCtrFixo.class,
                fields = {
                		@FieldResult(name = "id", column = "id"),
                		@FieldResult(name = "idCliente", column = "idCliente"),
                        @FieldResult(name = "cliente", column = "cliente"),
                        @FieldResult(name = "cnpj", column = "cnpj"),
                        @FieldResult(name = "mes", column = "mes"),
                        @FieldResult(name = "ano", column = "ano"),
                        @FieldResult(name = "qtdViagens", column = "qtdViagens"),
                        @FieldResult(name = "cavalo", column = "cavalo"),
                        @FieldResult(name = "produto", column = "produto"),
                        @FieldResult(name = "valor", column = "valor"),
                        @FieldResult(name = "idProduto", column = "idProduto"),
                        @FieldResult(name = "idContrato", column = "idContrato")
                }
        )              
)

@Entity
@Table(name="tmp_monitoramento")
public class MonitoramentoViagensCtrFixo implements BaseEntity{
	
	@Id
    @SequenceGenerator(name = "tmp_monitoramento_tmvoid_seq", sequenceName = "tmp_monitoramento_tmvoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tmp_monitoramento_tmvoid_seq")
	@Column(name="tmvoid")
	private Integer id;
	
	@Column(name="tmv_id_cliente")
	private Integer idCliente;
	
	@Column(name="tmv_cliente")
	private String  cliente;
	
	@Column(name="tmv_cnpj")
	private String  cnpj;
	
	@Column(name="tmv_mes")
	private Integer mes;
	
	@Column(name="tmv_ano")
	private Integer ano;
	
	@Column(name="tmv_qtd_viagens")
	private Integer qtdViagens;
	
	@Column(name="tmv_cavalo")
	private String  cavalo;
	
	@Column(name="tmv_produto")
	private String  produto;
	
	@Column(name="tmv_valor")
	private BigDecimal valor;
	
	@Column(name="tmv_id_produto")
	private Integer idProduto;
	
	@Column(name="tmv_id_contrato")
	private Integer idContrato;
	
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = TransacaoGerencial.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "tmv_trdgoid")
    private TransacaoGerencial transacao;
        
	public MonitoramentoViagensCtrFixo(){}

	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getIdCliente() {
		return idCliente;
	}


	public String getCliente() {
		return cliente;
	}


	public String getCnpj() {
		return cnpj;
	}


	public Integer getMes() {
		return mes;
	}


	public Integer getAno() {
		return ano;
	}


	public Integer getQtdViagens() {
		return qtdViagens;
	}


	public String getCavalo() {
		return cavalo;
	}


	public String getProduto() {
		return produto;
	}


	public BigDecimal getValor() {
		return valor;
	}


	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}


	public void setCliente(String cliente) {
		this.cliente = cliente;
	}


	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}


	public void setMes(Integer mes) {
		this.mes = mes;
	}


	public void setAno(Integer ano) {
		this.ano = ano;
	}


	public void setQtdViagens(Integer qtdViagens) {
		this.qtdViagens = qtdViagens;
	}

	public void setCavalo(String cavalo) {
		this.cavalo = cavalo;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getIdProduto() {
		return idProduto;
	}

	public Integer getIdContrato() {
		return idContrato;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}

	public void setIdContrato(Integer idContrato) {
		this.idContrato = idContrato;
	}	
	
}
