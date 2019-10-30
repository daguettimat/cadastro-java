package br.com.global5.rastreamento.model.bi;

import java.util.Date;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.rastreamento.model.sinc.SincViagem;

/**
 * @Inheritance(strategy= InheritanceType.SINGLE_TABLE)
 * @author francis
 *
 */

@Entity
@Table(name="evento")
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
			name = "evento_i",
			procedureName = "evento_i",
			parameters = {
					@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "sm"),
					@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "usuoid"),
					@StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class, name = "evento")
			}
			)
})
public class Evento implements BaseEntity {
	
	public static final char EVENTO_TIPO_SUSPEITA 	= 'S';
	public static final char EVENTO_TIPO_PERDA		= 'P';
	public static final char EVENTO_TIPO_DANO		= 'D';

	@Id 
	@SequenceGenerator(name = "evento_eveoid_seq", sequenceName = "evento_eveoid_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evento_eveoid_seq")
	@Column(name="eveoid")
	private Integer id;
	
	@Column(name="eve_ano")
	private Integer ano;
			
	@ManyToOne
	@JoinColumn(name="eve_viagoid")
	private SincViagem sincViagem;

	@Column(name="eve_nr_evento")
	private Integer nrEvento;
	
	@Column(name="eve_nr_boletim")
	private Integer nrBoletim;

	@Column(name="eve_nr_boletim_suspeita")
	private Integer nrBoletimSuspeita;

	@Column(name="eve_nr_boletim_perda")
	private Integer nrBoletimPerda;

	@Column(name="eve_nr_boletim_dano")
	private Integer nrBoletimDano;

	@Column(name="eve_tipo_inicial", precision = 1)
	private Character tipoInicial;

	@Column(name="eve_tipo_final", precision =1)
	private Character tipoFinal;
	
	@ManyToOne(	
			fetch = FetchType.EAGER,
			targetEntity = BoletimInformativo.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}			
			)
	@JoinColumn(name="eve_bioid_ultimo")
	private BoletimInformativo boletimInformativo;

	@ManyToOne(	
			fetch = FetchType.LAZY,
			targetEntity = Usuario.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}			
			)
	@JoinColumn(name="eve_usuoid_criacao")
	private Usuario usuCriacao;
	
	@ManyToOne(	
			fetch = FetchType.LAZY,
			targetEntity = Usuario.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}			
			)
	@JoinColumn(name="eve_usuoid_alteracao")
	private Usuario usuAlteracao;

	@ManyToOne(	
			fetch = FetchType.LAZY,
			targetEntity = Usuario.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}			
			)
	@JoinColumn(name="eve_usuoid_finalizacao")
	private Usuario usuFinalizacao;

	@ManyToOne(	
			fetch = FetchType.LAZY,
			targetEntity = Usuario.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}			
			)
	@JoinColumn(name="eve_usuoid_cancelamento")
	private Usuario usuCancelamento;
	
	@Column(name="eve_dt_criacao")
	private Date dtCriacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="eve_dt_alteracao")
	private Date dtAlteracao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="eve_dt_finalizacao")
	private Date dtFinalizacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="eve_dt_cancelamento")
	private Date dtCancelamento;
	
	@Column(name="eve_emails")
	private String emails;

	public Evento() {}
	public Evento(Integer id) {this.id = id;}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public SincViagem getSincViagem() {
		return sincViagem;
	}
	public void setSincViagem(SincViagem sincViagem) {
		this.sincViagem = sincViagem;
	}
	public Integer getNrEvento() {
		return nrEvento;
	}
	public void setNrEvento(Integer nrEvento) {
		this.nrEvento = nrEvento;
	}
	public Integer getNrBoletim() {
		return nrBoletim;
	}
	public void setNrBoletim(Integer nrBoletim) {
		this.nrBoletim = nrBoletim;
	}
	public Integer getNrBoletimSuspeita() {
		return nrBoletimSuspeita;
	}
	public void setNrBoletimSuspeita(Integer nrBoletimSuspeita) {
		this.nrBoletimSuspeita = nrBoletimSuspeita;
	}
	public Integer getNrBoletimPerda() {
		return nrBoletimPerda;
	}
	public void setNrBoletimPerda(Integer nrBoletimPerda) {
		this.nrBoletimPerda = nrBoletimPerda;
	}
	public Integer getNrBoletimDano() {
		return nrBoletimDano;
	}
	public void setNrBoletimDano(Integer nrBoletimDano) {
		this.nrBoletimDano = nrBoletimDano;
	}
	public Character getTipoInicial() {
		return tipoInicial;
	}
	public void setTipoInicial(Character tipoInicial) {
		this.tipoInicial = tipoInicial;
	}
	public Character getTipoFinal() {
		return tipoFinal;
	}
	public void setTipoFinal(Character tipoFinal) {
		this.tipoFinal = tipoFinal;
	}	
	public BoletimInformativo getBoletimInformativo() {
		return boletimInformativo;
	}
	public void setBoletimInformativo(BoletimInformativo boletimInformativo) {
		this.boletimInformativo = boletimInformativo;
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
	public Usuario getUsuFinalizacao() {
		return usuFinalizacao;
	}
	public void setUsuFinalizacao(Usuario usuFinalizacao) {
		this.usuFinalizacao = usuFinalizacao;
	}
	public Usuario getUsuCancelamento() {
		return usuCancelamento;
	}
	public void setUsuCancelamento(Usuario usuCancelamento) {
		this.usuCancelamento = usuCancelamento;
	}
	public Date getDtCriacao() {
		return dtCriacao;
	}
	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}
	public Date getDtAlteracao() {
		return dtAlteracao;
	}
	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}
	public Date getDtFinalizacao() {
		return dtFinalizacao;
	}
	public void setDtFinalizacao(Date dtFinalizacao) {
		this.dtFinalizacao = dtFinalizacao;
	}
	public Date getDtCancelamento() {
		return dtCancelamento;
	}
	public void setDtCancelamento(Date dtCancelamento) {
		this.dtCancelamento = dtCancelamento;
	}
	public String getEmails() {
		return emails;
	}
	public void setEmails(String emails) {
		this.emails = emails;
	}
	
	
}
