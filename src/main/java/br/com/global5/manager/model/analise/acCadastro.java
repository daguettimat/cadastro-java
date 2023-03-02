package br.com.global5.manager.model.analise;

import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.json.JsonStringType;
import br.com.global5.infra.model.BaseEntity;
import br.com.global5.infra.model.TipoValor;
import br.com.global5.manager.model.enums.FichaStatus;
import br.com.global5.manager.model.enums.FichaTipo;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.model.areas.Area;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "java.analise_cadastral")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@NamedQueries(value = {
        @NamedQuery( name = "cadastro.findMaxDateTransportadora",
                    query = "SELECT Max(dtCriacao) FROM acCadastro cad WHERE cad.clienteArea.id = :id"),
        @NamedQuery( name = "cadastro.findMaxDateFilial",
                query = "SELECT Max(dtCriacao) FROM acCadastro cad WHERE cad.filialArea.id = :id"),
        @NamedQuery( name = "cadastro.findMaxDate",
                    query = "SELECT Max(dtCriacao) FROM acCadastro cad")})
public class acCadastro implements BaseEntity {


    @Transient
    public static final String FIND_MAX_DATE_BY_TRANSPORTADORA = "cadastro.findMaxDateTransportadora";

    @Transient
    public static final String FIND_MAX_DATE_BY_FILIAL = "cadastro.findMaxDateFilial";

    @Transient
    public static final String FIND_MAX_DATE = "cadastro.findMaxDate";

    @Id
    @SequenceGenerator(name = "analise_cadastral_anacoid_seq", sequenceName = "analise_cadastral_anacoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "analise_cadastral_anacoid_seq")
    @Column(name = "anacoid")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "anac_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "anac_dt_alteracao")
    private Date dtAlteracao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "anac_dt_termino")
    private Date dtTermino;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "anac_dt_exclusao")
    private Date dtExclusao;

    @Temporal(TemporalType.DATE)
    @Column(name = "anac_dt_limite_renovacao")
    private Date dtVencimento;

    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtInicial;

    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtFinal;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anac_usuoid_criacao")
    private Usuario usuarioCriacao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anac_usuoid_alteracao")
    private Usuario  usuarioAlteracao;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anac_usuoid_termino")
    private Usuario  usuarioTermino;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anac_usuoid_exclusao")
    private Usuario  usuarioExclusao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Area.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anac_cliente_areaoid")
    private Area clienteArea;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Area.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anac_filial_areaoid")
    private Area filialArea;

    @Column(name = "anac_centro_custo")
    private String centroCusto;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=FichaTipo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anac_tipo_ficha")
    private FichaTipo tipoFicha;

    @Type(type = "jsonb")
    @Column(name = "anac_partes", columnDefinition = "jsonb")
    private List<TipoValor> partes;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=FichaStatus.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anac_status")
    private FichaStatus status;

    @Column(name = "anac_qtd_recomendacoes")
    private Integer qtdRecomendacoes;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=acLiberacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anac_aliboid")
    private acLiberacao acLiberacao;

    @Column(name = "anac_dias_validade")
    private Integer diasValidade;

    @Column(name = "anac_valor")
    private Double valor;

    @Column(name = "anac_observacao_cliente")
    private String observacaoCliente;

    @Column(name = "anac_observacao_interna")
    private String observacaoInterna;

    @Column(name = "anac_observacao_analise")
    private String observacaoAnalise;

    @Column(name = "anac_observacao_exclusao")
    private String observacaoExclusao;

    @Column(name = "anac_analise_especial_status")
    private Integer analiseEspecialStatus;


    @Column(name = "anac_analise_especial_detalhe" , updatable=false)
    private String analiseEspecialDetalhe;


    public acCadastro() {}

    public acCadastro(Integer id) {
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

    public Date getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Date dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }

    public Date getDtTermino() {
        return dtTermino;
    }

    public void setDtTermino(Date dtTermino) {
        this.dtTermino = dtTermino;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
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

    public Usuario getUsuarioTermino() {
        return usuarioTermino;
    }

    public void setUsuarioTermino(Usuario usuarioTermino) {
        this.usuarioTermino = usuarioTermino;
    }

    public Usuario getUsuarioExclusao() {
        return usuarioExclusao;
    }

    public void setUsuarioExclusao(Usuario usuarioExclusao) {
        this.usuarioExclusao = usuarioExclusao;
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

    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    public FichaTipo getTipoFicha() {
        return tipoFicha;
    }

    public void setTipoFicha(FichaTipo tipoFicha) {
        this.tipoFicha = tipoFicha;
    }

    public List<TipoValor> getPartes() {
        return partes;
    }

    public void setPartes(List<TipoValor> partes) {
        this.partes = partes;
    }

    public FichaStatus getStatus() {
        return status;
    }

    public void setStatus(FichaStatus status) {
        this.status = status;
    }

    public Integer getQtdRecomendacoes() {
        return qtdRecomendacoes;
    }

    public void setQtdRecomendacoes(Integer qtdRecomendacoes) {
        this.qtdRecomendacoes = qtdRecomendacoes;
    }

    public acLiberacao getAcLiberacao() {
        return acLiberacao;
    }

    public void setAcLiberacao(acLiberacao acLiberacao) {
        this.acLiberacao = acLiberacao;
    }

    public Integer getDiasValidade() {
        return diasValidade;
    }

    public void setDiasValidade(Integer diasValidade) {
        this.diasValidade = diasValidade;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getObservacaoCliente() {
        return observacaoCliente;
    }

    public void setObservacaoCliente(String observacaoCliente) {
        this.observacaoCliente = observacaoCliente;
    }

    public String getObservacaoInterna() {
        return observacaoInterna;
    }

    public void setObservacaoInterna(String observacaoInterna) {
        this.observacaoInterna = observacaoInterna;
    }

    public String getObservacaoAnalise() {
        return observacaoAnalise;
    }

    public void setObservacaoAnalise(String observacaoAnalise) {
        this.observacaoAnalise = observacaoAnalise;
    }

    public String getObservacaoExclusao() {
        return observacaoExclusao;
    }

    public void setObservacaoExclusao(String observacaoExclusao) {
        this.observacaoExclusao = observacaoExclusao;
    }

    public static String getFindMaxDateByTransportadora() {
        return FIND_MAX_DATE_BY_TRANSPORTADORA;
    }

    public static String getFindMaxDateByFilial() {
        return FIND_MAX_DATE_BY_FILIAL;
    }

    public static String getFindMaxDate() {
        return FIND_MAX_DATE;
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

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public Integer getAnaliseEspecialStatus() {
        return analiseEspecialStatus;
    }

    public void setAnaliseEspecialStatus(Integer analiseEspecialStatus) {
        this.analiseEspecialStatus = analiseEspecialStatus;
    }
    
    public String getAnaliseEspecialDetalhe() {
        return analiseEspecialDetalhe;
    }

    public void setAnaliseEspecialDetalhe(String analiseEspecialDetalhe) {
        this.analiseEspecialDetalhe = analiseEspecialDetalhe;
    }

    
}
