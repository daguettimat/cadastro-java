package br.com.global5.manager.model.analise;

import javax.persistence.*;

@SqlResultSetMapping(
        name = "acResumoFichas",
        entities = @EntityResult(
                entityClass = acTimeLine.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "mes", column = "mes"),
                        @FieldResult(name = "ano", column = "ano"),
                        @FieldResult(name = "analise", column = "analise"),
                        @FieldResult(name = "pendentes", column = "pendentes"),
                        @FieldResult(name = "excluidas", column = "excluidas"),
                        @FieldResult(name = "recomendadas", column = "recomendadas"),
                        @FieldResult(name = "naorecomendadas", column = "naorecomendadas"),
                        @FieldResult(name = "renovacoes", column = "renovacoes"),
                        @FieldResult(name = "total", column = "total")
                }
        )
)
@Entity
public class acResumoFichas {

    @Id
    private Integer id;
    private Integer mes;
    private Integer ano;
    private Integer analise;
    private Integer pendentes;
    private Integer excluidas;
    private Integer recomendadas;
    private Integer naorecomendadas;
    private Integer renovacoes;
    private Integer total;

    public acResumoFichas() {};

    public acResumoFichas(Integer id, Integer mes, Integer ano, Integer analise, Integer pendentes,
                          Integer excluidas, Integer recomendadas, Integer naorecomendadas,
                          Integer renovacoes, Integer total) {
        this.id = id;
        this.mes = mes;
        this.ano = ano;
        this.analise = analise;
        this.pendentes = pendentes;
        this.excluidas = excluidas;
        this.recomendadas = recomendadas;
        this.naorecomendadas = naorecomendadas;
        this.renovacoes = renovacoes;
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getAnalise() {
        return analise;
    }

    public void setAnalise(Integer analise) {
        this.analise = analise;
    }

    public Integer getPendentes() {
        return pendentes;
    }

    public void setPendentes(Integer pendentes) {
        this.pendentes = pendentes;
    }

    public Integer getExcluidas() {
        return excluidas;
    }

    public void setExcluidas(Integer excluidas) {
        this.excluidas = excluidas;
    }

    public Integer getRecomendadas() {
        return recomendadas;
    }

    public void setRecomendadas(Integer recomendadas) {
        this.recomendadas = recomendadas;
    }

    public Integer getNaorecomendadas() {
        return naorecomendadas;
    }

    public void setNaorecomendadas(Integer reprovadas) {
        this.naorecomendadas = reprovadas;
    }

    public Integer getRenovacoes() {
        return renovacoes;
    }

    public void setRenovacoes(Integer renovacoes) {
        this.renovacoes = renovacoes;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
