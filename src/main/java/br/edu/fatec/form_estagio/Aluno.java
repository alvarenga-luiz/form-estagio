package br.edu.fatec.form_estagio;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "aluno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="id")
public class Aluno {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String email;
	private String curso;
	private int semestre;
	private int periodo;
	private String contato;
	
	private List<Aluno> alunos;
	
	public Aluno(DadosCadastroAluno dados) {
		this.nome = dados.nome();
		this.contato = dados.contato();
		this.email = dados.email();
		this.curso = dados.curso();
		this.periodo = dados.periodo();
		this.semestre = dados.semestre();

	}
	
	public Aluno(String nome ) {
		this.nome = nome;
	}
	
	public void atualizarInformacoes(DadosAtualizacaoAluno dados) {
		if (dados.nome() != null)
			this.nome = dados.nome();
		if (dados.contato() != null)
			this.contato = dados.contato();
		if (dados.email() != null)
			this.email = dados.email();
		if (dados.curso() != null)
			this.curso = dados.curso();	
		if (dados.periodo() != 0)
			this.periodo = dados.periodo();
		if (dados.semestre() != 0)
			this.semestre = dados.semestre();
	}
}




