package br.edu.fatec.form_estagio.controller;

import java.util.List;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoEscola;
import br.edu.fatec.form_estagio.dto.DadosCadastroEscola;
import br.edu.fatec.form_estagio.dto.DadosListagemEscola;
import br.edu.fatec.form_estagio.model.Escola;
import br.edu.fatec.form_estagio.service.EscolaService;
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
@RequestMapping("/escola")
@Tag(name = "Escolas", description = "Gerenciamento de escolas")
@RequiredArgsConstructor
public class EscolaController {

	private final EscolaService escolaService;

	@GetMapping
	@Operation(summary = "Carrega página de listagem")
	public String carregaPaginaListagem(@RequestParam(required = false) String keyword, Model model) {
		model.addAttribute("lista", escolaService.listar(keyword));
		model.addAttribute("keyword", keyword);
		return "escola/listagem";
	}

	@GetMapping("/formulario")
	@Operation(summary = "Nova escola / buscar escola")
	public String novaEscola(@RequestParam(required = false) String termo, Model model) {
		model.addAttribute("termo", termo);

		if (termo != null && !termo.isBlank()) {
			List<DadosListagemEscola> resultados = escolaService.listar(termo);
			if (resultados.size() == 1) {
				model.addAttribute("escola", escolaService.buscarParaEdicao(resultados.get(0).id()));
				return paginaFormulario(model, true);
			} else if (resultados.isEmpty()) {
				model.addAttribute("error", "Nenhuma escola encontrada para '" + termo + "'.");
			} else {
				model.addAttribute("resultados", resultados);
			}
		}

		model.addAttribute("escola", DadosCadastroEscola.vazio());
		return paginaFormulario(model, false);
	}

	@GetMapping("/formulario/{id}")
	@Operation(summary = "Editar escola específica")
	public String carregaPaginaFormulario(@PathVariable Long id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("escola", escolaService.buscarParaEdicao(id));
			return paginaFormulario(model, true);
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/escola";
		}
	}

	@PostMapping("/salvar")
	@Operation(summary = "Salvar nova escola")
	public String salvarEscola(@Valid @ModelAttribute("escola") DadosCadastroEscola dados, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (!result.hasErrors()) {
			try {
				Escola salvo = escolaService.criar(dados);
				redirectAttributes.addFlashAttribute("message", "Escola '" + salvo.getNome() + "' cadastrada com sucesso!");
				return "redirect:/escola";
			} catch (IllegalStateException e) {
				result.rejectValue("nome", "nome.duplicado", e.getMessage());
			} catch (Exception e) {
				model.addAttribute("error", "Erro ao salvar escola: " + e.getMessage());
			}
		}
		return paginaFormulario(model, false);
	}

	@PostMapping("/atualizar")
	@Operation(summary = "Atualizar escola existente")
	public String atualizarEscola(@Valid @ModelAttribute("escola") DadosAtualizacaoEscola dados, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (!result.hasErrors()) {
			try {
				Escola salvo = escolaService.atualizar(dados);
				redirectAttributes.addFlashAttribute("message", "Escola '" + salvo.getNome() + "' atualizada com sucesso!");
				return "redirect:/escola";
			} catch (IllegalStateException e) {
				result.rejectValue("nome", "nome.duplicado", e.getMessage());
			} catch (Exception e) {
				model.addAttribute("error", "Erro ao atualizar escola: " + e.getMessage());
			}
		}
		return paginaFormulario(model, true);
	}

	@PostMapping("/delete/{id}")
	@Operation(summary = "Excluir escola")
	public String deleteEscola(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			escolaService.deletar(id);
			redirectAttributes.addFlashAttribute("message", "A escola " + id + " foi apagada!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/escola";
	}

	private String paginaFormulario(Model model, boolean edicao) {
		model.addAttribute("edicao", edicao);
		return "escola/formulario";
	}
}
