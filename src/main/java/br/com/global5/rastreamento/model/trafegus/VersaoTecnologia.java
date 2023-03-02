package br.com.global5.rastreamento.model.trafegus;

import java.util.List;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.rastreamento.model.enums.Tecnologia;

@Entity
@Table(name = "rastreamento.versao_tecnologia")
public class VersaoTecnologia implements BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @SequenceGenerator(name = "versao_tecnologia_vstoid_seq", sequenceName = "versao_tecnologia_vstoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "versao_tecnologia_vstoid_seq")
	@Column(name="vstoid")
	private Integer id;
	
	@Column(name="vst_codigo")
	private Integer idTecnologia;
	
	@Column(name="vst_descricao")  
	private String descricao;
	
	@Column(name="vst_versao")
	private String versao;
	
	@Column(name="vst_tecnologia")
	private String tecnologia;
	
    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Transacao.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "vst_trdboid")
    private Transacao transacao;
	
	@JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Tecnologia.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "vst_enumoid_tecnologia")
	private Tecnologia deParaTecnologia; 
	

	
	public VersaoTecnologia(){}
	
	public VersaoTecnologia(Integer id){this.id = id;}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getIdTecnologia() {
		return idTecnologia;
	}


	public void setIdTecnologia(Integer idTecnologia) {
		this.idTecnologia = idTecnologia;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getVersao() {
		return versao;
	}


	public void setVersao(String versao) {
		this.versao = versao;
	}


	public String getTecnologia() {
		return tecnologia;
	}


	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}


	public Transacao getTransacao() {
		return transacao;
	}


	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}


	public Tecnologia getDeParaTecnologia() {
		return deParaTecnologia;
	}


	public void setDeParaTecnologia(Tecnologia deParaTecnologia) {
		this.deParaTecnologia = deParaTecnologia;
	}


	
	
}
