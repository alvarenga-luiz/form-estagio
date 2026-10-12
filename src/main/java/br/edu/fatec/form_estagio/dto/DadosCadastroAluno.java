package br.edu.fatec.form_estagio.dto;

import br.edu.fatec.form_estagio.enums.TipoEnsino;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record DadosCadastroAluno(

        @NotBlank(message = "Informe o RA")
        String ra,

        @NotBlank(message = "Informe o nome")
        String nome,

        @NotBlank(message = "Informe o e-mail")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Informe o contato")
        String contato,

        @NotBlank(message = "Informe o CEP")
        String cep,

        @NotNull(message = "Informe a data de nascimento")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataNascimento,

        @NotNull(message = "Informe a data de ingresso")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataIngresso,

        @NotNull(message = "Selecione um curso")
        Long idCurso,

        @NotNull(message = "Selecione uma escola")
        Long idEscola,

        @NotNull(message = "Selecione o tipo de ensino")
        TipoEnsino tipoEnsino

) {
    public static DadosCadastroAluno vazio() {
        return new DadosCadastroAluno(null, null, null, null, null, null, null, null, null, null);
    }
}
