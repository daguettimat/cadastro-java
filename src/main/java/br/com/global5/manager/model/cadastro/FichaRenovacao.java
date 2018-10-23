package br.com.global5.manager.model.cadastro;

import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.json.JsonStringType;
import br.com.global5.infra.model.BaseEntity;
import br.com.global5.infra.model.TipoValor;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.enums.MotoristaVinculo;
import br.com.global5.manager.model.enums.RenovacaoStatus;
import br.com.global5.manager.model.geral.Motorista;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ficha_renovacao")

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)

@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FichaRenovacao implements BaseEntity {

    public class lVeiculo implements Serializable {

        private int tipo;
        private String placa;
        private int veioid;

        public int getTipo() {
            return tipo;
        }

        public void setTipo(int tipo) {
            this.tipo = tipo;
        }

        public int getVeioid() {
            return veioid;
        }

        public void setVeioid(int veioid) {
            this.veioid = veioid;
        }

        public String getPlaca() {
            return placa;
        }

        public void setPlaca(String placa) {
            this.placa = placa;
        }
    }

    public class analiseRetorno implements Serializable {

        private int ficha;
        private int npartes;
        private List<TipoValor> tipoValor;

        public analiseRetorno() {}

        public analiseRetorno(int ficha, int npartes, List<TipoValor> tipoValor) {
            this.ficha = ficha;
            this.npartes = npartes;
            this.tipoValor = tipoValor;
        }

        public int getFicha() {
            return ficha;
        }

        public void setFicha(int ficha) {
            this.ficha = ficha;
        }

        public int getNpartes() {
            return npartes;
        }

        public void setNpartes(int npartes) {
            this.npartes = npartes;
        }

        public List<TipoValor> getTipoValor() {
            return tipoValor;
        }

        public void setTipoValor(List<TipoValor> tipoValor) {
            this.tipoValor = tipoValor;
        }
    }



//    {
//        "ficha": 1263786,
//            "npartes": 2,
//            "partes": [{
//        "tipo": 137,
//                "valor": 2900094
//    }, {
//        "tipo": 46,
//                "valor": 2900095
//    }],
//        "novo_reboque": false
//    }

    @Id
    @SequenceGenerator(name = "ficha_renovacao_ficroid_seq", sequenceName = "ficha_renovacao_ficroid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ficha_renovacao_ficroid_seq")
    @Column(name = "ficroid")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ficr_dt_registro")
    private Date dtRegistro;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ficr_dt_resposta")
    private Date dtResposta;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficr_usuoid_resposta")
    private Usuario usuarioResposta;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=RenovacaoStatus.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficr_status")
    private RenovacaoStatus status;

//    @Type(type = "jsonb")
//    @Column(name = "ficr_anac_retorno", columnDefinition = "jsonb")
//    private analiseRetorno analiseRetorno;

    @Column(name="ficr_anacoid_origem")
    private int ficr_anacoid_origem;

    @Temporal(TemporalType.DATE)
    @Column(name = "ficr_anacoid_vencimento")
    private Date dtVencimento;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Area.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficr_anacoid_cliente")
    private Area clienteArea;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Area.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficr_anacoid_filial")
    private Area filialArea;

    @Column(name="ficr_anacoid_centro_custo")
    private String centroCusto;

    @Temporal(TemporalType.DATE)
    @Column(name = "ficr_vencimento")
    private Date rVencimento;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Motorista.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficr_motoid")
    private Motorista motorista;

    @Column(name="ficr_motcpf")
    private String cpf;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=MotoristaVinculo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficr_mot_vinculo")
    private MotoristaVinculo motVinculo;



    @Type(type = "jsonb")
    @Column(name = "ficr_veiculos", columnDefinition = "jsonb")
    private List<FichaRenovacao.lVeiculo> veiculos;


    public FichaRenovacao() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(Date dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public Date getDtResposta() {
        return dtResposta;
    }

    public void setDtResposta(Date dtResposta) {
        this.dtResposta = dtResposta;
    }

    public Usuario getUsuarioResposta() {
        return usuarioResposta;
    }

    public void setUsuarioResposta(Usuario usuarioResposta) {
        this.usuarioResposta = usuarioResposta;
    }

    public RenovacaoStatus getStatus() {
        return status;
    }

    public void setStatus(RenovacaoStatus status) {
        this.status = status;
    }

//    public FichaRenovacao.analiseRetorno getAnaliseRetorno() {
//        return analiseRetorno;
//    }
//
//    public void setAnaliseRetorno(FichaRenovacao.analiseRetorno analiseRetorno) {
//        this.analiseRetorno = analiseRetorno;
//    }

    public int getFicr_anacoid_origem() {
        return ficr_anacoid_origem;
    }

    public void setFicr_anacoid_origem(int ficr_anacoid_origem) {
        this.ficr_anacoid_origem = ficr_anacoid_origem;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
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

    public Date getrVencimento() {
        return rVencimento;
    }

    public void setrVencimento(Date rVencimento) {
        this.rVencimento = rVencimento;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public MotoristaVinculo getMotVinculo() {
        return motVinculo;
    }

    public void setMotVinculo(MotoristaVinculo motVinculo) {
        this.motVinculo = motVinculo;
    }

    public List<lVeiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<lVeiculo> veiculos) {
        this.veiculos = veiculos;
    }
}
