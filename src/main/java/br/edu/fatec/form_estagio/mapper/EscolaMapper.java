package br.edu.fatec.form_estagio.mapper;

import br.edu.fatec.form_estagio.model.Escola;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EscolaMapper {

    // Ignoramos o ID e a lista de alunos para não serem sobrescritos na hora de atualizar a escola
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "alunos", ignore = true)
    void atualizar(Escola origem, @MappingTarget Escola destino);
}
