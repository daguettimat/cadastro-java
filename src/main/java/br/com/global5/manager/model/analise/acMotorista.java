package br.com.global5.manager.model.analise;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.CNH;
import br.com.global5.manager.model.cadastro.Localizador;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.manager.model.enums.MotoristaVinculo;
import br.com.global5.manager.model.enums.ReferenciasAvaliacao;
import br.com.global5.manager.model.externos.ConsultaPessoa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@Entity
@Table(name = "analise_cadastral_motorista")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@NamedQueries(value = {

        @NamedQuery( name = "cadastro.motorista.list",
                query = "SELECT cad " +
                        "  FROM acMotorista cad " +
                        " WHERE cad.motorista.id = :idMotorista" +
                        "   AND (cad.acCadastro.status.id = 4 OR cad.acCadastro.status.id = 5) "),
         })

public class acMotorista implements BaseEntity {

    @Transient
    public static final String FIND_OPEN_MOTORISTAS = "cadastro.motorista.list";

    @Id
    @SequenceGenerator(name = "analise_cadastral_motorista_anacmoid_seq", sequenceName = "analise_cadastral_motorista_anacmoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "analise_cadastral_motorista_anacmoid_seq")
    @Column(name = "anacmoid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=acCadastro.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_anacoid")
    private acCadastro acCadastro;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=br.com.global5.manager.model.geral.Motorista.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_motoid")
    private br.com.global5.manager.model.geral.Motorista motorista;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Localizador.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_locoid")
    private Localizador endereco;

    @Column(name = "anacm_origem_anacmoid")
    private Integer origemMotorista;

    @Column(name = "anacm_origem_dt")
    private java.sql.Timestamp dtOrigem;

    @Column(name = "anacm_dt_registro")
    private java.sql.Timestamp dtRegistro;

    @Column(name = "anacm_dias_validade")
    private Integer diasValidade;

    @Column(name = "anacm_criminal_dprocesso")
    private Integer anacm_criminal_dprocesso;

    @Column(name = "anacm_criminal_pendencia")
    private String criminalPendencias;

    @Column(name = "anacm_ref_pessoal_pendencia")
    private String pessoalPendencias;

    @Column(name = "anacm_ref_comercial_pendencia")
    private String comercialPendencias;

    @Column(name = "anacm_cnh_pendencia")
    private String cnhPendencias;

    @Column(name = "anacm_cpf_pendencia")
    private String cpfPendencia;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_status_geral")
    private ReferenciasAvaliacao statusGeral;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_criminal")
    private ReferenciasAvaliacao statusCriminal;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_ref_pessoal")
    private ReferenciasAvaliacao statusPessoal;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_ref_comercial")
    private ReferenciasAvaliacao statusComercial;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_cnh_status")
    private ReferenciasAvaliacao statusCNH;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_cnh_categoria")
    private ReferenciasAvaliacao cnhCategoria;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_cnh_validade")
    private ReferenciasAvaliacao cnhValidade;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_cnh_pontos")
    private ReferenciasAvaliacao cnhPontos;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_cpf")
    private ReferenciasAvaliacao cpf;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=CNH.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_mcnhoid")
    private CNH cnh;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Telefone.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_teloid")
    private Telefone telefone;

    @Column(name = "anacm_cnh_qtdpontos")
    private Integer cnhQtdPontos;

    @Column(name = "anacm_conrfoid")
    private Integer consultaReferencia;

    @Column(name = "anacm_observacao")
    private String observacao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ConsultaPessoa.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_conpfoid")
    private ConsultaPessoa consultaPessoa;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=MotoristaVinculo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="anacm_vinculo")
    private MotoristaVinculo motoristaVinculo;

    public acMotorista() {}

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

    public br.com.global5.manager.model.geral.Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(br.com.global5.manager.model.geral.Motorista motorista) {
        this.motorista = motorista;
    }

    public Localizador getEndereco() {
        return endereco;
    }

    public void setEndereco(Localizador endereco) {
        this.endereco = endereco;
    }

    public Integer getOrigemMotorista() {
        return origemMotorista;
    }

    public void setOrigemMotorista(Integer origemMotorista) {
        this.origemMotorista = origemMotorista;
    }

    public Timestamp getDtOrigem() {
        return dtOrigem;
    }

    public void setDtOrigem(Timestamp dtOrigem) {
        this.dtOrigem = dtOrigem;
    }

    public Timestamp getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(Timestamp dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public Integer getDiasValidade() {
        return diasValidade;
    }

    public void setDiasValidade(Integer diasValidade) {
        this.diasValidade = diasValidade;
    }

    public Integer getAnacm_criminal_dprocesso() {
        return anacm_criminal_dprocesso;
    }

    public void setAnacm_criminal_dprocesso(Integer anacm_criminal_dprocesso) {
        this.anacm_criminal_dprocesso = anacm_criminal_dprocesso;
    }

    public String getCriminalPendencias() {
        return criminalPendencias;
    }

    public void setCriminalPendencias(String criminalPendencias) {
        this.criminalPendencias = criminalPendencias;
    }

    public String getPessoalPendencias() {
        return pessoalPendencias;
    }

    public void setPessoalPendencias(String pessoalPendencias) {
        this.pessoalPendencias = pessoalPendencias;
    }

    public String getComercialPendencias() {
        return comercialPendencias;
    }

    public void setComercialPendencias(String comercialPendencias) {
        this.comercialPendencias = comercialPendencias;
    }

    public String getCnhPendencias() {
        return cnhPendencias;
    }

    public void setCnhPendencias(String cnhPendencias) {
        this.cnhPendencias = cnhPendencias;
    }

    public String getCpfPendencia() {
        return cpfPendencia;
    }

    public void setCpfPendencia(String cpfPendencia) {
        this.cpfPendencia = cpfPendencia;
    }

    public ReferenciasAvaliacao getStatusGeral() {
        return statusGeral;
    }

    public void setStatusGeral(ReferenciasAvaliacao statusGeral) {
        this.statusGeral = statusGeral;
    }

    public ReferenciasAvaliacao getStatusCriminal() {
        return statusCriminal;
    }

    public void setStatusCriminal(ReferenciasAvaliacao statusCriminal) {
        this.statusCriminal = statusCriminal;
    }

    public ReferenciasAvaliacao getStatusPessoal() {
        return statusPessoal;
    }

    public void setStatusPessoal(ReferenciasAvaliacao statusPessoal) {
        this.statusPessoal = statusPessoal;
    }

    public ReferenciasAvaliacao getStatusComercial() {
        return statusComercial;
    }

    public void setStatusComercial(ReferenciasAvaliacao statusComercial) {
        this.statusComercial = statusComercial;
    }

    public ReferenciasAvaliacao getStatusCNH() {
        return statusCNH;
    }

    public void setStatusCNH(ReferenciasAvaliacao statusCNH) {
        this.statusCNH = statusCNH;
    }

    public ReferenciasAvaliacao getCnhCategoria() {
        return cnhCategoria;
    }

    public void setCnhCategoria(ReferenciasAvaliacao cnhCategoria) {
        this.cnhCategoria = cnhCategoria;
    }

    public ReferenciasAvaliacao getCnhValidade() {
        return cnhValidade;
    }

    public void setCnhValidade(ReferenciasAvaliacao cnhValidade) {
        this.cnhValidade = cnhValidade;
    }

    public ReferenciasAvaliacao getCnhPontos() {
        return cnhPontos;
    }

    public void setCnhPontos(ReferenciasAvaliacao cnhPontos) {
        this.cnhPontos = cnhPontos;
    }

    public ReferenciasAvaliacao getCpf() {
        return cpf;
    }

    public void setCpf(ReferenciasAvaliacao cpf) {
        this.cpf = cpf;
    }

    public CNH getCnh() {
        return cnh;
    }

    public void setCnh(CNH cnh) {
        this.cnh = cnh;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public Integer getCnhQtdPontos() {
        return cnhQtdPontos;
    }

    public void setCnhQtdPontos(Integer cnhQtdPontos) {
        this.cnhQtdPontos = cnhQtdPontos;
    }

    public Integer getConsultaReferencia() {
        return consultaReferencia;
    }

    public void setConsultaReferencia(Integer consultaReferencia) {
        this.consultaReferencia = consultaReferencia;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public ConsultaPessoa getConsultaPessoa() {
        return consultaPessoa;
    }

    public void setConsultaPessoa(ConsultaPessoa consultaPessoa) {
        this.consultaPessoa = consultaPessoa;
    }

    public MotoristaVinculo getMotoristaVinculo() {
        return motoristaVinculo;
    }

    public void setMotoristaVinculo(MotoristaVinculo motoristaVinculo) {
        this.motoristaVinculo = motoristaVinculo;
    }
}
