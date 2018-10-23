package br.com.global5.manager.model.analise;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zielinski on 02/07/17.
 */
@SqlResultSetMapping(
        name = "ListaFichaMapping",
        entities = @EntityResult(
                entityClass = acListaFicha.class,
                fields = {
                        @FieldResult(name = "anacoid", column = "anacoid"),
                        @FieldResult(name = "idCliente", column = "idCliente"),
                        @FieldResult(name = "idFilial", column = "idFilial"),
                        @FieldResult(name = "nomeFilial", column = "nomeFilial"),
                        @FieldResult(name = "nomeCliente", column = "nomeCliente"),
                        @FieldResult(name = "refPessoal", column = "refPessoal"),
                        @FieldResult(name = "refPessoalDsc", column = "refPessoalDsc"),
                        @FieldResult(name = "refComercial", column = "refComercial"),
                        @FieldResult(name = "refComercialDsc", column = "refComercialDsc"),
                        @FieldResult(name = "refCriminal", column = "refCriminal"),
                        @FieldResult(name = "refCriminalDsc", column = "refCriminalDsc"),
                        @FieldResult(name = "numLiberacao", column = "numLiberacao"),
                        @FieldResult(name = "dtCriacao", column = "dtCriacao"),
                        @FieldResult(name = "dtVencimento", column = "dtVencimento"),
                        @FieldResult(name = "dtExclusao", column = "dtExclusao"),
                        @FieldResult(name = "dtTermino", column = "dtTermino"),
                        @FieldResult(name = "status", column = "status"),
                        @FieldResult(name = "tipoficha", column = "tipoficha"),
                        @FieldResult(name = "StatusPendencias", column = "StatusPendencias"),
                        @FieldResult(name = "idMotorista", column = "idMotorista"),
                        @FieldResult(name = "tipoMotorista", column = "tipoMotorista"),
                        @FieldResult(name = "dtRegMotorista", column = "dtRegMotorista"),
                        @FieldResult(name = "dtRegVeiculo", column = "dtRegVeiculo"),
                        @FieldResult(name = "nomeMotorista", column = "nomeMotorista"),
                        @FieldResult(name = "statusFicha", column = "statusFicha"),
                        @FieldResult(name = "tipoAcVeiculo", column = "tipoAcVeiculo"),
                        @FieldResult(name = "placaVeiculo", column = "placaVeiculo"),
                        @FieldResult(name = "idVeiculo", column = "idVeiculo"),
                        @FieldResult(name = "tipoVeiculo", column = "tipoVeiculo"),
                        @FieldResult(name = "usuCriacao", column = "usuCriacao"),
                        @FieldResult(name = "usuAlteracao", column = "usuAlteracao"),
                        @FieldResult(name = "usuTermino", column = "usuTermino"),
                        @FieldResult(name = "dtCadastro", column = "dtCadastro"),
                        @FieldResult(name = "dtExclusaoFicha", column = "dtExclusaoFicha")
                }
        )
)
@Entity
public class acListaFicha {


    @Id private Integer anacoid;
    private Integer idCliente;
    private Integer idFilial;
    private String  nomeFilial;
    private String  nomeCliente;
    private Integer refPessoal;
    private String  refPessoalDsc;
    private Integer refComercial;
    private String  refComercialDsc;
    private Integer refCriminal;
    private String  refCriminalDsc;
    private Integer numLiberacao;
    private Date    dtCriacao;
    private Date    dtVencimento;
    private Date    dtExclusao;
    private Date    dtTermino;
    private Integer status;
    private Integer tipoficha;
    private Integer idMotorista;
    private Integer tipoMotorista;
    private Date    dtRegMotorista;
    private Date    dtRegVeiculo;
    private String  nomeMotorista;
    private String  statusFicha;
    private Integer tipoAcVeiculo;
    private String  placaVeiculo;
    private String  idVeiculo;
    private String  tipoVeiculo;
    private String  usuCriacao;
    private String  usuAlteracao;
    private String  usuTermino;
    private String  dtCadastro;
    private String  dtExclusaoFicha;

    public acListaFicha() {}

    public Integer getAnacoid() {
        return anacoid;
    }

    public void setAnacoid(Integer anacoid) {
        this.anacoid = anacoid;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
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

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Integer getRefPessoal() {
        return refPessoal;
    }

    public void setRefPessoal(Integer refPessoal) {
        this.refPessoal = refPessoal;
    }

    public String getRefPessoalDsc() {
        return refPessoalDsc;
    }

    public void setRefPessoalDsc(String refPessoalDsc) {
        this.refPessoalDsc = refPessoalDsc;
    }

    public Integer getRefComercial() {
        return refComercial;
    }

    public void setRefComercial(Integer refComercial) {
        this.refComercial = refComercial;
    }

    public String getRefComercialDsc() {
        return refComercialDsc;
    }

    public void setRefComercialDsc(String refComercialDsc) {
        this.refComercialDsc = refComercialDsc;
    }

    public Integer getRefCriminal() {
        return refCriminal;
    }

    public void setRefCriminal(Integer refCriminal) {
        this.refCriminal = refCriminal;
    }

    public String getRefCriminalDsc() {
        return refCriminalDsc;
    }

    public void setRefCriminalDsc(String refCriminalDsc) {
        this.refCriminalDsc = refCriminalDsc;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public Date getDtTermino() {
        return dtTermino;
    }

    public void setDtTermino(Date dtTermino) {
        this.dtTermino = dtTermino;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTipoficha() {
        return tipoficha;
    }

    public void setTipoficha(Integer tipoficha) {
        this.tipoficha = tipoficha;
    }

    public Integer getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(Integer idMotorista) {
        this.idMotorista = idMotorista;
    }

    public Date getDtRegMotorista() {
        return dtRegMotorista;
    }

    public void setDtRegMotorista(Date dtRegMotorista) {
        this.dtRegMotorista = dtRegMotorista;
    }

    public Date getDtRegVeiculo() {
        return dtRegVeiculo;
    }

    public void setDtRegVeiculo(Date dtRegVeiculo) {
        this.dtRegVeiculo = dtRegVeiculo;
    }

    public String getNomeMotorista() {
        return nomeMotorista;
    }

    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }

    public String getStatusFicha() {
        return statusFicha;
    }

    public void setStatusFicha(String statusFicha) {
        this.statusFicha = statusFicha;
    }

    public Integer getTipoAcVeiculo() {
        return tipoAcVeiculo;
    }

    public void setTipoAcVeiculo(Integer tipoAcVeiculo) {
        this.tipoAcVeiculo = tipoAcVeiculo;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public String getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(String idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public String getUsuCriacao() {
        return usuCriacao;
    }

    public void setUsuCriacao(String usuCriacao) {
        this.usuCriacao = usuCriacao;
    }

    public String getUsuAlteracao() {
        return usuAlteracao;
    }

    public void setUsuAlteracao(String usuAlteracao) {
        this.usuAlteracao = usuAlteracao;
    }

    public String getUsuTermino() {
        return usuTermino;
    }

    public void setUsuTermino(String usuTermino) {
        this.usuTermino = usuTermino;
    }

    public String getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(String dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public String getDtExclusaoFicha() {
        return dtExclusaoFicha;
    }

    public void setDtExclusaoFicha(String dtExclusaoFicha) {
        this.dtExclusaoFicha = dtExclusaoFicha;
    }

    public Integer getNumLiberacao() {
        return numLiberacao;
    }

    public void setNumLiberacao(Integer numLiberacao) {
        this.numLiberacao = numLiberacao;
    }

    public Integer getTipoMotorista() {
        return tipoMotorista;
    }

    public void setTipoMotorista(Integer tipoMotorista) {
        this.tipoMotorista = tipoMotorista;
    }
}
