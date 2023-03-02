package br.com.global5.rastreamento.model.bi;

import java.util.Date;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;

@Entity
@Table(name="rastreamento.boletim_informativo_relato")
public class BoletimInformativoRelato implements BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "boletim_informativo_relato_biroid_seq", sequenceName = "boletim_informativo_relato_biroid_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boletim_informativo_relato_biroid_seq")
	@Column(name="biroid")
	private Integer id ;

	@ManyToOne(	fetch = FetchType.EAGER,
			targetEntity = Evento.class,
			cascade={CascadeType.DETACH, CascadeType.MERGE})
	@JoinColumn(name="bir_eveoid")
	private Evento evento;

	@ManyToOne(
			fetch = FetchType.EAGER,
			targetEntity = BoletimInformativo.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}
		)
	@JoinColumn(name="bir_bioid")
	private BoletimInformativo boletimInformativo;
	
	@Column(name="bir_relato")
	private String relato;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bir_dt_criacao")
	private Date dtCriacao;
		
	@Column(name="bir_bi_tipo")
	private String tipo;



	public BoletimInformativoRelato() {}
	
	public BoletimInformativoRelato(Integer id) {this.id = id;}
	
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

	public BoletimInformativo getBoletimInformativo() {
		return boletimInformativo;
	}

	public void setBoletimInformativo(BoletimInformativo boletimInformativo) {
		this.boletimInformativo = boletimInformativo;
	}

	public String getRelato() {
		return relato;
	}

	public void setRelato(String relato) {
		this.relato = relato;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
