package br.com.global5.manager.model.contrato;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.enums.MotoristaVinculo;
import br.com.global5.manager.model.geral.Usuario;

@Entity
@Table(name = "java.regra_seguradora_motorista")
public class RegraSeguradoraMotorista implements BaseEntity {
	
    @Id
    @SequenceGenerator(name = "regra_seguradora_motorista_rsmoid_seq", sequenceName = "regra_seguradora_motorista_rsmoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regra_seguradora_motorista_rsmoid_seq")
    @Column(name = "rsmoid")
    private Integer id;
    
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Area.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "rsm_areaoid")
    private Area area;
    
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = MotoristaVinculo.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "rsm_enumoid_mot_vinculo")
    private MotoristaVinculo vinculo;
    
    @Column(name = "rsm_mercadoria" )
    private Integer mercadoria;
    
    @Column(name = "rsm_vlr_pgr_inicio")
    private BigDecimal vlrPgrInicio;
    
    @Column(name = "rsm_vlr_pgr_final")
    private BigDecimal vlrPgrFinal;
    
    @Column(name = "rsm_qtd_min_recomendacao")
    private int qtdMinRecomendacao;
    
    @Column(name = "rsm_qtd_min_recomendacao_meses")
    private int qtdMinRecomendacaoMeses;
    
    @Column(name = "rsm_validar_empresa")
    private boolean validarEmpresa;
    
    @Column(name = "rsm_ufrg_ufplaca")
    private boolean ufRgUfPlaca;
    
    @Column(name = "rsm_veiculo_prop_motorista")
    private boolean veiculoPropMotorista;
    
    @Column(name = "rsm_veiculo_prop_empresa")    
    private boolean veiculoPropEmpresa;
    
    @Column(name = "rsm_residencia_comprovante")      
    private boolean residenciaComprovante;
    
    @Column(name = "rsm_dt_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao;
    
	@Column(name = "rsm_dt_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtExclusao;
	
	@Column(name = "rsm_dt_alteracao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtAlteracao;
	
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "rsm_usuoid_criacao")
	private Usuario usuCriacao;
	
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "rsm_usuoid_alteracao")
	private Usuario usuAlteracao;
	
    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "rsm_usuoid_exclusao")	
	private Usuario usuExclusao;

	public RegraSeguradoraMotorista() {
	}

	public RegraSeguradoraMotorista(Integer id){
		this.id = id;
	}
	
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

	public MotoristaVinculo getVinculo() {
		return vinculo;
	}

	public void setVinculo(MotoristaVinculo vinculo) {
		this.vinculo = vinculo;
	}

	public Integer getMercadoria() {
		return mercadoria;
	}

	public void setMercadoria(Integer mercadoria) {
		this.mercadoria = mercadoria;
	}

	public BigDecimal getVlrPgrInicio() {
		return vlrPgrInicio;
	}

	public void setVlrPgrInicio(BigDecimal vlrPgrInicio) {
		this.vlrPgrInicio = vlrPgrInicio;
	}

	public BigDecimal getVlrPgrFinal() {
		return vlrPgrFinal;
	}

	public void setVlrPgrFinal(BigDecimal vlrPgrFinal) {
		this.vlrPgrFinal = vlrPgrFinal;
	}

	public int getQtdMinRecomendacao() {
		return qtdMinRecomendacao;
	}

	public void setQtdMinRecomendacao(int qtdMinRecomendacao) {
		this.qtdMinRecomendacao = qtdMinRecomendacao;
	}

	public int getQtdMinRecomendacaoMeses() {
		return qtdMinRecomendacaoMeses;
	}

	public void setQtdMinRecomendacaoMeses(int qtdMinRecomendacaoMeses) {
		this.qtdMinRecomendacaoMeses = qtdMinRecomendacaoMeses;
	}
	
	public boolean isValidarEmpresa() {
		return validarEmpresa;
	}

	public void setValidarEmpresa(boolean validarEmpresa) {
		this.validarEmpresa = validarEmpresa;
	}

	public boolean isUfRgUfPlaca() {
		return ufRgUfPlaca;
	}

	public void setUfRgUfPlaca(boolean ufRgUfPlaca) {
		this.ufRgUfPlaca = ufRgUfPlaca;
	}

	public boolean isVeiculoPropMotorista() {
		return veiculoPropMotorista;
	}

	public void setVeiculoPropMotorista(boolean veiculoPropMotorista) {
		this.veiculoPropMotorista = veiculoPropMotorista;
	}

	public boolean isVeiculoPropEmpresa() {
		return veiculoPropEmpresa;
	}

	public void setVeiculoPropEmpresa(boolean veiculoPropEmpresa) {
		this.veiculoPropEmpresa = veiculoPropEmpresa;
	}

	public boolean isResidenciaComprovante() {
		return residenciaComprovante;
	}

	public void setResidenciaComprovante(boolean residenciaComprovante) {
		this.residenciaComprovante = residenciaComprovante;
	}

	public Date getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(Date dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public Date getDtExclusao() {
		return dtExclusao;
	}

	public void setDtExclusao(Date dtExclusao) {
		this.dtExclusao = dtExclusao;
	}

	public Date getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
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
	
}
