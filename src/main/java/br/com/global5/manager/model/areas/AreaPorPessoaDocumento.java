package br.com.global5.manager.model.areas;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(
		name = "ListAreaPorPessoaDocumentoMapping",
		entities = @EntityResult(
					entityClass = AreaPorPessoaDocumento.class,
					fields = {							
							@FieldResult ( name = "id", column = "id")
					}
				) 
		)

@Entity
public class AreaPorPessoaDocumento {

	@Id
	private Integer id;	
	
	public AreaPorPessoaDocumento(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	
	
}
