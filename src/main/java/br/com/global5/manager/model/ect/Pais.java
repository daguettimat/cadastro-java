package br.com.global5.manager.model.ect;

import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "ect_pais")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pais implements BaseEntity {

    @Id
    @SequenceGenerator(name="ect_pais_paisoid_seq",
            sequenceName="ect_pais_paisoid_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="ect_pais_paisoid_seq")
    @Column(name = "paisoid")
    private Integer id;

    @Column(name = "pais_nome")
    private String nome;

    public Pais() {
    }

    public Pais(Integer id, String nome) {
        this.id = id;
        this.nome = nome;

    }

    public Pais(Integer id) {
        this.id = id;
    }

    public Pais nome(String nome) {
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

}
