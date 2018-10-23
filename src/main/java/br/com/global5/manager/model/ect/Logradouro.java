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
@Table(name = "ect_logradouro")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Logradouro implements BaseEntity {

    @Id
    @SequenceGenerator(name="ect_logradouro_logroid_seq",
            sequenceName="ect_logradouro_logroid_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="ect_logradouro_logroid_seq")
    @Column(name = "logroid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Cidade.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="logr_cidoid")
    private Cidade cidade;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Bairro.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="logr_baioid_ini")
    private Bairro iniBairro;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Bairro.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="logr_baioid_fim")
    private Bairro fimBairro;

    @Column(name = "logr_tipo")
    private String tipo;

    @Column(name = "logr_nome")
    private String nome;

    @Column(name = "logr_cep")
    private String cep;

    public Logradouro() {}

    public Logradouro(Cidade cidade, Bairro iniBairro, Bairro fimBairro, String tipo,
                      String nome, String cep) {
        this.cidade = cidade;
        this.iniBairro = iniBairro;
        this.fimBairro = fimBairro;
        this.tipo = tipo;
        this.nome = nome;
        this.cep = cep;
    }

    public Logradouro nome(String nome) {
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

    public Bairro getIniBairro() {
        return iniBairro;
    }

    public void setIniBairro(Bairro iniBairro) {
        this.iniBairro = iniBairro;
    }

    public Bairro getFimBairro() {
        return fimBairro;
    }

    public void setFimBairro(Bairro fimBairro) {
        this.fimBairro = fimBairro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
