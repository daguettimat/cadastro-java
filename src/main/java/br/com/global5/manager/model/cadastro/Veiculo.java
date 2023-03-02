package br.com.global5.manager.model.cadastro;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.auxiliar.Cor;
import br.com.global5.manager.model.enums.VeiculoCategoria;
import br.com.global5.manager.model.enums.VeiculoTipo;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "veiculo")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Veiculo implements BaseEntity {

    @Id
    @SequenceGenerator(name = "veiculo_veioid_seq", sequenceName = "veiculo_veioid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "veiculo_veioid_seq")
    @Column(name = "veioid")
    private Integer id;

    @Column(name = "vei_placa")
    private String placa;

    @Column(name = "vei_uf")
    private String uf;

    @Column(name = "vei_municipio")
    private String municipio;

    @Column(name = "vei_pais")
    private String pais;

    @Column(name = "vei_renavam")
    private String renavam;

    @Column(name = "vei_documento")
    private String documento;

    @Column(name = "vei_chassi")
    private String chassi;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Marca.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "vei_maroid")
    private Marca marca;


    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Modelo.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "vei_modoid")
    private Modelo modelo;

    @Column(name = "vei_versao")
    private String versao;

//    @Column(name = "vei_categoria")
//    private String categoria;

    @Column(name = "vei_anofabricacao")
    private Integer anoFabricacao;

    @Column(name = "vei_anomodelo")
    private Integer anoModelo;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Cor.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "vei_coroid")
    private Cor cor;

    @Column(name = "vei_obs")
    private String observacao;

    @Column(name = "vei_frota")
    private String frota;


    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = VeiculoTipo.class,
            cascade = {CascadeType.MERGE}
    )

    @JoinColumn(name = "vei_tipo")
    private VeiculoTipo tipo;

    @Column(name = "vei_leasing")
    private boolean leasing;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Proprietario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "vei_propoid")
    private Proprietario proprietario;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Proprietario.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "vei_arrendoid")
    private Proprietario arrendatario;

    @Column(name = "vei_nacional")
    private boolean nacional;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = VeiculoCategoria.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "vei_categoria_enumoid")
    private VeiculoCategoria categoriaVeiculo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "vei_dt_criacao")
    private Date dtCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "vei_dt_alteracao")
    private Date dtAlteracao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "vei_dt_exclusao")
    private Date dtExclusao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "vei_usuoid_criacao")
    private Usuario usuCriacao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "vei_usuoid_alteracao")
    private Usuario usuAlteracao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "vei_usuoid_exclusao")
    private Usuario usuExclusao;

    @Column(name="vei_placa_anterior")
    private String placaAnterior;
    
    @Column(name="vei_dt_placa_conversao")
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtPlacaConversao;
    
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(name = "vei_usuoid_placa_mercosul")
    private Usuario usuAlterouMercosul;
    
    @Column(name="vei_dt_verificacao_ws")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtVerificacaoWs;
    
    @Column(name="vei_dt_atualiza_ws")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAtualizacaoWs;
    
    
    public Veiculo() {
    }

    public boolean hasPlaca() {
        return placa != null && !"".equals(placa.trim());
    }

    public Veiculo placa(String placa) {
        this.placa = placa;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

//    public String getCategoria() {
//        return categoria;
//    }
//
//    public void setCategoria(String categoria) {
//        this.categoria = categoria;
//    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public Integer getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(Integer anoModelo) {
        this.anoModelo = anoModelo;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getFrota() {
        return frota;
    }

    public void setFrota(String frota) {
        this.frota = frota;
    }

//    public boolean isLeasing() {
//        if( this.leasing == null ) {
//            return false;
//        } else {
//            return leasing;
//        }
//    }
//
//    public void setLeasing(boolean leasing) {
//        if( leasing == null ) {
//            this.leasing = false;
//        } else {
//            this.leasing = leasing.booleanValue();
//        }
//    }


    public boolean isLeasing() {
        return leasing;
    }

    public void setLeasing(boolean leasing) {
        this.leasing = leasing;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public Proprietario getArrendatario() {
        return arrendatario;
    }

    public void setArrendatario(Proprietario arrendatario) {
        this.arrendatario = arrendatario;
    }

    public boolean isNacional() {
        return nacional;
    }

    public void setNacional(boolean nacional) {
        this.nacional = nacional;
    }

    public VeiculoCategoria getCategoriaVeiculo() {
        return categoriaVeiculo;
    }

    public void setCategoriaVeiculo(VeiculoCategoria categoriaVeiculo) {
        this.categoriaVeiculo = categoriaVeiculo;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Date dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public Usuario getUsuCriacao() {
        return usuCriacao;
    }

    public void setUsuCriacao(Usuario usuCriacao) {
        this.usuCriacao = usuCriacao;
    }

    public Usuario getUsuAlteracao() {
        return usuAlteracao;
    }

    public void setUsuAlteracao(Usuario usuAlteracao) {
        this.usuAlteracao = usuAlteracao;
    }

    public Usuario getUsuExclusao() {
        return usuExclusao;
    }

    public void setUsuExclusao(Usuario usuExclusao) {
        this.usuExclusao = usuExclusao;
    }

    public VeiculoTipo getTipo() {
        return tipo;
    }

    public void setTipo(VeiculoTipo tipo) {
        this.tipo = tipo;
    }

    public Boolean getLeasing() {
        return leasing;
    }

	public String getPlacaAnterior() {
		return placaAnterior;
	}

	public void setPlacaAnterior(String placaAnterior) {
		this.placaAnterior = placaAnterior;
	}

	public Date getDtPlacaConversao() {
		return dtPlacaConversao;
	}

	public void setDtPlacaConversao(Date dtPlacaConversao) {
		this.dtPlacaConversao = dtPlacaConversao;
	}

	public Usuario getUsuAlterouMercosul() {
		return usuAlterouMercosul;
	}

	public Date getDtVerificacaoWs() {
		return dtVerificacaoWs;
	}

	public Date getDtAtualizacaoWs() {
		return dtAtualizacaoWs;
	}

	public void setUsuAlterouMercosul(Usuario usuAlterouMercosul) {
		this.usuAlterouMercosul = usuAlterouMercosul;
	}

	public void setDtVerificacaoWs(Date dtVerificacaoWs) {
		this.dtVerificacaoWs = dtVerificacaoWs;
	}

	public void setDtAtualizacaoWs(Date dtAtualizacaoWs) {
		this.dtAtualizacaoWs = dtAtualizacaoWs;
	}
    
}
