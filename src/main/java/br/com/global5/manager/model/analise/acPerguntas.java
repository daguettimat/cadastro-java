package br.com.global5.manager.model.analise;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.ReferenciasPerguntas;
import br.com.global5.manager.model.enums.ReferenciasAvaliacao;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "analise_cadastral_perguntas")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class acPerguntas implements BaseEntity {

    @Id
    @SequenceGenerator(name = "analise_cadastral_perguntas_apergoid_seq", sequenceName = "analise_cadastral_perguntas_apergoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "analise_cadastral_perguntas_apergoid_seq")
    @Column(name = "apergoid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=acReferencia.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="aperg_arefoid")
    private acReferencia acReferencia;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasPerguntas.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="aperg_pergoid")
    private ReferenciasPerguntas pergunta;

    @Column(name = "aperg_resposta")
    private String resposta;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="aperg_avaliacao")

    private ReferenciasAvaliacao avaliacao;

    public acPerguntas() {}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public acReferencia getAcReferencia() {
        return acReferencia;
    }

    public void setAcReferencia(acReferencia acReferencia) {
        this.acReferencia = acReferencia;
    }

    public ReferenciasPerguntas getPergunta() {
        return pergunta;
    }

    public void setPergunta(ReferenciasPerguntas pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public ReferenciasAvaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(ReferenciasAvaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }
}
