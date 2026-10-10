package app.aluno;

import java.time.LocalDate;

public record DadosListagemAluno (

		Long id,
		String ra,
		String nome,
		String cep,
		LocalDate dataNascimento,
		LocalDate dataIngresso,
		Long cursoId,
		String cursoNome) {

	public DadosListagemAluno(Aluno aluno) {
		this(aluno.getId(),
			aluno.getRa(),
			aluno.getNome(),
			aluno.getCep(),
			aluno.getDataNascimento(),
			aluno.getDataIngresso(),
			aluno.getCurso().getId(),
			aluno.getCurso().getNome());
	}
}
