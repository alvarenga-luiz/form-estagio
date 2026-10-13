package br.edu.fatec.form_estagio.service;

import java.util.List;

import br.edu.fatec.form_estagio.repository.ContratoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.fatec.form_estagio.mapper.EmpresaMapper;
import br.edu.fatec.form_estagio.model.Empresa;
import br.edu.fatec.form_estagio.dto.DadosAtualizacaoEmpresa;
import br.edu.fatec.form_estagio.dto.DadosCadastroEmpresa;
import br.edu.fatec.form_estagio.dto.DadosListagemEmpresa;
import br.edu.fatec.form_estagio.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpresaService {
    
    private final EmpresaRepository empresaRepository;
    private final ContratoRepository contratoRepository;
    private final EmpresaMapper empresaMapper;
    
    @Transactional(readOnly = true)
    public List<DadosListagemEmpresa> listar(String termo) {
        List<Empresa> empresas = (termo == null || termo.isBlank()) ? empresaRepository.findAll(Sort.by("razaoSocial").ascending()) : empresaRepository.buscar(termo.trim());
        return empresas.stream().map(DadosListagemEmpresa::new).toList();
    }

    @Transactional(readOnly = true)
    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public DadosAtualizacaoEmpresa buscarParaEdicao(Long id) {
        return empresaMapper.toAtualizacao(buscarPorId(id));
    }

    @Transactional
    public Empresa criar(DadosCadastroEmpresa dados) {
        validarRazaoSocialUnica(dados.razaoSocial(), null);
        return empresaRepository.save(empresaMapper.toEntity(dados));
    }

    @Transactional
    public Empresa atualizar(DadosAtualizacaoEmpresa dados) {
        Empresa existente = buscarPorId(dados.id());
        validarRazaoSocialUnica(dados.razaoSocial(), dados.id());
        empresaMapper.atualizar(dados, existente);
        return empresaRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        if (contratoRepository.existsByEmpresaId(id)) {
            throw new IllegalStateException("Não foi possível excluir: a empresa possui contratos vinculados.");
        }
        empresaRepository.delete(buscarPorId(id));
    }

    private void validarRazaoSocialUnica(String razaoSocial, Long empresaId) {
        String razaoSocialLimpa = razaoSocial.trim();
        boolean duplicado = (empresaId == null) ? empresaRepository.existsByRazaoSocialIgnoreCase(razaoSocialLimpa) : empresaRepository.existsByRazaoSocialIgnoreCaseAndIdNot(razaoSocialLimpa, empresaId);
        
        if (duplicado) {
            throw new IllegalStateException("Já existe uma empresa cadastrada com esta Razão Social.");
        }
    }
}