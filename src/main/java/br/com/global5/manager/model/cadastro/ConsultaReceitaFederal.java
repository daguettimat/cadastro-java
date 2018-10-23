package br.com.global5.manager.model.cadastro;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.enums.DocumentoTipo;
import br.com.global5.manager.model.enums.ProdutoTipo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "consulta_receita_federal")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ConsultaReceitaFederal implements BaseEntity {

    @Id
    @SequenceGenerator(name = "conrecfederal_seq", sequenceName = "consulta_receita_federal_conrfoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "conrecfederal_seq")
    @Column(name = "conrfoid")
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "conrf_dt_consulta")
    private Date dtConsulta;

    @Column(name = "conrf_documento")
    private String documento;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = DocumentoTipo.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "conrf_documento_tipo")
    private DocumentoTipo documentoTipo;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = ProdutoTipo.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "conrf_origem_prodoid")
    private ProdutoTipo produtoTipo;

    @Column(name = "conrf_origem_oid")
    private Integer origem;

    @Column(name = "conrf_resultado")
    private String resultado;

    public ConsultaReceitaFederal() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDtConsulta() {
        return dtConsulta;
    }

    public void setDtConsulta(Date dtConsulta) {
        this.dtConsulta = dtConsulta;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public DocumentoTipo getDocumentoTipo() {
        return documentoTipo;
    }

    public void setDocumentoTipo(DocumentoTipo documentoTipo) {
        this.documentoTipo = documentoTipo;
    }

    public ProdutoTipo getProdutoTipo() {
        return produtoTipo;
    }

    public void setProdutoTipo(ProdutoTipo produtoTipo) {
        this.produtoTipo = produtoTipo;
    }

    public Integer getOrigem() {
        return origem;
    }

    public void setOrigem(Integer origem) {
        this.origem = origem;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
