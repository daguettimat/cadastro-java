package br.com.global5.manager.model.timeline;

/**
 * Created by zielinski on 10/04/17.
 */
public class Reprovadas {

    private Integer id;
    private String descricao;
    private String title;
    private String icon;
    private String url;


    public Reprovadas() {
    }

    public Reprovadas(Integer id, String descricao, String title, String icon, String url) {
        this.id = id;
        this.descricao = descricao;
        this.title = title;
        this.icon = icon;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
