package br.com.global5.manager.model.auxiliar;

import br.com.global5.infra.model.BaseEntity;

public class Endereco implements BaseEntity {

    private Integer id;

    private Integer tipoEndereco;
    private String logradouro;
    private Integer numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String pais;
    private String cep;
    
    public Endereco() {}

    public Endereco(Integer id ) {
        this.id = id;
    }

    public Endereco(Integer tipoEndereco, String logradouro, Integer numero, String complemento,
                    String bairro, String cidade, String uf, String pais, String cep) {
        this.tipoEndereco = tipoEndereco;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.pais = pais;
        this.cep = cep;
    }

    public Endereco nome(String logradouro) {
        this.logradouro = logradouro;
        return this;
    }


    public String toCSVHeader(String prefixo) {
        return  prefixo + "end_id;" + prefixo + "end_tipoEndereco;" + prefixo + "end_logradouro;" + prefixo + "end_numero;" +
                prefixo + "end_complemento;" + prefixo + "end_bairro;" + prefixo + "end_cidade;" + prefixo + "end_uf;" +
                prefixo + "end_pais;" + prefixo + "end_cep;";
    }

    public String toCSVString() {
        return id +
                ";" + tipoEndereco +
                ";" + logradouro +
                ";" + numero +
                ";" + complemento +
                ";" + bairro +
                ";" + cidade +
                ";" + uf +
                ";" + pais +
                ";" + cep ;
    }


    public boolean hasNome() {
        return logradouro != null && !"".equals(logradouro.trim());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInteger() {
        return tipoEndereco;
    }

    public void setInteger(Integer tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCep() {
        if(cep == null) {
            return "00000-000";
        } else {
            return cep;
        }
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getTipoEndereco() {
        return tipoEndereco;
    }

    public void setTipoEndereco(Integer tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }
}
