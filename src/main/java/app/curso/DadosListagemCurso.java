package app.curso;

public record DadosListagemCurso(
		Long id,
		String nome,
		String coordenador) {

	public DadosListagemCurso(Curso curso) {
		this(curso.getId(), curso.getNome(), curso.getCoordenador());
	}
}
