package br.com.global5.manager.model.excluir;

import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.cadastro.Transportadora;
import br.com.global5.manager.model.geral.Colaborador;
import br.com.global5.manager.model.geral.Usuario;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "ficha")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)

public class Ficha implements BaseEntity {


    @Id
    @SequenceGenerator(name = "ficha_ficoid_seq", sequenceName = "ficha_ficoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ficha_ficoid_seq")
    @Column(name = "ficoid")
    private Integer id;

    @Column(name = "ficcadastro")
    private java.sql.Timestamp cadastro;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficoperador_cadastro")
    private Usuario idOperadorCadastro;


    @Column(name = "ficanalise")
    private java.sql.Timestamp dtAnalise;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficoperador_analise")
    private Usuario idOperadorAnalise;

    @Column(name = "ficcriminal")
    private java.sql.Timestamp dtCriminal;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficoperador_criminal")
    private Usuario idOperadorCriminal;

    @Column(name = "fictermino")
    private java.sql.Timestamp fictermino;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficoperador_termino")
    private Usuario idOperadorTermino;

    @Column(name = "fictipoconsulta")
    private String tipoConsulta;

    @Column(name = "ficresultado")
    private String resultado;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Transportadora.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="fictraoid")    
    private Transportadora transportadora;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficutraoid")
    private Usuario usuarioTransportadora;

    @Column(name = "ficexclusao")
    private java.sql.Timestamp dtExclusao;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficoperador_exclusao")    
    private Usuario usuarioExclusao;

    @Column(name = "ficvalor")
    private Double valor;

    @Column(name = "ficvalorcolaborador")
    private Double valorColaborador;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Colaborador.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficcoloid")
    private Colaborador colaborador;

    @Column(name = "ficliberacao")
    private Integer liberacao;

    @Column(name = "ficobs")
    private String observacaoLiberacao;

    @Column(name = "ficobsinterna")
    private String observaoInterna;

    @Column(name = "ficratoid")
    private Integer ficratoid; // Perguntar Mateus

    @Column(name = "ficrenovacao")
    private String renovacao;

    @Column(name = "ficobservacao")
    private String observacao;

    @Column(name = "ficcobrar")
    private String cobrar;

    @Column(name = "ficobsexclusao")
    private String obsExclusao;

    @Column(name = "ficaux_tipo")
    private String auxTipo;

    @Column(name = "ficaux_valor")
    private Double auxValor;

    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Usuario.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficusuliberacao_ref")
    private Usuario usuarioLiberacaoReferencia;

    @Column(name = "ficdtliberacao_ref")
    private java.sql.Timestamp dtLiberacaoReferencia;

    @Column(name = "ficreferencia_termino")
    private String referenciaTermino;

    @Column(name = "ficconsulta_termino")
    private String consultaTermino;

    @Column(name = "ficalteracao_renov")
    private java.sql.Date dtAlteracaoRenovacao;

    @Column(name = "ficanterior")
    private Integer anterior;


    @ManyToOne(
            fetch=FetchType.LAZY,
            targetEntity=Ficha.class,
            cascade={CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name="ficficha_antiga")
    private Ficha fichaAntiga;

    @Column(name = "ficalterado_usuliberacao")
    private String alteradoUsuarioLiberacao;

    @Column(name = "ficvlor_inclusao")
    private Double valorInclusao;

    @Column(name = "ficvalor_serasa")
    private Double valorSerasa;

    @Column(name = "ficqtde_serasa")
    private Integer qtdeSerasa;

    @Column(name = "ficoiddev")
    private Integer idDev;

    @Column(name = "migracao")
    private Integer migracao;

    @Column(name = "ficliberacaodev")
    private Integer liberacaoDev;

    @Column(name = "ficcentro_custo")
    private String centroCusto;


    public Ficha() {}

    public Ficha(Timestamp cadastro, Usuario idOperadorCadastro, Timestamp dtAnalise,
                 Usuario idOperadorAnalise, Timestamp dtCriminal, Usuario idOperadorCriminal,
                 Timestamp fictermino, Usuario idOperadorTermino, String tipoConsulta,
                 String resultado, Transportadora transportadora, Usuario usuarioTransportadora,
                 Timestamp dtExclusao, Usuario usuarioExclusao, Double valor, Double valorColaborador,
                 Colaborador colaborador, Integer liberacao, String observacaoLiberacao, String observaoInterna,
                 Integer ficratoid, String renovacao, String observacao, String cobrar, String obsExclusao,
                 String auxTipo, Double auxValor, Usuario usuarioLiberacaoReferencia, Timestamp dtLiberacaoReferencia,
                 String referenciaTermino, String consultaTermino, Date dtAlteracaoRenovacao, Integer anterior,
                 Ficha fichaAntiga, String alteradoUsuarioLiberacao, Double valorInclusao, Double valorSerasa,
                 Integer qtdeSerasa, Integer idDev, Integer migracao, Integer liberacaoDev, String centroCusto) {
        this.cadastro = cadastro;
        this.idOperadorCadastro = idOperadorCadastro;
        this.dtAnalise = dtAnalise;
        this.idOperadorAnalise = idOperadorAnalise;
        this.dtCriminal = dtCriminal;
        this.idOperadorCriminal = idOperadorCriminal;
        this.fictermino = fictermino;
        this.idOperadorTermino = idOperadorTermino;
        this.tipoConsulta = tipoConsulta;
        this.resultado = resultado;
        this.transportadora = transportadora;
        this.usuarioTransportadora = usuarioTransportadora;
        this.dtExclusao = dtExclusao;
        this.usuarioExclusao = usuarioExclusao;
        this.valor = valor;
        this.valorColaborador = valorColaborador;
        this.colaborador = colaborador;
        this.liberacao = liberacao;
        this.observacaoLiberacao = observacaoLiberacao;
        this.observaoInterna = observaoInterna;
        this.ficratoid = ficratoid;
        this.renovacao = renovacao;
        this.observacao = observacao;
        this.cobrar = cobrar;
        this.obsExclusao = obsExclusao;
        this.auxTipo = auxTipo;
        this.auxValor = auxValor;
        this.usuarioLiberacaoReferencia = usuarioLiberacaoReferencia;
        this.dtLiberacaoReferencia = dtLiberacaoReferencia;
        this.referenciaTermino = referenciaTermino;
        this.consultaTermino = consultaTermino;
        this.dtAlteracaoRenovacao = dtAlteracaoRenovacao;
        this.anterior = anterior;
        this.fichaAntiga = fichaAntiga;
        this.alteradoUsuarioLiberacao = alteradoUsuarioLiberacao;
        this.valorInclusao = valorInclusao;
        this.valorSerasa = valorSerasa;
        this.qtdeSerasa = qtdeSerasa;
        this.idDev = idDev;
        this.migracao = migracao;
        this.liberacaoDev = liberacaoDev;
        this.centroCusto = centroCusto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCadastro() {
        return cadastro;
    }

    public void setCadastro(Timestamp cadastro) {
        this.cadastro = cadastro;
    }

    public Usuario getIdOperadorCadastro() {
        return idOperadorCadastro;
    }

    public void setIdOperadorCadastro(Usuario idOperadorCadastro) {
        this.idOperadorCadastro = idOperadorCadastro;
    }

    public Timestamp getDtAnalise() {
        return dtAnalise;
    }

    public void setDtAnalise(Timestamp dtAnalise) {
        this.dtAnalise = dtAnalise;
    }

    public Usuario getIdOperadorAnalise() {
        return idOperadorAnalise;
    }

    public void setIdOperadorAnalise(Usuario idOperadorAnalise) {
        this.idOperadorAnalise = idOperadorAnalise;
    }

    public Timestamp getDtCriminal() {
        return dtCriminal;
    }

    public void setDtCriminal(Timestamp dtCriminal) {
        this.dtCriminal = dtCriminal;
    }

    public Usuario getIdOperadorCriminal() {
        return idOperadorCriminal;
    }

    public void setIdOperadorCriminal(Usuario idOperadorCriminal) {
        this.idOperadorCriminal = idOperadorCriminal;
    }

    public Timestamp getFictermino() {
        return fictermino;
    }

    public void setFictermino(Timestamp fictermino) {
        this.fictermino = fictermino;
    }

    public Usuario getIdOperadorTermino() {
        return idOperadorTermino;
    }

    public void setIdOperadorTermino(Usuario idOperadorTermino) {
        this.idOperadorTermino = idOperadorTermino;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public Usuario getUsuarioTransportadora() {
        return usuarioTransportadora;
    }

    public void setUsuarioTransportadora(Usuario usuarioTransportadora) {
        this.usuarioTransportadora = usuarioTransportadora;
    }

    public Timestamp getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Timestamp dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public Usuario getUsuarioExclusao() {
        return usuarioExclusao;
    }

    public void setUsuarioExclusao(Usuario usuarioExclusao) {
        this.usuarioExclusao = usuarioExclusao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValorColaborador() {
        return valorColaborador;
    }

    public void setValorColaborador(Double valorColaborador) {
        this.valorColaborador = valorColaborador;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public Integer getLiberacao() {
        return liberacao;
    }

    public void setLiberacao(Integer liberacao) {
        this.liberacao = liberacao;
    }

    public String getObservacaoLiberacao() {
        return observacaoLiberacao;
    }

    public void setObservacaoLiberacao(String observacaoLiberacao) {
        this.observacaoLiberacao = observacaoLiberacao;
    }

    public String getObservaoInterna() {
        return observaoInterna;
    }

    public void setObservaoInterna(String observaoInterna) {
        this.observaoInterna = observaoInterna;
    }

    public Integer getFicratoid() {
        return ficratoid;
    }

    public void setFicratoid(Integer ficratoid) {
        this.ficratoid = ficratoid;
    }

    public String getRenovacao() {
        return renovacao;
    }

    public void setRenovacao(String renovacao) {
        this.renovacao = renovacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getCobrar() {
        return cobrar;
    }

    public void setCobrar(String cobrar) {
        this.cobrar = cobrar;
    }

    public String getObsExclusao() {
        return obsExclusao;
    }

    public void setObsExclusao(String obsExclusao) {
        this.obsExclusao = obsExclusao;
    }

    public String getAuxTipo() {
        return auxTipo;
    }

    public void setAuxTipo(String auxTipo) {
        this.auxTipo = auxTipo;
    }

    public Double getAuxValor() {
        return auxValor;
    }

    public void setAuxValor(Double auxValor) {
        this.auxValor = auxValor;
    }

    public Usuario getUsuarioLiberacaoReferencia() {
        return usuarioLiberacaoReferencia;
    }

    public void setUsuarioLiberacaoReferencia(Usuario usuarioLiberacaoReferencia) {
        this.usuarioLiberacaoReferencia = usuarioLiberacaoReferencia;
    }

    public Timestamp getDtLiberacaoReferencia() {
        return dtLiberacaoReferencia;
    }

    public void setDtLiberacaoReferencia(Timestamp dtLiberacaoReferencia) {
        this.dtLiberacaoReferencia = dtLiberacaoReferencia;
    }

    public String getReferenciaTermino() {
        return referenciaTermino;
    }

    public void setReferenciaTermino(String referenciaTermino) {
        this.referenciaTermino = referenciaTermino;
    }

    public String getConsultaTermino() {
        return consultaTermino;
    }

    public void setConsultaTermino(String consultaTermino) {
        this.consultaTermino = consultaTermino;
    }

    public Date getDtAlteracaoRenovacao() {
        return dtAlteracaoRenovacao;
    }

    public void setDtAlteracaoRenovacao(Date dtAlteracaoRenovacao) {
        this.dtAlteracaoRenovacao = dtAlteracaoRenovacao;
    }

    public Integer getAnterior() {
        return anterior;
    }

    public void setAnterior(Integer anterior) {
        this.anterior = anterior;
    }

    public Ficha getFichaAntiga() {
        return fichaAntiga;
    }

    public void setFichaAntiga(Ficha fichaAntiga) {
        this.fichaAntiga = fichaAntiga;
    }

    public String getAlteradoUsuarioLiberacao() {
        return alteradoUsuarioLiberacao;
    }

    public void setAlteradoUsuarioLiberacao(String alteradoUsuarioLiberacao) {
        this.alteradoUsuarioLiberacao = alteradoUsuarioLiberacao;
    }

    public Double getValorInclusao() {
        return valorInclusao;
    }

    public void setValorInclusao(Double valorInclusao) {
        this.valorInclusao = valorInclusao;
    }

    public Double getValorSerasa() {
        return valorSerasa;
    }

    public void setValorSerasa(Double valorSerasa) {
        this.valorSerasa = valorSerasa;
    }

    public Integer getQtdeSerasa() {
        return qtdeSerasa;
    }

    public void setQtdeSerasa(Integer qtdeSerasa) {
        this.qtdeSerasa = qtdeSerasa;
    }

    public Integer getIdDev() {
        return idDev;
    }

    public void setIdDev(Integer idDev) {
        this.idDev = idDev;
    }

    public Integer getMigracao() {
        return migracao;
    }

    public void setMigracao(Integer migracao) {
        this.migracao = migracao;
    }

    public Integer getLiberacaoDev() {
        return liberacaoDev;
    }

    public void setLiberacaoDev(Integer liberacaoDev) {
        this.liberacaoDev = liberacaoDev;
    }

    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }
}

