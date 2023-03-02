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
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.geral.Usuario;

@Entity
@Table(name = "java.regra_seguradora_veiculo")
public class RegraSeguradoraVeiculo implements BaseEntity {

    @Id
    @SequenceGenerator(name = "java.regra_seguradora_veiculo_rsvoid_seq", sequenceName = "java.regra_seguradora_veiculo_rsvoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "java.regra_seguradora_veiculo_rsvoid_seq")
    @Column(name = "rsvoid")
    private Integer id;
    
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Area.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "rsv_areaoid")
    private Area area;
    
    
    @Column(name = "rsv_apartir_ano")
    private Integer apartirAno;
    
    @Column(name = "rsv_dt_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao;
    
	@Column(name = "rsv_dt_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtExclusao;
	
	@Column(name = "rsv_dt_alteracao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao;
	
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "rsv_usuoid_criacao")
	private Usuario usuCriacao;
	
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "rsv_usuoid_alteracao")
	private Usuario usuAlteracao;
	
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "rsv_usuoid_exclusao")	
	private Usuario usuExclusao;
    
	public RegraSeguradoraVeiculo() {
	}

	public RegraSeguradoraVeiculo(Integer id){
		this.id = id;
	}

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

	public Integer getApartirAno() {
		return apartirAno;
	}

	public void setApartirAno(Integer apartirAno) {
		this.apartirAno = apartirAno;
	}
    
}
