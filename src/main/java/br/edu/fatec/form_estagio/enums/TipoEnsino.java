package br.edu.fatec.form_estagio.enums;

public enum TipoEnsino {

    MEDIO_REGULAR("Ensino Médio Regular"),
    MEDIO_TECNICO("Ensino Médio Integrado ao Técnico"),
    TECNICO("Ensino Técnico"),
    EJA("EJA / Supletivo"),
    SUPERIOR("Superior");

    private final String descricao;

    // Construtor para receber a mensagem do Enum
    TipoEnsino(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
