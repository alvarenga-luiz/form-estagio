package br.edu.fatec.form_estagio.controller;

import br.edu.fatec.form_estagio.repository.ContratoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contrato")
@Tag(name = "Contrato", description = "Gerenciamento de contratos")
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoRepository contratoRepository;
}
