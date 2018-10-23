package br.com.global5.manager.reports.model;

import javax.persistence.*;
import java.util.Date;

@SqlResultSetMapping(
        name = "HistoricoFichaMapping",
        entities = @EntityResult(
                entityClass = HistoricoFichas.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "ficha", column = "ficoid"),
                        @FieldResult(name = "idAnalise", column = "anacoid"),
                        @FieldResult(name = "dtUltCadastro", column = "data_ultimo_cadastro"),
                        @FieldResult(name = "dtTermino", column = "termino"),
                        @FieldResult(name = "tipoFicha", column = "tipo_ficha"),
                        @FieldResult(name = "status", column = "status"),
                        @FieldResult(name = "valor", column = "anac_valor"),
                        @FieldResult(name = "dtUltimoCadastro", column = "dias_ultimo_cadastro"),
                        @FieldResult(name = "nomeMotorista", column = "motorista"),
                        @FieldResult(name = "vinculo", column = "vinculo"),
                        @FieldResult(name = "cavalo", column = "cavalo"),
                        @FieldResult(name = "proprietario_cavalo", column = "proprietario_cavalo"),
                        @FieldResult(name = "reboque_1", column = "reboque1"),
                        @FieldResult(name = "proprietario_reboque_1", column = "proprietario_reboque1"),
                        @FieldResult(name = "reboque_2", column = "reboque2"),
                        @FieldResult(name = "proprietario_reboque_2", column = "proprietario_reboque2"),
                        @FieldResult(name = "reboque_3", column = "reboque3"),
                        @FieldResult(name = "proprietario_reboque_3", column = "proprietario_reboque3")
                }
        )
)
@Entity
public class HistoricoFichas {

    @Id
    private Integer id;
    private Integer ficha;
    private Integer idAnalise;
    private Integer dtUltCadastro;
    private Date dtTermino;
    private String tipoFicha;
    private String status;
    private double valor;
    private Date dtUltimoCadastro;
    private String nomeMotorista;
    private String vinculo;
    private String cavalo;
    private String proprietario_cavalo;
    private String reboque_1;
    private String proprietario_reboque_1;
    private String reboque_2;
    private String proprietario_reboque_2;
    private String reboque_3;
    private String proprietario_reboque_3;

    public HistoricoFichas() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFicha() {
        return ficha;
    }

    public void setFicha(Integer ficha) {
        this.ficha = ficha;
    }

    public Integer getIdAnalise() {
        return idAnalise;
    }

    public void setIdAnalise(Integer idAnalise) {
        this.idAnalise = idAnalise;
    }

    public Integer getDtUltCadastro() {
        return dtUltCadastro;
    }

    public void setDtUltCadastro(Integer dtUltCadastro) {
        this.dtUltCadastro = dtUltCadastro;
    }

    public Date getDtTermino() {
        return dtTermino;
    }

    public void setDtTermino(Date dtTermino) {
        this.dtTermino = dtTermino;
    }

    public String getTipoFicha() {
        return tipoFicha;
    }

    public void setTipoFicha(String tipoFicha) {
        this.tipoFicha = tipoFicha;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getDtUltimoCadastro() {
        return dtUltimoCadastro;
    }

    public void setDtUltimoCadastro(Date dtUltimoCadastro) {
        this.dtUltimoCadastro = dtUltimoCadastro;
    }

    public String getNomeMotorista() {
        return nomeMotorista;
    }

    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }

    public String getVinculo() {
        return vinculo;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }

    public String getCavalo() {
        return cavalo;
    }

    public void setCavalo(String cavalo) {
        this.cavalo = cavalo;
    }

    public String getProprietario_cavalo() {
        return proprietario_cavalo;
    }

    public void setProprietario_cavalo(String proprietario_cavalo) {
        this.proprietario_cavalo = proprietario_cavalo;
    }

    public String getReboque_1() {
        return reboque_1;
    }

    public void setReboque_1(String reboque_1) {
        this.reboque_1 = reboque_1;
    }

    public String getProprietario_reboque_1() {
        return proprietario_reboque_1;
    }

    public void setProprietario_reboque_1(String proprietario_reboque_1) {
        this.proprietario_reboque_1 = proprietario_reboque_1;
    }

    public String getReboque_2() {
        return reboque_2;
    }

    public void setReboque_2(String reboque_2) {
        this.reboque_2 = reboque_2;
    }

    public String getProprietario_reboque_2() {
        return proprietario_reboque_2;
    }

    public void setProprietario_reboque_2(String proprietario_reboque_2) {
        this.proprietario_reboque_2 = proprietario_reboque_2;
    }

    public String getReboque_3() {
        return reboque_3;
    }

    public void setReboque_3(String reboque_3) {
        this.reboque_3 = reboque_3;
    }

    public String getProprietario_reboque_3() {
        return proprietario_reboque_3;
    }

    public void setProprietario_reboque_3(String proprietario_reboque_3) {
        this.proprietario_reboque_3 = proprietario_reboque_3;
    }
}


