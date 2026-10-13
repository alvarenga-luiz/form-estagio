package br.edu.fatec.form_estagio.mapper;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoCurso;
import br.edu.fatec.form_estagio.dto.DadosCadastroCurso;
import br.edu.fatec.form_estagio.model.Curso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CursoMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "alunos", ignore = true)
	Curso toEntity(DadosCadastroCurso dados);

	// Converte Entity para DTO (para preencher formulário de edição)
	DadosAtualizacaoCurso toAtualizacao(Curso curso);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "alunos", ignore = true)
	void atualizar(DadosAtualizacaoCurso dados, @MappingTarget Curso curso);
}
