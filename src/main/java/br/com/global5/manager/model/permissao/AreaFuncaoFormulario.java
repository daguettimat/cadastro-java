package br.com.global5.manager.model.permissao;

import java.util.Date;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.areas.AreaFuncao;
import br.com.global5.manager.model.geral.Usuario;

@Entity
@Table(name = "area_funcao_formulario")
public class AreaFuncaoFormulario implements BaseEntity {
	
	@Id
	@SequenceGenerator ( name = "area_funcao_formulario_affoid_seq", sequenceName = "area_funcao_formulario_affoid_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "area_funcao_formulario_affoid_seq")
	@Column(name="affoid")
	private Integer id;
	
	
    @ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = AreaFuncao.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}
		)
    @JoinColumn(name = "aff_afunoid")    
    private AreaFuncao areaFuncao;
    
    @ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = Formulario.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}
		)
    @JoinColumn(name = "aff_formoid")       
    private Formulario formulario;
    
    @Column(name="aff_dt_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao;
    
    @ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = Usuario.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}
		)
    @JoinColumn(name = "aff_usuoid_criacao")    
    private Usuario usuCriacao;
    
    public AreaFuncaoFormulario () {}
    
    public AreaFuncaoFormulario (Integer Id) {this.id = id;}
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public AreaFuncao getAreaFuncao() {
		return areaFuncao;
	}
	public void setAreaFuncao(AreaFuncao areaFuncao) {
		this.areaFuncao = areaFuncao;
	}
	public Formulario getFormulario() {
		return formulario;
	}
	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}
	public Date getDtCriacao() {
		return dtCriacao;
	}
	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}
	public Usuario getUsuCriacao() {
		return usuCriacao;
	}
	public void setUsuCriacao(Usuario usuCriacao) {
		this.usuCriacao = usuCriacao;
	}
    
    
    
}
