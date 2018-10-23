package br.com.global5.infra.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zielinski on 04/05/17.
 */
public class wsCNPJ {

    private String status;

    @SerializedName("return")
    private String retorno;

    private wsDadosEmpresa result;

    public wsCNPJ() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public wsDadosEmpresa getResult() {
        return result;
    }

    public void setResult(wsDadosEmpresa result) {
        this.result = result;
    }
}
