package br.com.global5.manager.model.analise;

import java.math.BigDecimal;
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

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.json.JsonStringType;
import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.enums.ReferenciasAvaliacao;
import br.com.global5.manager.model.geral.Usuario;



@Entity
@Table(name="java.analise_cadastral_vitimologia")
@TypeDefs({
@TypeDef(name = "json", typeClass = JsonStringType.class),
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vitimologia implements BaseEntity {
	
	
	//nextval('java.analise_cadastral_vitimologia_acvoid_seq'::regclass)
	
    @Id
    @SequenceGenerator(name = "analise_cadastral_vitimologia_acvoid_seq", sequenceName = "analise_cadastral_vitimologia_acvoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analise_cadastral_vitimologia_acvoid_seq")
    @Column(name="acvoid")
    private Integer id; 

//    @ManyToOne(
//    		fetch = FetchType.LAZY,
//    		targetEntity = acCadastro.class,
//    		cascade = {CascadeType.DETACH, CascadeType.MERGE}
//    )
//    @JoinColumn( name = "acv_anacoid")
    @Column(name="acv_anacoid")
    private Integer analiseCadastral;

    @Column(name="acv_uf_bo")
    private String ufBo;

    @Column(name="acv_n_consulta")
    private Integer numConsulta; 

    @Column(name="acv_n_requisicao")
    private String numRequisicao; 

    @Column(name="acv_chave_acesso")
    private String chaveAcesso;

    @Column(name="acv_tipo_servico")
    private String tipoServico; 

    @Column(name="acv_cod_retorno")
    private String codRetorno;
    
    @Type(type = "jsonb")
    @Column(name="acv_xml_retorno", columnDefinition = "json")
    private String xmlRetorno;
    
    @Column(name="acv_st_ws_status")
    private String stWsStatus;
    
    @Column(name="acv_dt_ws_consulta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtWsConsulta;

    @ManyToOne(
    		fetch = FetchType.LAZY,
    		targetEntity = Usuario.class,
    		cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="acv_usuoid_criacao")    
    private Usuario usuCriacao; 

    @Column(name="acv_dt_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao; 
    
    @Column(name="acv_id_prd_vitimologia")
    private String idPrdVitimologia;
    
    @Column(name="acv_tipo")
    private String tipoMov;
    
    @Column(name="acv_vlr_vit")
    private BigDecimal vlrVitimologia;
    
    @Column(name="acv_pendencia")
    private String pendencia;
    
    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="acv_vitimologia_avaliacao")
    private ReferenciasAvaliacao avaliacao;
    
    public Vitimologia(){}
    
    public Vitimologia(Integer id){
    	this.id = id;
    }
    
    public Vitimologia(Integer id, Integer numConsulta){
    	this.id = id;
    	this.numConsulta = numConsulta;
    }

	public Integer getId() {
		return id;
	}



	public String getUfBo() {
		return ufBo;
	}

	public Integer getNumConsulta() {
		return numConsulta;
	}

	public String getNumRequisicao() {
		return numRequisicao;
	}

	public void setNumRequisicao(String numRequisicao) {
		this.numRequisicao = numRequisicao;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public String getTipoServico() {
		return tipoServico;
	}

	public String getCodRetorno() {
		return codRetorno;
	}



	public String getStWsStatus() {
		return stWsStatus;
	}

	public Date getDtWsConsulta() {
		return dtWsConsulta;
	}

	public Usuario getUsuCriacao() {
		return usuCriacao;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUfBo(String ufBo) {
		this.ufBo = ufBo;
	}

	public void setNumConsulta(Integer numConsulta) {
		this.numConsulta = numConsulta;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	public void setCodRetorno(String codRetorno) {
		this.codRetorno = codRetorno;
	}



	public void setStWsStatus(String stWsStatus) {
		this.stWsStatus = stWsStatus;
	}

	public void setDtWsConsulta(Date dtWsConsulta) {
		this.dtWsConsulta = dtWsConsulta;
	}

	public void setUsuCriacao(Usuario usuCriacao) {
		this.usuCriacao = usuCriacao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public Integer getAnaliseCadastral() {
		return analiseCadastral;
	}

	public void setAnaliseCadastral(Integer analiseCadastral) {
		this.analiseCadastral = analiseCadastral;
	}

	public String getXmlRetorno() {
		return xmlRetorno;
	}

	public void setXmlRetorno(String xmlRetorno) {
		this.xmlRetorno = xmlRetorno;
	}

	public String getIdPrdVitimologia() {
		return idPrdVitimologia;
	}

	public void setIdPrdVitimologia(String idPrdVitimologia) {
		this.idPrdVitimologia = idPrdVitimologia;
	}

	public BigDecimal getVlrVitimologia() {
		return vlrVitimologia;
	}

	public void setVlrVitimologia(BigDecimal vlrVitimologia) {
		this.vlrVitimologia = vlrVitimologia;
	}

	public String getTipoMov() {
		return tipoMov;
	}

	public void setTipoMov(String tipoMov) {
		this.tipoMov = tipoMov;
	}

	public String getPendencia() {
		return pendencia;
	}

	public void setPendencia(String pendencia) {
		this.pendencia = pendencia;
	}

	public ReferenciasAvaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(ReferenciasAvaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}        
	
}
