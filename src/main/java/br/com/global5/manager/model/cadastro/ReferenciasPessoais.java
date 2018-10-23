package br.com.global5.manager.model.cadastro;

import javax.persistence.Entity;

/**
 * Created by zielinski on 18/04/17.
 */

public class ReferenciasPessoais {

    private Integer id;
    private String contato;
    private String parentesco;
    private String ddd;
    private String telefone;

    public ReferenciasPessoais() {}

    public ReferenciasPessoais(Integer id, String contato, String parentesco, String ddd, String telefone) {
        this.id = id;
        this.contato = contato;
        this.parentesco = parentesco;
        this.ddd = ddd;
        this.telefone = telefone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
