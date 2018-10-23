package br.com.global5.manager.model.analise;

import br.com.global5.infra.model.BaseEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@Entity
@Table(name = "analise_cadastral_recomendacao")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class acRecomendacao implements BaseEntity {

    @Id
    @SequenceGenerator(name = "analise_cadastral_recomendacao_anacroid_seq", sequenceName = "analise_cadastral_recomendacao_anacroid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "analise_cadastral_recomendacao_anacroid_seq")
    @Column(name = "anacroid")
    private Integer id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = acCadastro.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "anacr_anacoid")
    private acCadastro acCadastro;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = acRecomendacao.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "anacr_recoid")
    private acRecomendacao acRecomendacao;

    @Column(name = "anacr_exclusao")
    private java.sql.Timestamp dtExclusao;

    public acRecomendacao() {}

    public acRecomendacao(acCadastro acCadastro, acRecomendacao acRecomendacao, Timestamp dtExclusao) {
        this.acCadastro = acCadastro;
        this.acRecomendacao = acRecomendacao;
        this.dtExclusao = dtExclusao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public acCadastro getAcCadastro() {
        return acCadastro;
    }

    public void setAcCadastro(acCadastro acCadastro) {
        this.acCadastro = acCadastro;
    }

    public acRecomendacao getAcRecomendacao() {
        return acRecomendacao;
    }

    public void setAcRecomendacao(acRecomendacao acRecomendacao) {
        this.acRecomendacao = acRecomendacao;
    }

    public Timestamp getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Timestamp dtExclusao) {
        this.dtExclusao = dtExclusao;
    }
}
