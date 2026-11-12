package com.conselho.notificacao_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
@Getter
@NoArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false, unique = true)
    private Long pedidoId;

    @Column(nullable = false)
    private String mensagem;

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm;

    public Notificacao(Long pedidoId, String mensagem) {
        this.pedidoId = pedidoId;
        this.mensagem = mensagem;
        this.criadaEm = LocalDateTime.now();
    }
}