package br.com.global5.rastreamento.model.sinc;

import javax.persistence.*;

@Entity
@Table(name="sinc_cliente")
public class SincCliente {
	
	@Id
	@Column(name="clioid")
	private Integer id ;
	
	@Column(name="cli_vl_ativo")
	private Integer ativo;

	@Column(name="cli_fantasia")
	private String fantasia;

	@Column(name="cli_razao_social")
	private String razaoSocial;

	@Column(name="cli_contato")
	private String contato;

	@Column(name="cli_cnpj_cpf")
	private String cnpjCpf;

	@Column(name="cli_endereco")
	private String endereco;
	
	@Column(name="cli_complemento")
	private String complemento;

	@Column(name="cli_cep")
	private String cep;

	@Column(name="cli_fone")
	private String fone;

	@Column(name="cli_email")
	private String email;

	@Column(name="cli_email_nao_conformidades")
	private String emailNaoConformidades;
	
	@Column(name="cli_uf")
	private String uf;

	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = SincCidade.class
		)
	@JoinColumn(name="cli_cidoid")
	private SincCidade cidade;
	
	@Column(name="cli_cidade")
	private String nomeCidade;
	
	public SincCliente (){}
	
	public SincCliente (Integer id){this.id = id;}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAtivo() {
		return ativo;
	}

	public void setAtivo(Integer ativo) {
		this.ativo = ativo;
	}

	public String getFantasia() {
		return fantasia;
	}

	public void setFantasia(String fantasia) {
		this.fantasia = fantasia;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public String getCnpjCpf() {
		return cnpjCpf;
	}

	public void setCnpjCpf(String cnpjCpf) {
		this.cnpjCpf = cnpjCpf;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getFone() {
		return fone;
	}

	public void setFone(String fone) {
		this.fone = fone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailNaoConformidades() {
		return emailNaoConformidades;
	}

	public void setEmailNaoConformidades(String emailNaoConformidades) {
		this.emailNaoConformidades = emailNaoConformidades;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public SincCidade getCidade() {
		return cidade;
	}

	public void setCidade(SincCidade cidade) {
		this.cidade = cidade;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

}
