package app.aluno;

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

import app.curso.Curso;
import app.curso.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/aluno")
@Tag(name = "Alunos", description = "Gerenciamento de alunos")
@RequiredArgsConstructor
public class AlunoController {

	private final AlunoService alunoService;
	private final CursoService cursoService; 

	@GetMapping
	@Operation(summary = "Carrega página de listagem")
	public String carregaPaginaListagem(@RequestParam(required = false) String keyword, Model model) {
		model.addAttribute("lista", alunoService.listar(keyword));
		model.addAttribute("keyword", keyword);
		return "aluno/listagem";
	}

	@GetMapping("/formulario")
	@Operation(summary = "Novo aluno / buscar aluno")
	public String novoAluno(@RequestParam(required = false) String termo, Model model) {
		Aluno aluno = alunoVazio();

		if (termo != null && !termo.isBlank()) {
			List<DadosListagemAluno> resultados = alunoService.listar(termo);
			if (resultados.size() == 1) {
				aluno = alunoService.buscarPorId(resultados.get(0).id()); 
			} else if (resultados.isEmpty()) {
				model.addAttribute("error", "Nenhum aluno encontrado para '" + termo + "'.");
			} else {
				model.addAttribute("resultados", resultados); 
			}
		}

		model.addAttribute("aluno", aluno);
		model.addAttribute("termo", termo);
		model.addAttribute("cursos", cursoService.listar(null));
		return "aluno/formulario";
	}

	@GetMapping("/formulario/{id}")
	@Operation(summary = "Editar aluno específico")
	public String carregaPaginaFormulario(@PathVariable Long id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("aluno", alunoService.buscarPorId(id));
			model.addAttribute("cursos", cursoService.listar(null));
			return "aluno/formulario";
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/aluno";
		}
	}

	@PostMapping("/salvar")
	@Operation(summary = "Salvar novo aluno")
	public String salvarAluno(@Valid @ModelAttribute("aluno") Aluno aluno, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		return gravar(aluno, result, model, redirectAttributes, false);
	}

	@PostMapping("/atualizar")
	@Operation(summary = "Atualizar aluno existente")
	public String atualizarAluno(@Valid @ModelAttribute("aluno") Aluno aluno, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (aluno.getId() == null) {
			result.reject("aluno.semId", "Busque um aluno antes de atualizar.");
		}
		return gravar(aluno, result, model, redirectAttributes, true);
	}

	@PostMapping("/delete/{id}")
	@Operation(summary = "Excluir aluno")
	public String deleteAluno(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			alunoService.deletar(id);
			redirectAttributes.addFlashAttribute("message", "O aluno " + id + " foi apagado!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/aluno";
	}

	private String gravar(Aluno aluno, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, boolean atualizando) {

		if (aluno.getCurso() == null || aluno.getCurso().getId() == null) {
			result.rejectValue("curso", "curso.obrigatorio", "Selecione um curso");
		}

		if (!result.hasErrors()) {
			try {
				Aluno salvo = atualizando ? alunoService.atualizar(aluno) : alunoService.criar(aluno);
				redirectAttributes.addFlashAttribute("message", "Aluno '" + salvo.getNome()
						+ (atualizando ? "' atualizado" : "' criado") + " com sucesso!");
				return "redirect:/aluno";
			} catch (IllegalStateException e) { 
				result.rejectValue("ra", "ra.duplicado", e.getMessage());
			} catch (Exception e) {
				model.addAttribute("error", "Erro ao salvar aluno: " + e.getMessage());
			}
		}
		model.addAttribute("cursos", cursoService.listar(null)); 
		return "aluno/formulario";
	}

	private Aluno alunoVazio() {
		Aluno aluno = new Aluno();
		aluno.setCurso(new Curso());
		return aluno;
	}
}