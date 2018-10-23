package br.com.global5.infra.model;

/**
 * Created by zielinski on 04/05/17.
 */
public class wsAtividade {

    private String text;
    private String code;

    public wsAtividade() {}

    public wsAtividade(String text, String code) {
        this.text = text;
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
