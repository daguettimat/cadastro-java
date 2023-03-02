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
@Table(name = "administrativo.cliente_veiculo" )
public class ClienteVeiculo implements BaseEntity{

    @Id
    @SequenceGenerator(name = "administrativo.cliente_veiculo_clivoid_seq", sequenceName = "administrativo.cliente_veiculo_clivoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "administrativo.cliente_veiculo_clivoid_seq")
    @Column(name ="clivoid")
	private Integer id;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Area.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="cliv_areaoid_cliente")
	private Area area;
    
    @Column(name ="cliv_vei_placa")
	private String placaVeiculo;
	
    @Column(name ="cliv_dt_criacao")
    @Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao;
    
    @Column(name ="cliv_dt_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
	private Date dtExclusao;
	
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="cliv_usuoid_criacao")
	private Usuario usuCriacao;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="cliv_usuoid_exclusao")
	private Usuario usuExclusao;
    
    @Column(name ="cliv_dt_ini_vigencia")
    @Temporal(TemporalType.DATE)
	private Date dtInicioVigencia;
    
    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name ="cliv_usuoid_placa_mercosul")
    private Usuario usuAlteraPlacaMercosul;
    
    @Column(name ="cliv_dt_alteracao_mercosul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAlteracaoPlacaMercosul;
    
    @Column(name ="cliv_vei_placa_anterior")
    private String placaAnteriorMercosul;
	
    public ClienteVeiculo(){}
    
    public ClienteVeiculo(Integer id, String placa){
    	this.id = id;
    	this.placaVeiculo = placa;
    }

	public Integer getId() {
		return id;
	}

	public Area getArea() {
		return area;
	}

	public String getPlacaVeiculo() {
		return placaVeiculo;
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

	public void setPlacaVeiculo(String placaVeiculo) {
		this.placaVeiculo = placaVeiculo;
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

	public Date getDtAlteracaoPlacaMercosul() {
		return dtAlteracaoPlacaMercosul;
	}

	public String getPlacaAnteriorMercosul() {
		return placaAnteriorMercosul;
	}

	public void setDtAlteracaoPlacaMercosul(Date dtAlteracaoPlacaMercosul) {
		this.dtAlteracaoPlacaMercosul = dtAlteracaoPlacaMercosul;
	}

	public void setPlacaAnteriorMercosul(String placaAnteriorMercosul) {
		this.placaAnteriorMercosul = placaAnteriorMercosul;
	}

	public Usuario getUsuAlteraPlacaMercosul() {
		return usuAlteraPlacaMercosul;
	}

	public void setUsuAlteraPlacaMercosul(Usuario usuAlteraPlacaMercosul) {
		this.usuAlteraPlacaMercosul = usuAlteraPlacaMercosul;
	}
	
	
}
