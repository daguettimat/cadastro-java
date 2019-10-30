package br.com.global5.manager.model.areas;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;


@SqlResultSetMapping(
		name = "ListAreaClienteMapping",
		entities = @EntityResult(
					entityClass = AreaQry.class,
					fields = {							
							@FieldResult ( name = "idcliente", column = "idcliente"),
							@FieldResult ( name = "razao", column = "razao")
					}
				) 
		)


@Entity
public class AreaQry {
	
	@Id
	private Integer idcliente;
	private String 	razao;
	
	public AreaQry(){}

	public Integer getIdcliente() {
		return idcliente;
	}

	public void setIdcliente(Integer idcliente) {
		this.idcliente = idcliente;
	}

	public String getRazao() {
		return razao;
	}

	public void setRazao(String razao) {
		this.razao = razao;
	}
	
}
