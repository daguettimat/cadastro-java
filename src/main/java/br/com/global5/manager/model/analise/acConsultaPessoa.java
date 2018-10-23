package br.com.global5.manager.model.analise;

import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.json.JsonStringType;
import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "consulta_pessoa")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class acConsultaPessoa implements BaseEntity {


    @Id
    @SequenceGenerator(name = "consulta_pessoa_conpfoid_seq", sequenceName = "consulta_pessoa_conpfoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "consulta_pessoa_conpfoid_seq")
    @Column(name = "conpfoid")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "conpf_dt_consulta")
    private Date dtConsulta;

    @Column(name = "conpf_codigo_solicitacao")
    private Integer idSolicitacao;

    @Column(name = "conpf_requisicao")
    private String requisicao;

    @Column(name = "conpf_resposta")
    private String resposta;

    @Temporal(TemporalType.TIMESTAMP)
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
    private String rgUf;

    @Temporal(TemporalType.DATE)
    @Column(name = "conpf_dt_nascimento")
    private Date dtNascimento;

    @Column(name = "conpf_artigo_primeiro")
    private Integer artigoPrimeiro;

    @Column(name = "conpf_origem_prodoid")
    private Integer origemIdProduto;

    @Column(name = "conpf_origem_oid")
    private Integer idOrigem;

    @Column(name = "conpf_origem_parte_oid")
    private Integer idOrigemParte;

    @Column(name = "conpf_nadaconsta")
    private boolean nadaConsta;

    public acConsultaPessoa() {}

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

    public Integer getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(Integer idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public String getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(String requisicao) {
        this.requisicao = requisicao;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
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

    public String getRgUf() {
        return rgUf;
    }

    public void setRgUf(String rgUf) {
        this.rgUf = rgUf;
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

    public Integer getOrigemIdProduto() {
        return origemIdProduto;
    }

    public void setOrigemIdProduto(Integer origemIdProduto) {
        this.origemIdProduto = origemIdProduto;
    }

    public Integer getIdOrigem() {
        return idOrigem;
    }

    public void setIdOrigem(Integer idOrigem) {
        this.idOrigem = idOrigem;
    }

    public Integer getIdOrigemParte() {
        return idOrigemParte;
    }

    public void setIdOrigemParte(Integer idOrigemParte) {
        this.idOrigemParte = idOrigemParte;
    }

    public boolean isNadaConsta() {
        return nadaConsta;
    }

    public void setNadaConsta(boolean nadaConsta) {
        this.nadaConsta = nadaConsta;
    }
}
