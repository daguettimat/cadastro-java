package br.com.global5.manager.model.cadastro;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Localizador;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "proprietario")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Proprietario implements BaseEntity {

    @Id
    @SequenceGenerator(name = "proprietario_seq", sequenceName = "proprietario_propoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "proprietario_seq")
    @Column(name = "propoid")
    private Integer id;

    @Column(name = "prop_documento")
    private String documento;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Localizador.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "prop_locoid")
    private Localizador endereco;

    @Column(name = "prop_nome")
    private String nome;

    @Column(name = "prop_nacional")
    private boolean nacional;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prop_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prop_dt_alteracao")
    private Date dtAlteracao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prop_dt_exclusao")
    private Date dtExclusao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "prop_usuoid_criacao")
    private Usuario usuCriacao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "prop_usuoid_alteracao")
    private Usuario usuAlteracao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "prop_usuoid_exclusao")
    private Usuario usuExclusao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Telefone.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "prop_teloid")
    private Telefone telefone;

    public Proprietario() {
    }


    public Proprietario(Integer id) {
        this.id = id;
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Localizador getEndereco() {
        return endereco;
    }

    public void setEndereco(Localizador endereco) {
        this.endereco = endereco;
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

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }
}
