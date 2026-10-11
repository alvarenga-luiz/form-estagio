package app.curso;

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
		Curso curso = new Curso();

		if (termo != null && !termo.isBlank()) {
			List<DadosListagemCurso> resultados = cursoService.listar(termo);
			if (resultados.size() == 1) {
				curso = cursoService.buscarPorId(resultados.get(0).id());
			} else if (resultados.isEmpty()) {
				model.addAttribute("error", "Nenhum curso encontrado para '" + termo + "'.");
			} else {
				model.addAttribute("resultados", resultados);
			}
		}

		model.addAttribute("curso", curso);
		model.addAttribute("termo", termo);
		return "curso/formulario";
	}

	@GetMapping("/formulario/{id}")
	@Operation(summary = "Editar curso específico")
	public String carregaPaginaFormulario(@PathVariable Long id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("curso", cursoService.buscarPorId(id));
			return "curso/formulario";
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/curso";
		}
	}

	@PostMapping("/salvar")
	@Operation(summary = "Salvar novo curso")
	public String salvarCurso(@Valid @ModelAttribute("curso") Curso curso, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		return gravar(curso, result, model, redirectAttributes, false);
	}

	@PostMapping("/atualizar")
	@Operation(summary = "Atualizar curso existente")
	public String atualizarCurso(@Valid @ModelAttribute("curso") Curso curso, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (curso.getId() == null) {
			result.reject("curso.semId", "Busque um curso antes de atualizar.");
		}
		return gravar(curso, result, model, redirectAttributes, true);
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

	private String gravar(Curso curso, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, boolean atualizando) {

		if (!result.hasErrors()) {
			try {
				Curso salvo = atualizando ? cursoService.atualizar(curso) : cursoService.criar(curso);
				redirectAttributes.addFlashAttribute("message", "Curso '" + salvo.getNome()
						+ (atualizando ? "' atualizado" : "' criado") + " com sucesso!");
				return "redirect:/curso";
			} catch (IllegalStateException e) { 
				result.rejectValue("nome", "nome.duplicado", e.getMessage());
			} catch (Exception e) {
				model.addAttribute("error", "Erro ao salvar curso: " + e.getMessage());
			}
		}
		return "curso/formulario"; 
	}
}