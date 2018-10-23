package br.com.global5.manager.model.analise;

import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.json.JsonStringType;
import br.com.global5.infra.model.BaseEntity;
import br.com.global5.infra.model.TipoValor;
import br.com.global5.manager.model.auxiliar.ImgJson;
import br.com.global5.manager.model.enums.*;
import br.com.global5.manager.model.geral.Imagem;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.model.areas.Area;
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
@Table(name = "analise_cadastral_pendencia")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@NamedQueries(value = {
        @NamedQuery( name = "pendencias.findTransportadora",
                query = "SELECT pen " +
                        "  FROM acPendencias pen, acCadastro ac " +
                        " WHERE ac.clienteArea.id = cast(:id as integer) " +
                        "   AND pen.cadastro.id = ac.id  " +
                        "   AND pen.pendenciasStatus.id != 212" +
                        " ORDER BY ac.id"),
        @NamedQuery( name = "pendencias.findAdmin",
                query = "SELECT pen " +
                        "  FROM acPendencias pen, acCadastro ac " +
                        " WHERE pen.cadastro.id = ac.id " +
                        "   AND pen.pendenciasStatus.id != 212 " +
                        "   AND pen.dtCriacao between (current_date - 15) " +
                        "   AND current_date " +
                        " ORDER BY ac.id"),
        @NamedQuery( name = "pendencias.findFilial",
                query = "SELECT pen " +
                        "  FROM acPendencias pen, acCadastro ac " +
                        " WHERE ac.filialArea.id =  cast(:id as integer) " +
                        "   AND pen.pendenciasStatus.id != 212 " +
                        "   AND pen.cadastro.id = ac.id " +
                        " ORDER BY ac.id")})
public class acPendencias implements BaseEntity {

    @Transient
    public static final String FIND_BY_TRANSPORTADORA = "pendencias.findTransportadora";

    @Transient
    public static final String FIND_ADMIN = "pendencias.findAdmin";

    @Transient
    public static final String FIND_BY_FILIAL = "pendencias.findFilial";


    @Id
    @SequenceGenerator(name = "analise_cadastral_pendencia_apenoid_seq",
            sequenceName = "analise_cadastral_pendencia_apenoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "analise_cadastral_pendencia_apenoid_seq")
    @Column(name = "apenoid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=acCadastro.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="apen_anacoid")
    private acCadastro cadastro;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "apen_dt_registro")
    private Date dtRegistro;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "apen_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "apen_dt_resposta")
    private Date dtResposta;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "apen_dt_exclusao")
    private Date dtExclusao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="apen_usuoid_criacao")
    private Usuario usuarioCriacao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="apen_usuoid_resposta")
    private Usuario  usuarioResposta;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="apen_usuoid_termino")
    private Usuario  usuarioTermino;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=TipoParte.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="apen_tipo_parte")
    private TipoParte tipoParte;

    @Column(name="apen_item")
    private int item;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=FichaStatus.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "apen_status")
    private FichaStatus status;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=PendenciasTipo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "apen_pendencia_requisito")
    private PendenciasTipo tipoPendencia;

    @Column(name = "apen_observacao_cliente")
    private String observacaoCliente;

    @Column(name = "apen_observacao_requisicao")
    private String observacaoRequisicao;

    @Type(type = "jsonb")
    @Column(name = "apen_imgjoid", columnDefinition = "jsonb")
    private List<ImgJson> imgJsonList;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=PendenciasStatus.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "apen_pendencia_status")
    private PendenciasStatus pendenciasStatus;

    public acPendencias() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public acCadastro getCadastro() {
        return cadastro;
    }

    public void setCadastro(acCadastro cadastro) {
        this.cadastro = cadastro;
    }

    public Date getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(Date dtRegistro) {
        this.dtRegistro = dtRegistro;
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

    public Usuario getUsuarioTermino() {
        return usuarioTermino;
    }

    public void setUsuarioTermino(Usuario usuarioTermino) {
        this.usuarioTermino = usuarioTermino;
    }

    public PendenciasTipo getTipoPendencia() {
        return tipoPendencia;
    }

    public void setTipoPendencia(PendenciasTipo tipoPendencia) {
        this.tipoPendencia = tipoPendencia;
    }

    public FichaStatus getStatus() {
        return status;
    }

    public void setStatus(FichaStatus status) {
        this.status = status;
    }

    public String getObservacaoCliente() {
        return observacaoCliente;
    }

    public void setObservacaoCliente(String observacaoCliente) {
        this.observacaoCliente = observacaoCliente;
    }

    public String getObservacaoRequisicao() {
        return observacaoRequisicao;
    }

    public void setObservacaoRequisicao(String observacaoRequisicao) {
        this.observacaoRequisicao = observacaoRequisicao;
    }

    public List<ImgJson> getImgJsonList() {
        return imgJsonList;
    }

    public void setImgJsonList(List<ImgJson> imgJsonList) {
        this.imgJsonList = imgJsonList;
    }

    public Usuario getUsuarioResposta() {
        return usuarioResposta;
    }

    public void setUsuarioResposta(Usuario usuarioResposta) {
        this.usuarioResposta = usuarioResposta;
    }

    public TipoParte getTipoParte() {
        return tipoParte;
    }

    public void setTipoParte(TipoParte tipoParte) {
        this.tipoParte = tipoParte;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public PendenciasStatus getPendenciasStatus() {
        return pendenciasStatus;
    }

    public void setPendenciasStatus(PendenciasStatus pendenciasStatus) {
        this.pendenciasStatus = pendenciasStatus;
    }
}
