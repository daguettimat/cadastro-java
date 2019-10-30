package br.com.global5.rastreamento.model.bi;

import java.util.Date;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.rastreamento.model.enums.TipoAnexo;
import br.com.global5.rastreamento.model.sinc.SincViagem;

@Entity
@Table(name = "evento_anexo")
public class EventoAnexo implements BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id 
	@SequenceGenerator(name = "evento_anexo_eveaoid_seq", sequenceName = "evento_anexo_eveaoid_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evento_anexo_eveaoid_seq")
	@Column(name="eveaoid")
	private Integer id;

	@ManyToOne
	@JoinColumn(name="evea_eveoid")
	private Evento evento;
	
	@Column(name="evea_eveoid_nr_evento")
	private Integer numEvento;

	@ManyToOne
	@JoinColumn(name="evea_viagoid")
	private SincViagem sincViagem;

	@ManyToOne(fetch = FetchType.EAGER,
			targetEntity = TipoAnexo.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}			
			)
	@JoinColumn(name="evea_enumoid_categoria")
	private TipoAnexo tipoAnexo;
	
	@Column(name="evea_arquivo_tipo")
	private String arquivoTipo;
	
	@Column(name="evea_arquivo_nome")
	private String arquivoNome;

	@Column(name="evea_descricao")
	private String descricao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="evea_dt_criacao")
	private Date dtCriacao;


	@ManyToOne(	
			fetch = FetchType.LAZY,
			targetEntity = Usuario.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}			
			)
	@JoinColumn(name="evea_usuoid_criacao")
	private Usuario usuCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="evea_dt_exclusao")
	private Date dtExclusao;

	@ManyToOne(	
			fetch = FetchType.LAZY,
			targetEntity = Usuario.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}			
			)
	@JoinColumn(name="evea_usuoid_exclusao")
	private Usuario usuExclusao;

	public EventoAnexo() {}
	
	public EventoAnexo(Integer id) {this.id = id;}

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}	

	public Integer getNumEvento() {
		return numEvento;
	}

	public void setNumEvento(Integer numEvento) {
		this.numEvento = numEvento;
	}

	public SincViagem getSincViagem() {
		return sincViagem;
	}

	public void setSincViagem(SincViagem sincViagem) {
		this.sincViagem = sincViagem;
	}

	public TipoAnexo getTipoAnexo() {
		return tipoAnexo;
	}

	public void setTipoAnexo(TipoAnexo tipoAnexo) {
		this.tipoAnexo = tipoAnexo;
	}

	public String getArquivoTipo() {
		return arquivoTipo;
	}

	public void setArquivoTipo(String arquivoTipo) {
		this.arquivoTipo = arquivoTipo;
	}

	public String getArquivoNome() {
		return arquivoNome;
	}

	public void setArquivoNome(String arquivoNome) {
		this.arquivoNome = arquivoNome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public Date getDtExclusao() {
		return dtExclusao;
	}

	public void setDtExclusao(Date dtExclusao) {
		this.dtExclusao = dtExclusao;
	}

	public Usuario getUsuExclusao() {
		return usuExclusao;
	}

	public void setUsuExclusao(Usuario usuExclusao) {
		this.usuExclusao = usuExclusao;
	}

}
