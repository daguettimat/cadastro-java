package br.com.global5.manager.model.apoio;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Localizador;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.manager.model.enums.Bandeiras;
import br.com.global5.manager.model.enums.TipoApoio;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "pontos_de_apoio")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PontosApoio implements BaseEntity {

    @Id
    @SequenceGenerator(name = "pontos_de_apoio_ptosoid_seq", sequenceName = "pontos_de_apoio_ptosoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "pontos_de_apoio_ptosoid_seq")
    @Column(name = "ptosoid")
    private Integer id;

    @CNPJ
    @Column(name = "ptos_cnpj")
    private String cnpj;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=TipoApoio.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptos_tipoid")
    private TipoApoio tipoApoio;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Bandeiras.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptos_banoid")
    private Bandeiras bandeira;



    @Column(name = "ptos_nome_fantasia")
    private String nomeFantasia;

    @Column(name = "ptos_razao_social")
    private String razaoSocial;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Telefone.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptos_fixo_teloid")
    private Telefone telFixo;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Telefone.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptos_publico_teloid")
    private Telefone telPublico;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Telefone.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptos_celular_teloid")
    private Telefone telCelular;

    @Email
    @Column(name = "ptos_email")
    private String email;

    @Column(name = "ptos_web")
    private String web;

    @Column(name = "ptos_responsavel_informacao")
    private String responsavel;

    @Column(name = "ptos_cargo_responsavel")
    private String cargo;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Localizador.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptos_locoid")
    private Localizador endereco;

    @Column(name = "ptos_broid")
    private Integer rodovia;

    @Column(name = "ptos_latitude")
    private double latitude;

    @Column(name = "ptos_longitude")
    private double logintude;

    @Column(name = "ptos_observacoes")
    private String observacoes;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptos_usuoid_criacao")
    private Usuario usuarioCriacao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptos_usuoid_alteracao")
    private Usuario usuarioAlteracao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ptos_usuoid_exclusao")
    private Usuario usuarioExclusao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ptos_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ptos_dt_exclusao")
    private Date dtExclusao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ptos_dt_alteracao")
    private Date dtAlteracao;

    public PontosApoio() {}

    public PontosApoio(Integer id) {
        this.id = id;
    }

    public PontosApoio(String cnpj, TipoApoio tipoApoio, Bandeiras bandeira, String nomeFantasia, String razaoSocial,
                       Telefone telFixo, Telefone telPublico, Telefone telCelular, String email, String web,
                       String responsavel, String cargo, Localizador endereco, Integer rodovia,
                       double latitude, double logintude, String observacoes, Usuario usuarioCriacao,
                       Usuario usuarioAlteracao, Usuario usuarioExclusao, Date dtCriacao, Date dtExclusao,
                       Date dtAlteracao) {
        this.cnpj = cnpj;
        this.tipoApoio = tipoApoio;
        this.bandeira = bandeira;
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.telFixo = telFixo;
        this.telPublico = telPublico;
        this.telCelular = telCelular;
        this.email = email;
        this.web = web;
        this.responsavel = responsavel;
        this.cargo = cargo;
        this.endereco = endereco;
        this.rodovia = rodovia;
        this.latitude = latitude;
        this.logintude = logintude;
        this.observacoes = observacoes;
        this.usuarioCriacao = usuarioCriacao;
        this.usuarioAlteracao = usuarioAlteracao;
        this.usuarioExclusao = usuarioExclusao;
        this.dtCriacao = dtCriacao;
        this.dtExclusao = dtExclusao;
        this.dtAlteracao = dtAlteracao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public TipoApoio getTipoApoio() {
        return tipoApoio;
    }

    public void setTipoApoio(TipoApoio tipoApoio) {
        this.tipoApoio = tipoApoio;
    }

    public Bandeiras getBandeira() {
        return bandeira;
    }

    public void setBandeira(Bandeiras bandeira) {
        this.bandeira = bandeira;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public Telefone getTelFixo() {
        return telFixo;
    }

    public void setTelFixo(Telefone telFixo) {
        this.telFixo = telFixo;
    }

    public Telefone getTelPublico() {
        return telPublico;
    }

    public void setTelPublico(Telefone telPublico) {
        this.telPublico = telPublico;
    }

    public Telefone getTelCelular() {
        return telCelular;
    }

    public void setTelCelular(Telefone telCelular) {
        this.telCelular = telCelular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Localizador getEndereco() {
        return endereco;
    }

    public void setEndereco(Localizador endereco) {
        this.endereco = endereco;
    }

    public Integer getRodovia() {
        return rodovia;
    }

    public void setRodovia(Integer rodovia) {
        this.rodovia = rodovia;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLogintude() {
        return logintude;
    }

    public void setLogintude(double logintude) {
        this.logintude = logintude;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Usuario getUsuarioCriacao() {
        return usuarioCriacao;
    }

    public void setUsuarioCriacao(Usuario usuarioCriacao) {
        this.usuarioCriacao = usuarioCriacao;
    }

    public Usuario getUsuarioAlteracao() {
        return usuarioAlteracao;
    }

    public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
        this.usuarioAlteracao = usuarioAlteracao;
    }

    public Usuario getUsuarioExclusao() {
        return usuarioExclusao;
    }

    public void setUsuarioExclusao(Usuario usuarioExclusao) {
        this.usuarioExclusao = usuarioExclusao;
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

    public Date getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Date dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }
}
