package br.com.global5.manager.model.auxiliar;

import br.com.global5.infra.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "enum_endereco_tipo")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoEndereco implements BaseEntity {

    /**
	 *
	 */
	private static final long serialVersionUID = 201704220858L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "enumoid")
    private Integer id;

    @Column(name = "enum_nome")
    private String descricao;

    @Column(name = "enum_dt_exclusao")
    private Date dtExclusao;

	public TipoEndereco(String descricao, Date dtExclusao) {
		this.descricao = descricao;
		this.dtExclusao = dtExclusao;
	}

	public TipoEndereco() {}

	public TipoEndereco(Integer id) {
		this.id = id;
	}
	
	public TipoEndereco descricao(String descricao) {
		this.descricao = descricao;
		return this;
	}

    public boolean hasDescricao() {
        return descricao != null && !"".equals(descricao.trim());
    }

	public static long getSerialVersionUID() {
		return serialVersionUID;
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

	public Date getDtExclusao() {
		return dtExclusao;
	}

	public void setDtExclusao(Date dtExclusao) {
		this.dtExclusao = dtExclusao;
	}
}
