package br.com.global5.infra.atualiza_motvei_chp;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SqlResultSetMapping (
        name = "ListaMotoristaCadTMapping",
        entities = @EntityResult(
                entityClass = acMotoristaCadT.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "cpf", column = "cpf"),
                        @FieldResult(name = "nome", column = "nome"),
                        @FieldResult(name = "rg", column = "rg"),
                        @FieldResult(name = "ufRgEmissor", column = "ufRgEmissor"),
                        @FieldResult(name = "cnh", column = "cnh"),
                        @FieldResult(name = "categoriaCnh", column = "categoriaCnh"),
                        @FieldResult(name = "ufCnh", column = "ufCnh"),
                        @FieldResult(name = "validacaoCnh", column = "validacaoCnh"),
                        @FieldResult(name = "dtVenctoCnh", column = "dtVenctoCnh"),                       
                        @FieldResult(name = "dtNascimento", column = "dtNascimento"),
                        @FieldResult(name = "dtPriEmissao", column = "dtPriEmissao"),
                        @FieldResult(name = "nomePai", column = "nomePai"),
                        @FieldResult(name = "nomeMae", column = "nomeMae"),
                        @FieldResult(name = "logradouro", column = "logradouro"),
                        @FieldResult(name = "cep", column = "cep"),
                        @FieldResult(name = "numEndereco", column = "numEndereco"),
                        @FieldResult(name = "ufEndereco", column = "ufEndereco"),
                        @FieldResult(name = "paisEndereco", column = "paisEndereco"),
                        @FieldResult(name = "bairroEndereco", column = "bairroEndereco"),
                        @FieldResult(name = "complEndereco", column = "complEndereco"),
                        @FieldResult(name = "cidadeEndereco", column = "cidadeEndereco")                       
                }
        )              
)



@Entity
public class acMotoristaCadT {
	@Id
	private Integer id;
	private String  cpf;
	private String  nome;
	private String  rg;
	private String ufRgEmissor;		// mot_documento2_uf	
	private String  cnh;
	private String  categoriaCnh;
	private String  ufCnh;
	private String  validacaoCnh;
	@Temporal(TemporalType.DATE)
	private Date 	dtVenctoCnh;	
	@Temporal(TemporalType.DATE)	//mot_dt_nascimento
	private Date dtNascimento;
	@Temporal(TemporalType.DATE)	// mcnh_primeira_emissao
	private Date dtPriEmissao;
	private String nomePai;			// mot_nomepai
	private String nomeMae;			// mot_nomemae	
	private String logradouro;		// loc_logradouro
	private String cep;				// loc_cep
	private Integer numEndereco;	// loc_numero
	private String ufEndereco;		// loc_uf
	private String paisEndereco;	// loc_pais
	private String bairroEndereco;  // loc_bairro
	private String complEndereco; 	// loc_complemento
	private String cidadeEndereco;  // loc_cidade
	
	public acMotoristaCadT(){}

	public Integer getId() {
		return id;
	}

	public String getCpf() {
		return cpf;
	}

	public String getNome() {
		return nome;
	}

	public String getRg() {
		return rg;
	}

	public String getCnh() {
		return cnh;
	}

	public String getCategoriaCnh() {
		return categoriaCnh;
	}

	public String getUfCnh() {
		return ufCnh;
	}

	public String getValidacaoCnh() {
		return validacaoCnh;
	}

	public Date getDtVenctoCnh() {
		return dtVenctoCnh;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public void setCnh(String cnh) {
		this.cnh = cnh;
	}

	public void setCategoriaCnh(String categoriaCnh) {
		this.categoriaCnh = categoriaCnh;
	}

	public void setUfCnh(String ufCnh) {
		this.ufCnh = ufCnh;
	}

	public void setValidacaoCnh(String validacaoCnh) {
		this.validacaoCnh = validacaoCnh;
	}

	public void setDtVenctoCnh(Date dtVenctoCnh) {
		this.dtVenctoCnh = dtVenctoCnh;
	}

	public String getUfRgEmissor() {
		return ufRgEmissor;
	}

	public Date getDtNascimento() {
		return dtNascimento;
	}

	public Date getDtPriEmissao() {
		return dtPriEmissao;
	}

	public String getNomePai() {
		return nomePai;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getCep() {
		return cep;
	}

	public Integer getNumEndereco() {
		return numEndereco;
	}

	public String getUfEndereco() {
		return ufEndereco;
	}

	public String getPaisEndereco() {
		return paisEndereco;
	}

	public String getBairroEndereco() {
		return bairroEndereco;
	}

	public String getComplEndereco() {
		return complEndereco;
	}

	public void setUfRgEmissor(String ufRgEmissor) {
		this.ufRgEmissor = ufRgEmissor;
	}

	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public void setDtPriEmissao(Date dtPriEmissao) {
		this.dtPriEmissao = dtPriEmissao;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setNumEndereco(Integer numEndereco) {
		this.numEndereco = numEndereco;
	}

	public void setUfEndereco(String ufEndereco) {
		this.ufEndereco = ufEndereco;
	}

	public void setPaisEndereco(String paisEndereco) {
		this.paisEndereco = paisEndereco;
	}

	public void setBairroEndereco(String bairroEndereco) {
		this.bairroEndereco = bairroEndereco;
	}

	public void setComplEndereco(String complEndereco) {
		this.complEndereco = complEndereco;
	}

	public String getCidadeEndereco() {
		return cidadeEndereco;
	}

	public void setCidadeEndereco(String cidadeEndereco) {
		this.cidadeEndereco = cidadeEndereco;
	}
	
	
}
