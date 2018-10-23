package br.com.global5.manager.model.externos;

import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.json.JsonStringType;
import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Produto;
import br.com.global5.manager.model.criminal.Consulta;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "consulta_pessoa")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class ConsultaPessoa implements BaseEntity {


    @Id
    @SequenceGenerator(name = "consulta_pessoa_conpfoid_seq",
            sequenceName = "consulta_pessoa_conpfoid_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "consulta_pessoa_conpfoid_seq")
    @Column(name = "conpfoid")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "conpf_dt_consulta")
    private Date dtConsulta;

    @Column(name = "conpf_codigo_solicitacao")
    private Integer codigoSolicitacao;

    @Column(name = "conpf_requisicao")
    private String requisicao;

//    @Type(type = "jsonb")
//    @Column(name = "conpf_resposta", columnDefinition = "jsonb")
//    private Consulta resposta;

    @Column(name = "conpf_resposta")
    private String consulta;

    @Temporal(TemporalType.DATE)
    @Column(name = "conpf_dt_resposta")
    private Date dtResposta;

    @Column(name = "conpf_nivel")
    private Integer nivel;

    @Column(name = "conpf_cpf")
    private String cpf;

    @Column(name = "conpf_nome")
    private String nome;

    @Column(name = "conpf_rg")
    private String rg;

    @Column(name = "conpf_rguf")
    private String rgUF;

    @Temporal(TemporalType.DATE)
    @Column(name = "conpf_dt_nascimento")
    private Date dtNascimento;

    @Column(name = "conpf_artigo_primeiro")
    private Integer artigoPrimeiro;

//    @Column(name = "conpf_artigos")
//    private Integer artigos;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Produto.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="conpf_origem_prodoid")
    private Produto origemProduto;

    @Column(name = "conpf_origem_oid")
    private Integer origem;

    @Column(name = "conpf_origem_parte_oid")
    private Integer origemParte;

    public ConsultaPessoa() {}

    public ConsultaPessoa nome(String nome) {
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

    public Date getDtConsulta() {
        return dtConsulta;
    }

    public void setDtConsulta(Date dtConsulta) {
        this.dtConsulta = dtConsulta;
    }

    public Integer getCodigoSolicitacao() {
        return codigoSolicitacao;
    }

    public void setCodigoSolicitacao(Integer codigoSolicitacao) {
        this.codigoSolicitacao = codigoSolicitacao;
    }

    public String getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(String requisicao) {
        this.requisicao = requisicao;
    }

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public Date getDtResposta() {
        return dtResposta;
    }

    public void setDtResposta(Date dtResposta) {
        this.dtResposta = dtResposta;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getRgUF() {
        return rgUF;
    }

    public void setRgUF(String rgUF) {
        this.rgUF = rgUF;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public Integer getArtigoPrimeiro() {
        return artigoPrimeiro;
    }

    public void setArtigoPrimeiro(Integer artigoPrimeiro) {
        this.artigoPrimeiro = artigoPrimeiro;
    }

//    public Integer getArtigos() {
//        return artigos;
//    }
//
//    public void setArtigos(Integer artigos) {
//        this.artigos = artigos;
//    }

    public Produto getOrigemProduto() {
        return origemProduto;
    }

    public void setOrigemProduto(Produto origemProduto) {
        this.origemProduto = origemProduto;
    }

    public Integer getOrigem() {
        return origem;
    }

    public void setOrigem(Integer origem) {
        this.origem = origem;
    }

    public Integer getOrigemParte() {
        return origemParte;
    }

    public void setOrigemParte(Integer origemParte) {
        this.origemParte = origemParte;
    }
}
