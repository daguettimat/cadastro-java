package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_marca_carreta")
public class SincMarcaCarreta {
	
	@Id
	@Column(name="marcoid")
	private Integer id;

	@Column(name="marc_marca")
	private String marca;

	public SincMarcaCarreta() {	}
	
	public SincMarcaCarreta(Integer id) {this.id = id;}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
		
}
