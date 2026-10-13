package br.edu.fatec.form_estagio.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "empresa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="id")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "area_atuacao", nullable = false)
    private String areaAtuacao;

    @Column(name = "endereco", nullable = false)
    private String endereco;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "cep", nullable = false)
    private String cep;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "site")
    private String site;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "nome_contato", nullable = false)
    private String nomeContato;

    @Column(name = "cargo_contato", nullable = false)
    private String cargoContato;

    @Column(name = "area_departamento", nullable = false)
    private String areaDepartamento;

    @Column(name = "linkedin")
    private String linkedin;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
