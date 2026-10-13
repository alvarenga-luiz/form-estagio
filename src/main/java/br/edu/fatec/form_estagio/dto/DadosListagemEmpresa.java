package br.edu.fatec.form_estagio.dto;

import br.edu.fatec.form_estagio.model.Empresa;

public record DadosListagemEmpresa(
    Long id,
    String razaoSocial,
    String cidade,
    String email,
    String telefone,
    String nomeContato
) {
    public DadosListagemEmpresa(Empresa empresa) {
        this(
            empresa.getId(),
            empresa.getRazaoSocial(),
            empresa.getCidade(),
            empresa.getEmail(),
            empresa.getTelefone(),
            empresa.getNomeContato()
        );
    }
}