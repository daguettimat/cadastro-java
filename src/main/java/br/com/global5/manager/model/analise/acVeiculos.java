package br.com.global5.manager.model.analise;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Localizador;
import br.com.global5.manager.model.cadastro.Proprietario;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.manager.model.cadastro.Veiculo;
import br.com.global5.manager.model.enums.ANTTSituacao;
import br.com.global5.manager.model.enums.ReferenciasAvaliacao;
import br.com.global5.manager.model.enums.TipoParte;
import br.com.global5.manager.model.geral.Usuario;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "analise_cadastral_veiculo")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class acVeiculos implements BaseEntity {

    @Id
    @SequenceGenerator(name = "analise_cadastral_veiculo_anacvoid_seq", sequenceName = "analise_cadastral_veiculo_anacvoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "analise_cadastral_veiculo_anacvoid_seq")
    @Column(name = "anacvoid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=acCadastro.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacv_anacoid")
    private acCadastro acCadastro;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Veiculo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacv_veioid")
    private Veiculo veiculo;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Proprietario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_propoid")
    private Proprietario proprietario;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Proprietario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_arrendatario_propoid")
    private Proprietario arrendatario;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_dveiculo")
    private Usuario dVeiculo;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=acVeiculos.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_origem_anacvoid")
    private acVeiculos origemVeiculo;

    @Temporal(TemporalType.DATE)
    @Column(name = "anacv_dt_emissao_documento")
    private Date dtDocumento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "anacv_origem_dt")
    private Date dtOrigem;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "anacv_dt_registro")
    private Date dtRegistro;

    @Column(name = "anacv_dias_validade")
    private Integer diasValidade;

    @Column(name = "anacv_ref_proprietario_pendencia")
    private String refPropPendencia;

    @Column(name = "anacv_renavam_pendencia")
    private String renavamPendencias;



    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_antt_apto")
    private ReferenciasAvaliacao anttApto;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Localizador.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_prop_locoid")
    private Localizador enderecoProprietario;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Localizador.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_arrend_locoid")
    private Localizador enderecoArrendatario;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_status_geral")
    private ReferenciasAvaliacao statusGeral;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=TipoParte.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_tipo")
    private TipoParte tipo;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_ref_proprietario")
    private ReferenciasAvaliacao referenciaProprietario;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_renavam")
    private ReferenciasAvaliacao renavam;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_ano")
    private ReferenciasAvaliacao ano;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_antt")
    private ReferenciasAvaliacao antt;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ANTTSituacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_antt_situacao")
    private ANTTSituacao anttSituacao;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Telefone.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_prop_teloid")
    private Telefone telefoneProprietario;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Telefone.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_arrend_teloid")
    private Telefone telefoneArrendatario;

    @Column(name = "anacv_propoid_conrfoid")
    private Integer proprietarioReferencias;

    @Column(name = "anacv_arrendoid_conrfoid")
    private Integer arrendatarioReferencias;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_propoid_cpf_cnpj")
    private ReferenciasAvaliacao documentoProprietario;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_arrendoid_cpf_cnpj")
    private ReferenciasAvaliacao documentoArrendatario;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_ref_arrendatario")
    private ReferenciasAvaliacao referenciaArrendatario;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "anacv_emissao_documento")
    private ReferenciasAvaliacao emissaoDocumentoAvaliacao;


    @Column(name = "anacv_antt_pendencia")
    private String anttPendencias;

    @Column(name = "anacv_observacao")
    private String observacao;

    public acVeiculos() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public acCadastro getAcCadastro() {
        return acCadastro;
    }

    public void setAcCadastro(acCadastro acCadastro) {
        this.acCadastro = acCadastro;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public Proprietario getArrendatario() {
        return arrendatario;
    }

    public void setArrendatario(Proprietario arrendatario) {
        this.arrendatario = arrendatario;
    }

    public Usuario getdVeiculo() {
        return dVeiculo;
    }

    public void setdVeiculo(Usuario dVeiculo) {
        this.dVeiculo = dVeiculo;
    }

    public acVeiculos getOrigemVeiculo() {
        return origemVeiculo;
    }

    public void setOrigemVeiculo(acVeiculos origemVeiculo) {
        this.origemVeiculo = origemVeiculo;
    }

    public Date getDtOrigem() {
        return dtOrigem;
    }

    public void setDtOrigem(Date dtOrigem) {
        this.dtOrigem = dtOrigem;
    }

    public Date getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(Date dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public Integer getDiasValidade() {
        return diasValidade;
    }

    public void setDiasValidade(Integer diasValidade) {
        this.diasValidade = diasValidade;
    }

    public String getRefPropPendencia() {
        return refPropPendencia;
    }

    public void setRefPropPendencia(String refPropPendencia) {
        this.refPropPendencia = refPropPendencia;
    }

    public String getRenavamPendencias() {
        return renavamPendencias;
    }

    public void setRenavamPendencias(String renavamPendencias) {
        this.renavamPendencias = renavamPendencias;
    }

    public ReferenciasAvaliacao getAnttApto() {
        return anttApto;
    }

    public void setAnttApto(ReferenciasAvaliacao anttApto) {
        this.anttApto = anttApto;
    }

    public Localizador getEnderecoProprietario() {
        return enderecoProprietario;
    }

    public void setEnderecoProprietario(Localizador enderecoProprietario) {
        this.enderecoProprietario = enderecoProprietario;
    }

    public Localizador getEnderecoArrendatario() {
        return enderecoArrendatario;
    }

    public void setEnderecoArrendatario(Localizador enderecoArrendatario) {
        this.enderecoArrendatario = enderecoArrendatario;
    }

    public ReferenciasAvaliacao getStatusGeral() {
        return statusGeral;
    }

    public void setStatusGeral(ReferenciasAvaliacao statusGeral) {
        this.statusGeral = statusGeral;
    }

    public TipoParte getTipo() {
        return tipo;
    }

    public void setTipo(TipoParte tipo) {
        this.tipo = tipo;
    }

    public ReferenciasAvaliacao getReferenciaProprietario() {
        return referenciaProprietario;
    }

    public void setReferenciaProprietario(ReferenciasAvaliacao referenciaProprietario) {
        this.referenciaProprietario = referenciaProprietario;
    }

    public ReferenciasAvaliacao getRenavam() {
        return renavam;
    }

    public void setRenavam(ReferenciasAvaliacao renavam) {
        this.renavam = renavam;
    }

    public ReferenciasAvaliacao getAno() {
        return ano;
    }

    public void setAno(ReferenciasAvaliacao ano) {
        this.ano = ano;
    }

    public ReferenciasAvaliacao getAntt() {
        return antt;
    }

    public void setAntt(ReferenciasAvaliacao antt) {
        this.antt = antt;
    }

    public ANTTSituacao getAnttSituacao() {
        return anttSituacao;
    }

    public void setAnttSituacao(ANTTSituacao anttSituacao) {
        if( anttSituacao == null ) {
            this.anttSituacao = new ANTTSituacao(38);
        } else {
            this.anttSituacao = anttSituacao;
        }
    }

    public Telefone getTelefoneProprietario() {
        return telefoneProprietario;
    }

    public void setTelefoneProprietario(Telefone telefoneProprietario) {
        this.telefoneProprietario = telefoneProprietario;
    }

    public Telefone getTelefoneArrendatario() {
        return telefoneArrendatario;
    }

    public void setTelefoneArrendatario(Telefone telefoneArrendatario) {
        this.telefoneArrendatario = telefoneArrendatario;
    }

    public Integer getProprietarioReferencias() {
        return proprietarioReferencias;
    }

    public void setProprietarioReferencias(Integer proprietarioReferencias) {
        this.proprietarioReferencias = proprietarioReferencias;
    }

    public Integer getArrendatarioReferencias() {
        return arrendatarioReferencias;
    }

    public void setArrendatarioReferencias(Integer arrendatarioReferencias) {
        this.arrendatarioReferencias = arrendatarioReferencias;
    }

    public ReferenciasAvaliacao getDocumentoProprietario() {
        return documentoProprietario;
    }

    public void setDocumentoProprietario(ReferenciasAvaliacao documentoProprietario) {
        this.documentoProprietario = documentoProprietario;
    }

    public ReferenciasAvaliacao getDocumentoArrendatario() {
        return documentoArrendatario;
    }

    public void setDocumentoArrendatario(ReferenciasAvaliacao documentoArrendatario) {
        this.documentoArrendatario = documentoArrendatario;
    }

    public ReferenciasAvaliacao getReferenciaArrendatario() {
        return referenciaArrendatario;
    }

    public void setReferenciaArrendatario(ReferenciasAvaliacao referenciaArrendatario) {
        this.referenciaArrendatario = referenciaArrendatario;
    }

    public String getAnttPendencias() {
        return anttPendencias;
    }

    public void setAnttPendencias(String anttPendencias) {
        this.anttPendencias = anttPendencias;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getDtDocumento() {
        return dtDocumento;
    }

    public void setDtDocumento(Date dtDocumento) {
        this.dtDocumento = dtDocumento;
    }


    public ReferenciasAvaliacao getEmissaoDocumentoAvaliacao() {
        return emissaoDocumentoAvaliacao;
    }

    public void setEmissaoDocumentoAvaliacao(ReferenciasAvaliacao emissaoDocumentoAvaliacao) {
        this.emissaoDocumentoAvaliacao = emissaoDocumentoAvaliacao;
    }
}
