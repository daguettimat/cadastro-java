package br.com.global5.manager.model.criminal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "codigo_retorno",
        "mensagem",
        "dados_solicitacao",
        "processos"
})
public class Retorno {

    // {"return" : {
    //               "codigo_retorno"   : "09",
    //               "mensagem"         : "Solicitação em processamento. Aguarde a conclusão da análise para obter o retorno",
    //               "dados_solicitacao": {
    //                                      "cpf_pessoa"         : "43401120972",
    //                                      "codigo_solicitacao" : 401074,
    //                                      "data_solicitacao"   : "2017-06-28 11:00:06",
    //                                      "login_solicitacao"  : "WS-GLOBAL5"
    //                                    },
    //               "processos":{}
    //             }
    // }

    @JsonProperty("codigo_retorno")
    private String codigoRetorno;
    @JsonProperty("mensagem")
    private String mensagem;
    @JsonProperty("dados_solicitacao")
    private DadosSolicitacao dadosSolicitacao;
    @JsonProperty("processos")
    private String processos;

    @JsonProperty("codigo_retorno")
    public String getCodigoRetorno() {
        return codigoRetorno;
    }

    @JsonProperty("codigo_retorno")
    public void setCodigoRetorno(String codigoRetorno) {
        this.codigoRetorno = codigoRetorno;
    }

    @JsonProperty("mensagem")
    public String getMensagem() {
        return mensagem;
    }

    @JsonProperty("mensagem")
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @JsonProperty("dados_solicitacao")
    public DadosSolicitacao getDadosSolicitacao() {
        return dadosSolicitacao;
    }

    @JsonProperty("dados_solicitacao")
    public void setDadosSolicitacao(DadosSolicitacao dadosSolicitacao) {
        this.dadosSolicitacao = dadosSolicitacao;
    }

    @JsonProperty("processos")
    public String getProcessos() {
        return processos;
    }

    @JsonProperty("processos")
    public void setProcessos(String processos) {
        this.processos = processos;
    }

}