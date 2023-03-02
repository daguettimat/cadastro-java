package br.com.global5.manager.model.geral;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Pessoa;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

//
//SELECT
//        areaoid,
//        area_nome,
//        area_anvloid,
//        CASE WHEN area_anvloid = 3
//        THEN area_areaoid_pai
//        ELSE areaoid END AS pai
//        FROM area, area_funcao, pessoa, usuario
//        WHERE usuoid = 7278 AND usu_pesoid = pesoid AND pes_afunoid = afunoid AND afun_areaoid = areaoid;
//
//

@Entity
@Table(name = "usuario")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@NamedQueries(value = { @NamedQuery( name = "Usuario.findByLoginSenha",
                                    query = "SELECT c FROM Usuario c WHERE upper(c.login) = :login AND c.passwd = :senha AND c.exclusao IS NULL"),
                        @NamedQuery( name = "Usuario.findInternos",
                                    query = "SELECT c FROM Usuario c WHERE c.exclusao is null AND c.tipo = 'O' order by c.nome"),
                        @NamedQuery( name = "Usuario.findArea",
                                    query = "SELECT c, p, a, af FROM Usuario c, Pessoa p, Area a, AreaFuncao af " +
                                                    "WHERE c.exclusao is null " +
                                                      "AND c.id = :id " +
                                                      "AND p.id = a.id " +
                                                      "AND a.id = af.id") } )
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario implements BaseEntity {

    @Transient
    public static final String FIND_BY_LOGIN_SENHA =
            "Usuario.findByLoginSenha";

    @Transient
    public static final String FIND_INTERNOS =
            "Usuario.findInternos";


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "usuoid")
    private Integer id;

    @Column(name = "usu_nome")
    private String nome;

    @Column(name = "usu_login")
    private String login;

    @Column(name = "usu_senha")
    private String senha;

    @Column(name = "usu_passwd")
    private String passwd;

    @Column(name = "usu_tipo")
    private String tipo;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Pessoa.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="usu_pesoid")
    private Pessoa pessoa;

    @Column(name = "usu_dt_criacao")
    private Date criacao;

    @Column(name = "usu_dt_exclusao")
    private Date exclusao;

    @Column(name = "usu_dt_passwd")
    private Date dtPasswd;

    @Column(name = "usu_email")
    private String email;

    @Column(name = "usu_acesso_externo")
    private boolean externo;

    @Column(name = "usu_passwd_req_code")
    private String usuPasswdReqCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "usu_passwd_req_data")
    private Date usuPasswdRecData;

    @Column(name = "usu_interno")
    private boolean interno;

    @Column(name="usu_dt_ultimo_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtUltimoLogin;
    
    public Usuario() {
    }

    public Usuario(Integer id) {
        this.id = id;
    }

    public Usuario nome(String nome) {
        this.nome = nome;
        return this;
    }

    public boolean hasNome() {
        return nome != null && !"".equals(nome.trim());
    }

    public static String getFindByLoginSenha() {
        return FIND_BY_LOGIN_SENHA;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getCriacao() {
        return criacao;
    }

    public void setCriacao(Date criacao) {
        this.criacao = criacao;
    }

    public Date getExclusao() {
        return exclusao;
    }

    public void setExclusao(Date exclusao) {
        this.exclusao = exclusao;
    }

    public Date getDtPasswd() {
        return dtPasswd;
    }

    public void setDtPasswd(Date dtPasswd) {
        this.dtPasswd = dtPasswd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getExterno() {
        return externo;
    }

    public void setExterno(boolean externo) {
        this.externo = externo;
    }

    public static String getFindInternos() {
        return FIND_INTERNOS;
    }

    public boolean isExterno() {
        return externo;
    }

    public String getUsuPasswdReqCode() {
        return usuPasswdReqCode;
    }

    public void setUsuPasswdReqCode(String usuPasswdReqCode) {
        this.usuPasswdReqCode = usuPasswdReqCode;
    }

    public Date getUsuPasswdRecData() {
        return usuPasswdRecData;
    }

    public void setUsuPasswdRecData(Date usuPasswdRecData) {
        this.usuPasswdRecData = usuPasswdRecData;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public boolean isInterno() {
        return interno;
    }

    public void setInterno(boolean interno) {
        this.interno = interno;
    }

	public Date getDtUltimoLogin() {
		return dtUltimoLogin;
	}

	public void setDtUltimoLogin(Date dtUltimoLogin) {
		this.dtUltimoLogin = dtUltimoLogin;
	}
    
    
}
