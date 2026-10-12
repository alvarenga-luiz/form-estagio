package br.edu.fatec.form_estagio.repository;


import br.edu.fatec.form_estagio.enums.StatusContrato;
import br.edu.fatec.form_estagio.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    @Query("""
        SELECT c FROM Contrato c JOIN c.aluno a JOIN c.empresa e
        WHERE (:ra IS NULL OR a.ra = :ra)
          AND (:razaoSocial IS NULL OR LOWER(e.razaoSocial) LIKE LOWER(CONCAT('%', :razaoSocial, '%')))
        """)
    List<Contrato> buscar(@Param("ra") String ra, @Param("razaoSocial") String razaoSocial);

    boolean existsByAlunoId(Long alunoId);

    boolean existsByEmpresaId(Long alunoId);

    boolean existsByAlunoIdAndStatusIn(Long alunoId, Collection<StatusContrato> status);

    boolean existsByAlunoIdAndStatusInAndIdNot(Long alunoId, Collection<StatusContrato> status, Long id);
}
