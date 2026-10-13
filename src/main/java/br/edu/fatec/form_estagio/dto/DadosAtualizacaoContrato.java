package br.edu.fatec.form_estagio.dto;

import br.edu.fatec.form_estagio.enums.StatusContrato;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// Sem aluno e empresa: o vínculo do contrato não muda; se estiver errado, apaga e cadastra outro
public record DadosAtualizacaoContrato(

        @NotNull
        Long id,

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
        String cargoSupervisor,

        @NotNull(message = "Selecione o status")
        StatusContrato status

) {
}
