package br.com.global5.manager.model.analise;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.cadastro.Produto;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "pesquisa_pessoa")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class acPesquisaPessoa implements BaseEntity {

    @Id
    @SequenceGenerator(name = "pesquisas_pesqoid_seq", sequenceName = "pesquisas_pesqoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "pesquisas_pesqoid_seq")
    @Column(name = "pesqoid")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pesq_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pesq_dt_resposta")
    private Date dtResposta;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="pesq_usuoid_consulta")
    private Usuario usuConsulta;

    @Column(name = "pesq_aprovado")
    private boolean aprovado;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Produto.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="pesq_prodoid")
    private Produto idProduto;

    @Column(name = "pesq_valor")
    private Double valor;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=acConsultaPessoa.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="pesq_id_consulta")
    private acConsultaPessoa consulta;

    @Column(name = "pesqp_nome")
    private String nome;

    @Column(name = "pesqp_cpf")
    private String cpf;

    @Column(name = "pesqp_rg")
    private String rg;

    @Column(name = "pesqp_uf_rg")
    private String ufRg;

    @Temporal(TemporalType.DATE)
    @Column(name = "pesqp_dt_nascimento")
    private Date dtNascimento;

    @Column(name = "pesqp_nomepai")
    private String nomePai;

    @Column(name = "pesqp_nomemae")
    private String nomeMae;

    @Column(name = "pesqp_cidade")
    private String cidade;

    @Column(name = "pesqp_uf")
    private String uf;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Area.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="pesq_cliente_areaoid")
    private Area clienteArea;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Area.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="pesq_filial_areaoid")
    private Area filialArea;

    @Transient
    @Temporal(TemporalType.DATE)
    private Date dtInicial;

    @Transient
    @Temporal(TemporalType.DATE)
    private Date dtFinal;

    public acPesquisaPessoa() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtResposta() {
        return dtResposta;
    }

    public void setDtResposta(Date dtResposta) {
        this.dtResposta = dtResposta;
    }

    public Usuario getUsuConsulta() {
        return usuConsulta;
    }

    public void setUsuConsulta(Usuario usuConsulta) {
        this.usuConsulta = usuConsulta;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public Produto getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Produto idProduto) {
        this.idProduto = idProduto;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public acConsultaPessoa getConsulta() {
        return consulta;
    }

    public void setConsulta(acConsultaPessoa consulta) {
        this.consulta = consulta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getUfRg() {
        return ufRg;
    }

    public void setUfRg(String ufRg) {
        this.ufRg = ufRg;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Area getClienteArea() {
        return clienteArea;
    }

    public void setClienteArea(Area clienteArea) {
        this.clienteArea = clienteArea;
    }

    public Area getFilialArea() {
        return filialArea;
    }

    public void setFilialArea(Area filialArea) {
        this.filialArea = filialArea;
    }

    public Date getDtInicial() {
        return dtInicial;
    }

    public void setDtInicial(Date dtInicial) {
        this.dtInicial = dtInicial;
    }

    public Date getDtFinal() {
        return dtFinal;
    }

    public void setDtFinal(Date dtFinal) {
        this.dtFinal = dtFinal;
    }
}
