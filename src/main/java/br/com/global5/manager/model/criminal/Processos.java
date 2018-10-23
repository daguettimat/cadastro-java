package br.com.global5.manager.model.criminal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "processo"
})
public class Processos {

    @JsonProperty("processo")
    private String processo;

    @JsonProperty("processo")
    public String getProcesso() {
        return processo;
    }

    @JsonProperty("processo")
    public void setProcesso(String processo) {
        this.processo = processo;
    }

}