package br.com.global5.manager.model.apoio;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.enums.FerramentasSeguranca;
import br.com.global5.manager.model.enums.HorarioFuncionamento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "pontos_apoio_servico_seguranca")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ServicosSeguranca implements BaseEntity {


    @Id
    @SequenceGenerator(name = "pontos_apoio_servico_ptosrvoid_seq",
                       sequenceName = "pontos_apoio_servico_ptosrvoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "pontos_apoio_servico_ptosrvoid_seq")
    @Column(name = "ptosrvoid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=PontosApoio.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptosrv_ptosoid")
    private PontosApoio pontosApoio;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=FerramentasSeguranca.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptosrv_enumoid")
    private FerramentasSeguranca tipo;

    @Column(name = "ptosrv_disponivel")
    private boolean disponivel;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=HorarioFuncionamento.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptosrv_horario")
    private HorarioFuncionamento horarioFuncionamento;

    @Column(name = "ptosrv_valor")
    private double valor;

    @Column(name = "ptosrv_observacoes")
    private String observacoes;

    public ServicosSeguranca() {}

    public ServicosSeguranca(Integer id) {
        this.id = id;
    }

    public ServicosSeguranca(int id, PontosApoio pontosApoio, int tipo, boolean disponivel, HorarioFuncionamento horarioFuncionamento,
                             double valor, String observacoes) {
        this.id = id;
        this.pontosApoio = pontosApoio;
        this.tipo = new FerramentasSeguranca(tipo);
        this.disponivel = disponivel;
        this.horarioFuncionamento = horarioFuncionamento;
        this.valor = valor;
        this.observacoes = observacoes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PontosApoio getPontosApoio() {
        return pontosApoio;
    }

    public void setPontosApoio(PontosApoio pontosApoio) {
        this.pontosApoio = pontosApoio;
    }

    public FerramentasSeguranca getTipo() {
        return tipo;
    }

    public void setTipo(FerramentasSeguranca tipo) {
        this.tipo = tipo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public HorarioFuncionamento getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(HorarioFuncionamento horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
