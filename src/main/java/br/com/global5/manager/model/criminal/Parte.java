package br.com.global5.manager.model.criminal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "nome_parte",
        "parte_pesquisada",
        "cpf",
        "data_nascimento",
        "tipo_parte",
        "situacao"
})
public class Parte {

    @JsonProperty("nome_parte")
    private String nomeParte;
    @JsonProperty("parte_pesquisada")
    private Boolean partePesquisada;
    @JsonProperty("cpf")
    private String cpf;
    @JsonProperty("data_nascimento")
    private String dataNascimento;
    @JsonProperty("tipo_parte")
    private String tipoParte;
    @JsonProperty("situacao")
    private String situacao;

    @JsonProperty("nome_parte")
    public String getNomeParte() {
        return nomeParte;
    }

    @JsonProperty("nome_parte")
    public void setNomeParte(String nomeParte) {
        this.nomeParte = nomeParte;
    }

    @JsonProperty("parte_pesquisada")
    public Boolean getPartePesquisada() {
        return partePesquisada;
    }

    @JsonProperty("parte_pesquisada")
    public void setPartePesquisada(Boolean partePesquisada) {
        this.partePesquisada = partePesquisada;
    }

    @JsonProperty("cpf")
    public String getCpf() {
        return cpf;
    }

    @JsonProperty("cpf")
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @JsonProperty("data_nascimento")
    public String getDataNascimento() {
        return dataNascimento;
    }

    @JsonProperty("data_nascimento")
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @JsonProperty("tipo_parte")
    public String getTipoParte() {
        return tipoParte;
    }

    @JsonProperty("tipo_parte")
    public void setTipoParte(String tipoParte) {
        this.tipoParte = tipoParte;
    }

    @JsonProperty("situacao")
    public String getSituacao() {
        return situacao;
    }

    @JsonProperty("situacao")
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

}