package br.com.global5.manager.model.cadastro;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Resposta",
        "Avaliacao"
})
public class ReferenciaRespostas {

    @JsonProperty("Resposta")
    private String resposta;
    @JsonProperty("Avaliacao")
    private Integer avaliacao;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Resposta")
    public String getResposta() {
        return resposta;
    }

    @JsonProperty("Resposta")
    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    @JsonProperty("Avaliacao")
    public Integer getAvaliacao() {
        return avaliacao;
    }

    @JsonProperty("Avaliacao")
    public void setAvaliacao(Integer avaliacao) {
        this.avaliacao = avaliacao;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}