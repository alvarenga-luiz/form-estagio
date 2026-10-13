package br.edu.fatec.form_estagio.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoEscola;
import br.edu.fatec.form_estagio.dto.DadosCadastroEscola;
import br.edu.fatec.form_estagio.dto.DadosListagemEscola;
import br.edu.fatec.form_estagio.mapper.EscolaMapper;
import br.edu.fatec.form_estagio.model.Escola;
import br.edu.fatec.form_estagio.repository.EscolaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EscolaService {

    private final EscolaRepository escolaRepository;
    private final EscolaMapper escolaMapper;

    @Transactional(readOnly = true)
    public List<DadosListagemEscola> listar(String termo) {
        List<Escola> escolas = (termo == null || termo.isBlank()) ? escolaRepository.findAll(Sort.by("nome").ascending()) : escolaRepository.buscar(termo.trim());
        return escolas.stream().map(DadosListagemEscola::new).toList();
    }

    @Transactional(readOnly = true)
    public Escola buscarPorId(Long id) {
        return escolaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public DadosAtualizacaoEscola buscarParaEdicao(Long id) {
        return escolaMapper.toAtualizacao(buscarPorId(id));
    }

    @Transactional
    public Escola criar(DadosCadastroEscola dados) {
        validarNomeUnico(dados.nome(), null);
        return escolaRepository.save(escolaMapper.toEntity(dados));
    }

    @Transactional
    public Escola atualizar(DadosAtualizacaoEscola dados) {
        Escola existente = buscarPorId(dados.id());
        validarNomeUnico(dados.nome(), dados.id());
        escolaMapper.atualizar(dados, existente);
        return escolaRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Escola escola = buscarPorId(id);
        if (!escola.getAlunos().isEmpty()) {
            throw new IllegalStateException("Não é possível apagar a escola: existem alunos vinculados a ela.");
        }
        escolaRepository.delete(escola);
    }

    private void validarNomeUnico(String nome, Long escolaId) {
        String nomeLimpo = nome.trim();
        boolean duplicado = (escolaId == null) ? escolaRepository.existsByNomeIgnoreCase(nomeLimpo) : escolaRepository.existsByNomeIgnoreCaseAndIdNot(nomeLimpo, escolaId);

        if (duplicado) {
            throw new IllegalStateException("Já existe uma escola cadastrada com este nome.");
        }
    }
}
