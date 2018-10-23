package br.com.global5.manager.model.excluir;


import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Motorista;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "ficha_motorista")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class FichaMotorista implements BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fictfichaoid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Motorista.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="fictmotoid")
    private Motorista motorista;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Endereco.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="fictendoid")
    private Endereco endereco;

    @Column(name = "fictnreferencias")
    private Integer ctrlRreferencias;

    @Column(name = "fictncarregamentos")
    private Integer ctrlCarregamentos;

    @Column(name = "fictcriminal")
    private Integer ctrlCriminal;

    @Column(name = "fictncheques")
    private Integer ctrlCheques;

    @Column(name = "ficttipooid")
    private Integer ficttipooid;

    @Column(name = "fictultdataverificacao")
    private java.sql.Date dtUltimaVerificacao;

    @Column(name = "fictmultapontos")
    private Integer ctrlMultaPontos;

    @Column(name = "fictmultavalor")
    private Double ctrlMultaValor;

    @Column(name = "fictmultanumero")
    private Integer ctrlMultaNumero;

    @Column(name = "fictnrestricoesref")
    private Integer ctrlRestricoesRef;

    @Column(name = "fictnrestricoesucar")
    private Integer fictnrestricoesucar;

    @Column(name = "fictfoneverificado")
    private Integer ctrlFoneVerificado;

    @Column(name = "fictcriminalonline")
    private Integer ctrlCriminalOnline;

    @Column(name = "ficttempo")
    private String ctrlTempo;

    @Column(name = "fictrestricao")
    private String ctrlRestricao;

    @Column(name = "fictcopiascheque")
    private String ctrlCopiasCheque;

    @Column(name = "fictimprc")
    private String ctrlImprc;

    @Column(name = "fictcnh")
    private Integer ctrlCnh;

    @Column(name = "fictserasa")
    private Integer ctrlSerasa;

    @Column(name = "fictcriminal2")
    private Integer ctrlCriminal2;

    @Column(name = "fictcriminalonline2")
    private Integer ctrlCriminalOnline2;

    @Column(name = "fictdata_criminal2")
    private java.sql.Timestamp dtCriminal2;

    @Column(name = "fictnum_copiascheque")
    private Integer numeroCopiascheque;

    @Column(name = "fictefetuou_serasa")
    private String ctrlEfetuouSerasa;

    @Column(name = "migracao")
    private Integer migracao;

    @Column(name = "fictzera_serasa")
    private String ctrlZeraSerasa;

    @Column(name = "fictufdivergente")
    private String ctrlUfdDvergente;

    public FichaMotorista() {}

    public FichaMotorista(Motorista motorista, Endereco endereco, Integer ctrlRreferencias, Integer ctrlCarregamentos,
                          Integer ctrlCriminal, Integer ctrlCheques, Integer ficttipooid, Date dtUltimaVerificacao,
                          Integer ctrlMultaPontos, Double ctrlMultaValor, Integer ctrlMultaNumero,
                          Integer ctrlRestricoesRef, Integer fictnrestricoesucar, Integer ctrlFoneVerificado,
                          Integer ctrlCriminalOnline, String ctrlTempo, String ctrlRestricao,
                          String ctrlCopiasCheque, String ctrlImprc, Integer ctrlCnh, Integer ctrlSerasa,
                          Integer ctrlCriminal2, Integer ctrlCriminalOnline2, Timestamp dtCriminal2,
                          Integer numeroCopiascheque, String ctrlEfetuouSerasa, Integer migracao,
                          String ctrlZeraSerasa, String ctrlUfdDvergente) {
        this.motorista = motorista;
        this.endereco = endereco;
        this.ctrlRreferencias = ctrlRreferencias;
        this.ctrlCarregamentos = ctrlCarregamentos;
        this.ctrlCriminal = ctrlCriminal;
        this.ctrlCheques = ctrlCheques;
        this.ficttipooid = ficttipooid;
        this.dtUltimaVerificacao = dtUltimaVerificacao;
        this.ctrlMultaPontos = ctrlMultaPontos;
        this.ctrlMultaValor = ctrlMultaValor;
        this.ctrlMultaNumero = ctrlMultaNumero;
        this.ctrlRestricoesRef = ctrlRestricoesRef;
        this.fictnrestricoesucar = fictnrestricoesucar;
        this.ctrlFoneVerificado = ctrlFoneVerificado;
        this.ctrlCriminalOnline = ctrlCriminalOnline;
        this.ctrlTempo = ctrlTempo;
        this.ctrlRestricao = ctrlRestricao;
        this.ctrlCopiasCheque = ctrlCopiasCheque;
        this.ctrlImprc = ctrlImprc;
        this.ctrlCnh = ctrlCnh;
        this.ctrlSerasa = ctrlSerasa;
        this.ctrlCriminal2 = ctrlCriminal2;
        this.ctrlCriminalOnline2 = ctrlCriminalOnline2;
        this.dtCriminal2 = dtCriminal2;
        this.numeroCopiascheque = numeroCopiascheque;
        this.ctrlEfetuouSerasa = ctrlEfetuouSerasa;
        this.migracao = migracao;
        this.ctrlZeraSerasa = ctrlZeraSerasa;
        this.ctrlUfdDvergente = ctrlUfdDvergente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Integer getCtrlRreferencias() {
        return ctrlRreferencias;
    }

    public void setCtrlRreferencias(Integer ctrlRreferencias) {
        this.ctrlRreferencias = ctrlRreferencias;
    }

    public Integer getCtrlCarregamentos() {
        return ctrlCarregamentos;
    }

    public void setCtrlCarregamentos(Integer ctrlCarregamentos) {
        this.ctrlCarregamentos = ctrlCarregamentos;
    }

    public Integer getCtrlCriminal() {
        return ctrlCriminal;
    }

    public void setCtrlCriminal(Integer ctrlCriminal) {
        this.ctrlCriminal = ctrlCriminal;
    }

    public Integer getCtrlCheques() {
        return ctrlCheques;
    }

    public void setCtrlCheques(Integer ctrlCheques) {
        this.ctrlCheques = ctrlCheques;
    }

    public Integer getFicttipooid() {
        return ficttipooid;
    }

    public void setFicttipooid(Integer ficttipooid) {
        this.ficttipooid = ficttipooid;
    }

    public Date getDtUltimaVerificacao() {
        return dtUltimaVerificacao;
    }

    public void setDtUltimaVerificacao(Date dtUltimaVerificacao) {
        this.dtUltimaVerificacao = dtUltimaVerificacao;
    }

    public Integer getCtrlMultaPontos() {
        return ctrlMultaPontos;
    }

    public void setCtrlMultaPontos(Integer ctrlMultaPontos) {
        this.ctrlMultaPontos = ctrlMultaPontos;
    }

    public Double getCtrlMultaValor() {
        return ctrlMultaValor;
    }

    public void setCtrlMultaValor(Double ctrlMultaValor) {
        this.ctrlMultaValor = ctrlMultaValor;
    }

    public Integer getCtrlMultaNumero() {
        return ctrlMultaNumero;
    }

    public void setCtrlMultaNumero(Integer ctrlMultaNumero) {
        this.ctrlMultaNumero = ctrlMultaNumero;
    }

    public Integer getCtrlRestricoesRef() {
        return ctrlRestricoesRef;
    }

    public void setCtrlRestricoesRef(Integer ctrlRestricoesRef) {
        this.ctrlRestricoesRef = ctrlRestricoesRef;
    }

    public Integer getFictnrestricoesucar() {
        return fictnrestricoesucar;
    }

    public void setFictnrestricoesucar(Integer fictnrestricoesucar) {
        this.fictnrestricoesucar = fictnrestricoesucar;
    }

    public Integer getCtrlFoneVerificado() {
        return ctrlFoneVerificado;
    }

    public void setCtrlFoneVerificado(Integer ctrlFoneVerificado) {
        this.ctrlFoneVerificado = ctrlFoneVerificado;
    }

    public Integer getCtrlCriminalOnline() {
        return ctrlCriminalOnline;
    }

    public void setCtrlCriminalOnline(Integer ctrlCriminalOnline) {
        this.ctrlCriminalOnline = ctrlCriminalOnline;
    }

    public String getCtrlTempo() {
        return ctrlTempo;
    }

    public void setCtrlTempo(String ctrlTempo) {
        this.ctrlTempo = ctrlTempo;
    }

    public String getCtrlRestricao() {
        return ctrlRestricao;
    }

    public void setCtrlRestricao(String ctrlRestricao) {
        this.ctrlRestricao = ctrlRestricao;
    }

    public String getCtrlCopiasCheque() {
        return ctrlCopiasCheque;
    }

    public void setCtrlCopiasCheque(String ctrlCopiasCheque) {
        this.ctrlCopiasCheque = ctrlCopiasCheque;
    }

    public String getCtrlImprc() {
        return ctrlImprc;
    }

    public void setCtrlImprc(String ctrlImprc) {
        this.ctrlImprc = ctrlImprc;
    }

    public Integer getCtrlCnh() {
        return ctrlCnh;
    }

    public void setCtrlCnh(Integer ctrlCnh) {
        this.ctrlCnh = ctrlCnh;
    }

    public Integer getCtrlSerasa() {
        return ctrlSerasa;
    }

    public void setCtrlSerasa(Integer ctrlSerasa) {
        this.ctrlSerasa = ctrlSerasa;
    }

    public Integer getCtrlCriminal2() {
        return ctrlCriminal2;
    }

    public void setCtrlCriminal2(Integer ctrlCriminal2) {
        this.ctrlCriminal2 = ctrlCriminal2;
    }

    public Integer getCtrlCriminalOnline2() {
        return ctrlCriminalOnline2;
    }

    public void setCtrlCriminalOnline2(Integer ctrlCriminalOnline2) {
        this.ctrlCriminalOnline2 = ctrlCriminalOnline2;
    }

    public Timestamp getDtCriminal2() {
        return dtCriminal2;
    }

    public void setDtCriminal2(Timestamp dtCriminal2) {
        this.dtCriminal2 = dtCriminal2;
    }

    public Integer getNumeroCopiascheque() {
        return numeroCopiascheque;
    }

    public void setNumeroCopiascheque(Integer numeroCopiascheque) {
        this.numeroCopiascheque = numeroCopiascheque;
    }

    public String getCtrlEfetuouSerasa() {
        return ctrlEfetuouSerasa;
    }

    public void setCtrlEfetuouSerasa(String ctrlEfetuouSerasa) {
        this.ctrlEfetuouSerasa = ctrlEfetuouSerasa;
    }

    public Integer getMigracao() {
        return migracao;
    }

    public void setMigracao(Integer migracao) {
        this.migracao = migracao;
    }

    public String getCtrlZeraSerasa() {
        return ctrlZeraSerasa;
    }

    public void setCtrlZeraSerasa(String ctrlZeraSerasa) {
        this.ctrlZeraSerasa = ctrlZeraSerasa;
    }

    public String getCtrlUfdDvergente() {
        return ctrlUfdDvergente;
    }

    public void setCtrlUfdDvergente(String ctrlUfdDvergente) {
        this.ctrlUfdDvergente = ctrlUfdDvergente;
    }
}
