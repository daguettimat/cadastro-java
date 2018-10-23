package br.com.global5.infra.model;

import java.io.Serializable;

/**
 * Created by zielinski on 01/06/17.
 */
public class TipoValor  {

    private String tipo;
    private String valor;

    public TipoValor() {
    }

    public TipoValor(String tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
