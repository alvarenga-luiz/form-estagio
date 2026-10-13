package br.edu.fatec.form_estagio.controller;

import java.util.List;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoCurso;
import br.edu.fatec.form_estagio.dto.DadosCadastroCurso;
import br.edu.fatec.form_estagio.dto.DadosListagemCurso;
import br.edu.fatec.form_estagio.model.Curso;
import br.edu.fatec.form_estagio.service.CursoService;
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
@RequestMapping("/curso")
@Tag(name = "Cursos", description = "Gerenciamento de cursos")
@RequiredArgsConstructor
public class CursoController {

	private final CursoService cursoService;

	@GetMapping
	@Operation(summary = "Carrega página de listagem")
	public String carregaPaginaListagem(@RequestParam(required = false) String keyword, Model model) {
		model.addAttribute("lista", cursoService.listar(keyword));
		model.addAttribute("keyword", keyword);
		return "curso/listagem";
	}

	@GetMapping("/formulario")
	@Operation(summary = "Novo curso / buscar curso")
	public String novoCurso(@RequestParam(required = false) String termo, Model model) {
		model.addAttribute("termo", termo);

		if (termo != null && !termo.isBlank()) {
			List<DadosListagemCurso> resultados = cursoService.listar(termo);
			if (resultados.size() == 1) {
				model.addAttribute("curso", cursoService.buscarParaEdicao(resultados.get(0).id()));
				return paginaFormulario(model, true);
			} else if (resultados.isEmpty()) {
				model.addAttribute("error", "Nenhum curso encontrado para '" + termo + "'.");
			} else {
				model.addAttribute("resultados", resultados);
			}
		}

		model.addAttribute("curso", DadosCadastroCurso.vazio());
		return paginaFormulario(model, false);
	}

	@GetMapping("/formulario/{id}")
	@Operation(summary = "Editar curso específico")
	public String carregaPaginaFormulario(@PathVariable Long id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("curso", cursoService.buscarParaEdicao(id));
			return paginaFormulario(model, true);
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/curso";
		}
	}

	@PostMapping("/salvar")
	@Operation(summary = "Salvar novo curso")
	public String salvarCurso(@Valid @ModelAttribute("curso") DadosCadastroCurso dados, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (!result.hasErrors()) {
			try {
				Curso salvo = cursoService.criar(dados);
				redirectAttributes.addFlashAttribute("message", "Curso '" + salvo.getNome() + "' cadastrado com sucesso!");
				return "redirect:/curso";
			} catch (IllegalStateException e) {
				result.rejectValue("nome", "nome.duplicado", e.getMessage());
			} catch (Exception e) {
				model.addAttribute("error", "Erro ao salvar curso: " + e.getMessage());
			}
		}
		return paginaFormulario(model, false);
	}

	@PostMapping("/atualizar")
	@Operation(summary = "Atualizar curso existente")
	public String atualizarCurso(@Valid @ModelAttribute("curso") DadosAtualizacaoCurso dados, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (!result.hasErrors()) {
			try {
				Curso salvo = cursoService.atualizar(dados);
				redirectAttributes.addFlashAttribute("message", "Curso '" + salvo.getNome() + "' atualizado com sucesso!");
				return "redirect:/curso";
			} catch (IllegalStateException e) {
				result.rejectValue("nome", "nome.duplicado", e.getMessage());
			} catch (Exception e) {
				model.addAttribute("error", "Erro ao atualizar curso: " + e.getMessage());
			}
		}
		return paginaFormulario(model, true);
	}

	@PostMapping("/delete/{id}")
	@Operation(summary = "Excluir curso")
	public String deleteCurso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			cursoService.deletar(id);
			redirectAttributes.addFlashAttribute("message", "O curso " + id + " foi apagado!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/curso";
	}

	private String paginaFormulario(Model model, boolean edicao) {
		model.addAttribute("edicao", edicao);
		return "curso/formulario";
	}
}
