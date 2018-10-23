package br.com.global5.infra.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zielinski on 04/05/17.
 */
public class wsDadosEmpresa {
//
//    {
//        "numero_de_inscricao": "10336846000150",
//        "tipo": "MATRIZ",
//        "abertura": "22\/07\/2008",
//        "nome": "GLOBAL 5 ENGENHARIA DE RISCOS LTDA - ME",
//        "fantasia": "GLOBAL 5 ENGENHARIA DE RISCOS",
//        "atividade_principal": {
//                                     "text": "Atividades de monitoramento de sistemas de segurança eletrônico",
//                                     "code": "80.20-0-01"
//         },
//        "atividades_secundarias": [
//                                   {
//                                      "text": "Atividades de cobranças e informações cadastrais",
//                                       "code": "82.91-1-00"
//                                   }
//		  ],
//        "natureza_juridica": "206-2 - Sociedade Empresária Limitada",
//        "logradouro": "R INACIO LUSTOSA",
//        "numero": "1000",
//        "complemento": "",
//        "cep": "80.510-000",
//        "bairro": "SAO FRANCISCO",
//        "municipio": "CURITIBA",
//        "uf": "PR",
//        "email": "",
//        "telefone": "(41) 3051-7550",
//        "entidade_federativo_responsavel": "",
//        "situacao": "ATIVA",
//        "dt_situacao_cadastral": "22\/07\/2008",
//        "situacao_especial": "",
//        "data_situacao_especial": "",
//        "capital_social": "400000.00",
//        "quadro_socios": [
//                               "VILDO FORTUNATO DE OLIVEIRA 49-Sócio-Administrador",
//                               "ANTONIO PAULO DOS SANTOS 49-Sócio-Administrador",
//                               "ANDERSON SELSON PEREZ MARTINS 22-Sócio"
//		                   ]
//    }

    @SerializedName("numero_de_inscricao")
    private String numero_inscricao;
    private String tipo;
    private String abertura;
    private String nome;
    private String fantasia;
    @SerializedName("atividade_principal")
    private wsAtividade atividadePrincipal;
    @SerializedName("atividades_secundarias")
    private wsAtividade[] atividadeSecundaria;
    @SerializedName("natureza_juridica")
    private String naturezaJuridica;
    private String logradouro;
    private String numero;
    private String complemento;
    private String cep;
    private String bairro;
    private String municipio;
    private String uf;
    private String email;
    private String telefone;
    @SerializedName("entidade_federativo_responsavel")
    private String entidadeFederativaResponsavel;

    private String situacao;
    @SerializedName("dt_situacao_cadastral")
    private String dtSituacaoCadastral;
    @SerializedName("situacao_especial")
    private String situacaoEspecial;
    @SerializedName("data_situacao_especial")
    private String dtSituacaoEspecial;
    @SerializedName("capital_social")
    private String capitalSocial;
    @SerializedName("quadro_socios")
    private String[] quadroSocios;

    public wsDadosEmpresa() {
    }

    @Override
    public String toString() {
        return "wsDadosEmpresa{" +
                "numero_inscricao='" + numero_inscricao + '\'' +
                ", tipo='" + tipo + '\'' +
                ", abertura='" + abertura + '\'' +
                ", nome='" + nome + '\'' +
                ", fantasia='" + fantasia + '\'' +
                ", atividadePrincipal=" + atividadePrincipal +
                ", atividadeSecundaria=" + atividadeSecundaria +
                ", naturezaJuridica='" + naturezaJuridica + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", numero='" + numero + '\'' +
                ", complemento='" + complemento + '\'' +
                ", cep='" + cep + '\'' +
                ", bairro='" + bairro + '\'' +
                ", municipio='" + municipio + '\'' +
                ", uf='" + uf + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", entidadeFederativaResponsavel='" + entidadeFederativaResponsavel + '\'' +
                ", situacao='" + situacao + '\'' +
                ", dtSituacaoCadastral='" + dtSituacaoCadastral + '\'' +
                ", situacaoEspecial='" + situacaoEspecial + '\'' +
                ", dtSituacaoEspecial='" + dtSituacaoEspecial + '\'' +
                ", capitalSocial='" + capitalSocial + '\'' +
                ", quadroSocios='" + quadroSocios + '\'' +
                '}';
    }

    public String getNumero_inscricao() {
        return numero_inscricao;
    }

    public void setNumero_inscricao(String numero_inscricao) {
        this.numero_inscricao = numero_inscricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAbertura() {
        return abertura;
    }

    public void setAbertura(String abertura) {
        this.abertura = abertura;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public wsAtividade getAtividadePrincipal() {
        return atividadePrincipal;
    }

    public void setAtividadePrincipal(wsAtividade atividadePrincipal) {
        this.atividadePrincipal = atividadePrincipal;
    }

    public wsAtividade[] getAtividadeSecundaria() {
        return atividadeSecundaria;
    }

    public void setAtividadeSecundaria(wsAtividade[] atividadeSecundaria) {
        this.atividadeSecundaria = atividadeSecundaria;
    }

    public String getNaturezaJuridica() {
        return naturezaJuridica;
    }

    public void setNaturezaJuridica(String naturezaJuridica) {
        this.naturezaJuridica = naturezaJuridica;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEntidadeFederativaResponsavel() {
        return entidadeFederativaResponsavel;
    }

    public void setEntidadeFederativaResponsavel(String entidadeFederativaResponsavel) {
        this.entidadeFederativaResponsavel = entidadeFederativaResponsavel;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getDtSituacaoCadastral() {
        return dtSituacaoCadastral;
    }

    public void setDtSituacaoCadastral(String dtSituacaoCadastral) {
        this.dtSituacaoCadastral = dtSituacaoCadastral;
    }

    public String getSituacaoEspecial() {
        return situacaoEspecial;
    }

    public void setSituacaoEspecial(String situacaoEspecial) {
        this.situacaoEspecial = situacaoEspecial;
    }

    public String getDtSituacaoEspecial() {
        return dtSituacaoEspecial;
    }

    public void setDtSituacaoEspecial(String dtSituacaoEspecial) {
        this.dtSituacaoEspecial = dtSituacaoEspecial;
    }

    public String getCapitalSocial() {
        return capitalSocial;
    }

    public void setCapitalSocial(String capitalSocial) {
        this.capitalSocial = capitalSocial;
    }


}
