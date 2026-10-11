package br.edu.fatec.form_estagio.model;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroEmpresa(
    @NotBlank
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