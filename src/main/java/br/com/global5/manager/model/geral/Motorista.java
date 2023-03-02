package br.com.global5.manager.model.geral;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Localizador;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.manager.model.enums.DocumentoTipo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import javax.persistence.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@Entity
@Table(name = "motorista")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)

@NamedQueries(value = { @NamedQuery( name = "Mot.findByDoc1",
                                     query = "SELECT mot FROM br.com.global5.manager.model.geral.Motorista mot WHERE mot.doc1 = :doc1"),
                        @NamedQuery( name = "Mot.findByDoc2",
                                     query = "SELECT mot FROM br.com.global5.manager.model.geral.Motorista mot WHERE mot.doc2 = :doc2")})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Motorista implements BaseEntity {

    @Transient
    public static final String FIND_BY_DOC1 = "Mot.findByDoc1";

    @Transient
    public static final String FIND_BY_DOC2 = "Mot.findByDoc2";

    @Id
    @SequenceGenerator(name = "motorista_motoid_seq", sequenceName = "motorista_motoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "motorista_motoid_seq")
    @Column(name = "motoid")
    private Integer id;

    @Column(name = "mot_nome")
    private String nome;

    @Column(name = "mot_documento1")
    private String doc1;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=DocumentoTipo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "mot_documento1_tipo")
    private DocumentoTipo doc1_tipo;

    @Column(name = "mot_documento1_emissor")
    private String doc1_emissor;

    @Column(name = "mot_documento1_emissao")
    private Date doc1_emissao;

    @Column(name = "mot_documento1_uf")
    private String doc1_uf;

    @Column(name = "mot_documento2")
    private String doc2;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=DocumentoTipo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "mot_documento2_tipo")
    private DocumentoTipo doc2_tipo;

    @Column(name = "mot_documento2_emissor")
    private String doc2_emissor;

    @Column(name = "mot_documento2_emissao")
    private Date doc2_emissao;

    @Column(name = "mot_documento2_uf")
    private String doc2_uf;

    @Column(name = "mot_dt_nascimento")
    private Date dtNascimento;

    @Column(name = "mot_naturalidade")
    private String naturalidade;

    @Column(name = "mot_nomepai")
    private String pai;

    @Column(name = "mot_nomemae")
    private String mae;

    @Column(name = "mot_nacional")
    private boolean nacional;

    @Column(name = "mot_obs")
    private String observacao;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=Localizador.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="mot_locoid")
    private Localizador localizador;

    @Column(name = "mot_dt_foto")
    private Date dtFoto;

    @Column(name = "mot_url_foto")
    private String urlFoto;

    @Column(name = "mot_mcnhoid")
    private Integer recCnh;

    @Column(name = "mot_dt_criacao")
    private Date dtCriacao;

    @Column(name = "mot_dt_alteracao")
    private Date dtAlteracao;

    @Column(name = "mot_dt_exclusao")
    private Date dtExclusao;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="mot_usuoid_criacao")
    private Usuario usuarioCriacao;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="mot_usuoid_alteracao")
    private Usuario usuarioAlteracao;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="mot_usuoid_exclusao")
    private Usuario usuarioExclusao;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=Telefone.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="mot_teloid")
    private Telefone telefone;

    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtInicial;

    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtFinal;

    @Transient
    private boolean mostrarExcluidos;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="mot_dt_atualiza_ws")
    private Date dtAtualizaWs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="mot_dt_verificacao_ws")
    private Date dtVerificacaoWs;
    
    /*
    @Column(name="mot_existe_registro_ws")
    private boolean existeRegistroWs;
    */
    public Motorista() {}

    public Motorista(Integer id) {
        this.id = id;
    }

    public Motorista nome(String nome) {
        this.nome = nome;
        return this;
    }

    public boolean hasNome() {
        return nome != null && !"".equals(nome.trim());
    }

    public static String getFindByDoc1() {
        return FIND_BY_DOC1;
    }

    public static String getFindByDoc2() {
        return FIND_BY_DOC2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDoc1() {
        return doc1;
    }

    public void setDoc1(String doc1) {
        this.doc1 = doc1;
        
    }

    public DocumentoTipo getDoc1_tipo() {
        return doc1_tipo;
    }

    public void setDoc1_tipo(DocumentoTipo doc1_tipo) {
        this.doc1_tipo = doc1_tipo;
    }

    public String getDoc1_emissor() {
        return doc1_emissor;
    }

    public void setDoc1_emissor(String doc1_emissor) {
        this.doc1_emissor = doc1_emissor;
    }

    public Date getDoc1_emissao() {
        return doc1_emissao;
    }

    public void setDoc1_emissao(Date doc1_emissao) {
        this.doc1_emissao = doc1_emissao;
    }

    public String getDoc2() {
        return doc2;
    }

    public void setDoc2(String doc2) {
        this.doc2 = doc2;
    }

    public DocumentoTipo getDoc2_tipo() {
        return doc2_tipo;
    }

    public void setDoc2_tipo(DocumentoTipo doc2_tipo) {
        this.doc2_tipo = doc2_tipo;
    }

    public String getDoc2_emissor() {
        return doc2_emissor;
    }

    public void setDoc2_emissor(String doc2_emissor) {
        this.doc2_emissor = doc2_emissor;
    }

    public Date getDoc2_emissao() {
        return doc2_emissao;
    }

    public void setDoc2_emissao(Date doc2_emissao) {
        this.doc2_emissao = doc2_emissao;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }

    public String getMae() {
        return mae;
    }

    public void setMae(String mae) {
        this.mae = mae;
    }

    public boolean isNacional() {
        return nacional;
    }

    public void setNacional(boolean nacional) {
        this.nacional = nacional;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Localizador getLocalizador() {
        return localizador;
    }

    public void setLocalizador(Localizador localizador) {
        this.localizador = localizador;
    }

    public Date getDtFoto() {
        return dtFoto;
    }

    public void setDtFoto(Date dtFoto) {
        this.dtFoto = dtFoto;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Integer getRecCnh() {
        return recCnh;
    }

    public void setRecCnh(Integer recCnh) {
        this.recCnh = recCnh;
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

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public Usuario getUsuarioCriacao() {
        return usuarioCriacao;
    }

    public void setUsuarioCriacao(Usuario usuarioCriacao) {
        this.usuarioCriacao = usuarioCriacao;
    }

    public Usuario getUsuarioAlteracao() {
        return usuarioAlteracao;
    }

    public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
        this.usuarioAlteracao = usuarioAlteracao;
    }

    public Usuario getUsuarioExclusao() {
        return usuarioExclusao;
    }

    public void setUsuarioExclusao(Usuario usuarioExclusao) {
        this.usuarioExclusao = usuarioExclusao;
    }

    public String getDoc1_uf() {
        return doc1_uf;
    }

    public void setDoc1_uf(String doc1_uf) {
        this.doc1_uf = doc1_uf;
    }

    public String getDoc2_uf() {
        return doc2_uf;
    }

    public void setDoc2_uf(String doc2_uf) {
        this.doc2_uf = doc2_uf;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public Date getDtInicial() {
        return dtInicial;
    }

    public void setDtInicial(Date dtInicial) {
        this.dtInicial = dtInicial;
    }

    public Date getDtFinal() {
        return dtFinal;
    }

    public void setDtFinal(Date dtFinal) {
        this.dtFinal = dtFinal;
    }

    public boolean isMostrarExcluidos() {
        return mostrarExcluidos;
    }

    public void setMostrarExcluidos(boolean mostrarExcluidos) {
        this.mostrarExcluidos = mostrarExcluidos;
    }

	public Date getDtAtualizaWs() {
		return dtAtualizaWs;
	}

	public void setDtAtualizaWs(Date dtAtualizaWs) {
		this.dtAtualizaWs = dtAtualizaWs;
	}

	public Date getDtVerificacaoWs() {
		return dtVerificacaoWs;
	}

	public void setDtVerificacaoWs(Date dtVerificacaoWs) {
		this.dtVerificacaoWs = dtVerificacaoWs;
	}
    
	
    
}
