package br.com.global5.manager.model.cadastro;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Localizador;
import br.com.global5.manager.model.cadastro.Telefone;
import br.com.global5.manager.model.enums.DocumentoTipo;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "proprietario")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Proprietario implements BaseEntity {

    @Id
    @SequenceGenerator(name = "proprietario_seq", sequenceName = "proprietario_propoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "proprietario_seq")
    @Column(name = "propoid")
    private Integer id;

    @Column(name = "prop_documento")
    private String documento;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Localizador.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "prop_locoid")
    private Localizador endereco;

    @Column(name = "prop_nome")
    private String nome;

    @Column(name = "prop_nacional")
    private boolean nacional;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prop_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prop_dt_alteracao")
    private Date dtAlteracao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "prop_dt_exclusao")
    private Date dtExclusao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "prop_usuoid_criacao")
    private Usuario usuCriacao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "prop_usuoid_alteracao")
    private Usuario usuAlteracao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "prop_usuoid_exclusao")
    private Usuario usuExclusao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Telefone.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "prop_teloid")
    private Telefone telefone;
    
    @Column(name = "prop_documento1_emissao")
    private Date doc1Emissão;
    
    @Column(name = "prop_documento1_emissor")
    private String doc1Emissor;
    
    @Column(name = "prop_documento1_uf")
    private String doc1Uf;
        
    @Column(name = "prop_documento2")
    private String documento2;
    
    @ManyToOne(
            fetch=FetchType.EAGER,
            targetEntity=DocumentoTipo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "prop_documento2_tipo")    
    private DocumentoTipo documento2_tipo;
    
    @Column(name = "prop_documento2_emissao")
    private Date doc2Emissão;
    
    @Column(name = "prop_documento2_emissor")
    private String doc2Emissor;
    
    @Column(name = "prop_documento2_uf")
    private String doc2Uf;
    
    @Column(name = "prop_dt_nascimento")
    private Date dtNascimento;
    
    @Column(name = "prop_naturalidade")
    private String Naturalidade;
    
    @Column(name = "prop_nomemae")
    private String nomeMae;
    
    @Column(name = "prop_nomepai")
    private String nomePai;

    public Proprietario() {
    }


    public Proprietario(Integer id) {
        this.id = id;
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Localizador getEndereco() {
        return endereco;
    }

    public void setEndereco(Localizador endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isNacional() {
        return nacional;
    }

    public void setNacional(boolean nacional) {
        this.nacional = nacional;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Date dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public Usuario getUsuCriacao() {
        return usuCriacao;
    }

    public void setUsuCriacao(Usuario usuCriacao) {
        this.usuCriacao = usuCriacao;
    }

    public Usuario getUsuAlteracao() {
        return usuAlteracao;
    }

    public void setUsuAlteracao(Usuario usuAlteracao) {
        this.usuAlteracao = usuAlteracao;
    }

    public Usuario getUsuExclusao() {
        return usuExclusao;
    }

    public void setUsuExclusao(Usuario usuExclusao) {
        this.usuExclusao = usuExclusao;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }


	public Date getDoc1Emissão() {
		return doc1Emissão;
	}


	public void setDoc1Emissão(Date doc1Emissão) {
		this.doc1Emissão = doc1Emissão;
	}


	public String getDoc1Emissor() {
		return doc1Emissor;
	}


	public void setDoc1Emissor(String doc1Emissor) {
		this.doc1Emissor = doc1Emissor;
	}


	public String getDoc1Uf() {
		return doc1Uf;
	}


	public void setDoc1Uf(String doc1Uf) {
		this.doc1Uf = doc1Uf;
	}


	public String getDocumento2() {
		return documento2;
	}


	public void setDocumento2(String documento2) {
		this.documento2 = documento2;
	}


	public DocumentoTipo getDocumento2_tipo() {
		return documento2_tipo;
	}


	public void setDocumento2_tipo(DocumentoTipo documento2_tipo) {
		this.documento2_tipo = documento2_tipo;
	}


	public Date getDoc2Emissão() {
		return doc2Emissão;
	}


	public void setDoc2Emissão(Date doc2Emissão) {
		this.doc2Emissão = doc2Emissão;
	}


	public String getDoc2Emissor() {
		return doc2Emissor;
	}


	public void setDoc2Emissor(String doc2Emissor) {
		this.doc2Emissor = doc2Emissor;
	}


	public String getDoc2Uf() {
		return doc2Uf;
	}


	public void setDoc2Uf(String doc2Uf) {
		this.doc2Uf = doc2Uf;
	}


	public Date getDtNascimento() {
		return dtNascimento;
	}


	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}


	public String getNaturalidade() {
		return Naturalidade;
	}


	public void setNaturalidade(String naturalidade) {
		Naturalidade = naturalidade;
	}


	public String getNomeMae() {
		return nomeMae;
	}


	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}


	public String getNomePai() {
		return nomePai;
	}


	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}
    
    
}
