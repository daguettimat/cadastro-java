package br.com.global5.manager.model.auxiliar;

import br.com.global5.infra.model.BaseEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tipo_motorista")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TipoMotorista implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tipmoid")
    private Long id;

    @Column(name = "tipmnome")
    private String descricao;

    public TipoMotorista() {
    }

    public TipoMotorista(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public TipoMotorista descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public boolean hasDescricao() {
        return descricao != null && !"".equals(descricao.trim());
    }

    public TipoMotorista(Long id) {
        this.id = id;
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
}
