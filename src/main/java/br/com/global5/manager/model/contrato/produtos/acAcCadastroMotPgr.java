package br.com.global5.manager.model.contrato.produtos;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import br.com.global5.manager.model.areas.AreaQry;

@SqlResultSetMapping(
		name = "ListAcAcCadastroMotPgr",
		entities = @EntityResult(
					entityClass = acAcCadastroMotPgr.class,
					fields = {							
							@FieldResult ( name = "id", column = "id"),
							@FieldResult ( name = "idCadastro", column = "idCadastro")
					}
				) 
		)


@Entity
public class acAcCadastroMotPgr {
	
	@Id
	private Integer id;
	private Integer idCadastro;

	public acAcCadastroMotPgr(){}
	
	public acAcCadastroMotPgr(Integer idCadastro){
		this.idCadastro = idCadastro;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdCadastro() {
		return idCadastro;
	}

	public void setIdCadastro(Integer idCadastro) {
		this.idCadastro = idCadastro;
	}
	
}
