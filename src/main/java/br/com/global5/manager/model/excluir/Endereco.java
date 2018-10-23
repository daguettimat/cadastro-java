package br.com.global5.manager.model.excluir;

import br.com.global5.infra.model.BaseEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "endereco")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Endereco implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "endoid")
    private Integer id;

    @Column(name = "endrua")
    private String rua;

    @Column(name = "endnumero")
    private Integer numero;

    @Column(name = "endcomplemento")
    private String complemento;

    @Column(name = "endbairro")
    private String bairro;

    @Column(name = "endcidade")
    private String cidade;

    @Column(name = "enduf")
    private String uf;

    @Column(name = "endcep")
    private Integer cep;

    @Column(name = "endddd")
    private Integer ddd;

    @Column(name = "endfone")
    private Integer fone;

    @Column(name = "endfax")
    private Integer fax;

    @Column(name = "enddddcel")
    private Integer dddcel;

    @Column(name = "endcel")
    private Integer celular;

    @Column(name = "endpais")
    private Integer pais;

    @Column(name = "endemail")
    private String email;


    public Endereco() {}

    public Endereco(String rua, Integer numero, String complemento, String bairro, String cidade, String uf,
                    Integer cep, Integer ddd, Integer fone, Integer fax, Integer dddcel, Integer celular,
                    Integer pais, String email) {
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
        this.ddd = ddd;
        this.fone = fone;
        this.fax = fax;
        this.dddcel = dddcel;
        this.celular = celular;
        this.pais = pais;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public Integer getDdd() {
        return ddd;
    }

    public void setDdd(Integer ddd) {
        this.ddd = ddd;
    }

    public Integer getFone() {
        return fone;
    }

    public void setFone(Integer fone) {
        this.fone = fone;
    }

    public Integer getFax() {
        return fax;
    }

    public void setFax(Integer fax) {
        this.fax = fax;
    }

    public Integer getDddcel() {
        return dddcel;
    }

    public void setDddcel(Integer dddcel) {
        this.dddcel = dddcel;
    }

    public Integer getCelular() {
        return celular;
    }

    public void setCelular(Integer celular) {
        this.celular = celular;
    }

    public Integer getPais() {
        return pais;
    }

    public void setPais(Integer pais) {
        this.pais = pais;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
