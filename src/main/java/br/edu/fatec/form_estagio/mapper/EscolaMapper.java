package br.edu.fatec.form_estagio.mapper;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoEscola;
import br.edu.fatec.form_estagio.dto.DadosCadastroEscola;
import br.edu.fatec.form_estagio.model.Escola;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EscolaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "alunos", ignore = true)
    Escola toEntity(DadosCadastroEscola dados);

    // Converte Entity para DTO (para preencher formulário de edição)
    DadosAtualizacaoEscola toAtualizacao(Escola escola);

    // Ignoramos o ID e a lista de alunos para não serem sobrescritos na hora de atualizar a escola
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "alunos", ignore = true)
    void atualizar(DadosAtualizacaoEscola dados, @MappingTarget Escola escola);
}
