package br.com.global5.manager.model.geral;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.enums.ImagemTipo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "imagem")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Imagem implements BaseEntity {

    @Id
    @SequenceGenerator(name="imagem_imgoid_seq",
            sequenceName="imagem_imgoid_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="imagem_imgoid_seq")
    @Column(name = "imgoid")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "img_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "img_dt_expiracao")
    private Date dtExpiracao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "img_dt_exclusao")
    private Date dtExclusao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ImagemTipo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "img_enum_tipo")
    private ImagemTipo imgTipo;

    @Column(name = "img_url_relativa")
    private String urlRelativa;

    @Column(name = "img_mime_type")
    private String mimeType;

    @Column(name = "img_enum_modulo")
    private Integer modulo;

    @Column(name = "img_item_modulo")
    private Integer itemModulo;

    public Imagem() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtExpiracao() {
        return dtExpiracao;
    }

    public void setDtExpiracao(Date dtExpiracao) {
        this.dtExpiracao = dtExpiracao;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public ImagemTipo getImgTipo() {
        return imgTipo;
    }

    public void setImgTipo(ImagemTipo imgTipo) {
        this.imgTipo = imgTipo;
    }

    public String getUrlRelativa() {
        return urlRelativa;
    }

    public void setUrlRelativa(String urlRelativa) {
        this.urlRelativa = urlRelativa;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getModulo() {
        return modulo;
    }

    public void setModulo(Integer modulo) {
        this.modulo = modulo;
    }

    public Integer getItemModulo() {
        return itemModulo;
    }

    public void setItemModulo(Integer itemModulo) {
        this.itemModulo = itemModulo;
    }
}
