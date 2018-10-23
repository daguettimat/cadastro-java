package br.com.global5.manager.model.auxiliar;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.global5.infra.model.BaseEntity;

@Entity
@Table(name = "tipo_contrato")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TipoContrato implements BaseEntity {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 201702210818L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tconoid")
    private Integer id;
    
    @Column(name = "tcondescricao")
    private String descricao;
    
    @Column(name = "tconcadastro")
    private Date cadastro;
    
	public TipoContrato(Integer id, String descricao, Date cadastro) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.cadastro = cadastro;
	}


	public TipoContrato() {
		super();

	}
    
	public TipoContrato(Integer id) {
		this.id = id;

	}
	
	public TipoContrato descricao(String descricao) {
		this.descricao = descricao;
		return this;
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


	public Date getCadastro() {
		return cadastro;
	}


	public void setCadastro(Date cadastro) {
		this.cadastro = cadastro;
	}

    public boolean hasDescricao() {
        return descricao != null && !"".equals(descricao.trim());
    }
    
    

}
