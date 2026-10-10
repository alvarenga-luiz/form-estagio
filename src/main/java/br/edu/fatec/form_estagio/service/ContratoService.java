package br.edu.fatec.form_estagio.service;

import br.edu.fatec.form_estagio.dto.DadosCadastroContrato;
import br.edu.fatec.form_estagio.dto.DadosListagemContrato;
import br.edu.fatec.form_estagio.model.Aluno;
import br.edu.fatec.form_estagio.model.Contrato;
import br.edu.fatec.form_estagio.repository.ContratoRepository;
import jakarta.transaction.Transactional;
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
