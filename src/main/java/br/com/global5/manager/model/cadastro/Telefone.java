package br.com.global5.manager.model.cadastro;


import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.enums.TelefoneOrigem;
import br.com.global5.manager.model.enums.TelefoneTipo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "telefone")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Telefone implements BaseEntity {

    @Id
    @SequenceGenerator(name = "telefone_teloid_seq", sequenceName = "telefone_teloid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "telefone_teloid_seq")
    @Column(name = "teloid")
    private Integer id;

    @Column(name = "telddd")
    private String ddd;

    @Column(name = "telfone")
    private String fone;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=TelefoneTipo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="tel_tipo")
    private TelefoneTipo tipo;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=TelefoneOrigem.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="tel_origem")
    private TelefoneOrigem origem;

    @Column(name = "tel_refoid")
    private Integer referencia;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="tel_dt_criacao")
    private Date dtCriacao;

    public Telefone() {}

    public Telefone(Integer id) {
        this.id = id;
    }

    public Telefone(String ddd, String fone, TelefoneTipo tipo, TelefoneOrigem origem, Integer referencia, Date dtCriacao) {
        this.ddd = ddd;
        this.fone = fone;
        this.tipo = tipo;
        this.origem = origem;
        this.referencia = referencia;
        this.dtCriacao = dtCriacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public TelefoneTipo getTipo() {
        return tipo;
    }

    public void setTipo(TelefoneTipo tipo) {
        this.tipo = tipo;
    }

    public TelefoneOrigem getOrigem() {
        return origem;
    }

    public void setOrigem(TelefoneOrigem origem) {
        this.origem = origem;
    }

    public Integer getReferencia() {
        return referencia;
    }

    public void setReferencia(Integer referencia) {
        this.referencia = referencia;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }
}
