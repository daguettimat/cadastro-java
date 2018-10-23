package br.com.global5.manager.model.geral;

import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@Entity
@Table(name = "pagina")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pagina implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pagoid")
    private Integer id;

    @Column(name = "pag_descricao")
    private String descricao;

    @Column(name = "pag_titulo")
    private String titulo;

    @Column(name = "pag_url")
    private String url;

    @Column(name = "pag_menu")
    private String menu;

    @Column(name = "pag_tipo")
    private String tipo;

    @Column(name = "pag_exclusao")
    private java.sql.Timestamp dtExclusao;

    @Column(name = "pag_admin")
    private String admin;

    @Column(name = "pag_operador")
    private String operador;

    @Column(name = "pag_transportadora")
    private String transportadora;

    @Column(name = "pag_filial")
    private String filial;

    @Column(name = "pag_colaborador")
    private String colaborador;

    @Column(name = "pag_seguradora")
    private String seguradora;

    @Column(name = "pag_hyperdrive")
    private String hyperdrive;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Pagina.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="pag_root")
    private Pagina root;


    public Pagina() {
    }

    public Pagina(String descricao, String titulo, String url, String menu, String tipo, Timestamp dtExclusao,
                  String admin, String operador, String transportadora, String filial, String colaborador,
                  String seguradora, String hyperdrive, Pagina root) {
        this.descricao = descricao;
        this.titulo = titulo;
        this.url = url;
        this.menu = menu;
        this.tipo = tipo;
        this.dtExclusao = dtExclusao;
        this.admin = admin;
        this.operador = operador;
        this.transportadora = transportadora;
        this.filial = filial;
        this.colaborador = colaborador;
        this.seguradora = seguradora;
        this.hyperdrive = hyperdrive;
        this.root = root;
    }

    public Pagina(Integer id) {
        this.id = id;
    }

    public Pagina descricao(String descricao) {
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Timestamp getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Timestamp dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public String getColaborador() {
        return colaborador;
    }

    public void setColaborador(String colaborador) {
        this.colaborador = colaborador;
    }

    public String getSeguradora() {
        return seguradora;
    }

    public void setSeguradora(String seguradora) {
        this.seguradora = seguradora;
    }

    public String getHyperdrive() {
        return hyperdrive;
    }

    public void setHyperdrive(String hyperdrive) {
        this.hyperdrive = hyperdrive;
    }

    public Pagina getRoot() {
        if( root == null ) {
            root = new Pagina();
        }
        return root;
    }

    public void setRoot(Pagina root) {
        this.root = root;
    }
}
