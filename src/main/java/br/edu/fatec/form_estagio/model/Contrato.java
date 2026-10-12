package br.edu.fatec.form_estagio.model;


import br.edu.fatec.form_estagio.enums.StatusContrato;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contrato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "carga_horaria", nullable = false)
    private int cargaHoraria;

    @Column(name = "area_estagio", nullable = false)
    private String areaEstagio;

    @Column(name = "descricao_funcao", columnDefinition = "TEXT")
    private String descFuncao;

    @Column(name = "supervisor", nullable = false)
    private String supervisor;

    @Column(name = "cargo_supervisor", nullable = false)
    private String cargoSupervisor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusContrato status = StatusContrato.EM_ANALISE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
