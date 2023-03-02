package br.com.global5.manager.model.contrato;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(
        name = "ListaContratosMapping",
        entities = @EntityResult(
                entityClass = acContrato.class,
                fields = {              		
                		@FieldResult(name = "id", column = "id"),
                		@FieldResult(name = "area", column = "area"),
                		@FieldResult(name = "cnpj", column = "cnpj"),
                		@FieldResult(name = "nome", column = "nome"),
                		@FieldResult(name = "telefone", column = "telefone"),
                		@FieldResult(name = "statusCadastro", column = "statusCadastro"),
                		@FieldResult(name = "statusMonitoramento", column = "statusMonitoramento"),
                }
            )
        )

@Entity
public class acContrato {

	@Id
	private Integer id;
	private Integer area;
	private String  cnpj;
	private String  nome;
	private String  telefone;
	private String  statusCadastro;
	private String  statusMonitoramento;
	
	public acContrato(){}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getStatusCadastro() {
		return statusCadastro;
	}

	public void setStatusCadastro(String statusCadastro) {
		this.statusCadastro = statusCadastro;
	}

	public String getStatusMonitoramento() {
		return statusMonitoramento;
	}

	public void setStatusMonitoramento(String statusMonitoramento) {
		this.statusMonitoramento = statusMonitoramento;
	}
	
}
