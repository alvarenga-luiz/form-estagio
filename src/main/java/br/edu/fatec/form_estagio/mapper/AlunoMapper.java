package br.edu.fatec.form_estagio.mapper;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoAluno;
import br.edu.fatec.form_estagio.dto.DadosCadastroAluno;
import br.edu.fatec.form_estagio.model.Aluno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AlunoMapper {

	// Curso e escola são resolvidos pelo service a partir dos IDs
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "curso", ignore = true)
	@Mapping(target = "escola", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Aluno toEntity(DadosCadastroAluno dados);

	// Converte Entity para DTO (para preencher formulário de edição)
	@Mapping(target = "idCurso", source = "curso.id")
	@Mapping(target = "idEscola", source = "escola.id")
	DadosAtualizacaoAluno toAtualizacao(Aluno aluno);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "curso", ignore = true)
	@Mapping(target = "escola", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	void atualizar(DadosAtualizacaoAluno dados, @MappingTarget Aluno aluno);
}
