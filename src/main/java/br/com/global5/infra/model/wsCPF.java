package br.com.global5.infra.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zielinski on 04/05/17.
 */
public class wsCPF {

    private String status;

    @SerializedName("return")
    private String retorno;

    private wsDadosPessoa result;

    public wsCPF() {}

    @Override
    public String toString() {
        return "wsCPF{" +
                "status='" + status + '\'' +
                ", retorno='" + retorno + '\'' +
                ", result=" + result +
                '}';
    }

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

    public wsDadosPessoa getResult() {
        return result;
    }

    public void setResult(wsDadosPessoa result) {
        this.result = result;
    }
}
