package br.edu.fatec.form_estagio.mapper;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoContrato;
import br.edu.fatec.form_estagio.model.Contrato;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContratoMapper {

    // Converte Entity para DTO (para preencher formulário de edição)
    DadosAtualizacaoContrato toAtualizacaoDto(Contrato contrato);

    @Mapping(target = "id", ignore = true)
    Contrato toEntityFromAtualizacao(DadosAtualizacaoContrato dto);

    @Mapping(target = "id", ignore = true)
    Contrato updateEntityFromDto(DadosAtualizacaoContrato dto, @MappingTarget Contrato contrato);

}
