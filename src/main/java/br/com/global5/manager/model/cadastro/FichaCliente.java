package br.com.global5.manager.model.cadastro;

import br.com.global5.infra.json.JsonBinaryType;
import br.com.global5.infra.json.JsonStringType;
import br.com.global5.infra.model.BaseEntity;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.ect.Pais;
import br.com.global5.manager.model.enums.MotoristaVinculo;
import br.com.global5.manager.model.enums.TelefoneTipo;
import br.com.global5.manager.model.geral.Mercadoria;
import br.com.global5.manager.model.geral.Motorista;
import br.com.global5.manager.model.geral.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ficha_cliente")

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)

@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@NamedStoredProcedureQuery(
        name = "ficha_cliente_prepare",
        procedureName = "ficha_cliente_prepare",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "ficoid")
        }
)
@SqlResultSetMapping(name="updateResult", columns = { @ColumnResult(name = "count")})


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class FichaCliente implements BaseEntity {

    @Id
    @SequenceGenerator(name = "ficha_cliente_ficoid_seq", sequenceName = "ficha_cliente_ficoid_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ficha_cliente_ficoid_seq")
    @Column(name = "ficoid")
    private Integer id;

    @Column(name = "fic_cpf")
    private String cpf;

    @Temporal(TemporalType.DATE)
    @Column(name = "fic_doc1_emissao")
    private Date dtEmissaoCPF;

    @Column(name = "fic_doc1_emissor")
    private String cpfEmissor;

    @Column(name = "fic_nome")
    private String nome;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = MotoristaVinculo.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "fic_tipmoid")
    private MotoristaVinculo tipoMotorista;

    @Column(name = "fic_tempo")
    private Integer tempo;

    @Column(name = "fic_tempotipo")
    private Integer tempoTipo;

    @Column(name = "fic_nacional")
    private boolean nacional;

    @Column(name = "fic_rg")
    private String fic_rg;

    @Column(name = "fic_ufrg")
    private String ufRg;

    @Temporal(TemporalType.DATE)
    @Column(name = "fic_doc2_emissao")
    private Date dtEmissaoRG;

    @Column(name = "fic_doc2_emissor")
    private String rgEmissor;

    @Temporal(TemporalType.DATE)
    @Column(name = "fic_nascimento")
    private Date dtNascimento;

    @Column(name = "fic_cidadenasc")
    private String cidadeNascimento;

    @Column(name = "fic_cnh")
    private String cnh;

    @Column(name = "fic_categoria")
    private int categoria;

    @Column(name = "fic_numsegcnh")
    private String numSegCnh;

    @Column(name = "fic_ufcnh")
    private String ufCNH;

    @Column(name = "fic_registrocnh")
    private String registroCNH;

    @Temporal(TemporalType.DATE)
    @Column(name = "fic_dt_priemissaocnh")
    private Date dtPrimeiraEmissaoCNH;

    @Temporal(TemporalType.DATE)
    @Column(name = "fic_dt_emissao_cnh")
    private Date dtEmissaoCNH;

    @Temporal(TemporalType.DATE)
    @Column(name = "fic_dt_validade_cnh")
    private Date dtValidadeCNH;

    @Column(name = "fic_pai")
    private String pai;

    @Column(name = "fic_mae")
    private String mae;

    @Column(name = "fic_cep")
    private String cep;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Pais.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "fic_paisoid")
    private Pais pais;

    @Column(name = "fic_uf")
    private String uf;

    @Column(name = "fic_cidade")
    private String cidade;

    @Column(name = "fic_bairro")
    private String bairro;

    @Column(name = "fic_endereco")
    private String endereco;

    @Column(name = "fic_numero")
    private Integer numero;

    @Column(name = "fic_complemento")
    private String complemento;

    @Column(name = "fic_ddd")
    private String ddd;

    @Column(name = "fic_telefone")
    private String telefone;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Usuario.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "fic_usuoid_inclusao")
    private Usuario usuarioInclusao;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Area.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "fic_traoid")
    private Area transportadora;

    @Type(type = "jsonb")
    @Column(name = "fic_referencia_pessoal", columnDefinition = "jsonb")
    private List<ReferenciasPessoais> referenciaPessoal;

    @Type(type = "jsonb")
    @Column(name = "fic_referencia_comercial", columnDefinition = "jsonb")
    private List<ReferenciasComerciais> referenciaComercial;

    @Type(type = "jsonb")
    @Column(name = "fic_veiculos", columnDefinition = "jsonb")
    private List<ReferenciaVeiculos> veiculos;

    @Column(name = "fic_referencia_pessoal_csv")
    private String refPessoalCSV;

    @Column(name = "fic_referencia_comercial_csv")
    private String refComercialCSV;

    @Column(name = "fic_veiculos_csv")
    private String VeiculosCSV;

    @Column(name = "fic_observacao_cliente")
    private String ObservacaoCliente;

    @Column(name = "fic_centro_custo")
    private String centroCusto;

    @Column(name = "fic_status")
    private Integer status;

    @Temporal(TemporalType.DATE)
    @Column(name = "fic_prepare")
    private Date executado;


    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Motorista.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "fic_motoid")
    private Motorista motorista;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = TelefoneTipo.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "fic_teltipo")
    private TelefoneTipo tipoTelefone;

    @Column(name = "fic_urlcnhfrente")
    private String urlCnhFrente;

    @Column(name = "fic_urlcnhverso")
    private String urlCnhVerso;

    @ManyToOne(
            fetch = FetchType.LAZY,
            targetEntity = Mercadoria.class,
            cascade = {CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinColumn(name = "fic_mer_meroid")
    private Mercadoria mercadoria;

    @Column(name = "fic_mer_valor")
    private double mercValor;

    @Column(name = "fic_mer_origem_paisoid")
    private Integer mercPaisOrigem;

    @Column(name = "fic_mer_origem_ufoid")
    private Integer mercUFOrigem;

    @Column(name = "fic_mer_origem_cidoid")
    private Integer mercCidadeOrigem;

    @Column(name = "fic_mer_destino_paisoid")
    private Integer mercPaisDestino;

    @Column(name = "fic_mer_destino_ufoid")
    private Integer mercUFDestino;

    @Column(name = "fic_mer_destino_cidoid")
    private Integer mercCidadeDestino;

    @Column(name = "fic_mcnhoid")
    private Integer idMotoristaCnh;
    

    public FichaCliente() {
    }

    public FichaCliente nome(String nome) {
        this.nome = nome;
        return this;
    }

    public boolean hasNome() {
        return nome != null && !"".equals(nome.trim());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDtEmissaoCPF() {
        return dtEmissaoCPF;
    }

    public void setDtEmissaoCPF(Date dtEmissaoCPF) {
        this.dtEmissaoCPF = dtEmissaoCPF;
    }

    public String getCpfEmissor() {
        return cpfEmissor;
    }

    public void setCpfEmissor(String cpfEmissor) {
        this.cpfEmissor = cpfEmissor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public MotoristaVinculo getTipoMotorista() {
        return tipoMotorista;
    }

    public void setTipoMotorista(MotoristaVinculo tipoMotorista) {
        this.tipoMotorista = tipoMotorista;
    }

    public Integer getTempo() {
        return tempo;
    }

    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }

    public Integer getTempoTipo() {
        return tempoTipo;
    }

    public void setTempoTipo(Integer tempoTipo) {
        this.tempoTipo = tempoTipo;
    }

    public boolean isNacional() {
        return nacional;
    }

    public void setNacional(boolean nacional) {
        this.nacional = nacional;
    }

    public String getFic_rg() {
        return fic_rg;
    }

    public void setFic_rg(String fic_rg) {
        this.fic_rg = fic_rg;
    }

    public String getUfRg() {
        return ufRg;
    }

    public void setUfRg(String ufRg) {
        this.ufRg = ufRg;
    }

    public Date getDtEmissaoRG() {
        return dtEmissaoRG;
    }

    public void setDtEmissaoRG(Date dtEmissaoRG) {
        this.dtEmissaoRG = dtEmissaoRG;
    }

    public String getRgEmissor() {
        return rgEmissor;
    }

    public void setRgEmissor(String rgEmissor) {
        this.rgEmissor = rgEmissor;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getCidadeNascimento() {
        return cidadeNascimento;
    }

    public void setCidadeNascimento(String cidadeNascimento) {
        this.cidadeNascimento = cidadeNascimento;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getNumSegCnh() {
        return numSegCnh;
    }

    public void setNumSegCnh(String numSegCnh) {
        this.numSegCnh = numSegCnh;
    }

    public Date getDtEmissaoCNH() {
        return dtEmissaoCNH;
    }

    public void setDtEmissaoCNH(Date dtEmissaoCNH) {
        this.dtEmissaoCNH = dtEmissaoCNH;
    }

    public Date getDtValidadeCNH() {
        return dtValidadeCNH;
    }

    public void setDtValidadeCNH(Date dtValidadeCNH) {
        this.dtValidadeCNH = dtValidadeCNH;
    }

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }

    public String getMae() {
        return mae;
    }

    public void setMae(String mae) {
        this.mae = mae;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public Area getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Area transportadora) {
        this.transportadora = transportadora;
    }

    public List<ReferenciasPessoais> getReferenciaPessoal() {
        return referenciaPessoal;
    }

    public void setReferenciaPessoal(List<ReferenciasPessoais> referenciaPessoal) {
        this.referenciaPessoal = referenciaPessoal;
    }

    public List<ReferenciasComerciais> getReferenciaComercial() {
        return referenciaComercial;
    }

    public void setReferenciaComercial(List<ReferenciasComerciais> referenciaComercial) {
        this.referenciaComercial = referenciaComercial;
    }

    public List<ReferenciaVeiculos> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<ReferenciaVeiculos> veiculos) {
        this.veiculos = veiculos;
    }

    public String getRefPessoalCSV() {
        return refPessoalCSV;
    }

    public void setRefPessoalCSV(String refPessoalCSV) {
        this.refPessoalCSV = refPessoalCSV;
    }

    public String getRefComercialCSV() {
        return refComercialCSV;
    }

    public void setRefComercialCSV(String refComercialCSV) {
        this.refComercialCSV = refComercialCSV;
    }

    public String getVeiculosCSV() {
        return VeiculosCSV;
    }

    public void setVeiculosCSV(String veiculosCSV) {
        VeiculosCSV = veiculosCSV;
    }

    public String getObservacaoCliente() {
        return ObservacaoCliente;
    }

    public void setObservacaoCliente(String observacaoCliente) {
        ObservacaoCliente = observacaoCliente;
    }

    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public TelefoneTipo getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(TelefoneTipo tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public Date getDtPrimeiraEmissaoCNH() {
        return dtPrimeiraEmissaoCNH;
    }

    public void setDtPrimeiraEmissaoCNH(Date dtPrimeiraEmissaoCNH) {
        this.dtPrimeiraEmissaoCNH = dtPrimeiraEmissaoCNH;
    }

    public String getUfCNH() {
        return ufCNH;
    }

    public void setUfCNH(String ufCNH) {
        this.ufCNH = ufCNH;
    }

    public String getRegistroCNH() {
        return registroCNH;
    }

    public void setRegistroCNH(String registroCNH) {
        this.registroCNH = registroCNH;
    }

    public String getUrlCnhFrente() {
        return urlCnhFrente;
    }

    public void setUrlCnhFrente(String urlCnhFrente) {
        this.urlCnhFrente = urlCnhFrente;
    }

    public String getUrlCnhVerso() {
        return urlCnhVerso;
    }

    public void setUrlCnhVerso(String urlCnhVerso) {
        this.urlCnhVerso = urlCnhVerso;
    }

    public Mercadoria getMercadoria() {
        return mercadoria;
    }

    public void setMercadoria(Mercadoria mercadoria) {
        this.mercadoria = mercadoria;
    }

    public double getMercValor() {
        return mercValor;
    }

    public void setMercValor(double mercValor) {
        this.mercValor = mercValor;
    }

    public Integer getMercPaisOrigem() {
        return mercPaisOrigem;
    }

    public void setMercPaisOrigem(Integer mercPaisOrigem) {
        this.mercPaisOrigem = mercPaisOrigem;
    }

    public Integer getMercUFOrigem() {
        return mercUFOrigem;
    }

    public void setMercUFOrigem(Integer mercUFOrigem) {
        this.mercUFOrigem = mercUFOrigem;
    }

    public void setMercPaisDestino(Integer mercPaisDestino) {
        this.mercPaisDestino = mercPaisDestino;
    }

    public void setMercUFDestino(Integer mercUFDestino) {
        this.mercUFDestino = mercUFDestino;
    }

    public Integer getMercCidadeOrigem() {
        return mercCidadeOrigem;
    }

    public void setMercCidadeOrigem(Integer mercCidadeOrigem) {
        this.mercCidadeOrigem = mercCidadeOrigem;
    }

    public Integer getMercPaisDestino() {
        return mercPaisDestino;
    }

    public Integer getMercUFDestino() {
        return mercUFDestino;
    }

    public Integer getMercCidadeDestino() {
        return mercCidadeDestino;
    }

    public void setMercCidadeDestino(Integer mercCidadeDestino) {
        this.mercCidadeDestino = mercCidadeDestino;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Date getExecutado() {
        return executado;
    }

    public void setExecutado(Date executado) {
        this.executado = executado;
    }

	public Integer getIdMotoristaCnh() {
		return idMotoristaCnh;
	}

	public void setIdMotoristaCnh(Integer idMotoristaCnh) {
		this.idMotoristaCnh = idMotoristaCnh;
	}

    
}
