package br.com.global5.manager.model.geral;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;


@SqlResultSetMapping(
        name = "ListaDashBase1Mapping",
        entities = @EntityResult(
                entityClass = DashBase1.class,
                fields = {
                        @FieldResult(name = "num_ficha", column = "num_ficha"),
                        @FieldResult(name = "cnpj", column = "cnpj"),
                        @FieldResult(name = "nome_cliente", column = "nome_cliente"),
                        @FieldResult(name = "finalizado", column = "finalizado"),                       
                        @FieldResult(name = "status", column = "status")
                }
        )
)

@Entity
public class DashBase1 {
	
	@Id
	private Integer num_ficha;
	private String  cnpj;
	private String  nome_cliente;
	private Date  finalizado;
	private String  status;
	
	public DashBase1(){}

	public Integer getNum_ficha() {
		return num_ficha;
	}

	public void setNum_ficha(Integer num_ficha) {
		this.num_ficha = num_ficha;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome_cliente() {
		return nome_cliente;
	}

	public void setNome_cliente(String nome_cliente) {
		this.nome_cliente = nome_cliente;
	}

	public Date getFinalizado() {
		return finalizado;
	}

	public void setFinalizado(Date finalizado) {
		this.finalizado = finalizado;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
