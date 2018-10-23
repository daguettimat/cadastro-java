package br.com.global5.infra.enumerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zielinski
 */
public enum uf {
        AC("Acre"),
        AL("Alagoas"),
        AM("Amazonas"),
        AP("Amapá"),
        BA("Bahia"),
        CE("Ceará"),
        DF("Distrito Federal"),
        ES("Espirito Santo"),
        GO("Goias"),
        MA("Maranhão"),
        MG("Minas Gerais"),
        MS("Mato Grosso Sul"),
        MT("Mato Grosso"),
        PA("Pará"),
        PB("Paraiba"),
        PE("Pernanbuco"),
        PI("Piaui"),
        PR("Parana"),
        RJ("Rio de Janeiro"),
        RN("Rio Grande do Norte"),
        RO("Rondônia"),
        RR("Roraima"),
        RS("Rio Grande do Sul"),
        SC("Santa Catarina"),
        SE("Sergipe"),
        SP("São Paulo"),
        TO("Tocantins");

    uf(String UF) {
        this.UF = UF;
    }

    public String getUF() {
        return UF;
    }

    public void setUF(String UF) {
        this.UF = UF;
    }

    private String UF;




}
