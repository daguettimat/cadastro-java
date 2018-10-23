package br.com.global5.manager.model.criminal;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "numero_processo",
        "uf",
        "data_processo",
        "area_processo",
        "classe_processual",
        "assuntos_processo",
        "situacao_processo",
        "comarca",
        "parte"
})
public class Processo {

    @JsonProperty("numero_processo")
    private String numeroProcesso;
    @JsonProperty("uf")
    private String uf;
    @JsonProperty("data_processo")
    private String dataProcesso;
    @JsonProperty("area_processo")
    private String areaProcesso;
    @JsonProperty("classe_processual")
    private String classeProcessual;
    @JsonProperty("assuntos_processo")
    private List<String> assuntosProcesso = null;
    @JsonProperty("situacao_processo")
    private String situacaoProcesso;
    @JsonProperty("comarca")
    private String comarca;
    @JsonProperty("parte")
    private List<Parte> parte = null;

    @JsonProperty("numero_processo")
    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    @JsonProperty("numero_processo")
    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    @JsonProperty("uf")
    public String getUf() {
        return uf;
    }

    @JsonProperty("uf")
    public void setUf(String uf) {
        this.uf = uf;
    }

    @JsonProperty("data_processo")
    public String getDataProcesso() {
        return dataProcesso;
    }

    @JsonProperty("data_processo")
    public void setDataProcesso(String dataProcesso) {
        this.dataProcesso = dataProcesso;
    }

    @JsonProperty("area_processo")
    public String getAreaProcesso() {
        return areaProcesso;
    }

    @JsonProperty("area_processo")
    public void setAreaProcesso(String areaProcesso) {
        this.areaProcesso = areaProcesso;
    }

    @JsonProperty("classe_processual")
    public String getClasseProcessual() {
        return classeProcessual;
    }

    @JsonProperty("classe_processual")
    public void setClasseProcessual(String classeProcessual) {
        this.classeProcessual = classeProcessual;
    }

    @JsonProperty("assuntos_processo")
    public List<String> getAssuntosProcesso() {
        return assuntosProcesso;
    }

    @JsonProperty("assuntos_processo")
    public void setAssuntosProcesso(List<String> assuntosProcesso) {
        this.assuntosProcesso = assuntosProcesso;
    }

    @JsonProperty("situacao_processo")
    public String getSituacaoProcesso() {
        return situacaoProcesso;
    }

    @JsonProperty("situacao_processo")
    public void setSituacaoProcesso(String situacaoProcesso) {
        this.situacaoProcesso = situacaoProcesso;
    }

    @JsonProperty("comarca")
    public String getComarca() {
        return comarca;
    }

    @JsonProperty("comarca")
    public void setComarca(String comarca) {
        this.comarca = comarca;
    }

    @JsonProperty("parte")
    public List<Parte> getParte() {
        return parte;
    }

    @JsonProperty("parte")
    public void setParte(List<Parte> parte) {
        this.parte = parte;
    }

}