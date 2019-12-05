package br.com.global5.manager.model.cadastro;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;


@SqlResultSetMapping(
		name = "ListPessoaClienteMapping",
		entities = @EntityResult(
					entityClass = PessoaQry.class,
					fields = {							
							@FieldResult ( name = "idcliente", column = "idcliente"),
							@FieldResult ( name = "razao", column = "razao"),
							@FieldResult ( name = "fantasia", column = "fantasia"),
							@FieldResult ( name = "cnpj", column = "cnpj"),
							@FieldResult ( name = "email", column = "email"),
							@FieldResult ( name = "idArea", column = "idArea")	
					}
				) 
		)


@Entity
public class PessoaQry {

	@Id
	private Integer idcliente;
	private String 	razao;
	private String 	fantasia;
	private String 	cnpj;
	private String 	email;
	private Integer idArea;
	
	public PessoaQry(){}

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

	public String getFantasia() {
		return fantasia;
	}

	public void setFantasia(String fantasia) {
		this.fantasia = fantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIdArea() {
		return idArea;
	}

	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}
		
	
}
