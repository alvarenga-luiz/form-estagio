package br.edu.fatec.form_estagio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/aluno")
@Tag(name = "Alunos", description = "API para gerenciamento de alunos")
public class AlunoController {
	
	@Autowired
	private AlunoRepository alunoRepository;
	
	@GetMapping ("/formulario/{id}")
	@Operation(summary = "Editar Aluno especifico")
	public String carregaPaginaFormulario (@PathVariable("id") Long id,
			HttpSession session,
			RedirectAttributes redirectAttributes,
			Model model) {
		try {
			Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
			model.addAllAttributes("aluno", aluno);
			return "aluno/formulario";
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			
			return "redirect:/aluno";
		}
	}

}
