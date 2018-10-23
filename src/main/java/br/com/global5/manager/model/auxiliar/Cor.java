package br.com.global5.manager.model.auxiliar;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "cor")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cor implements BaseEntity {


    @Id
    @SequenceGenerator(name = "cor_coroid_seq", sequenceName = "cor_coroid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "cor_coroid_seq")
    @Column(name = "coroid")
    private Integer id;

    @Column(name = "cor_nome")
    private String nome;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "cor_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "cor_dt_alteracao")
    private Date dtAlteracao;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "cor_dt_exclusao")
    private Date dtExclusao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="cor_usuoid_criacao")
    private Usuario usuCriacao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="cor_usuoid_alteracao")
    private Usuario usuAlteracao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="cor_usuoid_exclusao")
    private Usuario usuExclusao;

    public Cor() {
    }
    
    public Cor (Integer id){this.id = id;}
    
    public Cor nome(String nome) {
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

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Date dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public Usuario getUsuCriacao() {
        return usuCriacao;
    }

    public void setUsuCriacao(Usuario usuCriacao) {
        this.usuCriacao = usuCriacao;
    }

    public Usuario getUsuAlteracao() {
        return usuAlteracao;
    }

    public void setUsuAlteracao(Usuario usuAlteracao) {
        this.usuAlteracao = usuAlteracao;
    }

    public Usuario getUsuExclusao() {
        return usuExclusao;
    }

    public void setUsuExclusao(Usuario usuExclusao) {
        this.usuExclusao = usuExclusao;
    }
}
