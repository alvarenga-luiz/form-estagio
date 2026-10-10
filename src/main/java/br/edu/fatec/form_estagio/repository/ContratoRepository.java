package br.edu.fatec.form_estagio.repository;


import br.edu.fatec.form_estagio.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    @Query ("SELECT c FROM Contrato c JOIN c.empresa e WHERE e.nome = :nomeEmpresa")
    List<Contrato> findByEmpresaName(@Param("nomeEmpresa") String nomeEmpresa);

    @Query ("SELECT c FROM Contrato c JOIN c.aluno a WHERE a.nome = :nomeAluno")
    List<Contrato> findByAlunoName(@Param("nomeAluno") String nomeAluno);

    @Query("Select c FROM Contrato c JOIN c.aluno a WHERE a.ra = :raAluno")
    List<Contrato> findByRa(@Param("raAluno") String raAluno);


}
