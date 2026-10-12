package br.edu.fatec.form_estagio.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<Escola> escolas = (termo == null || termo.isBlank()) ? escolaRepository.findAll() : escolaRepository.buscar(termo.trim());
        return escolas.stream().map(DadosListagemEscola::new).toList();
    }

    @Transactional(readOnly = true)
    public Escola buscarPorId(Long id) {
        return escolaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com ID: " + id));
    }

    @Transactional
    public Escola criar(Escola escola) {
        escola.setId(null);
        validarNomeUnico(escola);
        return escolaRepository.save(escola);
    }

    @Transactional
    public Escola atualizar(Escola form) {
        Escola existente = buscarPorId(form.getId());
        validarNomeUnico(form);
        escolaMapper.atualizar(form, existente);
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

    private void validarNomeUnico(Escola escola) {
        String nome = escola.getNome().trim();
        boolean duplicado = (escola.getId() == null) ? escolaRepository.existsByNomeIgnoreCase(nome) : escolaRepository.existsByNomeIgnoreCaseAndIdNot(nome, escola.getId());

        if (duplicado) {
            throw new IllegalStateException("Já existe uma escola cadastrada com este nome.");
        }
    }
}
