package br.edu.fatec.form_estagio.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "escola")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="id")
public class Escola {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome", nullable = false)
	private String nome;

	@Column(name = "cep", nullable = false)
	private String cep;

	@Column(name = "rua", nullable = false)
	private String rua;

	@Column(name = "numero", nullable = false)
	private String numero;

	@OneToMany(mappedBy = "escola", fetch = FetchType.LAZY)
	private List<Aluno> alunos = new ArrayList<>();
}

