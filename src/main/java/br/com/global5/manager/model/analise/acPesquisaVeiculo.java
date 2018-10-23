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
@Table(name = "pesquisa_veiculo")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class acPesquisaVeiculo implements BaseEntity {

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
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "pesq_usuoid_consulta")
    private Usuario usuConsulta;

    @Column(name = "pesq_aprovado")
    private boolean aprovado;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Produto.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "pesq_prodoid")
    private Produto produto;

    @Column(name = "pesq_valor")
    private Double valor;

    @ManyToOne(
            fetch = FetchType.EAGER,
            targetEntity = acConsultaVeiculo.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "pesq_id_consulta")
    private acConsultaVeiculo consulta;

    @Column(name = "pesqv_placa")
    private String placa;

    @Column(name = "pesqv_chassi")
    private String chassi;

    @Column(name = "pesqv_uf")
    private String uf;

    @Column(name = "pesqv_doc_proprietario")
    private String docProprietario;

    @Column(name = "pesqv_renavam")
    private String renavam;

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

    public acPesquisaVeiculo() {}

    public acPesquisaVeiculo(Integer id) {
        this.id = id;
    }

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

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public acConsultaVeiculo getConsulta() {
        return consulta;
    }

    public void setConsulta(acConsultaVeiculo consulta) {
        this.consulta = consulta;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getDocProprietario() {
        return docProprietario;
    }

    public void setDocProprietario(String docProprietario) {
        this.docProprietario = docProprietario;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
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
