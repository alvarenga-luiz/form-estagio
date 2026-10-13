package br.edu.fatec.form_estagio.service;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoContrato;
import br.edu.fatec.form_estagio.dto.DadosCadastroContrato;
import br.edu.fatec.form_estagio.dto.DadosListagemContrato;
import br.edu.fatec.form_estagio.enums.StatusContrato;
import br.edu.fatec.form_estagio.mapper.ContratoMapper;
import br.edu.fatec.form_estagio.model.Aluno;
import br.edu.fatec.form_estagio.model.Contrato;
import br.edu.fatec.form_estagio.model.Empresa;
import br.edu.fatec.form_estagio.repository.AlunoRepository;
import br.edu.fatec.form_estagio.repository.ContratoRepository;
import br.edu.fatec.form_estagio.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratoService {

    // Um aluno só pode ter um contrato "em andamento" por vez
    private static final List<StatusContrato> STATUS_VIGENTES = List.of(StatusContrato.EM_ANALISE, StatusContrato.ATIVO);

    private final ContratoRepository contratoRepository;
    private final AlunoRepository alunoRepository;
    private final EmpresaRepository empresaRepository;
    private final ContratoMapper contratoMapper;

    @Transactional(readOnly = true)
    public List<DadosListagemContrato> listar(String ra, String razaoSocial) {
        return contratoRepository.buscar(vazioParaNull(ra), vazioParaNull(razaoSocial))
                .stream()
                .map(DadosListagemContrato::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Contrato buscarPorId(Long id) {
        return contratoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contrato não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public DadosAtualizacaoContrato buscarParaEdicao(Long id) {
        return contratoMapper.toAtualizacao(buscarPorId(id));
    }

    @Transactional
    public Contrato criar(DadosCadastroContrato dados) {
        validarPeriodo(dados.dataInicio(), dados.dataFim());
        validarContratoVigenteUnico(dados.idAluno(), StatusContrato.EM_ANALISE, null);

        Contrato contrato = contratoMapper.toEntity(dados);
        contrato.setAluno(resolverAluno(dados.idAluno()));
        contrato.setEmpresa(resolverEmpresa(dados.idEmpresa()));
        contrato.setStatus(StatusContrato.EM_ANALISE);
        return contratoRepository.save(contrato);
    }

    @Transactional
    public Contrato atualizar(DadosAtualizacaoContrato dados) {
        Contrato existente = buscarPorId(dados.id());
        validarPeriodo(dados.dataInicio(), dados.dataFim());
        validarContratoVigenteUnico(existente.getAluno().getId(), dados.status(), existente.getId());

        contratoMapper.atualizar(dados, existente);
        return contratoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        contratoRepository.delete(buscarPorId(id));
    }

    private Aluno resolverAluno(Long alunoId) {
        return alunoRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + alunoId));
    }

    private Empresa resolverEmpresa(Long empresaId) {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + empresaId));
    }

    private void validarPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (!dataFim.isAfter(dataInicio)) {
            throw new IllegalStateException("A data de término deve ser posterior à data de início.");
        }
    }

    private void validarContratoVigenteUnico(Long alunoId, StatusContrato status, Long contratoId) {
        if (!STATUS_VIGENTES.contains(status)) {
            return;
        }
        boolean duplicado = (contratoId == null) ? contratoRepository.existsByAlunoIdAndStatusIn(alunoId, STATUS_VIGENTES) : contratoRepository.existsByAlunoIdAndStatusInAndIdNot(alunoId, STATUS_VIGENTES, contratoId);

        if (duplicado) {
            throw new IllegalStateException("Este aluno já possui um contrato em análise ou ativo.");
        }
    }

    private String vazioParaNull(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }

}
