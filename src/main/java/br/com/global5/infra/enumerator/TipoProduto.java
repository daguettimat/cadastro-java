package br.com.global5.infra.enumerator;

public enum TipoProduto  {
    Cadastro(15, "Cadastro"),
    Monitoramento(16, "Monitoramento"),
    Prevencao_de_Acidentes(17, "Prevenção de Acidentes");

    private final Integer order;
    private final String value;

    TipoProduto(int order, String value) {
        this.order = order;
        this.value = value;
    }

    //recebe o codigo registrado do tipo em string
    public static TipoProduto fromValue(String value) {

        if (value != null) {
            for (TipoProduto tipo : values()) {
                if (tipo.order.toString().equals(value)) {
                    return tipo;
                }
            }
        }
            // you may return a default value
            return getDefault();
            // or throw an exception
            // throw new IllegalArgumentException("Invalid tipo: " + value);
    }

    public String toValue() {
        return value;
    }

    public static TipoProduto getDefault() {
        return Cadastro;
    }

    public Integer toKey() {
        return order;

    }

}


