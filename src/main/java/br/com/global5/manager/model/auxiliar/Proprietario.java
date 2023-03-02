package br.com.global5.manager.model.auxiliar;


import br.com.global5.infra.model.BaseEntity;

import java.util.Date;


public class Proprietario implements BaseEntity {

    private Integer id;
    private String documento;
    private Endereco endereco;
    private String nome;
    private boolean nacional;
    private Date dtCriacao;
    private Date dtAlteracao;
    private Date dtExclusao;
    private Integer usuCriacao;
    private Integer usuAlteracao;
    private Integer usuExclusao;
    private Integer telefone;
    
    private String natural;
    private String dtNascimento;
    private String rgNumero;
    private String rgEmissor;
    private String rgUF;
    private String nomePai;
    private String nomeMae;

    public Proprietario() {
    }

    public Proprietario(Integer id) {
        this.id = id;
    }

    public Proprietario(Integer id, String documento, Endereco endereco, String nome, boolean nacional,
                        Date dtCriacao, Date dtAlteracao, Date dtExclusao, Integer usuCriacao, Integer usuAlteracao,
                        Integer usuExclusao, Integer telefone, String natural, String dtNascimento, String rgNumero,
                        String rgEmissor, String rgUF, String nomePai, String nomeMae) {
        this.id = id;
        this.documento = documento;
        this.endereco = endereco;
        this.nome = nome;
        this.nacional = nacional;
        this.dtCriacao = dtCriacao;
        this.dtAlteracao = dtAlteracao;
        this.dtExclusao = dtExclusao;
        this.usuCriacao = usuCriacao;
        this.usuAlteracao = usuAlteracao;
        this.usuExclusao = usuExclusao;
        this.telefone = telefone;
	    this.natural = natural;
	    this.dtNascimento = dtNascimento;
	    this.rgNumero = rgNumero;
	    this.rgEmissor = rgEmissor;
	    this.rgUF = rgUF;
	    this.nomePai = nomePai;
	    this.nomeMae = nomeMae;
    }


    public String toCSVHeader(String prefixo) {
        if( endereco == null ) {
            endereco = new Endereco();
        }
        return  prefixo + "id;" + prefixo + "documento;" + endereco.toCSVHeader(prefixo) + prefixo + "nome;" +
                prefixo + "nacional;" + prefixo + "dtCriacao;" + prefixo + "dtAlteracao;" +
                prefixo + "dtExclusao;" + prefixo + "usuCriacao;" + prefixo + "usuAlteracao;" +
                prefixo + "usuExclusao;" + prefixo + "telefone" +
                prefixo + "natural;" + prefixo + "dtNascimento;" + prefixo + "rgNumero;" + prefixo + "rgEmissor;" +
                prefixo + "rgUF;" + prefixo + "nomePai;" + prefixo + "nomeMae";                                
    }

    public String toCSVString() {
        if( endereco == null ) {
            endereco = new Endereco();
        }
        return  id +
                ";" + documento + 
                ";" + endereco.toCSVString() +
                ";" + nome + 
                ";" + nacional +
                ";" + dtCriacao +
                ";" + dtAlteracao +
                ";" + dtExclusao +
                ";" + usuCriacao +
                ";" + usuAlteracao +
                ";" + usuExclusao +
                ";" + telefone +
        		";" + natural +
        		";" + dtNascimento +
        		";" + rgNumero +
        		";" + rgEmissor +
        		";" + rgUF +
        		";" + nomePai +
        		";" + nomeMae;    		                
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isNacional() {
        return nacional;
    }

    public void setNacional(boolean nacional) {
        this.nacional = nacional;
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

    public Integer getUsuCriacao() {
        return usuCriacao;
    }

    public void setUsuCriacao(Integer usuCriacao) {
        this.usuCriacao = usuCriacao;
    }

    public Integer getUsuAlteracao() {
        return usuAlteracao;
    }

    public void setUsuAlteracao(Integer usuAlteracao) {
        this.usuAlteracao = usuAlteracao;
    }

    public Integer getUsuExclusao() {
        return usuExclusao;
    }

    public void setUsuExclusao(Integer usuExclusao) {
        this.usuExclusao = usuExclusao;
    }

    public Integer getTelefone() {
        return telefone;
    }

    public void setTelefone(Integer telefone) {
        this.telefone = telefone;
    }

	public String getNatural() {
		return natural;
	}

	public void setNatural(String natural) {
		this.natural = natural;
	}

	public String getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(String dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public String getRgNumero() {
		return rgNumero;
	}

	public void setRgNumero(String rgNumero) {
		this.rgNumero = rgNumero;
	}

	public String getRgEmissor() {
		return rgEmissor;
	}

	public void setRgEmissor(String rgEmissor) {
		this.rgEmissor = rgEmissor;
	}

	public String getRgUF() {
		return rgUF;
	}

	public void setRgUF(String rgUF) {
		this.rgUF = rgUF;
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
    
    
}
