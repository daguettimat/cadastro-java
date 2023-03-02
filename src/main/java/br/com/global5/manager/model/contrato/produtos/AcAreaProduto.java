package br.com.global5.manager.model.contrato.produtos;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(
        name = "ListaAreaProdutoMapping",
        entities = @EntityResult(
                entityClass = AcAreaProduto.class,
                fields = {              		
                		@FieldResult(name = "id", column = "id"),
                		@FieldResult(name = "contrato", column = "contrato")

                }
            )
        )

@Entity
public class AcAreaProduto {
	@Id
	private Integer id;
	private Integer contrato;
	
	public AcAreaProduto(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getContrato() {
		return contrato;
	}

	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}
	
}
