package br.edu.fatec.form_estagio.repository;

import java.util.List;

import br.edu.fatec.form_estagio.model.Escola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {

    @Query("SELECT e FROM Escola e WHERE LOWER(e.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(e.tipoEnsino) LIKE LOWER(CONCAT('%', :termo, '%')) ORDER BY e.nome")
    List<Escola> buscar(@Param("termo") String termo);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
