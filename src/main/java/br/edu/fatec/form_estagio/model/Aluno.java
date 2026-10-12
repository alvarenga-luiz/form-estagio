package br.edu.fatec.form_estagio.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.edu.fatec.form_estagio.enums.TipoEnsino;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "aluno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="id")
public class Aluno implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ra", nullable = false)
	private String ra;

	@Column(name = "nome", nullable = false)
	private String nome;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "data_ingresso", nullable = false)
	private LocalDate dataIngresso;

	@Column(name = "data_nascimento", nullable = false)
	private LocalDate dataNascimento;

	@Column(name = "cep", nullable = false)
	private String cep;

	@Column(name = "contato", nullable = false)
	private String contato;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_ensino", nullable = false)
	private TipoEnsino tipoEnsino /*se é técnico ou regular luiz*/;
		
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "curso_id", nullable = false)
	private Curso curso;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "escola_id", nullable = false)
	private Escola escola;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}




