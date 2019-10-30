package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_marca_veiculo")
public class SincMarcaVeiculo {
	
	@Id
	@Column(name="marvoid")
	private Integer id;

	@Column(name="marv_marca")
	private String marca;
	
	public SincMarcaVeiculo (){}
	
	public SincMarcaVeiculo (Integer id){this.id = id;}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}
	
}
