package br.edu.fatec.form_estagio.dto;

import br.edu.fatec.form_estagio.enums.StatusContrato;

import java.time.LocalDate;

public record DadosAtualizacaoContrato(

        Long id,
        LocalDate dataFim,
        Integer cargaHoraria,
        String areaEstagio,
        String descFuncao,
        String supervisor,
        String cargoSupervisor,
        StatusContrato status

) {
}
