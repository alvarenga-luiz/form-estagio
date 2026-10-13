package br.edu.fatec.form_estagio.service;

import java.util.List;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoAluno;
import br.edu.fatec.form_estagio.dto.DadosCadastroAluno;
import br.edu.fatec.form_estagio.dto.DadosListagemAluno;
import br.edu.fatec.form_estagio.mapper.AlunoMapper;
import br.edu.fatec.form_estagio.model.Aluno;
import br.edu.fatec.form_estagio.model.Contrato;
import br.edu.fatec.form_estagio.model.Escola;
import br.edu.fatec.form_estagio.repository.AlunoRepository;
import br.edu.fatec.form_estagio.repository.ContratoRepository;
import br.edu.fatec.form_estagio.repository.EscolaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.fatec.form_estagio.model.Curso;
import br.edu.fatec.form_estagio.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoService {
	private final AlunoRepository alunoRepository;
	private final CursoRepository cursoRepository;
	private final EscolaRepository escolaRepository;
	private final ContratoRepository contratoRepository;
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

	@Transactional(readOnly = true)
	public DadosAtualizacaoAluno buscarParaEdicao(Long id) {
		return alunoMapper.toAtualizacao(buscarPorId(id));
	}

	@Transactional
	public Aluno criar(DadosCadastroAluno dados) {
		validarRaUnico(dados.ra(), null);

		Aluno aluno = alunoMapper.toEntity(dados);
		aluno.setCurso(resolverCurso(dados.idCurso()));
		aluno.setEscola(resolverEscola(dados.idEscola()));
		return alunoRepository.save(aluno);
	}

	@Transactional
	public Aluno atualizar(DadosAtualizacaoAluno dados) {
		Aluno existente = buscarPorId(dados.id());
		validarRaUnico(dados.ra(), dados.id());

		alunoMapper.atualizar(dados, existente);
		existente.setCurso(resolverCurso(dados.idCurso()));
		existente.setEscola(resolverEscola(dados.idEscola()));
		return alunoRepository.save(existente);
	}

	@Transactional
	public void deletar(Long id) {
		if(contratoRepository.existsByAlunoId(id)) {
			throw new IllegalStateException("Não é possível excluir: o aluno possui contratos vinculados.");
		}
		alunoRepository.delete(buscarPorId(id));
	}

	private Curso resolverCurso(Long cursoId) {
		return cursoRepository.findById(cursoId)
				.orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + cursoId));
	}

	private Escola resolverEscola(Long escolaId) {
		return escolaRepository.findById(escolaId)
				.orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + escolaId));
	}

	private void validarRaUnico(String ra, Long alunoId) {
		String raLimpo = ra.trim();
		boolean duplicado = (alunoId == null) ? alunoRepository.existsByRa(raLimpo) : alunoRepository.existsByRaAndIdNot(raLimpo, alunoId);
		if (duplicado) {
			throw new IllegalStateException("Já existe um aluno com este RA.");
		}
	}
}
