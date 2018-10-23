package br.com.global5.manager.model.analise;

import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.json.JsonStringType;
import br.com.global5.infra.model.BaseEntity;
import br.com.global5.infra.model.TipoValor;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.manager.model.enums.ReferenciasAvaliacao;
import br.com.global5.manager.model.enums.ReferenciasCategoria;
import br.com.global5.manager.model.enums.ReferenciasTipoContato;
import br.com.global5.manager.model.enums.TipoParte;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "analise_cadastral_referencia")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class acReferencia implements BaseEntity {

    @Id
    @SequenceGenerator(name = "analise_cadastral_referencia_arefoid_seq", sequenceName = "analise_cadastral_referencia_arefoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "analise_cadastral_referencia_arefoid_seq")
    @Column(name = "arefoid")
    private Integer id;

    @Column(name = "aref_nome")
    private String nome;

    @Column(name = "aref_ddd")
    private String ddd;

    @Column(name = "aref_fone")
    private String fone;

    @Column(name = "aref_empresa")
    private String empresa;

    @Type(type = "jsonb")
    @Column(name = "aref_item", columnDefinition = "jsonb")
    private List<TipoValor> item;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasCategoria.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "aref_categoria")
    private ReferenciasCategoria categoria;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasAvaliacao.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "aref_avaliacao")
    private ReferenciasAvaliacao avaliacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "aref_dt_registro")
    private Date dtRegistro;

    @Column(name = "aref_observacao")
    private String observacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "aref_dt_criacao")
    private Date dtCriacao;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Telefone.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "aref_teloid")
    private Telefone telefone;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=TipoParte.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "aref_tipo_parte")
    private TipoParte tipoParte;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=ReferenciasTipoContato.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "aref_tipo_contato")
    private ReferenciasTipoContato tipoContato;

    @Column(name = "aref_informativo")
    private String informativo;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=acCadastro.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "aref_anacoid")
    private acCadastro acCadastro;


    public acReferencia() {}

    public acReferencia nome(String nome) {
        this.nome = nome;
        return this;
    }

    public boolean hasNome() {
        return nome != null && !"".equals(nome.trim());
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

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public List<TipoValor> getItem() {
        return item;
    }

    public void setItem(List<TipoValor> item) {
        this.item = item;
    }

    public ReferenciasCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ReferenciasCategoria categoria) {
        this.categoria = categoria;
    }

    public ReferenciasAvaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(ReferenciasAvaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Date getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(Date dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public TipoParte getTipoParte() {
        return tipoParte;
    }

    public void setTipoParte(TipoParte tipoParte) {
        this.tipoParte = tipoParte;
    }

    public ReferenciasTipoContato getTipoContato() {
        return tipoContato;
    }

    public void setTipoContato(ReferenciasTipoContato tipoContato) {
        this.tipoContato = tipoContato;
    }

    public String getInformativo() {
        return informativo;
    }

    public void setInformativo(String informativo) {
        this.informativo = informativo;
    }

    public acCadastro getAcCadastro() {
        return acCadastro;
    }

    public void setAcCadastro(acCadastro acCadastro) {
        this.acCadastro = acCadastro;
    }
}
