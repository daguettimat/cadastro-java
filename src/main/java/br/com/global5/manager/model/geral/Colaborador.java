package br.com.global5.manager.model.geral;

import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;

@Entity
@Table(name = "colaborador")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Colaborador implements BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "coloid")
    private Long id;

    @Column(name = "colnome")
    private String nome;

    @Column(name = "colcpfcnpj")
    private Double documento;

    @Column(name = "colendoid")
    private Long endereco;       // buscar o endereco

    @Column(name = "colcadastro")
    private java.sql.Date cadastro;

    @Column(name = "colexclusao")
    private java.sql.Date exclusao;

    @Column(name = "colemail")
    private String email;

    public Colaborador() {}



    public Colaborador Nome(String nome) {
        this.nome = nome;
        return this;
    }

    public boolean hasNome() {
        return nome != null && !"".equals(nome.trim());
    }


    public Colaborador(String nome, Double documento, Long endereco, Date cadastro, Date exclusao, String email) {
        this.nome = nome;
        this.documento = documento;
        this.endereco = endereco;
        this.cadastro = cadastro;
        this.exclusao = exclusao;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getDocumento() {
        return documento;
    }

    public void setDocumento(Double documento) {
        this.documento = documento;
    }

    public Long getEndereco() {
        return endereco;
    }

    public void setEndereco(Long endereco) {
        this.endereco = endereco;
    }

    public Date getCadastro() {
        return cadastro;
    }

    public void setCadastro(Date cadastro) {
        this.cadastro = cadastro;
    }

    public Date getExclusao() {
        return exclusao;
    }

    public void setExclusao(Date exclusao) {
        this.exclusao = exclusao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
