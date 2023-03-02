package br.com.global5.manager.model.geral;

import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@Entity
@Table(name = "java.mercadoria")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Mercadoria implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meroid")
    private Integer id;

    @Column(name = "mer_nome")
    private String descricao;

    @Temporal(TemporalType.DATE)
    @Column(name = "mer_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.DATE)
    @Column(name = "mer_dt_exclusao")
    private Date dtExclusao;

    @Column(name = "mer_risco")
    private Boolean risco;

    public Mercadoria() {
    }


    public Mercadoria(Integer id) {
        this.id = id;
    }

    public Mercadoria descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public boolean hasDescricao() {
        return descricao != null && !"".equals(descricao.trim());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public Boolean getRisco() {
        return risco;
    }

    public void setRisco(Boolean risco) {
        this.risco = risco;
    }
}
