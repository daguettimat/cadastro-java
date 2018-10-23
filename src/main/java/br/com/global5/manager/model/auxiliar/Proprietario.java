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

    public Proprietario() {
    }

    public Proprietario(Integer id) {
        this.id = id;
    }

    public Proprietario(Integer id, String documento, Endereco endereco, String nome, boolean nacional,
                        Date dtCriacao, Date dtAlteracao, Date dtExclusao, Integer usuCriacao, Integer usuAlteracao,
                        Integer usuExclusao, Integer telefone) {
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
    }


    public String toCSVHeader(String prefixo) {
        if( endereco == null ) {
            endereco = new Endereco();
        }
        return  prefixo + "id;" + prefixo + "documento;" + endereco.toCSVHeader(prefixo) + prefixo + "nome;" +
                prefixo + "nacional;" + prefixo + "dtCriacao;" + prefixo + "dtAlteracao;" +
                prefixo + "dtExclusao;" + prefixo + "usuCriacao;" + prefixo + "usuAlteracao;" +
                prefixo + "usuExclusao;" + prefixo + "telefone";
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
                ";" + telefone;
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
}
