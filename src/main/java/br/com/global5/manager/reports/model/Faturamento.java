package br.com.global5.manager.reports.model;

import javax.persistence.*;
import java.util.Date;

@SqlResultSetMapping(
        name = "FaturamentoMapping",
        entities = @EntityResult(
                entityClass = Faturamento.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "idTransportadora", column = "traoid"),
                        @FieldResult(name = "nomeTransportadora", column = "tranome"),
                        @FieldResult(name = "idFilial", column = "utraoid"),
                        @FieldResult(name = "nomeFilial", column = "utranome"),
                        @FieldResult(name = "cnpjFilial", column = "utracnpj"),
                        @FieldResult(name = "contratoRecebimento", column = "tracontr_recbto"),
                        @FieldResult(name = "valor", column = "valor"),
                        @FieldResult(name = "valorTotal", column = "valorTotal"),
                        @FieldResult(name = "faturaMinima", column = "trfminima")
                }
        )
)
@Entity
public class Faturamento {

    @Id
    private Integer id;
    private Integer idTransportadora;
    private String  nomeTransportadora;
    private Integer idFilial;
    private String  nomeFilial;
    private String  cnpjFilial;
    private Date    contratoRecebimento;
    private Double  valor;
    private Double  valorTotal;
    private Double  faturaMinima;

    public Faturamento() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTransportadora() {
        return idTransportadora;
    }

    public void setIdTransportadora(Integer idTransportadora) {
        this.idTransportadora = idTransportadora;
    }

    public String getNomeTransportadora() {
        return nomeTransportadora;
    }

    public String getCnpjFilial() {
        return cnpjFilial;
    }

    public void setCnpjFilial(String cnpjFilial) {
        this.cnpjFilial = cnpjFilial;
    }

    public void setNomeTransportadora(String nomeTransportadora) {
        this.nomeTransportadora = nomeTransportadora;
    }

    public Integer getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Integer idFilial) {
        this.idFilial = idFilial;
    }

    public String getNomeFilial() {
        return nomeFilial;
    }

    public void setNomeFilial(String nomeFilial) {
        this.nomeFilial = nomeFilial;
    }

    public Date getContratoRecebimento() {
        return contratoRecebimento;
    }

    public void setContratoRecebimento(Date contratoRecebimento) {
        this.contratoRecebimento = contratoRecebimento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getFaturaMinima() {
        return faturaMinima;
    }

    public void setFaturaMinima(Double faturaMinima) {
        this.faturaMinima = faturaMinima;
    }
}
