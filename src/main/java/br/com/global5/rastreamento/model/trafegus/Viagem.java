package br.com.global5.rastreamento.model.trafegus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;


import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.json.JsonStringType;
import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.cadastro.Produto;
import br.com.global5.rastreamento.model.enums.Tecnologia;

@Entity
@Table(name = "rastreamento.viagem")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
			name = "update_json_viagem",
			procedureName = "update_json",
			parameters = {
					@StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "tabela"),
					@StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "campo_chave"),
					@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "chave_id"),
					@StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "campo_tabela_json"),
					@StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "valor_json"),
					@StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class, name = "resultUp")
			}
			)
})
public class Viagem  implements BaseEntity{

	private static final long serialVersionUID = 1L;

	@Id 
    @SequenceGenerator(name = "rastreamento.viagem_viagoid_seq", sequenceName = "rastreamento.viagem_viagoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rastreamento.viagem_viagoid_seq")
	@Column(name="viagoid")
	private Integer id;
	
	@Column(name="viag_dt_importacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtImportacao;
	
	@Column(name="viag_sm")
	private Integer sm;
	
	@Column(name="viag_dt_inicio")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtInicio;
	
	@Column(name="viag_dt_termino")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtTermino;
	
	@Column(name="viag_status")
	private String statusViagem;
	
	@Column(name="viag_transportador")
	private String transportador;
	
	@Column(name="viag_motoristas" , columnDefinition = "json")
	@Type(type = "jsonb")
	private String motorista;
	
	@Column(name="viag_cavalo")
	private String cavalo;
	
	@Column(name="viag_reboque1")
	private String reboque1;
	
	@Column(name="viag_reboque2")
	private String reboque2;
	
	@Column(name="viag_reboque3")
	private String reboque3;
		
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Tecnologia.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "viag_tecnologia")
	private Tecnologia tecnologia; 
	
	@Column(name="viag_mct")
	private String mct; 
	
	
	@Column(name="viag_autotrac_mct")
	private String autotrac_mct;
	
	@Column(name="viag_qtd_escoltas")
	private Integer qtdEscoltas;
	
	@Column(name="viag_escolta" , columnDefinition = "json")
	@Type(type = "jsonb")
	private String escolta;
	
	@Column(name="viag_qtd_iscas")
	private Integer qtdIscas;
	
	@Column(name="viag_isca" , columnDefinition = "json")
	@Type(type = "jsonb")
	private String isca;
	
	@Column(name="viag_json" , columnDefinition = "json")
	@Type(type = "jsonb")
	private String jsonViagem;
	
    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Transacao.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "viag_trdboid")
    private Transacao transacao;
	
    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Area.class,
            cascade={ CascadeType.ALL, CascadeType.MERGE }
    )
    @JoinColumn(name="viag_areaoid_cliente")
    private Area area;
	
	@Column(name="viag_conoid")
	private Integer idContrato;
	
	@Column(name="viag_escolta_vlr")
	private BigDecimal vlrEscolta;
	
	@Column(name="viag_iscas_viagem_qtd")
	private Integer qteViagem;
	
	@Column(name="viag_iscas_viagem_vlr")
	private BigDecimal vlrIscaViagem;
	
	@Column(name="viag_iscas_fixo_qtd")
	private Integer qtdIscaFixo;
	
    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Produto.class,
            cascade={ CascadeType.ALL, CascadeType.MERGE }
    )
    @JoinColumn(name="viag_prodoid_viagem")
    private Produto produto;
	
	@Column(name="viag_vlr")
	private BigDecimal vlrViagem;
	
	@Column(name="viag_ciclo")
	private Integer cicloViagem;
	
	@Column(name="viag_prevcoid")
	private Integer previsaoCiclo;
	
	public Viagem(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDtImportacao() {
		return dtImportacao;
	}

	public void setDtImportacao(Date dtImportacao) {
		this.dtImportacao = dtImportacao;
	}

	public Integer getSm() {
		return sm;
	}

	public void setSm(Integer sm) {
		this.sm = sm;
	}

	public Date getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}

	public Date getDtTermino() {
		return dtTermino;
	}

	public void setDtTermino(Date dtTermino) {
		this.dtTermino = dtTermino;
	}

	public String getStatusViagem() {
		return statusViagem;
	}

	public void setStatusViagem(String statusViagem) {
		this.statusViagem = statusViagem;
	}

	public String getTransportador() {
		return transportador;
	}

	public void setTransportador(String transportador) {
		this.transportador = transportador;
	}

	public String getMotorista() {
		return motorista;
	}

	public void setMotorista(String motorista) {
		this.motorista = motorista;
	}

	public String getCavalo() {
		return cavalo;
	}

	public void setCavalo(String cavalo) {
		this.cavalo = cavalo;
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

	public Tecnologia getTecnologia() {
		return tecnologia;
	}

	public void setTecnologia(Tecnologia tecnologia) {
		this.tecnologia = tecnologia;
	}

	public String getMct() {
		return mct;
	}

	public void setMct(String mct) {
		this.mct = mct;
	}

	public String getAutotrac_mct() {
		return autotrac_mct;
	}

	public void setAutotrac_mct(String autotrac_mct) {
		this.autotrac_mct = autotrac_mct;
	}

	public Integer getQtdEscoltas() {
		return qtdEscoltas;
	}

	public void setQtdEscoltas(Integer qtdEscoltas) {
		this.qtdEscoltas = qtdEscoltas;
	}

	public String getEscolta() {
		return escolta;
	}

	public void setEscolta(String escolta) {
		this.escolta = escolta;
	}

	public Integer getQtdIscas() {
		return qtdIscas;
	}

	public void setQtdIscas(Integer qtdIscas) {
		this.qtdIscas = qtdIscas;
	}

	public String getIsca() {
		return isca;
	}

	public void setIsca(String isca) {
		this.isca = isca;
	}

	public String getJsonViagem() {
		return jsonViagem;
	}

	public void setJsonViagem(String jsonViagem) {
		this.jsonViagem = jsonViagem;
	}

	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Integer getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(Integer idContrato) {
		this.idContrato = idContrato;
	}

	public BigDecimal getVlrEscolta() {
		return vlrEscolta;
	}

	public void setVlrEscolta(BigDecimal vlrEscolta) {
		this.vlrEscolta = vlrEscolta;
	}

	public Integer getQteViagem() {
		return qteViagem;
	}

	public void setQteViagem(Integer qteViagem) {
		this.qteViagem = qteViagem;
	}

	public BigDecimal getVlrIscaViagem() {
		return vlrIscaViagem;
	}

	public void setVlrIscaViagem(BigDecimal vlrIscaViagem) {
		this.vlrIscaViagem = vlrIscaViagem;
	}

	public Integer getQtdIscaFixo() {
		return qtdIscaFixo;
	}

	public void setQtdIscaFixo(Integer qtdIscaFixo) {
		this.qtdIscaFixo = qtdIscaFixo;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public BigDecimal getVlrViagem() {
		return vlrViagem;
	}

	public void setVlrViagem(BigDecimal vlrViagem) {
		this.vlrViagem = vlrViagem;
	}

	public Integer getCicloViagem() {
		return cicloViagem;
	}

	public void setCicloViagem(Integer cicloViagem) {
		this.cicloViagem = cicloViagem;
	}

	public Integer getPrevisaoCiclo() {
		return previsaoCiclo;
	}

	public void setPrevisaoCiclo(Integer previsaoCiclo) {
		this.previsaoCiclo = previsaoCiclo;
	}


	
}
