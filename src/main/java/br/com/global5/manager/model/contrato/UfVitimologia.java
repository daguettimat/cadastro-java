package br.com.global5.manager.model.contrato;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;

@Entity
@Table(name = "java.uf_vitimologia")
public class UfVitimologia implements BaseEntity {
	
    @Id
    @SequenceGenerator(name = "uf_vitimologia_ufvoid_seq", sequenceName = "uf_vitimologia_ufvoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uf_vitimologia_ufvoid_seq")
	@Column(name="ufvoid")
	private Integer id;
	
	@Column(name="ufv_regiao")
	private String regiao;
	
	@Column(name="ufv_ids")
	private String ids;
		
	@Column(name="ufv_descricao")
	private String descricao;
	
	public UfVitimologia(){}
	
	public UfVitimologia(Integer id){this.id = id;}

	public Integer getId() {
		return id;
	}

	public String getRegiao() {
		return regiao;
	}

	public String getIds() {
		return ids;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
