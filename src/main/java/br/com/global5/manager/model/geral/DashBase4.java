package br.com.global5.manager.model.geral;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;


@SqlResultSetMapping(
        name = "ListaDashBase4Mapping",
        entities = @EntityResult(
                entityClass = DashBase4.class,
                fields = {
                        @FieldResult(name = "num_ficha", column = "num_ficha"),
                        @FieldResult(name = "cnpj", column = "cnpj"),
                        @FieldResult(name = "nome_cliente", column = "nome_cliente"),
                        @FieldResult(name = "finalizado", column = "finalizado"),
                        @FieldResult(name = "placa_veiculo", column = "placa_veiculo"),
                        @FieldResult(name = "motorista", column = "motorista"),
                        @FieldResult(name = "proprietario", column = "proprietario"),                            
                        @FieldResult(name = "status", column = "status")
                }
        )
)

@Entity
public class DashBase4 {
	
	@Id
	private Integer num_ficha;
	private String  cnpj;
	private String  nome_cliente;
	private Date  finalizado;
	private String placa_veiculo;
	private String motorista;
	private String proprietario;	
	private String  status;
	
	public DashBase4(){}

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

	public String getPlaca_veiculo() {
		return placa_veiculo;
	}

	public void setPlaca_veiculo(String placa_veiculo) {
		this.placa_veiculo = placa_veiculo;
	}

	public String getMotorista() {
		return motorista;
	}

	public void setMotorista(String motorista) {
		this.motorista = motorista;
	}

	public String getProprietario() {
		return proprietario;
	}

	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
	}	
	
}
