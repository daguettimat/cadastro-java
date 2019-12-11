package br.com.global5.manager.model.geral;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import br.com.global5.infra.model.BaseEntity;

@SqlResultSetMapping(
        name = "UsuarioCtrMapping",
        entities = @EntityResult(
                entityClass = UsuarioCtr.class,
                fields = {
                		@FieldResult(name = "id" , column = "id"),
                		@FieldResult(name = "conoid" , column = "conoid"),
                		@FieldResult(name = "produtoid" , column = "produtoid")
                }
            )
        )

@Entity
public class UsuarioCtr implements BaseEntity{
	
	@Id
	private Integer id;
	private Integer conoid;
	private Integer produtoid;
	
	public UsuarioCtr(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getConoid() {
		return conoid;
	}

	public void setConoid(Integer conoid) {
		this.conoid = conoid;
	}

	public Integer getProdutoid() {
		return produtoid;
	}

	public void setProdutoid(Integer produtoid) {
		this.produtoid = produtoid;
	}	
	
	
	
}
