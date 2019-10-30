package br.com.global5.manager.model.areas;


import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.json.JsonStringType;
import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Pessoa;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@Entity
@Table(name = "area")

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@NamedQueries(value = {
        @NamedQuery( name = "Area.findByUser",
                query = "SELECT a " +
                        "  FROM Usuario c, Pessoa p, Area a, AreaFuncao af" +
                        " WHERE c.exclusao is null" +
                        "   AND c.id = :usuario" +
                        "   AND p.id = a.id" +
                        "   AND a.id = af.id") } )
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Area implements BaseEntity {

    @Transient
    public static final String FIND_AREA =
            "Area.findByUser";

    @Id
    @SequenceGenerator(name = "area_areaoid_seq", sequenceName = "area_areaoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "area_areaoid_seq")
    @Column(name = "areaoid")
    private Integer id;

    @Column(name = "area_nome")
    private String nome;

    @Column(name = "area_dt_criacao")
    private java.sql.Timestamp dtCriacao;

    @Column(name = "area_dt_exclusao")
    private java.sql.Timestamp dtExclusao;

    @Column(name = "area_dt_alteracao")
    private java.sql.Timestamp dtAlteracao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "area_usuoid_criacao")
    private Usuario usuarioCriacao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "area_usuoid_alteracao")
    private Usuario usuarioAlteracao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "area_usuoid_exclusao")
    private Usuario usuarioExclusao;

    @Column(name = "area_interna")
    private boolean areaInterna;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Area.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "area_areaoid_pai")
    private Area root;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Pessoa.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "area_pesoid_responsavel")
    private Pessoa pessoaResponsavel;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = AreaNivel.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "area_anvloid")
    private AreaNivel nivel;

    @Column(name = "area_fin_gerar_pagamento")
    private boolean gerarPagamento;

    @Column(name = "area_fin_gerar_fatura")
    private boolean gerarFatura;

    @Column(name = "area_fin_areaoid_pagante")
    private Integer areaPagante;

    @Type(type = "jsonb")
    @Column(name = "area_dados_cliente", columnDefinition = "json")
    private String dadosCliente;

    @Type(type = "jsonb")
    @Column(name = "area_dados_seguradora", columnDefinition = "json")
    private String dadosSeguradora;

    @Type(type = "jsonb")
    @Column(name = "area_dados_corretora", columnDefinition = "json")
    private String dadosCorretora;

    @Type(type = "json")
    @Column(name = "area_dados_embarcador", columnDefinition = "json")
    private String dadosEmbarcador;

    @Column(name = "area_email")
    private String emails;


    public Area() {
    }

    public Area(Integer id) {
        this.id = id;
    }


    public Area nome(String nome) {
        this.nome = nome;
        return this;
    }

    public boolean hasNome() {
        return nome != null && !"".equals(nome.trim());
    }

    public static String getFindArea() {
        return FIND_AREA;
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

    public Timestamp getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Timestamp dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Timestamp getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Timestamp dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public Timestamp getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Timestamp dtAlteracao) {
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

    public boolean isAreaInterna() {
        return areaInterna;
    }

    public void setAreaInterna(boolean areaInterna) {
        this.areaInterna = areaInterna;
    }

    public Area getRoot() {
        return root;
    }

    public void setRoot(Area root) {
        this.root = root;
    }

    public Pessoa getPessoaResponsavel() {
        return pessoaResponsavel;
    }

    public void setPessoaResponsavel(Pessoa pessoaResponsavel) {
        this.pessoaResponsavel = pessoaResponsavel;
    }

    public AreaNivel getNivel() {
        return nivel;
    }

    public void setNivel(AreaNivel nivel) {
        this.nivel = nivel;
    }

    public boolean isGerarPagamento() {
        return gerarPagamento;
    }

    public void setGerarPagamento(boolean gerarPagamento) {
        this.gerarPagamento = gerarPagamento;
    }

    public boolean isGerarFatura() {
        return gerarFatura;
    }

    public void setGerarFatura(boolean gerarFatura) {
        this.gerarFatura = gerarFatura;
    }

    public Integer getAreaPagante() {
        return areaPagante;
    }

    public void setAreaPagante(Integer areaPagante) {
        this.areaPagante = areaPagante;
    }

    public String getDadosCliente() {
        return dadosCliente;
    }

    public void setDadosCliente(String dadosCliente) {
        this.dadosCliente = dadosCliente;
    }

    public String getDadosSeguradora() {
        return dadosSeguradora;
    }

    public void setDadosSeguradora(String dadosSeguradora) {
        this.dadosSeguradora = dadosSeguradora;
    }

    public String getDadosCorretora() {
        return dadosCorretora;
    }

    public void setDadosCorretora(String dadosCorretora) {
        this.dadosCorretora = dadosCorretora;
    }

    public String getDadosEmbarcador() {
        return dadosEmbarcador;
    }

    public void setDadosEmbarcador(String dadosEmbarcador) {
        this.dadosEmbarcador = dadosEmbarcador;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

}
