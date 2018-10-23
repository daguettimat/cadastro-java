package br.com.global5.infra.model;

import java.util.Collection;
import java.util.Map;

/**
 * Created by zielinski on 05/03/17.
 */
public class Criteria {

    public class Veiculos {
        private int tipo;
        private int refProprietario;
        private int refVeiculo;
        private int comProprietario;
        private int comArrendatario;
        private int anoVeiculo;
        private int sitAntt;
        private int avaAntt;
        private int docRenavan;

        public Veiculos(int tipo, int refProprietario, int refVeiculo, int comProprietario,
                        int comArrendatario, int anoVeiculo, int sitAntt, int avaAntt, int docRenavan) {
            this.tipo = tipo;
            this.refProprietario = refProprietario;
            this.refVeiculo = refVeiculo;
            this.comProprietario = comProprietario;
            this.comArrendatario = comArrendatario;
            this.anoVeiculo = anoVeiculo;

            this.avaAntt = avaAntt;
            this.docRenavan = docRenavan;
        }

        public int getTipo() {
            return tipo;
        }

        public void setTipo(int tipo) {
            this.tipo = tipo;
        }

        public int getRefProprietario() {
            return refProprietario;
        }

        public void setRefProprietario(int refProprietario) {
            this.refProprietario = refProprietario;
        }

        public int getRefVeiculo() {
            return refVeiculo;
        }

        public void setRefVeiculo(int refVeiculo) {
            this.refVeiculo = refVeiculo;
        }

        public int getComProprietario() {
            return comProprietario;
        }

        public void setComProprietario(int comProprietario) {
            this.comProprietario = comProprietario;
        }

        public int getComArrendatario() {
            return comArrendatario;
        }

        public void setComArrendatario(int comArrendatario) {
            this.comArrendatario = comArrendatario;
        }

        public int getAnoVeiculo() {
            return anoVeiculo;
        }

        public void setAnoVeiculo(int anoVeiculo) {
            this.anoVeiculo = anoVeiculo;
        }

        public int getSitAntt() {
            return sitAntt;
        }

        public void setSitAntt(int sitAntt) {
            this.sitAntt = sitAntt;
        }

        public int getAvaAntt() {
            return avaAntt;
        }

        public void setAvaAntt(int avaAntt) {
            this.avaAntt = avaAntt;
        }

        public int getDocRenavan() {
            return docRenavan;
        }

        public void setDocRenavan(int docRenavan) {
            this.docRenavan = docRenavan;
        }
    }

    //
    // Analise Mercadoria
    //
    private int tipoMercadoria;
    private double vlrMercadoria;

    //
    // Analise acMotorista
    //
    private int tipoPessoaFisica;
    private int cnhPessoaFisica;
    private int refMotorista;
    private int refPessoalMotorista;
    private int refComercialMotorista;
    private int comercial;
    private int nivelCriminal;
    private int numeroPontos;
    private int cargasRealizadas;
    private int serasa;
    private int cheques;

    private Collection<Veiculos> veiculos;

    public Criteria() {
    }

    public Criteria(int tipoMercadoria, double vlrMercadoria, int tipoPessoaFisica, int cnhPessoaFisica,
                    int refMotorista, int refPessoalMotorista, int refComercialMotorista, int comercial,
                    int nivelCriminal, int numeroPontos, int cargasRealizadas, int serasa,
                    int cheques, Collection<Veiculos> veiculos) {
        this.tipoMercadoria = tipoMercadoria;
        this.vlrMercadoria = vlrMercadoria;
        this.tipoPessoaFisica = tipoPessoaFisica;
        this.cnhPessoaFisica = cnhPessoaFisica;
        this.refMotorista = refMotorista;
        this.refPessoalMotorista = refPessoalMotorista;
        this.refComercialMotorista = refComercialMotorista;
        this.comercial = comercial;
        this.nivelCriminal = nivelCriminal;
        this.numeroPontos = numeroPontos;
        this.cargasRealizadas = cargasRealizadas;
        this.serasa = serasa;
        this.cheques = cheques;
        this.veiculos = veiculos;
    }

    public int getTipoMercadoria() {
        return tipoMercadoria;
    }

    public void setTipoMercadoria(int tipoMercadoria) {
        this.tipoMercadoria = tipoMercadoria;
    }

    public double getVlrMercadoria() {
        return vlrMercadoria;
    }

    public void setVlrMercadoria(double vlrMercadoria) {
        this.vlrMercadoria = vlrMercadoria;
    }

    public int getTipoPessoaFisica() {
        return tipoPessoaFisica;
    }

    public void setTipoPessoaFisica(int tipoPessoaFisica) {
        this.tipoPessoaFisica = tipoPessoaFisica;
    }

    public int getCnhPessoaFisica() {
        return cnhPessoaFisica;
    }

    public void setCnhPessoaFisica(int cnhPessoaFisica) {
        this.cnhPessoaFisica = cnhPessoaFisica;
    }

    public int getRefMotorista() {
        return refMotorista;
    }

    public void setRefMotorista(int refMotorista) {
        this.refMotorista = refMotorista;
    }

    public int getRefPessoalMotorista() {
        return refPessoalMotorista;
    }

    public void setRefPessoalMotorista(int refPessoalMotorista) {
        this.refPessoalMotorista = refPessoalMotorista;
    }

    public int getRefComercialMotorista() {
        return refComercialMotorista;
    }

    public void setRefComercialMotorista(int refComercialMotorista) {
        this.refComercialMotorista = refComercialMotorista;
    }

    public int getComercial() {
        return comercial;
    }

    public void setComercial(int comercial) {
        this.comercial = comercial;
    }

    public int getNivelCriminal() {
        return nivelCriminal;
    }

    public void setNivelCriminal(int nivelCriminal) {
        this.nivelCriminal = nivelCriminal;
    }

    public int getNumeroPontos() {
        return numeroPontos;
    }

    public void setNumeroPontos(int numeroPontos) {
        this.numeroPontos = numeroPontos;
    }

    public int getCargasRealizadas() {
        return cargasRealizadas;
    }

    public void setCargasRealizadas(int cargasRealizadas) {
        this.cargasRealizadas = cargasRealizadas;
    }

    public int getSerasa() {
        return serasa;
    }

    public void setSerasa(int serasa) {
        this.serasa = serasa;
    }

    public int getCheques() {
        return cheques;
    }

    public void setCheques(int cheques) {
        this.cheques = cheques;
    }

    public Collection<Veiculos> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(Collection<Veiculos> veiculos) {
        this.veiculos = veiculos;
    }
}

