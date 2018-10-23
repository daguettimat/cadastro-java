package br.com.global5.manager.model.analise;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Veiculo;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "consulta_veiculo")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class acConsultaVeiculo implements BaseEntity {

    @Id
    @SequenceGenerator(name = "consulta_veiculo_convoid_seq", sequenceName = "consulta_veiculo_convoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "consulta_veiculo_convoid_seq")
    @Column(name = "convoid")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "conv_dt_consulta")
    private Date dtConsulta;

    @Column(name = "conv_codigo_solicitacao")
    private Integer idSolicitacao;

    @Column(name = "conv_requisicao")
    private String requisicao;

    @Column(name = "conv_resposta")
    private String resposta;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "conv_dt_resposta")
    private Date dtResposta;

    @Column(name = "conv_aprovado")
    private boolean aprovado;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Veiculo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "conv_veioid")
    private Veiculo veiculo;

    @Column(name = "conv_placa")
    private String conv_placa;

    @Column(name = "conv_proprietario_resultado")
    private boolean propResultado;

    @Column(name = "conv_antt_status")
    private String anttStatus;

    @Column(name = "conv_antt_resultado")
    private boolean anttResultado;

    @Column(name = "conv_antt_ocorrencias")
    private boolean anttOcorrencias;

    @Column(name = "conv_origem_prodoid")
    private Integer idOrigemProduto;

    @Column(name = "conv_origem_oid")
    private Integer idOrigem;

    @Column(name = "conv_origem_parte_oid")
    private Integer idOrigemParte;

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

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public String getConv_placa() {
        return conv_placa;
    }

    public void setConv_placa(String conv_placa) {
        this.conv_placa = conv_placa;
    }

    public boolean isPropResultado() {
        return propResultado;
    }

    public void setPropResultado(boolean propResultado) {
        this.propResultado = propResultado;
    }

    public String getAnttStatus() {
        return anttStatus;
    }

    public void setAnttStatus(String anttStatus) {
        this.anttStatus = anttStatus;
    }

    public boolean isAnttResultado() {
        return anttResultado;
    }

    public void setAnttResultado(boolean anttResultado) {
        this.anttResultado = anttResultado;
    }

    public boolean isAnttOcorrencias() {
        return anttOcorrencias;
    }

    public void setAnttOcorrencias(boolean anttOcorrencias) {
        try {
            this.anttOcorrencias = anttOcorrencias;
        } catch (Exception e) {
            this.anttOcorrencias = false;
        }
    }

    public Integer getIdOrigemProduto() {
        return idOrigemProduto;
    }

    public void setIdOrigemProduto(Integer idOrigemProduto) {
        this.idOrigemProduto = idOrigemProduto;
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
}
