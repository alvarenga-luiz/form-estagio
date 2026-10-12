package br.edu.fatec.form_estagio.dto;

import br.edu.fatec.form_estagio.enums.TipoEnsino;
import br.edu.fatec.form_estagio.model.Escola;

public record DadosListagemEscola(
        Long id,
        String nome,
        String cep,
        String rua,
        String numero) {

    public DadosListagemEscola(Escola escola) {
        this(escola.getId(), escola.getNome(), escola.getCep(), escola.getRua(), escola.getNumero());
    }
}
