package br.com.global5.manager.model.cadastro;


import br.com.global5.infra.enumerator.TipoProduto;
import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.enums.ProdutoTipo;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "produto")
@Indexed(index = "produto")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Produto implements BaseEntity {



    @Id
    @SequenceGenerator(name = "produto_prodoid_seq", sequenceName = "produto_prodoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "produto_prodoid_seq")
    @Column(name = "prodoid")
    private Integer id;

    @Column(name = "prod_nome")
    private String nome;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ProdutoTipo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="prod_tipo")
    private ProdutoTipo tipo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prod_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prod_dt_exclusao")
    private Date dtExclusao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prod_dt_alteracao")
    private Date dtAlteracao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="prod_usuoid_criacao")
    private Usuario usuarioCriacao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="prod_usuoid_alteracao")
    private Usuario usuarioAlteracao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="prod_usuoid_exclusao")
    private Usuario usuarioExclusao;

    public Produto() {}

    public Produto(Integer id) {
        this.id = id;
    }

    public Produto nome(String nome) {
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
    /*
    @Transient
    public TipoProduto getTipo() {
        return TipoProduto.fromValue(tipo.toString());
    }
    public void setTipo(TipoProduto tipo) {
        this.tipo = tipo.toKey();
    }
    */

    public ProdutoTipo getTipo() {
        return tipo;
    }

    public void setTipo(ProdutoTipo tipo) {
        this.tipo = tipo;
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

    public Usuario getUsuarioCriacao() {
        return usuarioCriacao;
    }

    public void setUsuarioCriacao(Usuario usuarioCriacao) {
        this.usuarioCriacao = usuarioCriacao;
    }

    public Usuario getUsuarioAlteracao() {
        return usuarioAlteracao;
    }

    public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
        this.usuarioAlteracao = usuarioAlteracao;
    }

    public Usuario getUsuarioExclusao() {
        return usuarioExclusao;
    }

    public void setUsuarioExclusao(Usuario usuarioExclusao) {
        this.usuarioExclusao = usuarioExclusao;
    }
}

