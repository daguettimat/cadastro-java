package br.com.global5.manager.model.cadastro;

import br.com.global5.manager.model.auxiliar.Proprietario;
import com.google.gson.annotations.Expose;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zielinski on 09/05/17.
 */
public class ReferenciaVeiculos {

    private Integer id;
    private Integer idVeiculo;
    private String  placa;
    private String  uf;
    private String  municipio;
    private String  renavam;
    private String  documento;
    private Date    dataDocumento;
    private String  chassi;
    private Integer marca;
    private Integer modelo;
    private String  versao;

    private String  categoria;
    private Integer anoFabricacao;
    private Integer anoModelo;
    private Integer cor;
    private String  observacao;
    private String  frota;
    private Integer  tipo;
    private boolean leasing;

    private boolean nacional;

    // tipo,telefone,ddd,municipio,renavam,cor,documento,nacional,modelo,leasing,uf,marca,proprietario,idVeiculo,chassi,id,idCategoria,placa

    private Integer idCategoria;

    private Integer idTelefone;

    private String  ddd;
    private String  telefone;


    private transient Proprietario proprietario;

    private String nomeproprietario;

    private transient Proprietario arrendatario;

    public ReferenciaVeiculos() {}


    public String toCSVString() {


        if( arrendatario == null ) {
            arrendatario = new Proprietario();
        }

        if( proprietario == null ) {
            proprietario = new Proprietario();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return  id +
                ";" + idVeiculo +
                ";" + placa + 
                ";" + uf + 
                ";" + municipio + 
                ";" + renavam + 
                ";" + documento +
                ";" + dateFormat.format(dataDocumento) +
                ";" + chassi + 
                ";" + marca +
                ";" + modelo +
                ";" + versao + 
                ";" + categoria + 
                ";" + anoFabricacao +
                ";" + anoModelo +
                ";" + cor +
                ";" + observacao + 
                ";" + frota + 
                ";" + tipo +
                ";" + leasing +
                ";" + nacional +
                ";" + idCategoria +
                ";" + idTelefone +
                ";" + ddd + 
                ";" + telefone + 
                ";" + proprietario.toCSVString() +
                ";" + nomeproprietario + 
                ";" + arrendatario.toCSVString();
    }


    public String toCSVHeader() {
        if( arrendatario == null ) {
            arrendatario = new Proprietario();
        }

        if( proprietario == null ) {
            proprietario = new Proprietario();
        }

        return "id;idVeiculo;placa;uf;municipio;renavam;documento;datadocumento;chassi;marca;modelo;versao;categoria;anoFabricacao;" +
                "anoModelo;cor;observacao;frota;tipo;leasing;nacional;idCategoria;idTelefone;ddd;telefone;" +
                proprietario.toCSVHeader("prop_") + ";nomeproprietario;" + arrendatario.toCSVHeader("arre_");
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(Integer idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public Integer getMarca() {
        return marca;
    }

    public void setMarca(Integer marca) {
        this.marca = marca;
    }

    public Integer getModelo() {
        return modelo;
    }

    public void setModelo(Integer modelo) {
        this.modelo = modelo;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public Integer getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(Integer anoModelo) {
        this.anoModelo = anoModelo;
    }

    public Integer getCor() {
        return cor;
    }

    public void setCor(Integer cor) {
        this.cor = cor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getFrota() {
        return frota;
    }

    public void setFrota(String frota) {
        this.frota = frota;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public boolean isLeasing() {
        return leasing;
    }

    public void setLeasing(boolean leasing) {
        this.leasing = leasing;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public Proprietario getArrendatario() {
        return arrendatario;
    }

    public void setArrendatario(Proprietario arrendatario) {
        this.arrendatario = arrendatario;
    }

    public boolean isNacional() {
        return nacional;
    }

    public void setNacional(boolean nacional) {
        this.nacional = nacional;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Integer getIdTelefone() {
        return idTelefone;
    }

    public void setIdTelefone(Integer idTelefone) {
        this.idTelefone = idTelefone;
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

    public String getNomeproprietario() {
        return nomeproprietario;
    }

    public void setNomeproprietario(String nomeproprietario) {
        this.nomeproprietario = nomeproprietario;
    }

    public Date getDataDocumento() {
        return dataDocumento;
    }

    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }
}
