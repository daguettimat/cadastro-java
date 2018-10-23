package br.com.global5.manager.model.apoio;

public class Servicos {

    private int id;
    private int tipoServico;
    private boolean disponivel;
    private int horFunc;
    private double valor;
    private String observacoes;

    public Servicos() {}

    public Servicos(int id, int tipoServico, boolean disponivel, int horFunc, double valor, String observacoes) {
        this.id = id;
        this.tipoServico = tipoServico;
        this.disponivel = disponivel;
        this.horFunc = horFunc;
        this.valor = valor;
        this.observacoes = observacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(int tipoServico) {
        this.tipoServico = tipoServico;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public int getHorFunc() {
        return horFunc;
    }

    public void setHorFunc(int horFunc) {
        this.horFunc = horFunc;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
