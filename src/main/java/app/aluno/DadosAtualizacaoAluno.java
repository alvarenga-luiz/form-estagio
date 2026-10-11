package app.aluno;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoAluno(
	@NotNull
	Long id,
	String nome,
	String contato,
	String email,
	String curso,
	int periodo,
	int semestre) {
	
}
