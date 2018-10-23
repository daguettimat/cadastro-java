package br.com.global5.manager.model.geral;

import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "quadro_de_avisos")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Avisos implements BaseEntity {

    /*

    create table quadro_de_avisos
    (
      qavsoid serial not null
        constraint quadro_de_avisos_pkey
        primary key,
      qavs_nome text not null,
      qavs_documento text not null,
      qavs_dt_inicio timestamp,
      qavs_dt_validade timestamp,
      qavs_expirado boolean,
      qavs_usuoid_criacao integer,
      qavs_usuoid_alteracao integer,
      qavs_usuoid_exclusao integer,
      qavs_dt_criacao timestamp,
      qavs_dt_exclusao timestamp,
      qavs_dt_alteracao timestamp
    )
    ;

     */

    @Id
    @SequenceGenerator(name = "quadro_de_avisos_qavsoid_seq", sequenceName = "quadro_de_avisos_qavsoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "quadro_de_avisos_qavsoid_seq")
    @Column(name = "qavsoid")
    private Integer id;

    @Column(name = "qavs_nome")
    private String nome;

    @Column(name = "qavs_documento")
    private String documento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "qavs_dt_inicio")
    private Date dtInicial;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "qavs_dt_validade")
    private Date dtFinal;

    @Column(name = "qavs_expirado")
    private boolean expirado;

    @Column(name = "qavs_dt_criacao")
    private Date dtCriacao;

    @Column(name = "qavs_dt_alteracao")
    private Date dtAlteracao;

    @Column(name = "qavs_dt_exclusao")
    private Date dtExclusao;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="qavs_usuoid_criacao")
    private Usuario usuarioCriacao;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="qavs_usuoid_alteracao")
    private Usuario usuarioAlteracao;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="qavs_usuoid_exclusao")
    private Usuario usuarioExclusao;

    public Avisos() {}

    public Avisos(int id) {
        this.id = id;
    }

    public Avisos nome(String nome) {
        this.nome = nome;
        return this;
    }

    public boolean hasNome() {
        return nome != null && !"".equals(nome.trim());
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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

    public boolean isExpirado() {
        return expirado;
    }

    public void setExpirado(boolean expirado) {
        this.expirado = expirado;
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
}
