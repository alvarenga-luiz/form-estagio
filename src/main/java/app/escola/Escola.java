package app.escola;

import java.util.ArrayList;
import java.util.List;

import app.aluno.Aluno;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
	private long id;	
	private String nome;
	private int cep;
	private String rua;
	private String numero;
	private String tipoEnsino /*se é técnico ou regular luiz*/;
	
	@OneToMany(mappedBy = "escola", fetch = FetchType.LAZY)
	private List<Aluno> alunos = new ArrayList<>();
}

