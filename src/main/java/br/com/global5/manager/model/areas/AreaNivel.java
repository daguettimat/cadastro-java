package br.com.global5.manager.model.areas;

import br.com.global5.infra.model.BaseEntity;
import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "area_nivel")
@Indexed(index = "area_nivel")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)

public class AreaNivel implements BaseEntity {

    @Id
    @SequenceGenerator(name = "area_nivel_anvloid_seq", sequenceName = "area_nivel_anvloid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "area_nivel_anvloid_seq")
    @Column(name = "anvloid")
    private Integer id;


    @Column(name = "anvl_nome")
    private String nome;


    @Column(name = "anvl_interna")
    private boolean interna;
    //private String interna;

    public AreaNivel() {}

    //public AreaNivel(String nome, String interna) {
    public AreaNivel(String nome, boolean interna) {
        this.nome = nome;
        this.interna = interna;
    }

    public AreaNivel nome(String nome) {
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

	public boolean isInterna() {
		return interna;
	}

	public void setInterna(boolean interna) {
		this.interna = interna;
	}

    /*
    public String getInterna() {
        return interna;
    }

    public void setInterna(String interna) {
        this.interna = interna;
    }
    */
    
    
    
}
