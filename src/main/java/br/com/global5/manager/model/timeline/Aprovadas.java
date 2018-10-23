package br.com.global5.manager.model.timeline;


/**
 * Created by zielinski on 09/04/17.
 */

public class Aprovadas {
    private Integer id;
    private String cpf;
    private String nome;
    private String liberacao;
    private String url;
    private String title;
    private String icon;

    public Aprovadas() {}

    public Aprovadas(Integer id, String cpf, String nome, String liberacao,
                     String url, String title, String icon) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.liberacao = liberacao;
        this.url = url;
        this.title = title;
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLiberacao() {
        return liberacao;
    }

    public void setLiberacao(String liberacao) {
        this.liberacao = liberacao;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
