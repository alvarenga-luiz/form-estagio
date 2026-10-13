package br.edu.fatec.form_estagio.controller;

import java.util.List;

import br.edu.fatec.form_estagio.dto.DadosAtualizacaoAluno;
import br.edu.fatec.form_estagio.dto.DadosCadastroAluno;
import br.edu.fatec.form_estagio.enums.TipoEnsino;
import br.edu.fatec.form_estagio.model.Aluno;
import br.edu.fatec.form_estagio.service.AlunoService;
import br.edu.fatec.form_estagio.dto.DadosListagemAluno;
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

import br.edu.fatec.form_estagio.service.CursoService;
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
	private final EscolaService escolaService;

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
		model.addAttribute("termo", termo);

		if (termo != null && !termo.isBlank()) {
			List<DadosListagemAluno> resultados = alunoService.listar(termo);
			if (resultados.size() == 1) {
				model.addAttribute("aluno", alunoService.buscarParaEdicao(resultados.get(0).id()));
				return paginaFormulario(model, true);
			} else if (resultados.isEmpty()) {
				model.addAttribute("error", "Nenhum aluno encontrado para '" + termo + "'.");
			} else {
				model.addAttribute("resultados", resultados);
			}
		}

		model.addAttribute("aluno", DadosCadastroAluno.vazio());
		return paginaFormulario(model, false);
	}

	@GetMapping("/formulario/{id}")
	@Operation(summary = "Editar aluno específico")
	public String carregaPaginaFormulario(@PathVariable Long id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("aluno", alunoService.buscarParaEdicao(id));
			return paginaFormulario(model, true);
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/aluno";
		}
	}

	@PostMapping("/salvar")
	@Operation(summary = "Salvar novo aluno")
	public String salvarAluno(@Valid @ModelAttribute("aluno") DadosCadastroAluno dados, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (!result.hasErrors()) {
			try {
				Aluno salvo = alunoService.criar(dados);
				redirectAttributes.addFlashAttribute("message", "Aluno '" + salvo.getNome() + "' criado com sucesso!");
				return "redirect:/aluno";
			} catch (IllegalStateException e) {
				result.rejectValue("ra", "ra.duplicado", e.getMessage());
			} catch (Exception e) {
				model.addAttribute("error", "Erro ao salvar aluno: " + e.getMessage());
			}
		}
		return paginaFormulario(model, false);
	}

	@PostMapping("/atualizar")
	@Operation(summary = "Atualizar aluno existente")
	public String atualizarAluno(@Valid @ModelAttribute("aluno") DadosAtualizacaoAluno dados, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (!result.hasErrors()) {
			try {
				Aluno salvo = alunoService.atualizar(dados);
				redirectAttributes.addFlashAttribute("message", "Aluno '" + salvo.getNome() + "' atualizado com sucesso!");
				return "redirect:/aluno";
			} catch (IllegalStateException e) {
				result.rejectValue("ra", "ra.duplicado", e.getMessage());
			} catch (Exception e) {
				model.addAttribute("error", "Erro ao atualizar aluno: " + e.getMessage());
			}
		}
		return paginaFormulario(model, true);
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

	// Carrega as opções dos selects (também quando o formulário volta com erro)
	private String paginaFormulario(Model model, boolean edicao) {
		model.addAttribute("edicao", edicao);
		model.addAttribute("cursos", cursoService.listar(null));
		model.addAttribute("escolas", escolaService.listar(null));
		model.addAttribute("tiposEnsino", TipoEnsino.values());
		return "aluno/formulario";
	}
}
