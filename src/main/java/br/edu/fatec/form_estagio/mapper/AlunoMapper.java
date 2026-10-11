package br.edu.fatec.form_estagio.mapper;

import br.edu.fatec.form_estagio.model.Aluno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AlunoMapper {
	@Mapping(target = "id", ignore = true)    
	@Mapping(target = "curso", ignore = true) 
	void atualizar(Aluno origem, @MappingTarget Aluno destino);
}
