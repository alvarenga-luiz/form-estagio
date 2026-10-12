package br.edu.fatec.form_estagio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoEscola(

        @NotNull
        Long id,

        @NotBlank(message = "Informe o nome da escola")
        String nome,

        @NotBlank(message = "Informe o CEP")
        String cep,

        @NotBlank(message = "Informe a rua")
        String rua,

        @NotBlank(message = "Informe o número")
        String numero

) {
}
