package br.com.global5.manager.model.analise;

import javax.persistence.*;
import java.util.Date;

@SqlResultSetMapping(
        name = "TimeLineMapping",
        entities = @EntityResult(
                entityClass = acTimeLine.class,
                fields = {
                        @FieldResult(name = "id", column = "anacoid"),
                        @FieldResult(name = "cliente", column = "clienteoid"),
                        @FieldResult(name = "filial", column = "filialoid"),
                        @FieldResult(name = "dtCriacao", column = "anac_dt_criacao"),
                        @FieldResult(name = "status", column = "anac_status"),
                        @FieldResult(name = "tipoFicha", column = "anac_tipo_ficha"),
                        @FieldResult(name = "liberacao", column = "anac_aliboid"),
                        @FieldResult(name = "idMotorista", column = "anacm_motoid"),
                        @FieldResult(name = "cpf", column = "mot_documento1"),
                        @FieldResult(name = "nomeMotorista", column = "mot_nome"),
                        @FieldResult(name = "nomeStatus", column = "anac_status_nome"),
                        @FieldResult(name = "usuarioCriacao", column = "usu_criacao"),
                        @FieldResult(name = "usuarioTermino", column = "usu_termino"),
                        @FieldResult(name = "dtCadastro", column = "cadastro"),
                        @FieldResult(name = "dtExclusao", column = "anac_dt_exclusao")
                }
        )
)
@Entity
public class acTimeLine {

    @Id
    private Integer id;
    private Integer cliente;
    private Integer filial;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao;
    private Integer status;
    private Integer tipoFicha;
    private Integer liberacao;
    private Integer idMotorista;
    private String cpf;
    private String nomeMotorista;
    private String nomeStatus;
    private String usuarioCriacao;
    private String usuarioTermino;
    private String dtCadastro;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtExclusao;

    public acTimeLine() {}

    public acTimeLine(Integer id, Integer cliente, Integer filial, Date dtCriacao, Integer status, Integer tipoFicha,
                      Integer liberacao, Integer idMotorista, String cpf, String nomeMotorista, String nomeStatus,
                      String usuarioCriacao, String usuarioTermino, String dtCadastro, Date dtExclusao) {
        this.id = id;
        this.cliente = cliente;
        this.filial = filial;
        this.dtCriacao = dtCriacao;
        this.status = status;
        this.tipoFicha = tipoFicha;
        this.liberacao = liberacao;
        this.idMotorista = idMotorista;
        this.cpf = cpf;
        this.nomeMotorista = nomeMotorista;
        this.nomeStatus = nomeStatus;
        this.usuarioCriacao = usuarioCriacao;
        this.usuarioTermino = usuarioTermino;
        this.dtCadastro = dtCadastro;
        this.dtExclusao = dtExclusao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCliente() {
        return cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public Integer getFilial() {
        return filial;
    }

    public void setFilial(Integer filial) {
        this.filial = filial;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTipoFicha() {
        return tipoFicha;
    }

    public void setTipoFicha(Integer tipoFicha) {
        this.tipoFicha = tipoFicha;
    }

    public Integer getLiberacao() {
        return liberacao;
    }

    public void setLiberacao(Integer liberacao) {
        this.liberacao = liberacao;
    }

    public Integer getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(Integer idMotorista) {
        this.idMotorista = idMotorista;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomeMotorista() {
        return nomeMotorista;
    }

    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }

    public String getNomeStatus() {
        return nomeStatus;
    }

    public void setNomeStatus(String nomeStatus) {
        this.nomeStatus = nomeStatus;
    }

    public String getUsuarioCriacao() {
        return usuarioCriacao;
    }

    public void setUsuarioCriacao(String usuarioCriacao) {
        this.usuarioCriacao = usuarioCriacao;
    }

    public String getUsuarioTermino() {
        return usuarioTermino;
    }

    public void setUsuarioTermino(String usuarioTermino) {
        this.usuarioTermino = usuarioTermino;
    }

    public String getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(String dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }
}
