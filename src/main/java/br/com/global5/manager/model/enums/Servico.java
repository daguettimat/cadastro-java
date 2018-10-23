package br.com.global5.manager.model.enums;

import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@Entity
@Table(name = "enum_servico")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Servico implements BaseEntity {

    @Id
    @SequenceGenerator(name="enum_sistema_enumoid_seq",
            sequenceName="enum_sistema_enumoid_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="enum_sistema_enumoid_seq")
    @Column(name = "enumoid")
    private Integer id;

    @Column(name = "enum_nome")
    private String descricao;

    @Column(name = "enum_dt_exclusao")
    private Timestamp exclusao;

    public Servico() {
    }

    public Servico(Integer id, String descricao, Timestamp exclusao) {
        this.id = id;
        this.descricao = descricao;
        this.exclusao = exclusao;
    }

    public Servico(Integer id) {
        this.id = id;
    }

    public Servico descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public boolean hasDescricao() {
        return descricao != null && !"".equals(descricao.trim());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
