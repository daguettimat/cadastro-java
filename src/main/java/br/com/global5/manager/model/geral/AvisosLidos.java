package br.com.global5.manager.model.geral;

import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "quadro_de_avisos_lidos")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AvisosLidos implements BaseEntity {

    @Id
    @SequenceGenerator(name = "quadro_de_avisos_lidos_qavsloid_seq", sequenceName = "quadro_de_avisos_lidos_qavsloid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "quadro_de_avisos_lidos_qavsloid_seq")
    @Column(name = "qavsloid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=Avisos.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="qavsl_qavsoid")
    private Avisos aviso;

    @Column(name = "qavsl_dt_criacao")
    private Date dtCriacao;

    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="qavsl_usuoid")
    private Usuario usuarioCriacao;

    public AvisosLidos() {}

    public AvisosLidos(int id) {
        this.id = id;
    }

    public AvisosLidos(Avisos aviso, Date dtCriacao, Usuario usuarioCriacao) {
        this.aviso = aviso;
        this.dtCriacao = dtCriacao;
        this.usuarioCriacao = usuarioCriacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Avisos getAviso() {
        return aviso;
    }

    public void setAviso(Avisos aviso) {
        this.aviso = aviso;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Usuario getUsuarioCriacao() {
        return usuarioCriacao;
    }

    public void setUsuarioCriacao(Usuario usuarioCriacao) {
        this.usuarioCriacao = usuarioCriacao;
    }
}
