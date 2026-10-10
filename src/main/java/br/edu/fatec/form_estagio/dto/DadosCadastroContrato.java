package br.edu.fatec.form_estagio.dto;

import br.edu.fatec.form_estagio.enums.StatusContrato;

import java.time.LocalDate;

public record DadosCadastroContrato (

        Long idAluno,
        Long idEmpresa,
        LocalDate dataInicio,
        LocalDate dataFim,
        int cargaHoraria,
        String areaEstagio,
        String descFuncao,
        String supervisor,
        String cargoSupervisor,
        StatusContrato status

) {
}
