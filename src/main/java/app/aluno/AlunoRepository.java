package app.aluno;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
	@Query("SELECT a FROM Aluno a JOIN FETCH a.curso ORDER BY a.nome")
	List<Aluno> findAllComCurso();	
	
	@Query("SELECT a FROM Aluno a JOIN FETCH a.curso WHERE a.ra = :termo OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :termo, '%'))ORDER BY a.nome ") List<Aluno> buscar(@Param("termo") String termo);

	boolean existsByRa(String ra);

	boolean existsByRaAndIdNot(String ra, Long id);
	long countByCursoId(Long cursoId);
}
