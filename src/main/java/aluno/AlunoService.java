package aluno;

import java.util.List;
import curso.Curso;
import curso.CursoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoService {
	private final AlunoRepository alunoRepository;
	private final CursoRepository cursoRepository;
	private final AlunoMapper alunoMapper;
	
	@Transactional(readOnly = true)
	public List<DadosListagemAluno> listar(String termo) {
		List<Aluno> alunos = (termo == null || termo.isBlank()) ? alunoRepository.findAllComCurso() : alunoRepository.buscar(termo.trim());
		return alunos.stream().map(DadosListagemAluno::new).toList();
	}

	@Transactional(readOnly = true)
	public Aluno buscarPorId(Long id) {
		return alunoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
	}

	@Transactional
	public Aluno criar(Aluno aluno) {
		aluno.setId(null);
		validarRaUnico(aluno);
		aluno.setCurso(resolverCurso(aluno)); 
		return alunoRepository.save(aluno);
	}

	@Transactional
	public Aluno atualizar(Aluno form) {
		Aluno existente = buscarPorId(form.getId());
		validarRaUnico(form);
		alunoMapper.atualizar(form, existente);
		existente.setCurso(resolverCurso(form));
		return alunoRepository.save(existente);
	}

	@Transactional
	public void deletar(Long id) {
		alunoRepository.delete(buscarPorId(id));
	}

	private Curso resolverCurso(Aluno aluno) {
		Long cursoId = aluno.getCurso().getId();
		return cursoRepository.findById(cursoId)
				.orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + cursoId));
	}

	private void validarRaUnico(Aluno aluno) {
		String ra = aluno.getRa().trim();
		boolean duplicado = (aluno.getId() == null) ? alunoRepository.existsByRa(ra) : alunoRepository.existsByRaAndIdNot(ra, aluno.getId());
		if (duplicado) {
			throw new IllegalStateException("Já existe um aluno com este RA.");
		}
	}
}
