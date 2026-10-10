package app.curso;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.aluno.AlunoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class CursoService {

	private final CursoRepository cursoRepository;
	private final AlunoRepository alunoRepository;
	private final CursoMapper cursoMapper;

	@Transactional(readOnly = true)
	public List<DadosListagemCurso> listar(String termo) {
		List<Curso> cursos = (termo == null || termo.isBlank())
				? cursoRepository.findAll(Sort.by("nome").ascending())
				: cursoRepository.buscar(termo.trim());
		return cursos.stream().map(DadosListagemCurso::new).toList();
	}

	@Transactional(readOnly = true)
	public Curso buscarPorId(Long id) {
		return cursoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + id));
	}

	@Transactional
	public Curso criar(Curso curso) {
		curso.setId(null); 
		validarNomeUnico(curso);
		return cursoRepository.save(curso);
	}

	@Transactional
	public Curso atualizar(Curso form) {
		Curso existente = buscarPorId(form.getId());
		validarNomeUnico(form);
		cursoMapper.atualizar(form, existente);
		return cursoRepository.save(existente);
	}

	@Transactional
	public void deletar(Long id) {
		Curso curso = buscarPorId(id);
		if (alunoRepository.countByCursoId(id) > 0) {
			throw new IllegalStateException("Não é possível excluir: existem alunos vinculados a este curso.");
		}
		cursoRepository.delete(curso);
	}

	private void validarNomeUnico(Curso curso) {
		String nome = curso.getNome().trim();
		boolean duplicado = (curso.getId() == null) ? cursoRepository.existsByNomeIgnoreCase(nome) : cursoRepository.existsByNomeIgnoreCaseAndIdNot(nome, curso.getId());
		if (duplicado) {
			throw new IllegalStateException("Já existe um curso com este nome.");
		}
	}
}
