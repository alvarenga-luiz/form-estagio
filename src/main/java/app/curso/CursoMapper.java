package app.curso;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CursoMapper {

	@Mapping(target = "id", ignore = true)     
	@Mapping(target = "alunos", ignore = true) 
	void atualizar(Curso origem, @MappingTarget Curso destino);
}
