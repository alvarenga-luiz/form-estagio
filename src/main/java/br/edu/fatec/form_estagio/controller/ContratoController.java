package br.edu.fatec.form_estagio.controller;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoContrato;
import br.edu.fatec.form_estagio.dto.DadosCadastroContrato;
import br.edu.fatec.form_estagio.dto.DadosListagemContrato;
import br.edu.fatec.form_estagio.enums.StatusContrato;
import br.edu.fatec.form_estagio.model.Contrato;
import br.edu.fatec.form_estagio.service.AlunoService;
import br.edu.fatec.form_estagio.service.ContratoService;
import br.edu.fatec.form_estagio.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contrato")
@Tag(name = "Contrato", description = "Gerenciamento de contratos")
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoService contratoService;
    private final AlunoService alunoService;
    private final EmpresaService empresaService;

    @GetMapping
    @Operation(summary = "Carrega página de listagem")
    public String carregaPaginaListagem(@RequestParam(required = false) String ra,
            @RequestParam(required = false) String razaoSocial, Model model) {
        model.addAttribute("lista", contratoService.listar(ra, razaoSocial));
        model.addAttribute("ra", ra);
        model.addAttribute("razaoSocial", razaoSocial);
        return "contrato/listagem";
    }

    @GetMapping("/formulario")
    @Operation(summary = "Novo contrato")
    public String novoContrato(Model model) {
        model.addAttribute("contrato", DadosCadastroContrato.vazio());
        return paginaCadastro(model);
    }

    @GetMapping("/formulario/{id}")
    @Operation(summary = "Editar contrato específico")
    public String carregaPaginaFormulario(@PathVariable Long id, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("contrato", contratoService.buscarParaEdicao(id));
            return paginaEdicao(id, model);
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/contrato";
        }
    }

    @PostMapping("/salvar")
    @Operation(summary = "Salvar novo contrato")
    public String salvarContrato(@Valid @ModelAttribute("contrato") DadosCadastroContrato dados, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            try {
                Contrato salvo = contratoService.criar(dados);
                redirectAttributes.addFlashAttribute("message", mensagemSucesso(salvo, "cadastrado"));
                return "redirect:/contrato";
            } catch (IllegalStateException e) {
                result.reject("contrato.invalido", e.getMessage());
            } catch (Exception e) {
                model.addAttribute("error", "Erro ao salvar contrato: " + e.getMessage());
            }
        }
        return paginaCadastro(model);
    }

    @PostMapping("/atualizar")
    @Operation(summary = "Atualizar contrato existente")
    public String atualizarContrato(@Valid @ModelAttribute("contrato") DadosAtualizacaoContrato dados, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            try {
                Contrato salvo = contratoService.atualizar(dados);
                redirectAttributes.addFlashAttribute("message", mensagemSucesso(salvo, "atualizado"));
                return "redirect:/contrato";
            } catch (IllegalStateException e) {
                result.reject("contrato.invalido", e.getMessage());
            } catch (Exception e) {
                model.addAttribute("error", "Erro ao atualizar contrato: " + e.getMessage());
            }
        }
        return paginaEdicao(dados.id(), model);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "Excluir contrato")
    public String deleteContrato(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contratoService.deletar(id);
            redirectAttributes.addFlashAttribute("message", "O contrato " + id + " foi apagado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/contrato";
    }

    private String paginaCadastro(Model model) {
        model.addAttribute("edicao", false);
        model.addAttribute("alunos", alunoService.listar(null));
        model.addAttribute("empresas", empresaService.listar(null));
        return "contrato/formulario";
    }

    // Na edição aluno e empresa não mudam, então só exibimos os nomes
    private String paginaEdicao(Long id, Model model) {
        model.addAttribute("edicao", true);
        model.addAttribute("contratoAtual", new DadosListagemContrato(contratoService.buscarPorId(id)));
        model.addAttribute("statusOpcoes", StatusContrato.values());
        return "contrato/formulario";
    }

    private String mensagemSucesso(Contrato contrato, String acao) {
        return "Contrato de '" + contrato.getAluno().getNome() + "' com '"
                + contrato.getEmpresa().getRazaoSocial() + "' " + acao + " com sucesso!";
    }
}
