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
@Table(name = "ect_cidade")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cidade implements BaseEntity {

    @Id
    @SequenceGenerator(name="ect_cidade_cidoid_seq",
            sequenceName="ect_cidade_cidoid_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="ect_cidade_cidoid_seq")
    @Column(name = "cidoid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Pais.class,
            cascade={CascadeType.ALL}
    )
    @JoinColumn(name="cid_paisoid")
    private Pais pais;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=UF.class,
            cascade={CascadeType.ALL}
    )
    @JoinColumn(name="cid_ufoid")
    private UF uf;

    @Column(name = "cid_nome")
    private String nome;

    public Cidade() {}

    public Cidade( Integer id ) {
        this.id = id;
    }

    public Cidade(Pais pais, UF uf, String nome) {
        this.pais = pais;
        this.uf = uf;
        this.nome = nome;
    }

    public Cidade nome(String nome) {
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

    public UF getUf() {
        return uf;
    }

    public void setUf(UF uf) {
        this.uf = uf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
