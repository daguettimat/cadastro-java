package br.com.global5.manager.model.cadastro;


import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "ficha_email")

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FichaEmail implements BaseEntity {

    @Id
    @SequenceGenerator(name = "ficha_email_ficeoid_seq", sequenceName = "ficha_email_ficeoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ficha_email_ficeoid_seq")
    @Column(name = "ficeoid")
    private Integer id;

    @Column(name = "fice_nome")
    private String nome;

    @Column(name = "fice_documento")
    private String documento;

    @Column(name = "fice_responsavel")
    private String responsavel;

    @Column(name = "fice_email")
    private String email;

    @Column(name = "fice_telefone")
    private String telefone;

    @Column(name = "fice_validacao")
    private String validacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fice_dt_email")
    private Date dtEmail;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fice_dt_retorno")
    private Date dtRetorno;

    @Column(name = "fice_concluido")
    private boolean concluido;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="fice_usuoid_criacao")
    private Usuario usuarioCriacao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="fice_usuoid_validacao")
    private Usuario usuarioValidacao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="fice_usuoid_alteracao")
    private Usuario usuarioAlteracao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="fice_usuoid_exclusao")
    private Usuario usuarioExclusao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fice_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fice_dt_validacao")
    private Date dtValidacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fice_dt_exclusao")
    private Date dtExclusao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fice_dt_alteracao")
    private Date dtAlteracao;

    public FichaEmail() {}

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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getValidacao() {
        return validacao;
    }

    public void setValidacao(String validacao) {
        this.validacao = validacao;
    }

    public Date getDtEmail() {
        return dtEmail;
    }

    public void setDtEmail(Date dtEmail) {
        this.dtEmail = dtEmail;
    }

    public Date getDtRetorno() {
        return dtRetorno;
    }

    public void setDtRetorno(Date dtRetorno) {
        this.dtRetorno = dtRetorno;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public Usuario getUsuarioCriacao() {
        return usuarioCriacao;
    }

    public void setUsuarioCriacao(Usuario usuarioCriacao) {
        this.usuarioCriacao = usuarioCriacao;
    }

    public Usuario getUsuarioValidacao() {
        return usuarioValidacao;
    }

    public void setUsuarioValidacao(Usuario usuarioValidacao) {
        this.usuarioValidacao = usuarioValidacao;
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

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtValidacao() {
        return dtValidacao;
    }

    public void setDtValidacao(Date dtValidacao) {
        this.dtValidacao = dtValidacao;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public Date getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Date dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }
}
