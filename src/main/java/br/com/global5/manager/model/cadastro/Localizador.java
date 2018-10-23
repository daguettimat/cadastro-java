package br.com.global5.manager.model.cadastro;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.auxiliar.TipoEndereco;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "localizador")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NamedQueries(value = {
        @NamedQuery( name = "localizador.findByCEP",
                query = "SELECT l " +
                        "  FROM Localizador l" +
                        " WHERE l.cep = :cep" +
                        "   AND l.numero = :numero" ) } )

public class Localizador implements BaseEntity {

    @Transient
    public static final String FIND_CEP =
            "localizador.findByCEP";

    @Id
    @SequenceGenerator(name = "localizador_locoid_seq", sequenceName = "localizador_locoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "localizador_locoid_seq")
    @Column(name = "locoid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=TipoEndereco.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="loc_tipo")
    private TipoEndereco tipoEndereco;

    @Column(name = "loc_logradouro")
    private String logradouro;

    @Column(name = "loc_numero")
    private Integer numero;

    @Column(name = "loc_complemento")
    private String complemento;

    @Column(name = "loc_bairro")
    private String bairro;

    @Column(name = "loc_cidade")
    private String cidade;

    @Column(name = "loc_uf")
    private String uf;

    @Column(name = "loc_pais")
    private String pais;

    @Column(name = "loc_cep")
    private String cep;

    public Localizador() {}

    public Localizador( Integer id ) {
        this.id = id;
    }

    public Localizador(TipoEndereco tipoEndereco, String logradouro, Integer numero, String complemento,
                       String bairro, String cidade, String uf, String pais, String cep) {
        this.tipoEndereco = tipoEndereco;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.pais = pais;
        this.cep = cep;
    }

    public Localizador nome(String logradouro) {
        this.logradouro = logradouro;
        return this;
    }

    public boolean hasNome() {
        return logradouro != null && !"".equals(logradouro.trim());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoEndereco getTipoEndereco() {
        return tipoEndereco;
    }

    public void setTipoEndereco(TipoEndereco tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCep() {
        if(cep == null) {
            return "00000-000";
        } else {
            return cep;
        }
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
