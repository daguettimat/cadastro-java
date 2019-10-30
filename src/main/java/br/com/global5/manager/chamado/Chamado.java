package br.com.global5.manager.chamado;

import java.util.Date;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.geral.Usuario;

@Entity
@Table(name = "chamado")
public class Chamado implements BaseEntity {

	@Id
	@SequenceGenerator(name = "chamado_chamoid_seq", sequenceName = "chamado_chamoid_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "chamado_chamoid_seq")
	@Column(name = "chamoid")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = ChamadoTipo.class, cascade = { CascadeType.DETACH,
			CascadeType.MERGE })
	@JoinColumn(name = "cham_tipo")
	private ChamadoTipo tipoChamado;

	@Column(name = "cham_descricao")
	private String descricao;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Area.class, cascade = { CascadeType.DETACH, CascadeType.MERGE })
	@JoinColumn(name = "cham_abertura_areaoid")
	private Area area;

	@Column(name = "cham_abertura_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAbertura;

	@Column(name = "cham_atendimento_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAtendimento;

	@Column(name = "cham_finalizacao_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtFinalizacao;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Usuario.class, cascade = { CascadeType.DETACH,
			CascadeType.MERGE })
	@JoinColumn(name = "cham_abertura_usuoid")
	private Usuario usuAbertura;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Usuario.class, cascade = { CascadeType.DETACH,
			CascadeType.MERGE })
	@JoinColumn(name = "cham_atendimento_usuoid")
	private Usuario usuAtendimento;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Usuario.class, cascade = { CascadeType.DETACH,
			CascadeType.MERGE })
	@JoinColumn(name = "cham_finalizacao_usuoid")
	private Usuario usuFinalizacao;

	public Chamado() {
	}

	public Chamado(Integer id) {
		this.id = id;
	}

	public Chamado(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ChamadoTipo getTipoChamado() {
		return tipoChamado;
	}

	public void setTipoChamado(ChamadoTipo tipoChamado) {
		this.tipoChamado = tipoChamado;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Date getDtAbertura() {
		return dtAbertura;
	}

	public void setDtAbertura(Date dtAbertura) {
		this.dtAbertura = dtAbertura;
	}

	public Date getDtAtendimento() {
		return dtAtendimento;
	}

	public void setDtAtendimento(Date dtAtendimento) {
		this.dtAtendimento = dtAtendimento;
	}

	public Date getDtFinalizacao() {
		return dtFinalizacao;
	}

	public void setDtFinalizacao(Date dtFinalizacao) {
		this.dtFinalizacao = dtFinalizacao;
	}

	public Usuario getUsuAbertura() {
		return usuAbertura;
	}

	public void setUsuAbertura(Usuario usuAbertura) {
		this.usuAbertura = usuAbertura;
	}

	public Usuario getUsuAtendimento() {
		return usuAtendimento;
	}

	public void setUsuAtendimento(Usuario usuAtendimento) {
		this.usuAtendimento = usuAtendimento;
	}

	public Usuario getUsuFinalizacao() {
		return usuFinalizacao;
	}

	public void setUsuFinalizacao(Usuario usuFinalizacao) {
		this.usuFinalizacao = usuFinalizacao;
	}

}
