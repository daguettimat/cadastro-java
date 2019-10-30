package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_tipo_veiculo")
public class SincTipoVeiculo {
	
	@Id
	@Column(name="tpveioid")
	private Integer id ;

	@Column(name="tpvei_tipo")
	private String tipo;
	
	public SincTipoVeiculo(){}
	public SincTipoVeiculo(Integer id){this.id = id;}
	
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
