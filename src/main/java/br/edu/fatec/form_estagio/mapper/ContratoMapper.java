package br.edu.fatec.form_estagio.mapper;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoContrato;
import br.edu.fatec.form_estagio.dto.DadosCadastroContrato;
import br.edu.fatec.form_estagio.model.Contrato;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContratoMapper {

    // Aluno, empresa e status são definidos pelo service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aluno", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Contrato toEntity(DadosCadastroContrato dados);

    // Converte Entity para DTO (para preencher formulário de edição)
    DadosAtualizacaoContrato toAtualizacao(Contrato contrato);

    // O vínculo com aluno e empresa e as datas de auditoria não podem ser sobrescritos
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aluno", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void atualizar(DadosAtualizacaoContrato dados, @MappingTarget Contrato contrato);
}
