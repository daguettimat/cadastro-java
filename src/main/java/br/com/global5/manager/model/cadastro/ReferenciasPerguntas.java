package br.com.global5.manager.model.cadastro;

import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "pergunta_referencias")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@TypeDefs({
            @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ReferenciasPerguntas implements BaseEntity {

    @Id
    @SequenceGenerator(name = "perguntas_referencia_pergoid_seq", sequenceName = "perguntas_referencia_pergoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perguntas_referencia_pergoid_seq")
    @Column(name = "pergoid")
    private Integer id;

    @Column(name = "perg_texto")
    private String texto;

    @Column(name = "perg_obrigatoria")
    private boolean obrigatoria;

    @Column(name = "perg_ativa")
    private boolean ativa;

    @Column(name = "perg_categoria")
    private Integer categoria;

    @Column(name = "perg_tipo")
    private Integer tipo;

    @Type(type = "jsonb")
    @Column(name = "perg_respostas", columnDefinition = "jsonb")
    private List<ReferenciaRespostas> itens;

    public ReferenciasPerguntas() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isObrigatoria() {
        return obrigatoria;
    }

    public void setObrigatoria(boolean obrigatoria) {
        this.obrigatoria = obrigatoria;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public List<ReferenciaRespostas> getItens() {
        return itens;
    }

    public void setItens(List<ReferenciaRespostas> itens) {
        this.itens = itens;
    }
}
