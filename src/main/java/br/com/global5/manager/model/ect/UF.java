package br.com.global5.manager.model.ect;

import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by zielinski on 18/03/17.
 */
@Entity
@Table(name = "ect_uf")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UF implements BaseEntity {

    @Id
    @SequenceGenerator(name="ect_uf_ufoid_seq",
            sequenceName="ect_uf_ufoid_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="ect_uf_ufoid_seq")
    @Column(name = "ufoid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Pais.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="uf_paisoid")
    private Pais pais;

    @Column(name = "uf_nome")
    private String nome;

    @Column(name = "uf_sigla")
    private String sigla;

    public UF() {}

    public UF(Pais pais, String nome, String sigla) {
        this.pais = pais;
        this.nome = nome;
        this.sigla = sigla;
    }

    public UF(Integer Id) {
        this.id = id;
    }

    public UF nome(String nome) {
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

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }


}
