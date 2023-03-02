package br.com.global5.manager.model.analise;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import br.com.global5.manager.model.contrato.acContrato;

@SqlResultSetMapping(
        name = "ListaAcVitimologiaMapping",
        entities = @EntityResult(
                entityClass = acVitimologia.class,
                fields = {              		
                		@FieldResult(name = "id", column = "id"),
                		@FieldResult(name = "analiseCadastral", column = "analiseCadastral"),
                		@FieldResult(name = "numConsulta", column = "numConsulta"),
                		@FieldResult(name = "numRequisicao", column = "numRequisicao"),
                		@FieldResult(name = "chaveAcesso", column = "chaveAcesso"),
                		@FieldResult(name = "stWsStatus", column = "stWsStatus")
                }
            )
        )
@Entity
public class acVitimologia {
	
	@Id
	private Integer id;
	private Integer analiseCadastral;
	private Integer numConsulta;
	private String numRequisicao; 
	private String chaveAcesso;
	private String stWsStatus;
	
	public acVitimologia(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAnaliseCadastral() {
		return analiseCadastral;
	}

	public void setAnaliseCadastral(Integer analiseCadastral) {
		this.analiseCadastral = analiseCadastral;
	}

	public Integer getNumConsulta() {
		return numConsulta;
	}

	public void setNumConsulta(Integer numConsulta) {
		this.numConsulta = numConsulta;
	}

	public String getNumRequisicao() {
		return numRequisicao;
	}

	public void setNumRequisicao(String numRequisicao) {
		this.numRequisicao = numRequisicao;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	public String getStWsStatus() {
		return stWsStatus;
	}

	public void setStWsStatus(String stWsStatus) {
		this.stWsStatus = stWsStatus;
	}
	
}
