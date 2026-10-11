package br.edu.fatec.form_estagio.controller;

import java.util.List;

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

import br.edu.fatec.form_estagio.model.Empresa;
import br.edu.fatec.form_estagio.model.DadosListagemEmpresa;
import br.edu.fatec.form_estagio.service.EmpresaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/empresa")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping
    public String carregaPaginaListagem(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("lista", empresaService.listar(keyword));
        model.addAttribute("keyword", keyword);
        return "empresa/listagem";
    }

    @GetMapping("/formulario")
    public String novaEmpresa(@RequestParam(required = false) String termo, Model model) {
        Empresa empresa = new Empresa();

        if (termo != null && !termo.isBlank()) {
            List<DadosListagemEmpresa> resultados = empresaService.listar(termo);
            if (resultados.size() == 1) {
                empresa = empresaService.buscarPorId(resultados.get(0).id());
            } else if (resultados.isEmpty()) {
                model.addAttribute("error", "Nenhuma empresa encontrada para '" + termo + "'.");
            } else {
                model.addAttribute("resultados", resultados);
            }
        }

        model.addAttribute("empresa", empresa);
        model.addAttribute("termo", termo);
        return "empresa/formulario";
    }

    @GetMapping("/formulario/{id}")
    public String carregaPaginaFormulario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("empresa", empresaService.buscarPorId(id));
            return "empresa/formulario";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/empresa";
        }
    }

    @PostMapping("/salvar")
    public String salvarEmpresa(@Valid @ModelAttribute("empresa") Empresa empresa, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {
        return gravar(empresa, result, model, redirectAttributes, false);
    }

    @PostMapping("/atualizar")
    public String atualizarEmpresa(@Valid @ModelAttribute("empresa") Empresa empresa, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {
        if (empresa.getId() == null) {
            result.reject("empresa.semId", "Busque uma empresa antes de atualizar.");
        }
        return gravar(empresa, result, model, redirectAttributes, true);
    }

    @PostMapping("/delete/{id}")
    public String deleteEmpresa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            empresaService.deletar(id);
            redirectAttributes.addFlashAttribute("message", "A empresa " + id + " foi apagada!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/empresa";
    }

    private String gravar(Empresa empresa, BindingResult result, Model model,
            RedirectAttributes redirectAttributes, boolean atualizando) {

        if (!result.hasErrors()) {
            try {
                Empresa salvo = atualizando ? empresaService.atualizar(empresa) : empresaService.criar(empresa);
                redirectAttributes.addFlashAttribute("message", "Empresa '" + salvo.getRazaoSocial()
                        + (atualizando ? "' atualizada" : "' cadastrada") + " com sucesso!");
                return "redirect:/empresa";
            } catch (IllegalStateException e) {
                result.rejectValue("razaoSocial", "razaoSocial.duplicada", e.getMessage());
            } catch (Exception e) {
                model.addAttribute("error", "Erro ao salvar empresa: " + e.getMessage());
            }
        }
        return "empresa/formulario";
    }
}