package br.com.global5.manager.model.analise;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.geral.Mercadoria;
import br.com.global5.manager.model.geral.Usuario;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@Entity
@Table(name = "java.analise_cadastral_mercadoria")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class acMercadoria implements BaseEntity {

    @Id
    @SequenceGenerator(name = "analise_cadastral_mercadoria_ameroid_seq", sequenceName = "analise_cadastral_mercadoria_ameroid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "analise_cadastral_mercadoria_ameroid_seq")
    @Column(name = "ameroid")
    private Integer id;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=acCadastro.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="amer_anacoid")
    private acCadastro acCadastro; 
    
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Mercadoria.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "amer_meroid")
    private Mercadoria mercadoria;

    @Column(name = "amer_dt_registro")
    private Timestamp mercDtRegistro;

    @Column(name = "amer_valor")
    private double mercValor;

    @Column(name="amer_origem_paisoid")
    private Integer mercPaisOrigem;

    @Column(name="amer_origem_ufoid")
    private Integer mercUFOrigem;

    @Column(name="amer_origem_cidoid")
    private Integer mercCidadeOrigem;

    @Column(name="amer_destino_paisoid")
    private Integer mercPaisDestino;

    @Column(name="amer_destino_ufoid")
    private Integer mercUFDestino;

    @Column(name="amer_destino_cidoid")
    private Integer mercCidadeDestino;

    @Column(name="amer_origem_cidade")
    private String mercCidOrigem;

    @Column(name="amer_destino_cidade")
    private String mercCidDestino;


    public acMercadoria() {}


    public acMercadoria(Integer id ) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Mercadoria getMercadoria() {
		return mercadoria;
	}


	public void setMercadoria(Mercadoria mercadoria) {
		this.mercadoria = mercadoria;
	}


	public Timestamp getMercDtRegistro() {
        return mercDtRegistro;
    }

    public void setMercDtRegistro(Timestamp mercDtRegistro) {
        this.mercDtRegistro = mercDtRegistro;
    }

    public double getMercValor() {
        return mercValor;
    }

    public void setMercValor(double mercValor) {
        this.mercValor = mercValor;
    }

    public Integer getMercPaisOrigem() {
        return mercPaisOrigem;
    }

    public void setMercPaisOrigem(Integer mercPaisOrigem) {
        this.mercPaisOrigem = mercPaisOrigem;
    }

    public Integer getMercUFOrigem() {
        return mercUFOrigem;
    }

    public void setMercUFOrigem(Integer mercUFOrigem) {
        this.mercUFOrigem = mercUFOrigem;
    }

    public Integer getMercCidadeOrigem() {
        return mercCidadeOrigem;
    }

    public void setMercCidadeOrigem(Integer mercCidadeOrigem) {
        this.mercCidadeOrigem = mercCidadeOrigem;
    }

    public Integer getMercPaisDestino() {
        return mercPaisDestino;
    }

    public void setMercPaisDestino(Integer mercPaisDestino) {
        this.mercPaisDestino = mercPaisDestino;
    }

    public Integer getMercUFDestino() {
        return mercUFDestino;
    }

    public void setMercUFDestino(Integer mercUFDestino) {
        this.mercUFDestino = mercUFDestino;
    }

    public Integer getMercCidadeDestino() {
        return mercCidadeDestino;
    }

    public void setMercCidadeDestino(Integer mercCidadeDestino) {
        this.mercCidadeDestino = mercCidadeDestino;
    }

    public String getMercCidOrigem() {
        return mercCidOrigem;
    }

    public void setMercCidOrigem(String mercCidOrigem) {
        this.mercCidOrigem = mercCidOrigem;
    }

    public String getMercCidDestino() {
        return mercCidDestino;
    }

    public void setMercCidDestino(String mercCidDestino) {
        this.mercCidDestino = mercCidDestino;
    }


	public acCadastro getAcCadastro() {
		return acCadastro;
	}


	public void setAcCadastro(acCadastro acCadastro) {
		this.acCadastro = acCadastro;
	}
    
}
