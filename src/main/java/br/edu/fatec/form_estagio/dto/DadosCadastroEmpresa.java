package br.edu.fatec.form_estagio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroEmpresa(

        @NotBlank(message = "Informe a razão social")
        String razaoSocial,

        @NotBlank(message = "Informe a área de atuação")
        String areaAtuacao,

        @NotBlank(message = "Informe o endereço")
        String endereco,

        @NotBlank(message = "Informe a cidade")
        String cidade,

        @NotBlank(message = "Informe o CEP")
        String cep,

        @NotBlank(message = "Informe o e-mail")
        @Email(message = "E-mail inválido")
        String email,

        String site,

        @NotBlank(message = "Informe o telefone")
        String telefone,

        @NotBlank(message = "Informe o nome do contato")
        String nomeContato,

        @NotBlank(message = "Informe o cargo do contato")
        String cargoContato,

        @NotBlank(message = "Informe a área / departamento")
        String areaDepartamento,

        String linkedin

) {
    public static DadosCadastroEmpresa vazio() {
        return new DadosCadastroEmpresa(null, null, null, null, null, null, null, null, null, null, null, null);
    }
}
