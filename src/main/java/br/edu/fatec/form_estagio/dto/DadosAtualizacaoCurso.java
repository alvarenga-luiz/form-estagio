package br.edu.fatec.form_estagio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoCurso(

        @NotNull
        Long id,

        @NotBlank(message = "Informe o nome do curso")
        String nome,

        @NotBlank(message = "Informe o coordenador")
        String coordenador

) {
}
