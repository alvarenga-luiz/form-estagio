package br.edu.fatec.form_estagio.dto;

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
    String cargoContato,
    String areaDepartamento,
    String linkedin
) {
}