package br.com.global5.manager.model.cadastro;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "marca")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Marca implements BaseEntity {

    @Id
    @SequenceGenerator(name = "marca_maroid_seq", sequenceName = "marca_maroid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "marca_maroid_seq")
    @Column(name = "maroid")
    private Integer id;

    @Column(name = "mar_nome")
    private String nome;

    @Column(name = "mar_nacional")
    private boolean nacional;

    @Column(name = "mar_estrangeira")
    private boolean estrangeira;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="mar_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="mar_dt_exclusao")
    private Date dtExclusao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="mar_dt_alteracao")
    private Date dtAlteracao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="mar_usuoid_criacao")
    private Usuario usuCriacao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="mar_usuoid_alteracao")
    private Usuario usuAlteracao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="mar_usuoid_exclusao")
    private Usuario usuExclusao;

    public Marca() {}

    public Marca(Integer id) {
        this.id = id;
    }

    public Marca Nome(String nome) {
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

    public boolean isNacional() {
        return nacional;
    }

    public void setNacional(boolean nacional) {
        this.nacional = nacional;
    }

    public boolean isEstrangeira() {
        return estrangeira;
    }

    public void setEstrangeira(boolean estrangeira) {
        this.estrangeira = estrangeira;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public Date getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Date dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
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
