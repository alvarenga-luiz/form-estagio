package br.edu.fatec.form_estagio.enums;

public enum StatusContrato {

    EM_ANALISE("Em análise"),
    ATIVO("Ativo"),
    ENCERRADO("Encerrado"),
    RESCINDIDO("Rescindido");

    private final String descricao;

    StatusContrato(String descricao) {this.descricao = descricao;}

    public String getDescricao() {return descricao;}
}
