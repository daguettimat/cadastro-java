package br.com.global5.manager.model.contrato.produtos;

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
@Table(name = "administrativo.cliente_isca" )
public class ClienteIsca implements BaseEntity{

    @Id
    @SequenceGenerator(name = "cliente_isca_cliioid_seq", sequenceName = "cliente_isca_cliioid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "cliente_isca_cliioid_seq")
    @Column(name ="cliioid")
	private Integer id;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Area.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="clii_areaoid_cliente")
	private Area area;
    
    @Column(name ="clii_isca")
	private String isca;
	
    @Column(name ="clii_dt_criacao")
    @Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao;
    
    @Column(name ="clii_dt_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
	private Date dtExclusao;
	
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="clii_usuoid_criacao")
	private Usuario usuCriacao;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="clii_usuoid_exclusao")
	private Usuario usuExclusao;
    
    @Column(name ="clii_dt_ini_vigencia")
    @Temporal(TemporalType.DATE)
	private Date dtInicioVigencia;
	
    public ClienteIsca(){}
    
    public ClienteIsca(Integer id, String isca){
    	this.id = id;
    	this.isca = isca;
    }

	public Integer getId() {
		return id;
	}

	public Area getArea() {
		return area;
	}

	public String getIsca() {
		return isca;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public Date getDtExclusao() {
		return dtExclusao;
	}

	public Usuario getUsuCriacao() {
		return usuCriacao;
	}

	public Usuario getUsuExclusao() {
		return usuExclusao;
	}

	public Date getDtInicioVigencia() {
		return dtInicioVigencia;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public void setIsca(String isca) {
		this.isca = isca;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public void setDtExclusao(Date dtExclusao) {
		this.dtExclusao = dtExclusao;
	}

	public void setUsuCriacao(Usuario usuCriacao) {
		this.usuCriacao = usuCriacao;
	}

	public void setUsuExclusao(Usuario usuExclusao) {
		this.usuExclusao = usuExclusao;
	}

	public void setDtInicioVigencia(Date dtInicioVigencia) {
		this.dtInicioVigencia = dtInicioVigencia;
	}

}
