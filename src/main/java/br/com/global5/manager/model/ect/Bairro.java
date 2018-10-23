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
@Table(name = "ect_bairro")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Bairro implements BaseEntity {


    @Id
    @SequenceGenerator(name="ect_bairro_baioid_seq",
            sequenceName="ect_bairro_baioid_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="ect_bairro_baioid_seq")
    @Column(name = "baioid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Cidade.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="bai_cidoid")
    private Cidade cidade;

    @Column(name = "bai_nome")
    private String nome;

    public Bairro() {
    }

    public Bairro(Cidade cidade, String nome) {
        this.cidade = cidade;
        this.nome = nome;
    }

    public Bairro(Integer id) {
        this.id = id;
    }

    public Bairro nome(String nome) {
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

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
