package br.edu.fatec.form_estagio.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroCurso(

        @NotBlank(message = "Informe o nome do curso")
        String nome,

        @NotBlank(message = "Informe o coordenador")
        String coordenador

) {
    public static DadosCadastroCurso vazio() {
        return new DadosCadastroCurso(null, null);
    }
}
