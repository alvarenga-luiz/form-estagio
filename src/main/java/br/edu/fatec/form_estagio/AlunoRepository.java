package br.edu.fatec.form_estagio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
	@Query("SELECT f FROM Aluno f JOIN FETCH f.estagio WHERE f.id = :id")
    List<Aluno> findByIdWithFilmes(@Param("id") Long id);
	
	@Query("SELECT f FROM Aluno f JOIN FETCH f.estagio WHERE f.id = :id")
    Optional<Aluno> findByAtores(@Param("id") Long id);
	
	boolean existsByImdbId (String imdbId);
	
	
}
