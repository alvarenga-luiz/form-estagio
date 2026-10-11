package app.escola;

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
@RequestMapping("/escola")
@Tag(name = "Escolas", description = "Gerenciamento de escolas")
@RequiredArgsConstructor
public class EscolaController {

	private final EscolaService EscolaService;
	
	@GetMapping
	@Operation(summary = "Carrega página de listagem")
	public String carregaPaginaListagem(@RequestParam(required = false) String keyword, Model model) {
		model.addAttribute("lista", EscolaService.listar(keyword));
		model.addAttribute("keyword", keyword);
		return "escola/listagem";
	}
	
	@GetMapping("/formulario")
	@Operation(summary = "Nova Escola / buscar Escola")
	public String novaEscola(@RequestParam(required = false) String termo, Model model) {
		Escola escola = new Escola();

		if (termo != null && !termo.isBlank()) {
			List<DadosListagemEscola> resultados = escolaService.listar(termo);
			if (resultados.size() == 1) {
				escola = escolaService.buscarPorId(resultados.get(0).id());
			} else if (resultados.isEmpty()) {
				model.addAttribute("error", "Nenhuma escola encontrada para '" + termo + "'.");
			} else {
				model.addAttribute("resultados", resultados);
			}
		}

		model.addAttribute("escola", escola);
		model.addAttribute("termo", termo);
		return "escola/formulario";
	}
	
	@GetMapping("/formulario/{id}")
	@Operation(summary = "Editar escola específica")
	public String carregaPaginaFormulario(@PathVariable Long id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("escola", escolaService.buscarPorId(id));
			return "escola/formulario";
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/escola";
		}
	}

	@PostMapping("/salvar")
	@Operation(summary = "Salvar nova escola")
	public String salvarEscola(@Valid @ModelAttribute("escola") Escola escola, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		return gravar(escola, result, model, redirectAttributes, false);
	}

	@PostMapping("/atualizar")
	@Operation(summary = "Atualizar escola existente")
	public String atualizarEscola(@Valid @ModelAttribute("escola") Escola escola, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (escola.getId() == null) {
			result.reject("escola.semId", "Busque uma escola antes de atualizar.");
		}
		return gravar(escola, result, model, redirectAttributes, true);
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

	private String gravar(Escola escola, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, boolean atualizando) {

		if (!result.hasErrors()) {
			try {
				Escola salvo = atualizando ? escolaService.atualizar(escola) : escolaService.criar(escola);
				redirectAttributes.addFlashAttribute("message", "Escola '" + salvo.getNome()
						+ (atualizando ? "' atualizado" : "' criado") + " com sucesso!");
				return "redirect:/escola";
			} catch (IllegalStateException e) { 
				result.rejectValue("nome", "nome.duplicado", e.getMessage());
			} catch (Exception e) {
				model.addAttribute("error", "Erro ao salvar escola: " + e.getMessage());
			}
		}
		return "escola/formulario"; 
	}

}
