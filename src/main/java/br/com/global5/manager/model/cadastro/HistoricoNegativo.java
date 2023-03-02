package br.com.global5.manager.model.cadastro;

import java.util.Date;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.enums.DocumentoTipo;
import br.com.global5.manager.model.geral.Usuario;

@Entity
@Table(name = "historico_negativo")
public class HistoricoNegativo implements BaseEntity {
	
    @Id
    @SequenceGenerator(name = "historico_negativo_hnegoid_seq", sequenceName = "historico_negativo_hnegoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historico_negativo_hnegoid_seq")
    @Column(name = "hnegoid")
    private Integer id;
    
    @Column(name = "hneg_dt_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao;
    
    @Column(name = "hneg_dt_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtExclusao;
    
    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "hneg_usuoid_criacao")
    private Usuario usuCriacao;
    
    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "hneg_usuoid_exclusao")
    private Usuario usuExclusao;
    
    @Column(name = "hneg_nome")
    private String nome;
    
    @Column(name = "hneg_documento")
    private String documento;
    
    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=DocumentoTipo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "hneg_documento_tipo")
    private DocumentoTipo documentoTipo;
    
    @Column(name = "hneg_placa")
    private String placa;
    
    @Column(name = "hneg_observacao")
    private String observacao;
    
    public HistoricoNegativo(){}
    
    public HistoricoNegativo(Integer id){this.id = id;}

	public Integer getId() {
		return id;
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

	public String getNome() {
		return nome;
	}

	public String getDocumento() {
		return documento;
	}

	public DocumentoTipo getDocumentoTipo() {
		return documentoTipo;
	}

	public String getPlaca() {
		return placa;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public void setDocumentoTipo(DocumentoTipo documentoTipo) {
		this.documentoTipo = documentoTipo;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
    
}
