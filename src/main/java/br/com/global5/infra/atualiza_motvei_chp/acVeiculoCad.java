package br.com.global5.infra.atualiza_motvei_chp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;

@SqlResultSetMapping (
        name = "ListaVeiculoCadMapping",
        entities = @EntityResult(
                entityClass = acVeiculoCad.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "placa", column = "placa"),
                        @FieldResult(name = "uf", column = "uf"),
                        @FieldResult(name = "municipio", column = "municipio"),
                        @FieldResult(name = "pais", column = "pais"),
                        @FieldResult(name = "renavam", column = "renavam"),
                        @FieldResult(name = "chassi", column = "chassi"),
                        @FieldResult(name = "anoFabricacao", column = "anoFabricacao"),
                        @FieldResult(name = "anoModelo", column = "anoModelo"),
                        @FieldResult(name = "cor", column = "cor"),
                        @FieldResult(name = "marca", column = "marca"),
                        @FieldResult(name = "modelo", column = "modelo"),
                        @FieldResult(name = "tipo", column = "tipo"),
                        @FieldResult(name = "nacional", column = "nacional")
                }
        )              
)

@Entity
public class acVeiculoCad {
	
	@Id
	private Integer id;
	private String  placa;
	private String  uf;
	private String  municipio;
	private String  pais;
	private String 	renavam;
	private String 	chassi;
	private String anoFabricacao;
	private String anoModelo;
	private String  cor;
	private String marca;
	private String modelo;
	private String tipo;
	private String nacional;
	
	public acVeiculoCad(){}

	public Integer getId() {
		return id;
	}

	public String getPlaca() {
		return placa;
	}

	public String getUf() {
		return uf;
	}

	public String getMunicipio() {
		return municipio;
	}

	
	public String getAnoModelo() {
		return anoModelo;
	}

	public String getCor() {
		return cor;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}


	public void setChassi(String chassi) {
		this.chassi = chassi;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getRenavam() {
		return renavam;
	}

	public void setRenavam(String renavam) {
		this.renavam = renavam;
	}

	public String getMarca() {
		return marca;
	}

	public String getModelo() {
		return modelo;
	}

	public String getTipo() {
		return tipo;
	}


	public void setMarca(String marca) {
		this.marca = marca;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}



	public String getChassi() {
		return chassi;
	}

	public String getAnoFabricacao() {
		return anoFabricacao;
	}

	public void setAnoFabricacao(String anoFabricacao) {
		this.anoFabricacao = anoFabricacao;
	}

	public void setAnoModelo(String anoModelo) {
		this.anoModelo = anoModelo;
	}

	public String getNacional() {
		return nacional;
	}

	public void setNacional(String nacional) {
		this.nacional = nacional;
	}
		
}
