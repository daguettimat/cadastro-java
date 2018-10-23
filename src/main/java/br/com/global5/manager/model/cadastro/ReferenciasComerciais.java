package br.com.global5.manager.model.cadastro;

/**
 * Created by zielinski on 18/04/17.
 */

public class ReferenciasComerciais {

    private Integer id;
    private String empresa;
    private String contato;
    private String tipoContato;
    private String ddd;
    private String telefone;

    public ReferenciasComerciais() {}

    public ReferenciasComerciais(Integer id, String empresa, String contato, String tipoContato,
                                 String ddd, String telefone) {
        this.id = id;
        this.empresa = empresa;
        this.contato = contato;
        this.tipoContato = tipoContato;
        this.ddd = ddd;
        this.telefone = telefone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getTipoContato() {
        return tipoContato;
    }

    public void setTipoContato(String tipoContato) {
        this.tipoContato = tipoContato;
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
