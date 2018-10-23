package br.com.global5.manager.model.geral;


import br.com.global5.infra.model.BaseEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@Entity
@Table(name = "recomendacao")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Recomendacao implements BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "recoid")
    private Long id;

    @Column(name = "redescricao")
    private String descricao;

    @Column(name = "recadastro")
    private java.sql.Timestamp cadastro;

    @Column(name = "reexclusao")
    private java.sql.Timestamp exclusao;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "reusuexclusao")
    private Usuario usuarioExclusao;

    public Recomendacao descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public boolean hasDescricao() {
        return descricao != null && !"".equals(descricao.trim());
    }

    public Recomendacao() {}

    public Recomendacao(String descricao, Timestamp cadastro, Timestamp exclusao, Usuario usuarioExclusao) {
        this.descricao = descricao;
        this.cadastro = cadastro;
        this.exclusao = exclusao;
        this.usuarioExclusao = usuarioExclusao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Timestamp getCadastro() {
        return cadastro;
    }

    public void setCadastro(Timestamp cadastro) {
        this.cadastro = cadastro;
    }

    public Timestamp getExclusao() {
        return exclusao;
    }

    public void setExclusao(Timestamp exclusao) {
        this.exclusao = exclusao;
    }

    public Usuario getUsuarioExclusao() {
        return usuarioExclusao;
    }

    public void setUsuarioExclusao(Usuario usuarioExclusao) {
        this.usuarioExclusao = usuarioExclusao;
    }
}
