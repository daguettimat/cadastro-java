package br.com.global5.rastreamento.model.trafegus;

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

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.rastreamento.model.bi.Evento;
import br.com.global5.rastreamento.model.bi.EventoInfoViagem;

@Entity
@Table(name="rastreamento.evento_info_rastreador")
public class EventoInfoRastreador implements BaseEntity {
	
	@Id 
    @SequenceGenerator(name = "evento_info_rastreador_eiroid_seq", sequenceName = "evento_info_rastreador_eiroid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "evento_info_rastreador_eiroid_seq")
	@Column(name="eiroid")
	private Integer id;
	
	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = EventoInfoViagem.class,
			cascade = {CascadeType.DETACH, CascadeType.MERGE}
		)
	@JoinColumn(name="eir_evevoid")
	private EventoInfoViagem evInfoViagem;
	
	@Column(name="eir_codigo")
	private String codigo;
	
	@Column(name="eir_descricao")
	private String descricao;
	
	@Column(name="eir_telemonitorado")
	private String telemonitorado;
	
	@Column(name="eir_numero_terminal")
	private String numeroTerminal;
	
	@Column(name="eir_vtec_codigo")
	private String vtecCodigo;	
	
	@Column(name="eir_isca")
	private String isca;
	
	public EventoInfoRastreador(){}
	
	public EventoInfoRastreador(Integer id){
		this.id = id;
	}

	public EventoInfoRastreador(int id, String termCodigo, String termDescricao, String termTelemonitorado,
			String termNumTerminal, String termVtecCodigo, String termIsca) {
		this.id = id;
		this.codigo = termCodigo;
		this.descricao = termDescricao;
		this.telemonitorado = termTelemonitorado;
		this.numeroTerminal = termNumTerminal;
		this.vtecCodigo = termVtecCodigo;
		this.isca = termIsca;
	}

	public Integer getId() {
		return id;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getNumeroTerminal() {
		return numeroTerminal;
	}
	
	public String getTelemonitorado() {
		return telemonitorado;
	}

	public String getVtecCodigo() {
		return vtecCodigo;
	}

	public String getIsca() {
		return isca;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setTelemonitorado(String telemonitorado) {
		this.telemonitorado = telemonitorado;
	}

	public void setNumeroTerminal(String numeroTerminal) {
		this.numeroTerminal = numeroTerminal;
	}

	public void setVtecCodigo(String vtecCodigo) {
		this.vtecCodigo = vtecCodigo;
	}

	public void setIsca(String isca) {
		this.isca = isca;
	}

	public EventoInfoViagem getEvInfoViagem() {
		return evInfoViagem;
	}

	public void setEvInfoViagem(EventoInfoViagem evInfoViagem) {
		this.evInfoViagem = evInfoViagem;
	}
	
	
	
}
