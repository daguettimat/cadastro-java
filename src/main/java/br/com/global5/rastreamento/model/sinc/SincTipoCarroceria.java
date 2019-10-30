package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_tipo_carroceria")
public class SincTipoCarroceria {
	
	@Id
	@Column(name="tpcaroid")
	private Integer id;
	
	@Column(name="tpcar_tipo")
	private String tipo;

	public SincTipoCarroceria() {}
	public SincTipoCarroceria(Integer id) {this.id = id;}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
		
}
