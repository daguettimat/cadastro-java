package br.com.global5.manager.model.contrato.produtos;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;



@SqlResultSetMapping(
        name = "ListaClienteVeiculoMapping",
        entities = @EntityResult(
                entityClass = AcClienteVeiculo.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "placa", column = "placa")
                }
        )
)


@Entity
public class AcClienteVeiculo {
	
	@Id
	private Integer id;
	private String placa;
	
	public AcClienteVeiculo(){}

	public Integer getId() {
		return id;
	}

	public String getPlaca() {
		return placa;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
}
