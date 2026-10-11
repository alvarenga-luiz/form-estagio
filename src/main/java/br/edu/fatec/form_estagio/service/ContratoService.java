package br.edu.fatec.form_estagio.service;

import br.edu.fatec.form_estagio.model.Contrato;
import br.edu.fatec.form_estagio.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository contratoRepository;
//    private final AlunoRepository alunoRepository;

    public List<Contrato> getAllContrato () {
        return contratoRepository.findAll(Sort.by("id").ascending());
    }

//    public Contrato cadastrar (DadosCadastroContrato dados) {
//        Aluno aluno = alunoRepository.
//    }
}
