package br.com.global5.manager.model.geral;

import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@Entity
@Table(name = "banco")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Banco implements BaseEntity {

    @Id
    @SequenceGenerator(name="banco_banoid_seq",
            sequenceName="banco_banoid_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="banco_banoid_seq")
    @Column(name = "banoid")
    private Long id;

    @Column(name = "bannome")
    private String descricao;

    @Column(name = "banexclusao")
    private java.sql.Timestamp exclusao;

    public Banco() {
    }

    public Banco(Long id, String descricao, Timestamp exclusao) {
        this.id = id;
        this.descricao = descricao;
        this.exclusao = exclusao;
    }

    public Banco(Long id) {
        this.id = id;
    }

    public Banco descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public boolean hasDescricao() {
        return descricao != null && !"".equals(descricao.trim());
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

    public Timestamp getExclusao() {
        return exclusao;
    }

    public void setExclusao(Timestamp exclusao) {
        this.exclusao = exclusao;
    }
}
