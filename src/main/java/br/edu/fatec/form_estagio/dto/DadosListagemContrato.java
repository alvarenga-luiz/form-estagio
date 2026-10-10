package br.edu.fatec.form_estagio.dto;

import br.edu.fatec.form_estagio.enums.StatusContrato;
import br.edu.fatec.form_estagio.model.Contrato;

import java.time.LocalDate;

public record DadosListagemContrato(

        Long id,
        String nomeAluno,
        String nomeEmpresa,
        LocalDate dataInicio,
        LocalDate dataFim,
        StatusContrato status
) {
    public DadosListagemContrato(Contrato contrato) {
        this(
                contrato.getId(),
                contrato.getAluno().getNome(),
                contrato.getEmpresa().getRazaoSocial(),
                contrato.getDataInicio(),
                contrato.getDataFim(),
                contrato.getStatus()
        );
    }

}
