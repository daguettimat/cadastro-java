package br.com.global5.manager.model.cadastro;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.excluir.Endereco;
import br.com.global5.manager.model.geral.Banco;
import br.com.global5.manager.model.auxiliar.TipoContrato;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;

@Entity
@Table(name = "transportadora")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Transportadora implements BaseEntity {

    @Id
    @SequenceGenerator(name = "transportadora_traoid_seq", sequenceName = "transportadora_traoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "transportadora_traoid_seq")
    @Column(name = "traoid")
    private Integer id;

    @Column(name = "tranome")
    private String nome;

    @Column(name = "tracnpj")
    private Double cnpj;

    @Column(name = "trainsestadual")
    private String inscricaoEstadual;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Endereco.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="traendoid")
    private Endereco endereco;

    @Column(name = "tracontato")
    private String contato;

    @Column(name = "traemail")
    private String email;

    @Column(name = "tracoloid")
    private Integer idColaborador;

    @Column(name = "trasegoid")
    private Integer idSeguradora;

    @Column(name = "tracadastro")
    private java.sql.Date dtCadastro;

    @Column(name = "traexclusao")
    private java.sql.Date dtExclusao;

    @Column(name = "trastatus")
    private String status;

    @Column(name = "tracontr_envio")
    private java.sql.Date dtContratoEnvio;

    @Column(name = "tracontr_recbto")
    private java.sql.Date dtContratoRecbto;

    @Column(name = "tramdeoid")
    private Integer tramdeoid;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=TipoContrato.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="tratiptoid")
    private TipoContrato tipoContrato;

    @Column(name = "trafaturamento")
    private String faturamento;

    @Column(name = "tragerafaturamento")
    private String geraFaturamento;

    @Column(name = "travencimento")
    private Integer vencimento;

    @Column(name = "tranfserie")
    private String serie;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Endereco.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="traendoid_corresp")
    private Endereco enderecoCorrespondencia;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Banco.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="trabanoid")
    private Banco banco;

    @Column(name = "traenvia_foto")
    private String enviaFoto;

    @Column(name = "traprim_ficha")
    private java.sql.Date dtPrimeiraFicha;

    @Column(name = "travlr_rastreamento_obr")
    private Double vlrRastreamentoObr;

    @Column(name = "tratipo_validade")
    private Integer tipoValidade;

    @Column(name = "trarespcontas")
    private String responsalContas;

    @Column(name = "tradddrespcontas")
    private Integer dddResponsavelContas;

    @Column(name = "trafonerespcontas")
    private Integer foneResponsavelContas;

    @Column(name = "trapesq_validade")
    private String pesquisaValidade;

    @Column(name = "traeimpressao")
    private String impressao;

    @Column(name = "trafrase_nf")
    private String frase_nf;

    @Column(name = "tratecnorisk")
    private String tecnorisk;

    @Column(name = "traocorrencia_email")
    private String ocorrenciaEmail;

    @Column(name = "tratipoenvio")
    private String tipoEnvio;

    @Column(name = "tratipocobserasa")
    private String tipoCobrancaSerasa;

    @Column(name = "tramacroinicio")
    private Integer macroInicio;
    
    @Column(name = "tramacrofim")
    private Integer macroFim;

    @Column(name = "tramacrochegada")
    private Integer macroChegada;

    @Column(name = "tratipoobrigatoriedade")
    private String tipoObrigatoriedade;

    @Column(name = "traoiddev")
    private Integer iddev;

    @Column(name = "migracao")
    private Integer migracao;

    @Column(name = "tratiposolicitacao")
    private Integer tipoSolicitacao;
    

    public Transportadora() {}

    public Transportadora(String nome, Double cnpj, String inscricaoEstadual, Endereco endereco, String contato,
                          String email, Integer idColaborador, Integer idSeguradora, Date dtCadastro, Date dtExclusao,
                          String status, Date dtContratoEnvio, Date dtContratoRecbto, Integer tramdeoid,
                          TipoContrato tipoContrato, String faturamento, String geraFaturamento, Integer vencimento,
                          String serie, Endereco enderecoCorrespondencia, Banco banco, String enviaFoto,
                          Date dtPrimeiraFicha, Double vlrRastreamentoObr, Integer tipoValidade,
                          String responsalContas, Integer dddResponsavelContas, Integer foneResponsavelContas,
                          String pesquisaValidade, String impressao, String frase_nf, String tecnorisk,
                          String ocorrenciaEmail, String tipoEnvio, String tipoCobrancaSerasa, Integer macroInicio,
                          Integer macroFim, Integer macroChegada, String tipoObrigatoriedade, Integer iddev,
                          Integer migracao, Integer tipoSolicitacao) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
        this.endereco = endereco;
        this.contato = contato;
        this.email = email;
        this.idColaborador = idColaborador;
        this.idSeguradora = idSeguradora;
        this.dtCadastro = dtCadastro;
        this.dtExclusao = dtExclusao;
        this.status = status;
        this.dtContratoEnvio = dtContratoEnvio;
        this.dtContratoRecbto = dtContratoRecbto;
        this.tramdeoid = tramdeoid;
        this.tipoContrato = tipoContrato;
        this.faturamento = faturamento;
        this.geraFaturamento = geraFaturamento;
        this.vencimento = vencimento;
        this.serie = serie;
        this.enderecoCorrespondencia = enderecoCorrespondencia;
        this.banco = banco;
        this.enviaFoto = enviaFoto;
        this.dtPrimeiraFicha = dtPrimeiraFicha;
        this.vlrRastreamentoObr = vlrRastreamentoObr;
        this.tipoValidade = tipoValidade;
        this.responsalContas = responsalContas;
        this.dddResponsavelContas = dddResponsavelContas;
        this.foneResponsavelContas = foneResponsavelContas;
        this.pesquisaValidade = pesquisaValidade;
        this.impressao = impressao;
        this.frase_nf = frase_nf;
        this.tecnorisk = tecnorisk;
        this.ocorrenciaEmail = ocorrenciaEmail;
        this.tipoEnvio = tipoEnvio;
        this.tipoCobrancaSerasa = tipoCobrancaSerasa;
        this.macroInicio = macroInicio;
        this.macroFim = macroFim;
        this.macroChegada = macroChegada;
        this.tipoObrigatoriedade = tipoObrigatoriedade;
        this.iddev = iddev;
        this.migracao = migracao;
        this.tipoSolicitacao = tipoSolicitacao;
    }

    public Transportadora Nome(String nome) {
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getCnpj() {
        return cnpj;
    }

    public void setCnpj(Double cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(Integer idColaborador) {
        this.idColaborador = idColaborador;
    }

    public Integer getIdSeguradora() {
        return idSeguradora;
    }

    public void setIdSeguradora(Integer idSeguradora) {
        this.idSeguradora = idSeguradora;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDtContratoEnvio() {
        return dtContratoEnvio;
    }

    public void setDtContratoEnvio(Date dtContratoEnvio) {
        this.dtContratoEnvio = dtContratoEnvio;
    }

    public Date getDtContratoRecbto() {
        return dtContratoRecbto;
    }

    public void setDtContratoRecbto(Date dtContratoRecbto) {
        this.dtContratoRecbto = dtContratoRecbto;
    }

    public Integer getTramdeoid() {
        return tramdeoid;
    }

    public void setTramdeoid(Integer tramdeoid) {
        this.tramdeoid = tramdeoid;
    }

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getFaturamento() {
        return faturamento;
    }

    public void setFaturamento(String faturamento) {
        this.faturamento = faturamento;
    }

    public String getGeraFaturamento() {
        return geraFaturamento;
    }

    public void setGeraFaturamento(String geraFaturamento) {
        this.geraFaturamento = geraFaturamento;
    }

    public Integer getVencimento() {
        return vencimento;
    }

    public void setVencimento(Integer vencimento) {
        this.vencimento = vencimento;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Endereco getEnderecoCorrespondencia() {
        return enderecoCorrespondencia;
    }

    public void setEnderecoCorrespondencia(Endereco enderecoCorrespondencia) {
        this.enderecoCorrespondencia = enderecoCorrespondencia;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getEnviaFoto() {
        return enviaFoto;
    }

    public void setEnviaFoto(String enviaFoto) {
        this.enviaFoto = enviaFoto;
    }

    public Date getDtPrimeiraFicha() {
        return dtPrimeiraFicha;
    }

    public void setDtPrimeiraFicha(Date dtPrimeiraFicha) {
        this.dtPrimeiraFicha = dtPrimeiraFicha;
    }

    public Double getVlrRastreamentoObr() {
        return vlrRastreamentoObr;
    }

    public void setVlrRastreamentoObr(Double vlrRastreamentoObr) {
        this.vlrRastreamentoObr = vlrRastreamentoObr;
    }

    public Integer getTipoValidade() {
        return tipoValidade;
    }

    public void setTipoValidade(Integer tipoValidade) {
        this.tipoValidade = tipoValidade;
    }

    public String getResponsalContas() {
        return responsalContas;
    }

    public void setResponsalContas(String responsalContas) {
        this.responsalContas = responsalContas;
    }

    public Integer getDddResponsavelContas() {
        return dddResponsavelContas;
    }

    public void setDddResponsavelContas(Integer dddResponsavelContas) {
        this.dddResponsavelContas = dddResponsavelContas;
    }

    public Integer getFoneResponsavelContas() {
        return foneResponsavelContas;
    }

    public void setFoneResponsavelContas(Integer foneResponsavelContas) {
        this.foneResponsavelContas = foneResponsavelContas;
    }

    public String getPesquisaValidade() {
        return pesquisaValidade;
    }

    public void setPesquisaValidade(String pesquisaValidade) {
        this.pesquisaValidade = pesquisaValidade;
    }

    public String getImpressao() {
        return impressao;
    }

    public void setImpressao(String impressao) {
        this.impressao = impressao;
    }

    public String getFrase_nf() {
        return frase_nf;
    }

    public void setFrase_nf(String frase_nf) {
        this.frase_nf = frase_nf;
    }

    public String getTecnorisk() {
        return tecnorisk;
    }

    public void setTecnorisk(String tecnorisk) {
        this.tecnorisk = tecnorisk;
    }

    public String getOcorrenciaEmail() {
        return ocorrenciaEmail;
    }

    public void setOcorrenciaEmail(String ocorrenciaEmail) {
        this.ocorrenciaEmail = ocorrenciaEmail;
    }

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public String getTipoCobrancaSerasa() {
        return tipoCobrancaSerasa;
    }

    public void setTipoCobrancaSerasa(String tipoCobrancaSerasa) {
        this.tipoCobrancaSerasa = tipoCobrancaSerasa;
    }

    public Integer getMacroInicio() {
        return macroInicio;
    }

    public void setMacroInicio(Integer macroInicio) {
        this.macroInicio = macroInicio;
    }

    public Integer getMacroFim() {
        return macroFim;
    }

    public void setMacroFim(Integer macroFim) {
        this.macroFim = macroFim;
    }

    public Integer getMacroChegada() {
        return macroChegada;
    }

    public void setMacroChegada(Integer macroChegada) {
        this.macroChegada = macroChegada;
    }

    public String getTipoObrigatoriedade() {
        return tipoObrigatoriedade;
    }

    public void setTipoObrigatoriedade(String tipoObrigatoriedade) {
        this.tipoObrigatoriedade = tipoObrigatoriedade;
    }

    public Integer getIddev() {
        return iddev;
    }

    public void setIddev(Integer iddev) {
        this.iddev = iddev;
    }

    public Integer getMigracao() {
        return migracao;
    }

    public void setMigracao(Integer migracao) {
        this.migracao = migracao;
    }

    public Integer getTipoSolicitacao() {
        return tipoSolicitacao;
    }

    public void setTipoSolicitacao(Integer tipoSolicitacao) {
        this.tipoSolicitacao = tipoSolicitacao;
    }
}
