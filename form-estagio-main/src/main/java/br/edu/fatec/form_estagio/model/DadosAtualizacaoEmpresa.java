package br.edu.fatec.form_estagio.model;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoEmpresa(
    @NotNull
    Long id,
    String razaoSocial,
    String areaAtuacao,
    String endereco,
    String cidade,
    String cep,
    String email,
    String site,
    String telefone,
    String nomeContato,
    String cargoContrato,
    String areaDepartamento,
    String linkedin
) {
}