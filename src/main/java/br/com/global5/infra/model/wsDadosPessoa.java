package br.com.global5.infra.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zielinski on 04/05/17.
 */
public class wsDadosPessoa {

/*
		"numero_de_cpf": "876.304.439-00",
		"nome_da_pf": "JOSE ROBERTO ZIELINSKI",
		"data_nascimento": "05\/03\/1972",
		"situacao_cadastral": "REGULAR",
		"data_inscricao": "08\/04\/1992",
		"digito_verificador": "00",
		"comprovante_emitido": "A15F.5707.6C9A.BF13",
		"comprovante_emitido_data": "11:56:33 às 04\/05\/2017"
*/

    @SerializedName("numero_de_cpf")
    private String numeroCPF;
    @SerializedName("nome_da_pf")
    private String nome;
    @SerializedName("data_nascimento")
    private String dtNascimento;
    @SerializedName("situacao_cadastral")
    private String situacaoCadastral;
    @SerializedName("data_inscricao")
    private String dtInscricao;
    @SerializedName("digito_verificador")
    private String digitoVerificador;
    @SerializedName("comprovante_emitido")
    private String numeroComprovante;
    @SerializedName("comprovante_emitido_data")
    private String dtComprovante;

    public wsDadosPessoa() {}

    @Override
    public String toString() {
        return "wsDadosPessoa{" +
                "numeroCPF='" + numeroCPF + '\'' +
                ", nome='" + nome + '\'' +
                ", dtNascimento='" + dtNascimento + '\'' +
                ", situacaoCadastral='" + situacaoCadastral + '\'' +
                ", dtInscricao='" + dtInscricao + '\'' +
                ", digitoVerificador='" + digitoVerificador + '\'' +
                ", numeroComprovante='" + numeroComprovante + '\'' +
                ", dtComprovante='" + dtComprovante + '\'' +
                '}';
    }

    public String getNumeroCPF() {
        return numeroCPF;
    }

    public void setNumeroCPF(String numeroCPF) {
        this.numeroCPF = numeroCPF;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(String dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getSituacaoCadastral() {
        return situacaoCadastral;
    }

    public void setSituacaoCadastral(String situacaoCadastral) {
        this.situacaoCadastral = situacaoCadastral;
    }

    public String getDtInscricao() {
        return dtInscricao;
    }

    public void setDtInscricao(String dtInscricao) {
        this.dtInscricao = dtInscricao;
    }

    public String getDigitoVerificador() {
        return digitoVerificador;
    }

    public void setDigitoVerificador(String digitoVerificador) {
        this.digitoVerificador = digitoVerificador;
    }

    public String getNumeroComprovante() {
        return numeroComprovante;
    }

    public void setNumeroComprovante(String numeroComprovante) {
        this.numeroComprovante = numeroComprovante;
    }

    public String getDtComprovante() {
        return dtComprovante;
    }

    public void setDtComprovante(String dtComprovante) {
        this.dtComprovante = dtComprovante;
    }
}
