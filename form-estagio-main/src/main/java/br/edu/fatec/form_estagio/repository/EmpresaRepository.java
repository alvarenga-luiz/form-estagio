package br.edu.fatec.form_estagio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.fatec.form_estagio.model.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    @Query("SELECT e FROM Empresa e WHERE e.razaoSocial = :termo OR LOWER(e.razaoSocial) LIKE LOWER(CONCAT('%', :termo, '%')) ORDER BY e.razaoSocial") 
    List<Empresa> buscar(@Param("termo") String termo);
    
    boolean existsByRazaoSocial(String razaoSocial);
    boolean existsByRazaoSocialAndIdNot(String razaoSocial, Long id);
}