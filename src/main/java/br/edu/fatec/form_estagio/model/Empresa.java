package br.edu.fatec.form_estagio.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "area_atuacao")
    private String areaAtuacao;

    private String endereco;
    private String cidade;
    private String cep;
    private String email;
    private String site;
    private String telefone;

    @Column(name = "nome_contato")
    private String nomeContato;

    @Column(name = "cargo_contrato")
    private String cargoContrato;

    @Column(name = "area_departamento")
    private String areaDepartamento;
    
    private String linkedin;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
