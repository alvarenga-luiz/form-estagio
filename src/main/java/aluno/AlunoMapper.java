package aluno;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AlunoMapper {
	@Mapping(target = "id", ignore = true)    
	@Mapping(target = "curso", ignore = true) 
	void atualizar(Aluno origem, @MappingTarget Aluno destino);
}
