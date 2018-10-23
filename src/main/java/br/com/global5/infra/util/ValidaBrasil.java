package br.com.global5.infra.util;

import java.util.regex.Pattern;

public class ValidaBrasil {
	
	
	public static boolean validateDocumento(String documento) {

        return validateCNPJ(documento) || validateCPF(documento);
	 
	}

    public static boolean validateRENAVAM(String renavam) {

        renavam = renavam.trim().replaceAll(Pattern.quote("."), "")
                .replaceAll(Pattern.quote("-"), "")
                .replaceAll(Pattern.quote("/"), "");

        // Pegando como exemplo o renavam = 639884962

        // Completa com zeros a esquerda se for no padrao antigo de 9 digitos
        // renavam = 00639884962
        if (renavam.matches("^([0-9]{9})$")) {
            renavam = "00" + renavam;
        }

        // Valida se possui 11 digitos pos formatacao
        if (!renavam.matches("[0-9]{11}")) {
            return false;
        }

        // Remove o digito (11 posicao)
        // renavamSemDigito = 0063988496
        String renavamSemDigito = renavam.substring(0, 10);

        // Inverte os caracteres (reverso)
        // renavamReversoSemDigito = 69488936
        String renavamReversoSemDigito = new StringBuffer(renavamSemDigito).reverse().toString();

        int soma = 0;

        // Multiplica as strings reversas do renavam pelos numeros multiplicadores
        // para apenas os primeiros 8 digitos de um total de 10
        // Exemplo: renavam reverso sem digito = 69488936
        // 6, 9, 4, 8, 8, 9, 3, 6
        // * * * * * * * *
        // 2, 3, 4, 5, 6, 7, 8, 9 (numeros multiplicadores - sempre os mesmos [fixo])
        // 12 + 27 + 16 + 40 + 48 + 63 + 24 + 54
        // soma = 284
        for (int i = 0; i < 8; i++) {
            Integer algarismo = Integer.parseInt(renavamReversoSemDigito.substring(i, i + 1));
            Integer multiplicador = i + 2;
            soma += algarismo * multiplicador;
        }

        // Multiplica os dois ultimos digitos e soma
        soma += Character.getNumericValue(renavamReversoSemDigito.charAt(8)) * 2;
        soma += Character.getNumericValue(renavamReversoSemDigito.charAt(9)) * 3;

        // mod11 = 284 % 11 = 9 (resto da divisao por 11)
        int mod11 = soma % 11;

        // Faz-se a conta 11 (valor fixo) - mod11 = 11 - 9 = 2
        int ultimoDigitoCalculado = 11 - mod11;

        // ultimoDigito = Caso o valor calculado anteriormente seja 10 ou 11, transformo ele em 0
        // caso contrario, eh o proprio numero
        ultimoDigitoCalculado = (ultimoDigitoCalculado >= 10 ? 0 : ultimoDigitoCalculado);

        // Pego o ultimo digito do renavam original (para confrontar com o calculado)
        int digitoRealInformado = Integer.valueOf(renavam.substring(renavam.length() - 1, renavam.length()));

        // Comparo os digitos calculado e informado
        return ultimoDigitoCalculado == digitoRealInformado;
    }
    
    public static boolean validateCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll(Pattern.quote("."), "")
                .replaceAll(Pattern.quote("-"), "")
                .replaceAll(Pattern.quote("/"), "");


        if (cnpj.length() != 14 || cnpj.matches("^(\\d)\\1*$")) {
            return false;
        }

	   /* Valida Dv's */
        String cnpj_calc = cnpj.substring(0, 12);

        char[] chr_cnpj = cnpj.toCharArray();  

	   /* Primeira parte */
        int soma = 0, dig = 0;

        for (int i = 0; i < 4; i++) {
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
                soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
            }
        }
        dig = 11 - (soma % 11);
        cnpj_calc += (dig == 10 || dig == 11) ?
                "0" : Integer.toString(dig);  

	   /* Segunda parte */
        soma = 0;
        for (int i = 0; i < 5; i++) {
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
            }
        }

        for (int i = 0; i < 8; i++) {
            if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
                soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
            }
        }

        dig = 11 - (soma % 11);
        cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

        return cnpj.equals(cnpj_calc);

    }
    
    public static boolean validateCPF(String cpf) {
        cpf = cpf.replaceAll(Pattern.quote("."), "").replaceAll(Pattern.quote("-"), "");

        int i, j, k;
        int soma;
        int digito;
        int numeroAtual;
        String numero;
        if (cpf.length() != 11 || cpf.matches("^(\\d)\\1*$")) {
            return false;
        }
        numero = cpf.substring(0, 9);
        for (j = 1; j < 3; j++) {
            k = 2;
            soma = 0;
            for (i = (numero.length() - 1); i >= 0; i--) {
                numeroAtual = Integer.parseInt(String.valueOf(numero.charAt(i)));
                soma = soma + numeroAtual * k;
                k = k + 1;
            }
            digito = 11 - (soma % 11);
            if (digito >= 10) {
                digito = 0;
            }
            numero = numero + digito;
        }

        return cpf.equals(numero);
    } 
    
    public static boolean validatePIS(String valor) {
        valor = valor.trim().replaceAll(Pattern.quote("."), "")
                .replaceAll(Pattern.quote("-"), "")
                .replaceAll(Pattern.quote("/"), "");

        int liTamanho = 0;
        StringBuffer lsAux = null;
        StringBuffer lsMultiplicador = new StringBuffer("3298765432");
        int liTotalizador = 0;
        int liResto = 0;
        int liMultiplicando = 0;
        int liMultiplicador = 0;
        boolean lbRetorno = true;
        int liDigito = 99;
        lsAux = new StringBuffer().append(valor);
        liTamanho = lsAux.length();
        if (liTamanho != 11) {
            lbRetorno = false;
        }
        if (lbRetorno) {
            for (int i = 0; i < 10; i++) {
                liMultiplicando = Integer.parseInt(lsAux.substring(i, i + 1));
                liMultiplicador = Integer.parseInt(lsMultiplicador.substring(i, i + 1));
                liTotalizador += liMultiplicando * liMultiplicador;
            }
            liResto = 11 - liTotalizador % 11;
            liResto = liResto == 10 || liResto == 11 ? 0 : liResto;
            liDigito = Integer.parseInt("" + lsAux.charAt(10));
            lbRetorno = liResto == liDigito;
        }
        return lbRetorno;
    }    
	
}
