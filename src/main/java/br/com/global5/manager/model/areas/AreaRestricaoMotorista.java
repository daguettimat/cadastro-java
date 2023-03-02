package br.com.global5.manager.model.areas;

import javax.persistence.*;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.enums.MotoristaVinculo;

@Entity
@Table(name = "java.area_restricao_motorista")
public class AreaRestricaoMotorista implements BaseEntity {
		
    @Id
    @SequenceGenerator(name = "area_restricao_motorista_aresm_seq", sequenceName = "area_restricao_motorista_aresm_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "area_restricao_motorista_aresm_seq")
    @Column(name = "aresm")
	private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Area.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="aresm_areaoid")
	private Area area;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=MotoristaVinculo.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="aresm_vinculo_enumoid")
	private MotoristaVinculo motoristaVinculo;
	
    @Column(name = "aresm_validade_cadastral")
	private Integer validadeCadastral;
	
    @Column(name = "aresm_validade_embarque")
	private Integer validadeEmbarque;
	
    @Column(name = "aresm_criminal_limpo")
	private boolean criminalLimpo;
    
    public AreaRestricaoMotorista(){}
    
    public AreaRestricaoMotorista(Integer id){this.id = id;}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public MotoristaVinculo getMotoristaVinculo() {
		return motoristaVinculo;
	}

	public void setMotoristaVinculo(MotoristaVinculo motoristaVinculo) {
		this.motoristaVinculo = motoristaVinculo;
	}

	public Integer getValidadeCadastral() {
		return validadeCadastral;
	}

	public void setValidadeCadastral(Integer validadeCadastral) {
		this.validadeCadastral = validadeCadastral;
	}

	public Integer getValidadeEmbarque() {
		return validadeEmbarque;
	}

	public void setValidadeEmbarque(Integer validadeEmbarque) {
		this.validadeEmbarque = validadeEmbarque;
	}

	public boolean isCriminalLimpo() {
		return criminalLimpo;
	}

	public void setCriminalLimpo(boolean criminalLimpo) {
		this.criminalLimpo = criminalLimpo;
	}
    
}
