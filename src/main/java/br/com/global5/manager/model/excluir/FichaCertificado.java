package br.com.global5.manager.model.excluir;


import br.com.global5.infra.model.BaseEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@Entity
@Table(name = "ficha_certificado")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)

public class FichaCertificado implements BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ficoid")
    private Integer id;

    @Column(name = "datageracaocertificado")
    private java.sql.Timestamp dataGeracao;

    @Column(name = "certificado")
    private String certificado;

    @Column(name = "chave")
    private String chave;

    public FichaCertificado() {}

    public FichaCertificado(Integer id, Timestamp dataGeracao, String certificado, String chave) {
        this.id = id;
        this.dataGeracao = dataGeracao;
        this.certificado = certificado;
        this.chave = chave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(Timestamp dataGeracao) {
        this.dataGeracao = dataGeracao;
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
}
