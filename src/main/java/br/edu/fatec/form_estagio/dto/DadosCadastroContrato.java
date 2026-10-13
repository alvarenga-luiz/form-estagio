package br.edu.fatec.form_estagio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// Sem "status": todo contrato nasce EM_ANALISE, quem define é o service
public record DadosCadastroContrato(

        @NotNull(message = "Selecione um aluno")
        Long idAluno,

        @NotNull(message = "Selecione uma empresa")
        Long idEmpresa,

        @NotNull(message = "Informe a data de início")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataInicio,

        @NotNull(message = "Informe a data de término")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataFim,

        @NotNull(message = "Informe a carga horária")
        @Positive(message = "A carga horária deve ser maior que zero")
        Integer cargaHoraria,

        @NotBlank(message = "Informe a área do estágio")
        String areaEstagio,

        String descFuncao,

        @NotBlank(message = "Informe o supervisor")
        String supervisor,

        @NotBlank(message = "Informe o cargo do supervisor")
        String cargoSupervisor

) {
    public static DadosCadastroContrato vazio() {
        return new DadosCadastroContrato(null, null, null, null, null, null, null, null, null);
    }
}
