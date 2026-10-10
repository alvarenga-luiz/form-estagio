package app.curso;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

	@Query("SELECT c FROM Curso c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(c.coordenador) LIKE LOWER(CONCAT('%', :termo, '%')) ORDER BY c.nome")
	List<Curso> buscar(@Param("termo") String termo);

	boolean existsByNomeIgnoreCase(String nome);

	boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
