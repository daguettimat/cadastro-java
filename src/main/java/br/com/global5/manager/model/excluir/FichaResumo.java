package br.com.global5.manager.model.excluir;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Transportadora;
import br.com.global5.manager.model.geral.Motorista;
import br.com.global5.manager.model.cadastro.Veiculo;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "ficha_resumo")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class FichaResumo implements BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "firfichaoid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Transportadora.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="firtraoid")
    private Transportadora transportadora;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Motorista.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="firmotoid")
    private Motorista motorista;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Veiculo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="firveioid")
    private Veiculo veiculo;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Veiculo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="firreb1oid")
    private Veiculo primeiroReboque;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Veiculo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="firreb2oid")
    private Veiculo segundoReboque;

    @Column(name = "firdata")
    private java.sql.Date data;

    @Column(name = "firdiasvalidade")
    private Integer diasValidade;

    @Column(name = "firrestr_cad")
    private Integer restricaoCadastro;

    @Column(name = "firalteracao")
    private String alteracao;

    @Column(name = "firdtalteracao")
    private java.sql.Timestamp dtAlteracao;

    @Column(name = "firobservacao")
    private String observacao;

    @Column(name = "migracao")
    private Integer migracao;

    public FichaResumo() {}

    public FichaResumo(Transportadora transportadora, Motorista motorista, Veiculo veiculo, Veiculo primeiroReboque,
                       Veiculo segundoReboque, Date data, Integer diasValidade, Integer restricaoCadastro,
                       String alteracao, Timestamp dtAlteracao, String observacao, Integer migracao) {
        this.transportadora = transportadora;
        this.motorista = motorista;
        this.veiculo = veiculo;
        this.primeiroReboque = primeiroReboque;
        this.segundoReboque = segundoReboque;
        this.data = data;
        this.diasValidade = diasValidade;
        this.restricaoCadastro = restricaoCadastro;
        this.alteracao = alteracao;
        this.dtAlteracao = dtAlteracao;
        this.observacao = observacao;
        this.migracao = migracao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Veiculo getPrimeiroReboque() {
        return primeiroReboque;
    }

    public void setPrimeiroReboque(Veiculo primeiroReboque) {
        this.primeiroReboque = primeiroReboque;
    }

    public Veiculo getSegundoReboque() {
        return segundoReboque;
    }

    public void setSegundoReboque(Veiculo segundoReboque) {
        this.segundoReboque = segundoReboque;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getDiasValidade() {
        return diasValidade;
    }

    public void setDiasValidade(Integer diasValidade) {
        this.diasValidade = diasValidade;
    }

    public Integer getRestricaoCadastro() {
        return restricaoCadastro;
    }

    public void setRestricaoCadastro(Integer restricaoCadastro) {
        this.restricaoCadastro = restricaoCadastro;
    }

    public String getAlteracao() {
        return alteracao;
    }

    public void setAlteracao(String alteracao) {
        this.alteracao = alteracao;
    }

    public Timestamp getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Timestamp dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getMigracao() {
        return migracao;
    }

    public void setMigracao(Integer migracao) {
        this.migracao = migracao;
    }
}
