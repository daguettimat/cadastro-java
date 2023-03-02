package br.com.global5.manager.model.analise;

import java.util.Date;

import javax.persistence.*;

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
@Table(name="java.analise_cadastral_ficha_financeira")
@TypeDefs({
@TypeDef(name = "json", typeClass = JsonStringType.class),
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FichaFinanceiraMotorista implements BaseEntity{
	
	@Id
    @SequenceGenerator(name = "analise_cadastral_ficha_financeira_acfoid_seq", sequenceName = "analise_cadastral_ficha_financeira_acfoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analise_cadastral_ficha_financeira_acfoid_seq")
    @Column(name="acfoid")
	private Integer id;
	
	@Column(name="acf_anacoid")
	private Integer analiseCadastral;
	
    @Column(name="acf_n_consulta")
    private Integer numConsulta; 
    
    @Column(name="acf_n_requisicao")
    private String numRequisicao; 

    @Column(name="acf_chave_acesso")
    private String chaveAcesso;

    @Column(name="acf_tipo_servico")
    private String tipoServico; 
    
    @Column(name="acf_cod_retorno")
    private String codRetorno;

    @Type(type = "jsonb")
    @Column(name="acf_xml_retorno", columnDefinition = "json")
    private String xmlRetorno;
    
    @Column(name="acf_st_ws_status")
    private String stWsStatus;
    
    @Column(name="acf_dt_ws_consulta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtWsConsulta;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="acf_avaliacao")
    private ReferenciasAvaliacao avaliacao;
    
    @ManyToOne(
    		fetch = FetchType.LAZY,
    		targetEntity = Usuario.class,
    		cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="acf_usuoid_criacao")    
    private Usuario usuCriacao; 

    @Column(name="acf_dt_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao;

    @Column(name="acf_pendencia")
    private String pendencia;
    
	public FichaFinanceiraMotorista() {}

	public FichaFinanceiraMotorista(Integer id) {
		this.id = id;
	}
	
	public FichaFinanceiraMotorista(Integer id, Integer numConsulta) {
		this.id = id;
		this.numConsulta = numConsulta;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAnaliseCadastral() {
		return analiseCadastral;
	}

	public void setAnaliseCadastral(Integer analiseCadastral) {
		this.analiseCadastral = analiseCadastral;
	}

	public Integer getNumConsulta() {
		return numConsulta;
	}

	public void setNumConsulta(Integer numConsulta) {
		this.numConsulta = numConsulta;
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

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	public String getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	public String getCodRetorno() {
		return codRetorno;
	}

	public void setCodRetorno(String codRetorno) {
		this.codRetorno = codRetorno;
	}

	public String getXmlRetorno() {
		return xmlRetorno;
	}

	public void setXmlRetorno(String xmlRetorno) {
		this.xmlRetorno = xmlRetorno;
	}

	public String getStWsStatus() {
		return stWsStatus;
	}

	public void setStWsStatus(String stWsStatus) {
		this.stWsStatus = stWsStatus;
	}

	public Date getDtWsConsulta() {
		return dtWsConsulta;
	}

	public void setDtWsConsulta(Date dtWsConsulta) {
		this.dtWsConsulta = dtWsConsulta;
	}

	public Usuario getUsuCriacao() {
		return usuCriacao;
	}

	public void setUsuCriacao(Usuario usuCriacao) {
		this.usuCriacao = usuCriacao;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public ReferenciasAvaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(ReferenciasAvaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	public String getPendencia() {
		return pendencia;
	}

	public void setPendencia(String pendencia) {
		this.pendencia = pendencia;
	}
	
	
	
}
