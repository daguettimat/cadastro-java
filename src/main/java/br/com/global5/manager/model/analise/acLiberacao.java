package br.com.global5.manager.model.analise;

import br.com.global5.infra.model.BaseEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "analise_cadastral_liberacao")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@NamedStoredProcedureQuery(
        name = "stpLiberacao",
        procedureName = "analise_cadastral_liberacao_i",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "ficha"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class, name = "id")
        }
)
public class acLiberacao implements BaseEntity {

    @Id
    @SequenceGenerator(name = "analise_cadastral_liberacao_aliboid_seq", sequenceName = "analise_cadastral_liberacao_aliboid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "analise_cadastral_liberacao_aliboid_seq")
    @Column(name = "aliboid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=acCadastro.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="alib_anacoid")
    private acCadastro cadastro;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "alib_anacoid_dt_registro")
    private Date dtRegistroFicha;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "alib_dt_liberacao")
    private Date dtLiberacao;


    @Column(name = "alib_certificado")
    private String certificado;

    @Column(name = "alib_chave")
    private String chave;

    public acLiberacao() {}

    public acLiberacao(Integer id) {
        this.id = id;
    }

    public acLiberacao(acCadastro cadastro, Date dtRegistroFicha, Date dtLiberacao, String certificado, String chave) {
        this.cadastro = cadastro;
        this.dtRegistroFicha = dtRegistroFicha;
        this.dtLiberacao = dtLiberacao;
        this.certificado = certificado;
        this.chave = chave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public acCadastro getCadastro() {
        return cadastro;
    }

    public void setCadastro(acCadastro cadastro) {
        this.cadastro = cadastro;
    }

    public Date getDtRegistroFicha() {
        return dtRegistroFicha;
    }

    public void setDtRegistroFicha(Date dtRegistroFicha) {
        this.dtRegistroFicha = dtRegistroFicha;
    }

    public Date getDtLiberacao() {
        return dtLiberacao;
    }

    public void setDtLiberacao(Date dtLiberacao) {
        this.dtLiberacao = dtLiberacao;
    }

    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public acLiberacao Chave(String chave) {
        this.chave = chave;
        return this;
    }

    public boolean hasChave() {
        return chave != null && !"".equals(chave.trim());
    }


}
