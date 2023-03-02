package br.com.global5.manager.model.contrato;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(
        name = "ListaContratoConsultaMapping",
        entities = @EntityResult(
                entityClass = acContratoConsulta.class,
                fields = {              		
                		@FieldResult(name = "id", column = "id"),
                		@FieldResult(name = "idarea", column = "idarea"),
                		@FieldResult(name = "idproduto", column = "idproduto"),
                		@FieldResult(name = "dtalteracao", column = "dtalteracao"),
                		@FieldResult(name = "empresa", column = "empresa"),
                		@FieldResult(name = "status", column = "status")
                }
            )
        )

@Entity
public class acContratoConsulta {

	@Id
	private Integer id;
	private Integer idarea;
	private Integer idproduto;
	private Date  	dtalteracao;
	private Integer empresa;
	private String  status;
	
	public acContratoConsulta(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdarea() {
		return idarea;
	}

	public void setIdarea(Integer idarea) {
		this.idarea = idarea;
	}

	public Integer getIdproduto() {
		return idproduto;
	}

	public void setIdproduto(Integer idproduto) {
		this.idproduto = idproduto;
	}

	public Date getDtalteracao() {
		return dtalteracao;
	}

	public void setDtalteracao(Date dtalteracao) {
		this.dtalteracao = dtalteracao;
	}

	public Integer getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Integer empresa) {
		this.empresa = empresa;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
