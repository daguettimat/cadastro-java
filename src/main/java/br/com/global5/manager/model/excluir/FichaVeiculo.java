package br.com.global5.manager.model.excluir;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Proprietario;
import br.com.global5.manager.model.cadastro.Veiculo;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;

@Entity
@Table(name = "ficha_veiculo")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class FichaVeiculo implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ficvfichaoid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Veiculo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficvveioid")
    private Veiculo veiculo;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Proprietario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficvpropoid")
    private Proprietario proprietario;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Endereco.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficvpropendoid")
    private Endereco enderecoProprietario;

    @Column(name = "ficvpropncheques")
    private Integer numeroCheques;

    @Column(name = "ficvultdataverificacao")
    private java.sql.Date ficvultdataverificacao;

    @Column(name = "ficvcriminal")
    private Integer criminal;

    @Column(name = "ficvfoneverificado")
    private Integer foneVerificado;

    @Column(name = "ficvrestricao")
    private String restricao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Proprietario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficvarrendoid")
    private Proprietario arrendatario;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Endereco.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficvarrendendoid")
    private Endereco enderecoArrendatario;

    @Column(name = "ficvrenavam")
    private Integer renavam;

    @Column(name = "ficvarrendncheques")
    private Integer arrendatarioNumerocheques;

    @Column(name = "ficvrestricao_arrend")
    private String restricaoArrendatario;

    @Column(name = "ficvserasa")
    private Integer serasa;

    @Column(name = "ficvnum_copiascheque")
    private Integer numeroCopiasCheque;

    @Column(name = "ficvcopiascheque")
    private String copiasCheque;

    @Column(name = "ficvnum_copiascheque_arrend")
    private Integer numeroCopiasChequeArrend;

    @Column(name = "ficvcopiascheque_arrend")
    private String copiasChequeArrend;

    @Column(name = "ficvefetuou_serasa")
    private String efetuou_serasa;

    @Column(name = "migracao")
    private Integer migracao;

    @Column(name = "ficvmotfunc")
    private String ficvmotfunc;

    @Column(name = "ficvcompravenda")
    private String compraVenda;

    @Column(name = "ficvarrendamento")
    private String arrendamento;

    @Column(name = "ficvufdivergente")
    private String ufDivergente;

    @Column(name = "ficvpropfisica")
    private String propFisica;

    @Column(name = "ficvantt_rntrc")
    private String antt_rntrc;

    @Column(name = "ficvantt_situacao")
    private String anttSituacao;

    @Column(name = "ficvantt_aval")
    private String anttAval;

    public FichaVeiculo() {}

    public FichaVeiculo(Veiculo veiculo, Proprietario proprietario, Endereco enderecoProprietario,
                        Integer numeroCheques, Date ficvultdataverificacao, Integer criminal, Integer foneVerificado,
                        String restricao, Proprietario arrendatario, Endereco enderecoArrendatario, Integer renavam,
                        Integer arrendatarioNumerocheques, String restricaoArrendatario, Integer serasa,
                        Integer numeroCopiasCheque, String copiasCheque, Integer numeroCopiasChequeArrend,
                        String copiasChequeArrend, String efetuou_serasa, Integer migracao, String ficvmotfunc,
                        String compraVenda, String arrendamento, String ufDivergente, String propFisica,
                        String antt_rntrc, String anttSituacao, String anttAval) {
        this.veiculo = veiculo;
        this.proprietario = proprietario;
        this.enderecoProprietario = enderecoProprietario;
        this.numeroCheques = numeroCheques;
        this.ficvultdataverificacao = ficvultdataverificacao;
        this.criminal = criminal;
        this.foneVerificado = foneVerificado;
        this.restricao = restricao;
        this.arrendatario = arrendatario;
        this.enderecoArrendatario = enderecoArrendatario;
        this.renavam = renavam;
        this.arrendatarioNumerocheques = arrendatarioNumerocheques;
        this.restricaoArrendatario = restricaoArrendatario;
        this.serasa = serasa;
        this.numeroCopiasCheque = numeroCopiasCheque;
        this.copiasCheque = copiasCheque;
        this.numeroCopiasChequeArrend = numeroCopiasChequeArrend;
        this.copiasChequeArrend = copiasChequeArrend;
        this.efetuou_serasa = efetuou_serasa;
        this.migracao = migracao;
        this.ficvmotfunc = ficvmotfunc;
        this.compraVenda = compraVenda;
        this.arrendamento = arrendamento;
        this.ufDivergente = ufDivergente;
        this.propFisica = propFisica;
        this.antt_rntrc = antt_rntrc;
        this.anttSituacao = anttSituacao;
        this.anttAval = anttAval;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Endereco getEnderecoProprietario() {
        return enderecoProprietario;
    }

    public void setEnderecoProprietario(Endereco enderecoProprietario) {
        this.enderecoProprietario = enderecoProprietario;
    }

    public Integer getNumeroCheques() {
        return numeroCheques;
    }

    public void setNumeroCheques(Integer numeroCheques) {
        this.numeroCheques = numeroCheques;
    }

    public Date getFicvultdataverificacao() {
        return ficvultdataverificacao;
    }

    public void setFicvultdataverificacao(Date ficvultdataverificacao) {
        this.ficvultdataverificacao = ficvultdataverificacao;
    }

    public Integer getCriminal() {
        return criminal;
    }

    public void setCriminal(Integer criminal) {
        this.criminal = criminal;
    }

    public Integer getFoneVerificado() {
        return foneVerificado;
    }

    public void setFoneVerificado(Integer foneVerificado) {
        this.foneVerificado = foneVerificado;
    }

    public String getRestricao() {
        return restricao;
    }

    public void setRestricao(String restricao) {
        this.restricao = restricao;
    }

    public Proprietario getArrendatario() {
        return arrendatario;
    }

    public void setArrendatario(Proprietario arrendatario) {
        this.arrendatario = arrendatario;
    }

    public Endereco getEnderecoArrendatario() {
        return enderecoArrendatario;
    }

    public void setEnderecoArrendatario(Endereco enderecoArrendatario) {
        this.enderecoArrendatario = enderecoArrendatario;
    }

    public Integer getRenavam() {
        return renavam;
    }

    public void setRenavam(Integer renavam) {
        this.renavam = renavam;
    }

    public Integer getArrendatarioNumerocheques() {
        return arrendatarioNumerocheques;
    }

    public void setArrendatarioNumerocheques(Integer arrendatarioNumerocheques) {
        this.arrendatarioNumerocheques = arrendatarioNumerocheques;
    }

    public String getRestricaoArrendatario() {
        return restricaoArrendatario;
    }

    public void setRestricaoArrendatario(String restricaoArrendatario) {
        this.restricaoArrendatario = restricaoArrendatario;
    }

    public Integer getSerasa() {
        return serasa;
    }

    public void setSerasa(Integer serasa) {
        this.serasa = serasa;
    }

    public Integer getNumeroCopiasCheque() {
        return numeroCopiasCheque;
    }

    public void setNumeroCopiasCheque(Integer numeroCopiasCheque) {
        this.numeroCopiasCheque = numeroCopiasCheque;
    }

    public String getCopiasCheque() {
        return copiasCheque;
    }

    public void setCopiasCheque(String copiasCheque) {
        this.copiasCheque = copiasCheque;
    }

    public Integer getNumeroCopiasChequeArrend() {
        return numeroCopiasChequeArrend;
    }

    public void setNumeroCopiasChequeArrend(Integer numeroCopiasChequeArrend) {
        this.numeroCopiasChequeArrend = numeroCopiasChequeArrend;
    }

    public String getCopiasChequeArrend() {
        return copiasChequeArrend;
    }

    public void setCopiasChequeArrend(String copiasChequeArrend) {
        this.copiasChequeArrend = copiasChequeArrend;
    }

    public String getEfetuou_serasa() {
        return efetuou_serasa;
    }

    public void setEfetuou_serasa(String efetuou_serasa) {
        this.efetuou_serasa = efetuou_serasa;
    }

    public Integer getMigracao() {
        return migracao;
    }

    public void setMigracao(Integer migracao) {
        this.migracao = migracao;
    }

    public String getFicvmotfunc() {
        return ficvmotfunc;
    }

    public void setFicvmotfunc(String ficvmotfunc) {
        this.ficvmotfunc = ficvmotfunc;
    }

    public String getCompraVenda() {
        return compraVenda;
    }

    public void setCompraVenda(String compraVenda) {
        this.compraVenda = compraVenda;
    }

    public String getArrendamento() {
        return arrendamento;
    }

    public void setArrendamento(String arrendamento) {
        this.arrendamento = arrendamento;
    }

    public String getUfDivergente() {
        return ufDivergente;
    }

    public void setUfDivergente(String ufDivergente) {
        this.ufDivergente = ufDivergente;
    }

    public String getPropFisica() {
        return propFisica;
    }

    public void setPropFisica(String propFisica) {
        this.propFisica = propFisica;
    }

    public String getAntt_rntrc() {
        return antt_rntrc;
    }

    public void setAntt_rntrc(String antt_rntrc) {
        this.antt_rntrc = antt_rntrc;
    }

    public String getAnttSituacao() {
        return anttSituacao;
    }

    public void setAnttSituacao(String anttSituacao) {
        this.anttSituacao = anttSituacao;
    }

    public String getAnttAval() {
        return anttAval;
    }

    public void setAnttAval(String anttAval) {
        this.anttAval = anttAval;
    }
}
