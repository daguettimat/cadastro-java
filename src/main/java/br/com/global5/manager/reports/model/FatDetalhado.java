package br.com.global5.manager.reports.model;

import javax.persistence.*;
import java.util.Date;

@SqlResultSetMapping(
        name = "FatDetalhadoMapping",
        entities = @EntityResult(
                entityClass = FatDetalhado.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "idTransportadora", column = "traoid"),
                        @FieldResult(name = "nomeTransportadora", column = "tranome"),
                        @FieldResult(name = "idFilial", column = "utraoid"),
                        @FieldResult(name = "nomeFilial", column = "utranome"),
                        @FieldResult(name = "idFicha", column = "ficoid"),
                        @FieldResult(name = "idVinculo", column = "anacm_vinculo"),
                        @FieldResult(name = "nomeVinculo", column = "tiponome"),
                        @FieldResult(name = "nomeMotorista", column = "motnome"),
                        @FieldResult(name = "cavalo", column = "cavalo"),
                        @FieldResult(name = "reboque_1", column = "reboque_1"),
                        @FieldResult(name = "reboque_2", column = "reboque_2"),
                        @FieldResult(name = "centroCusto", column = "ficcentro_custo"),
                        @FieldResult(name = "valorFicha", column = "valorfic"),
                        @FieldResult(name = "criacao", column = "criacao"),
                        @FieldResult(name = "termino", column = "termino"),
                        @FieldResult(name = "validade", column = "validade"),
                        @FieldResult(name = "usuario", column = "usunome"),
                        @FieldResult(name = "tipoFicha", column = "dscTipoFicha"),
                        @FieldResult(name = "statusFicha", column = "dscFichaStatus")
                }
        )
)
@Entity
public class FatDetalhado {

    @Id
    private Integer id;
    private Integer idTransportadora;
    private String  nomeTransportadora;
    private Integer idFilial;
    private String  nomeFilial;
    private Integer idFicha;
    private Integer idVinculo;
    private String  nomeVinculo;
    private String  nomeMotorista;
    private String  cavalo;
    private String  reboque_1;
    private String  reboque_2;
    private String  centroCusto;
    private Double  valorFicha;
    private String  tipoFicha;
    private String  criacao;
    private String  termino;
    private String  validade;
    private String  statusFicha;
    private String  usuario;

    public FatDetalhado() {}

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

    public Integer getIdFicha() {
        return idFicha;
    }

    public void setIdFicha(Integer idFicha) {
        this.idFicha = idFicha;
    }

    public Integer getIdVinculo() {
        return idVinculo;
    }

    public void setIdVinculo(Integer idVinculo) {
        this.idVinculo = idVinculo;
    }

    public String getNomeVinculo() {
        return nomeVinculo;
    }

    public void setNomeVinculo(String nomeVinculo) {
        this.nomeVinculo = nomeVinculo;
    }

    public String getNomeMotorista() {
        return nomeMotorista;
    }

    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }

    public String getCavalo() {
        return cavalo;
    }

    public void setCavalo(String cavalo) {
        this.cavalo = cavalo;
    }

    public String getReboque_1() {
        return reboque_1;
    }

    public void setReboque_1(String reboque_1) {
        this.reboque_1 = reboque_1;
    }

    public String getReboque_2() {
        return reboque_2;
    }

    public void setReboque_2(String reboque_2) {
        this.reboque_2 = reboque_2;
    }

    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    public Double getValorFicha() {
        return valorFicha;
    }

    public void setValorFicha(Double valorFicha) {
        this.valorFicha = valorFicha;
    }

    public String getTipoFicha() {
        return tipoFicha;
    }

    public void setTipoFicha(String tipoFicha) {
        this.tipoFicha = tipoFicha;
    }

    public String getStatusFicha() {
        return statusFicha;
    }

    public void setStatusFicha(String statusFicha) {
        this.statusFicha = statusFicha;
    }

    public String getCriacao() {
        return criacao;
    }

    public void setCriacao(String criacao) {
        this.criacao = criacao;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
