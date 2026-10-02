package br.edu.fatec.form_estagio;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroAluno (

	@NotBlank
	String nome,
	String contato,
	String email,
	String curso,
	int semestre,
	int periodo
	) {

}
