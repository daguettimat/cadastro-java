package br.com.global5.manager.model.auxiliar;

import br.com.global5.infra.model.BaseEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tipo_regulacao")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TipoRegulacao implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tproid")
    private Long id;

    @Column(name = "tprdescricao")
    private String descricao;

    @Column(name = "tprtipo")
    private String tipo;


    public TipoRegulacao() {
    }

    public TipoRegulacao(Long id, String descricao, String tipo) {
        this.id = id;
        this.descricao = descricao;
        this.tipo = tipo;
    }

    public TipoRegulacao descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public boolean hasDescricao() {
        return descricao != null && !"".equals(descricao.trim());
    }

    public TipoRegulacao(Long id) {
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
