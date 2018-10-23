package br.com.global5.manager.model.auxiliar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zielinski on 05/07/17.
 */
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class ImgJson implements Serializable {

    @Id
    private int id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date    dtCriacao;
    private String  urlRelativa;

    public ImgJson() {}

    public ImgJson(int id, Date dtCriacao, String urlRelativa) {
        this.id = id;
        this.dtCriacao = dtCriacao;
        this.urlRelativa = urlRelativa;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public String getUrlRelativa() {
        return urlRelativa;
    }

    public void setUrlRelativa(String urlRelativa) {
        this.urlRelativa = urlRelativa;
    }



}
