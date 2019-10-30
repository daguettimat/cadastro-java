package br.com.global5.rastreamento.model.sinc;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="sinc_motorista")
public class SincMotorista {

	@Id
	@Column(name="motoid")
	private Integer id;

	@Column(name="mot_nome")
	private String nome;

	@Temporal(TemporalType.DATE)
	@Column(name="mot_dt_nascimento")
	private Date dtNascimento;	
	
	@Column(name="mot_uf_nascimento")
	private String ufNascimento;
	
	@Column(name="mot_cidade_nascimento")
	private String cidadeNascimento;	

	@ManyToOne(
				fetch = FetchType.LAZY,
				targetEntity = SincCliente.class
			)
	@JoinColumn(name="mot_clioid")
	private SincCliente cliente;

	@Column(name="mot_cpf")
	private String cpf;
	
	@Column(name="mot_rg")
	private String rg;
	
	@Temporal(TemporalType.DATE)
	@Column(name="mot_rg_dt_emissao")
	private Date rgDtEmissao;

	@Column(name="mot_cnh")
	private String cnh;

	@Temporal(TemporalType.DATE)
	@Column(name="mot_cnh_dt_vencimento")
	private Date cnhDtVencimento;
	
	@Column(name="mot_cnh_categoria")
	private String cnhCategoria;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincUf.class
		)
	@JoinColumn(name="mot_cnh_uf")
	private SincUf cnhUf;	
	
	@Column(name="mot_fone_residencial")
	private String foneResidencial;

	@Column(name="mot_fone_cel1")
	private String foneCel1;

	@Column(name="mot_fone_cel2")
	private String foneCel2;

	@Column(name="mot_sexo")
	private Integer sexo;
	
	@Column(name="mot_vinculo")
	private Integer vinculo;
	
	@Column(name="mot_nome_pai")
	private String nomePai;
		
	@Column(name="mot_nome_mae")
	private String nomeMae;

	@Column(name="mot_rua")
	private String rua;

	@Column(name="mot_numero")
	private String numero;

	@Column(name="mot_complemento")
	private String complemento;

	@Column(name="mot_bairro")
	private String bairro;

	@Column(name="mot_uf")
	private String descUf;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincUf.class
		)
	@JoinColumn(name="mot_ufoid")
	private SincUf uf;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCidade.class
		)
	@JoinColumn(name="mot_cidoid")
	private SincCidade cidade;

	@Column(name="mot_cep")
	private String cep;
	
	@Column(name="mot_vl_status")
	private Integer status;
	
	@Column(name="mot_vl_ativo")
	private Integer ativo;

	@Column(name="mot_rg_uf")
	private String rgUf;

	@Column(name="mot_cidade")
	private String nomeCidade;

	
	public SincMotorista() {}
	
	public SincMotorista(Integer id) {this.id = id;}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public String getUfNascimento() {
		return ufNascimento;
	}

	public void setUfNascimento(String ufNascimento) {
		this.ufNascimento = ufNascimento;
	}

	public String getCidadeNascimento() {
		return cidadeNascimento;
	}

	public void setCidadeNascimento(String cidadeNascimento) {
		this.cidadeNascimento = cidadeNascimento;
	}

	public SincCliente getCliente() {
		return cliente;
	}

	public void setCliente(SincCliente cliente) {
		this.cliente = cliente;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Date getRgDtEmissao() {
		return rgDtEmissao;
	}

	public void setRgDtEmissao(Date rgDtEmissao) {
		this.rgDtEmissao = rgDtEmissao;
	}

	public String getCnh() {
		return cnh;
	}

	public void setCnh(String cnh) {
		this.cnh = cnh;
	}

	public Date getCnhDtVencimento() {
		return cnhDtVencimento;
	}

	public void setCnhDtVencimento(Date cnhDtVencimento) {
		this.cnhDtVencimento = cnhDtVencimento;
	}

	public String getCnhCategoria() {
		return cnhCategoria;
	}

	public void setCnhCategoria(String cnhCategoria) {
		this.cnhCategoria = cnhCategoria;
	}

	public SincUf getCnhUf() {
		return cnhUf;
	}

	public void setCnhUf(SincUf cnhUf) {
		this.cnhUf = cnhUf;
	}

	public String getFoneResidencial() {
		return foneResidencial;
	}

	public void setFoneResidencial(String foneResidencial) {
		this.foneResidencial = foneResidencial;
	}

	public String getFoneCel1() {
		return foneCel1;
	}

	public void setFoneCel1(String foneCel1) {
		this.foneCel1 = foneCel1;
	}

	public String getFoneCel2() {
		return foneCel2;
	}

	public void setFoneCel2(String foneCel2) {
		this.foneCel2 = foneCel2;
	}

	public Integer getSexo() {
		return sexo;
	}

	public void setSexo(Integer sexo) {
		this.sexo = sexo;
	}

	public Integer getVinculo() {
		return vinculo;
	}

	public void setVinculo(Integer vinculo) {
		this.vinculo = vinculo;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getDescUf() {
		return descUf;
	}

	public void setDescUf(String descUf) {
		this.descUf = descUf;
	}

	public SincUf getUf() {
		return uf;
	}

	public void setUf(SincUf uf) {
		this.uf = uf;
	}

	public SincCidade getCidade() {
		return cidade;
	}

	public void setCidade(SincCidade cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAtivo() {
		return ativo;
	}

	public void setAtivo(Integer ativo) {
		this.ativo = ativo;
	}

	public String getRgUf() {
		return rgUf;
	}

	public void setRgUf(String rgUf) {
		this.rgUf = rgUf;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public void setId(Integer id) {
		this.id = id;
	}
		
}
