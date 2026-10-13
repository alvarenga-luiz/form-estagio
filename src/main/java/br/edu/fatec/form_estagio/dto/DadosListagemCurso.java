package br.edu.fatec.form_estagio.dto;

import br.edu.fatec.form_estagio.model.Curso;

public record DadosListagemCurso(
		Long id,
		String nome,
		String coordenador) {

	public DadosListagemCurso(Curso curso) {
		this(curso.getId(), curso.getNome(), curso.getCoordenador());
	}
}
