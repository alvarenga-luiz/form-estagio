package br.edu.fatec.form_estagio.service;

import java.util.List;

import br.edu.fatec.form_estagio.repository.CursoRepository;
import br.edu.fatec.form_estagio.dto.DadosAtualizacaoCurso;
import br.edu.fatec.form_estagio.dto.DadosCadastroCurso;
import br.edu.fatec.form_estagio.dto.DadosListagemCurso;
import br.edu.fatec.form_estagio.mapper.CursoMapper;
import br.edu.fatec.form_estagio.model.Curso;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.fatec.form_estagio.repository.AlunoRepository;
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

	@Transactional(readOnly = true)
	public DadosAtualizacaoCurso buscarParaEdicao(Long id) {
		return cursoMapper.toAtualizacao(buscarPorId(id));
	}

	@Transactional
	public Curso criar(DadosCadastroCurso dados) {
		validarNomeUnico(dados.nome(), null);
		return cursoRepository.save(cursoMapper.toEntity(dados));
	}

	@Transactional
	public Curso atualizar(DadosAtualizacaoCurso dados) {
		Curso existente = buscarPorId(dados.id());
		validarNomeUnico(dados.nome(), dados.id());
		cursoMapper.atualizar(dados, existente);
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

	private void validarNomeUnico(String nome, Long cursoId) {
		String nomeLimpo = nome.trim();
		boolean duplicado = (cursoId == null) ? cursoRepository.existsByNomeIgnoreCase(nomeLimpo) : cursoRepository.existsByNomeIgnoreCaseAndIdNot(nomeLimpo, cursoId);
		if (duplicado) {
			throw new IllegalStateException("Já existe um curso com este nome.");
		}
	}
}
