package br.com.global5.manager.model.autotrac;

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

import org.hibernate.criterion.MatchMode;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.geral.Usuario;



@Entity
@Table(name = "administrativo.autotrac_sinal")
public class AutotracSinal implements BaseEntity {

    @Id
    @SequenceGenerator(name = "autotrac_sinal_autsoid_seq", sequenceName = "autotrac_sinal_autsoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "autotrac_sinal_autsoid_seq")
    @Column(name = "autsoid")
    private Integer id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Area.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "auts_areaoid_cliente")
    private Area areaCliente;
    
    @Column(name = "auts_ini_vigencia")
    private Date inicioVigencia;
    
    @Column(name = "auts_fim_vigencia")
    private Date finalVigencia;
    
    @Column(name = "auts_mct")
    private String mct;
    
    @Column(name = "auts_placa")
    private String placa;

    @Column(name = "auts_nome_cliente")
    private String nomeCliente;
    
    @Column(name = "auts_req_nome")
    private String nomeRequisitante;
    
    @Column(name = "auts_req_email")
    private String emailRequisitante;
    
    @Column(name = "auts_req_requisicao")
    private String requisicao;
    
    @Column(name = "auts_cancelamento")
    private boolean cancelado;
    
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "auts_usuoid_inclusao")
    private Usuario usuInclusao;
        
    @Column(name = "auts_dt_inclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtInclusao;
    
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "auts_usuoid_exclusao")
    private Usuario usuExclusao;
    
    @Column(name = "auts_dt_finalizacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtFinalizacao;


    public AutotracSinal(){}
    
    public AutotracSinal(Integer id) { this.id = id;}
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Area getAreaCliente() {
		return areaCliente;
	}

	public void setAreaCliente(Area areaCliente) {
		this.areaCliente = areaCliente;
	}

	public Date getInicioVigencia() {
		return inicioVigencia;
	}

	public void setInicioVigencia(Date inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}

	public Date getFinalVigencia() {
		return finalVigencia;
	}

	public void setFinalVigencia(Date finalVigencia) {
		this.finalVigencia = finalVigencia;
	}

	public String getMct() {
		return mct;
	}

	public void setMct(String mct) {
		this.mct = mct;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getNomeRequisitante() {
		return nomeRequisitante;
	}

	public void setNomeRequisitante(String nomeRequisitante) {
		this.nomeRequisitante = nomeRequisitante;
	}

	public String getEmailRequisitante() {
		return emailRequisitante;
	}

	public void setEmailRequisitante(String emailRequisitante) {
		this.emailRequisitante = emailRequisitante;
	}

	public String getRequisicao() {
		return requisicao;
	}

	public void setRequisicao(String requisicao) {
		this.requisicao = requisicao;
	}

	public Usuario getUsuInclusao() {
		return usuInclusao;
	}

	public void setUsuInclusao(Usuario usuInclusao) {
		this.usuInclusao = usuInclusao;
	}

	public Date getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	public Usuario getUsuExclusao() {
		return usuExclusao;
	}

	public void setUsuExclusao(Usuario usuExclusao) {
		this.usuExclusao = usuExclusao;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public Date getDtFinalizacao() {
		return dtFinalizacao;
	}

	public void setDtFinalizacao(Date dtFinalizacao) {
		this.dtFinalizacao = dtFinalizacao;
	}

	public boolean isCancelado() {
		return cancelado;
	}

	public void setCancelado(boolean cancelado) {
		this.cancelado = cancelado;
	}

}
