package br.edu.fatec.form_estagio.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.fatec.form_estagio.mapper.EmpresaMapper;
import br.edu.fatec.form_estagio.model.Empresa;
import br.edu.fatec.form_estagio.model.DadosListagemEmpresa;
import br.edu.fatec.form_estagio.repository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpresaService {
    
    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;
    
    @Transactional(readOnly = true)
    public List<DadosListagemEmpresa> listar(String termo) {
        List<Empresa> empresas = (termo == null || termo.isBlank()) ? empresaRepository.findAll() : empresaRepository.buscar(termo.trim());
        return empresas.stream().map(DadosListagemEmpresa::new).toList();
    }

    @Transactional(readOnly = true)
    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com ID: " + id));
    }

    @Transactional
    public Empresa criar(Empresa empresa) {
        empresa.setId(null);
        validarRazaoSocialUnica(empresa);
        return empresaRepository.save(empresa);
    }

    @Transactional
    public Empresa atualizar(Empresa form) {
        Empresa existente = buscarPorId(form.getId());
        validarRazaoSocialUnica(form);
        empresaMapper.atualizar(form, existente);
        return empresaRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        empresaRepository.delete(buscarPorId(id));
    }

    private void validarRazaoSocialUnica(Empresa empresa) {
        String razaoSocial = empresa.getRazaoSocial().trim();
        boolean duplicado = (empresa.getId() == null) ? empresaRepository.existsByRazaoSocial(razaoSocial) : empresaRepository.existsByRazaoSocialAndIdNot(razaoSocial, empresa.getId());
        
        if (duplicado) {
            throw new IllegalStateException("Já existe uma empresa cadastrada com esta Razão Social.");
        }
    }
}