package br.com.global5.manager.model.geral;

import java.io.Serializable;

/**
 * Created by zielinski on 23/02/17.
 */
public class DashBoard implements Serializable {

    private Integer enviadas;
    private Integer aprovadas;
    private Integer analise;
    private Integer reprovadas;


    public DashBoard() {

    }

    public DashBoard(Integer enviadas, Integer aprovadas, Integer analise, Integer reprovadas) {
        this.enviadas = enviadas;
        this.aprovadas = aprovadas;
        this.analise = analise;
        this.reprovadas = reprovadas;
    }

    public Integer getEnviadas() {
        return enviadas;
    }

    public void setEnviadas(Integer enviadas) {
        this.enviadas = enviadas;
    }

    public Integer getAprovadas() {
        return aprovadas;
    }

    public void setAprovadas(Integer aprovadas) {
        this.aprovadas = aprovadas;
    }

    public Integer getAnalise() {
        return analise;
    }

    public void setAnalise(Integer analise) {
        this.analise = analise;
    }

    public Integer getReprovadas() {
        return reprovadas;
    }

    public void setReprovadas(Integer reprovadas) {
        this.reprovadas = reprovadas;
    }
}
