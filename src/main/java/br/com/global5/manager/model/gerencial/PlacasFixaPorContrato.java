package br.com.global5.manager.model.gerencial;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import br.com.global5.infra.model.BaseEntity;

import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@SqlResultSetMapping (
        name = "ListaPlacasFixaPorContratoMapping",
        entities = @EntityResult(
                entityClass = PlacasFixaPorContrato.class,
                fields = {
                		@FieldResult(name = "id", column = "id"),
                		@FieldResult(name = "idPlaca", column = "idPlaca"),
                        @FieldResult(name = "idCliente", column = "idCliente"),
                        @FieldResult(name = "placaFixa", column = "placaFixa"),
                        @FieldResult(name = "checado", column = "checado"),                        
                        @FieldResult(name = "cliente", column = "cliente"),
                        @FieldResult(name = "cnpj", column = "cnpj"),
                        @FieldResult(name = "mes", column = "mes"),
                        @FieldResult(name = "ano", column = "ano"),                        
                		@FieldResult(name = "excluida", column = "excluida"),
                		@FieldResult(name = "periodo", column = "periodo"),
                		@FieldResult(name = "vlrContrato", column = "vlrContrato"),
                		@FieldResult(name = "idProduto", column = "idProduto"),
                		@FieldResult(name = "nomeProduto", column = "nomeProduto"),
                		@FieldResult(name = "numContrato", column = "numContrato")
                }
        )              
)


@Entity
@Table(name="tmp_placasfixas")
public class PlacasFixaPorContrato  implements BaseEntity {
		
	@Id
    @SequenceGenerator(name = "tmp_placasfixas_pcv_idregistro_seq", sequenceName = "tmp_placasfixas_pcv_idregistro_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tmp_placasfixas_pcv_idregistro_seq")
	@Column(name="pcv_idregistro")
	private Integer id;		// cv_clivoid	
	
	@Column(name="pcv_idPlaca")
	private Integer idPlaca;		// cv_clivoid
	
	@Column(name="pcv_idCliente")
	private Integer idCliente;		// cv_tcliv_areaoid_cliente
	
	@Column(name="pcv_placaFixa")
	private String  placaFixa;		// cv_tplaca
	
	@Column(name="pcv_checado")
	private int	checado;			//  cv_tchecado
	
	@Column(name="pcv_cliente")		// nome do cliente
	private String  cliente;
	
	@Column(name="pcv_cnpj")		
	private String  cnpj;
	
	@Column(name="pcv_mes")
	private Integer mes;
	
	@Column(name="pcv_ano")
	private Integer ano;
	
	@Column(name="pcv_excluida")	//
	private boolean excluida;
		
	@Column(name="pcv_periodo")
	private String periodo;
	
	@Column(name="pcv_vlr_contrato")
	private BigDecimal vlrContrato;
	
	@Column(name="pcv_id_produto")
	private Integer idProduto;
	
	@Column(name="pcv_nome_produto")
	private String nomeProduto;
	
	@Column(name="pcv_num_contrato")
	private Integer numContrato;
		
	public PlacasFixaPorContrato(){}

	public Integer getIdPlaca() {
		return idPlaca;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public String getPlacaFixa() {
		return placaFixa;
	}

	public int getChecado() {
		return checado;
	}

	public void setIdPlaca(Integer idPlaca) {
		this.idPlaca = idPlaca;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public void setPlacaFixa(String placaFixa) {
		this.placaFixa = placaFixa;
	}

	public void setChecado(int checado) {
		this.checado = checado;
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

	public boolean isExcluida() {
		return excluida;
	}

	public String getPeriodo() {
		return periodo;
	}

	public BigDecimal getVlrContrato() {
		return vlrContrato;
	}

	public Integer getIdProduto() {
		return idProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public Integer getNumContrato() {
		return numContrato;
	}

	public void setExcluida(boolean excluida) {
		this.excluida = excluida;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public void setVlrContrato(BigDecimal vlrContrato) {
		this.vlrContrato = vlrContrato;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public void setNumContrato(Integer numContrato) {
		this.numContrato = numContrato;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
