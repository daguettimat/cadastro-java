
package br.com.global5.manager.model.criminal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cpf_pessoa",
        "codigo_solicitacao",
        "data_solicitacao",
        "login_solicitacao"
})
public class DadosSolicitacao {

    @JsonProperty("cpf_pessoa")
    private String cpfPessoa;
    @JsonProperty("codigo_solicitacao")
    private Integer codigoSolicitacao;
    @JsonProperty("data_solicitacao")
    private String dataSolicitacao;
    @JsonProperty("login_solicitacao")
    private String loginSolicitacao;

    @JsonProperty("cpf_pessoa")
    public String getCpfPessoa() {
        return cpfPessoa;
    }

    @JsonProperty("cpf_pessoa")
    public void setCpfPessoa(String cpfPessoa) {
        this.cpfPessoa = cpfPessoa;
    }

    @JsonProperty("codigo_solicitacao")
    public Integer getCodigoSolicitacao() {
        return codigoSolicitacao;
    }

    @JsonProperty("codigo_solicitacao")
    public void setCodigoSolicitacao(Integer codigoSolicitacao) {
        this.codigoSolicitacao = codigoSolicitacao;
    }

    @JsonProperty("data_solicitacao")
    public String getDataSolicitacao() {
        return dataSolicitacao;
    }

    @JsonProperty("data_solicitacao")
    public void setDataSolicitacao(String dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    @JsonProperty("login_solicitacao")
    public String getLoginSolicitacao() {
        return loginSolicitacao;
    }

    @JsonProperty("login_solicitacao")
    public void setLoginSolicitacao(String loginSolicitacao) {
        this.loginSolicitacao = loginSolicitacao;
    }

}