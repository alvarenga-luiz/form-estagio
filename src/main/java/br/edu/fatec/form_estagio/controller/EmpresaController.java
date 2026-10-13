package br.edu.fatec.form_estagio.controller;

import java.util.List;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoEmpresa;
import br.edu.fatec.form_estagio.dto.DadosCadastroEmpresa;
import br.edu.fatec.form_estagio.dto.DadosListagemEmpresa;
import br.edu.fatec.form_estagio.model.Empresa;
import br.edu.fatec.form_estagio.service.EmpresaService;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/empresa")
@Tag(name = "Empresas", description = "Gerenciamento de empresas")
@RequiredArgsConstructor
public class EmpresaController {

	private final EmpresaService empresaService;

	@GetMapping
	@Operation(summary = "Carrega página de listagem")
	public String carregaPaginaListagem(@RequestParam(required = false) String keyword, Model model) {
		model.addAttribute("lista", empresaService.listar(keyword));
		model.addAttribute("keyword", keyword);
		return "empresa/listagem";
	}

	@GetMapping("/formulario")
	@Operation(summary = "Nova empresa / buscar empresa")
	public String novaEmpresa(@RequestParam(required = false) String termo, Model model) {
		model.addAttribute("termo", termo);

		if (termo != null && !termo.isBlank()) {
			List<DadosListagemEmpresa> resultados = empresaService.listar(termo);
			if (resultados.size() == 1) {
				model.addAttribute("empresa", empresaService.buscarParaEdicao(resultados.get(0).id()));
				return paginaFormulario(model, true);
			} else if (resultados.isEmpty()) {
				model.addAttribute("error", "Nenhuma empresa encontrada para '" + termo + "'.");
			} else {
				model.addAttribute("resultados", resultados);
			}
		}

		model.addAttribute("empresa", DadosCadastroEmpresa.vazio());
		return paginaFormulario(model, false);
	}

	@GetMapping("/formulario/{id}")
	@Operation(summary = "Editar empresa específica")
	public String carregaPaginaFormulario(@PathVariable Long id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("empresa", empresaService.buscarParaEdicao(id));
			return paginaFormulario(model, true);
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/empresa";
		}
	}

	@PostMapping("/salvar")
	@Operation(summary = "Salvar nova empresa")
	public String salvarEmpresa(@Valid @ModelAttribute("empresa") DadosCadastroEmpresa dados, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (!result.hasErrors()) {
			try {
				Empresa salvo = empresaService.criar(dados);
				redirectAttributes.addFlashAttribute("message", "Empresa '" + salvo.getRazaoSocial() + "' cadastrada com sucesso!");
				return "redirect:/empresa";
			} catch (IllegalStateException e) {
				result.rejectValue("razaoSocial", "razaoSocial.duplicado", e.getMessage());
			} catch (Exception e) {
				model.addAttribute("error", "Erro ao salvar empresa: " + e.getMessage());
			}
		}
		return paginaFormulario(model, false);
	}

	@PostMapping("/atualizar")
	@Operation(summary = "Atualizar empresa existente")
	public String atualizarEmpresa(@Valid @ModelAttribute("empresa") DadosAtualizacaoEmpresa dados, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (!result.hasErrors()) {
			try {
				Empresa salvo = empresaService.atualizar(dados);
				redirectAttributes.addFlashAttribute("message", "Empresa '" + salvo.getRazaoSocial() + "' atualizada com sucesso!");
				return "redirect:/empresa";
			} catch (IllegalStateException e) {
				result.rejectValue("razaoSocial", "razaoSocial.duplicado", e.getMessage());
			} catch (Exception e) {
				model.addAttribute("error", "Erro ao atualizar empresa: " + e.getMessage());
			}
		}
		return paginaFormulario(model, true);
	}

	@PostMapping("/delete/{id}")
	@Operation(summary = "Excluir empresa")
	public String deleteEmpresa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			empresaService.deletar(id);
			redirectAttributes.addFlashAttribute("message", "A empresa " + id + " foi apagada!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/empresa";
	}

	private String paginaFormulario(Model model, boolean edicao) {
		model.addAttribute("edicao", edicao);
		return "empresa/formulario";
	}
}
