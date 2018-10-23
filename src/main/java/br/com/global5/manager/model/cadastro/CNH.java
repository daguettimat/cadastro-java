package br.com.global5.manager.model.cadastro;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Motorista;
import br.com.global5.manager.model.enums.CNHCategoria;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@Entity
@Table(name = "motorista_cnh")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CNH implements BaseEntity {

    @Id
    @SequenceGenerator(name = "motorista_cnh_mcnhoid_seq", sequenceName = "motorista_cnh_mcnhoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "motorista_cnh_mcnhoid_seq")
    @Column(name = "mcnhoid")
    private Integer id;

    @Column(name = "mcnh_documento")
    private String documento;

    @Column(name = "mcnh_primeira_emissao")
    private Date dtPrimeiraEmissao;

    @Column(name = "mcnh_uf")
    private String uf;

    @Column(name = "mcnh_registro")
    private String registro;

    @Temporal(TemporalType.DATE)
    @Column(name = "mcnh_dt_registro")
    private Date dtRegistro;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=CNHCategoria.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )

    @JoinColumn(name = "mcnh_categoria")
    private CNHCategoria categoria;

    @Column(name = "mcnh_validacao")
    private String validacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mcnh_cadastro")
    private Date dtCadastro;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mcnh_atualizacao")
    private Date dtAtualizacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mcnh_substituicao")
    private Date dtSubstituicao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Motorista.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="mcnh_motoid")
    private Motorista motorista;

    @Column(name = "mcnh_dt_vencimento")
    private Date dtVencimento;

    @Column( name = "mcnh_urlcnhfrente")
    private String urlCnhFrente;

    @Column( name = "mcnh_urlcnhverso")
    private String urlCnhVerso;

    public CNH() {}

    public CNH(Integer id) {
        this.id = id;
    }

    public CNH(String documento, Date dtPrimeiraEmissao, String uf, String registro, Date dtRegistro,
               CNHCategoria categoria, String validacao, Date dtCadastro, Date dtAtualizacao, Date dtSubstituicao,
               Motorista motorista, Date dtVencimento, String urlCnhFrente, String urlCnhVerso) {
        this.documento = documento;
        this.dtPrimeiraEmissao = dtPrimeiraEmissao;
        this.uf = uf;
        this.registro = registro;
        this.dtRegistro = dtRegistro;
        this.categoria = categoria;
        this.validacao = validacao;
        this.dtCadastro = dtCadastro;
        this.dtAtualizacao = dtAtualizacao;
        this.dtSubstituicao = dtSubstituicao;
        this.motorista = motorista;
        this.dtVencimento = dtVencimento;
        this.urlCnhFrente = urlCnhFrente;
        this.urlCnhVerso = urlCnhVerso;
    }

    public CNH documento(String documento) {
        this.documento = documento;
        return this;
    }

    public boolean hasDocumento() {
        return documento != null && !"".equals(documento.trim());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getDtPrimeiraEmissao() {
        return dtPrimeiraEmissao;
    }

    public void setDtPrimeiraEmissao(Date dtPrimeiraEmissao) {
        this.dtPrimeiraEmissao = dtPrimeiraEmissao;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public Date getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(Date dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public CNHCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(CNHCategoria categoria) {
        this.categoria = categoria;
    }

    public String getValidacao() {
        return validacao;
    }

    public void setValidacao(String validacao) {
        this.validacao = validacao;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Date getDtAtualizacao() {
        return dtAtualizacao;
    }

    public void setDtAtualizacao(Date dtAtualizacao) {
        this.dtAtualizacao = dtAtualizacao;
    }

    public Date getDtSubstituicao() {
        return dtSubstituicao;
    }

    public void setDtSubstituicao(Date dtSubstituicao) {
        this.dtSubstituicao = dtSubstituicao;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public String getUrlCnhFrente() {
        return urlCnhFrente;
    }

    public void setUrlCnhFrente(String urlCnhFrente) {
        this.urlCnhFrente = urlCnhFrente;
    }

    public String getUrlCnhVerso() {
        return urlCnhVerso;
    }

    public void setUrlCnhVerso(String urlCnhVerso) {
        this.urlCnhVerso = urlCnhVerso;
    }



}
