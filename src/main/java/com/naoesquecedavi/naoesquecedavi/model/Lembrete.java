package com.naoesquecedavi.naoesquecedavi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lembrete")
public class Lembrete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDENTE;
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
