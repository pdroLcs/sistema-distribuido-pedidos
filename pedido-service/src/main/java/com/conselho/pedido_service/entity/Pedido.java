package com.conselho.pedido_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "pedidos")
@Getter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(nullable = false)
    private int quantidade;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    public Pedido(String nome, int quantidade, BigDecimal valorTotal) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do pedido não pode ser vazio");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade do pedido deve ser maior que zero");
        }
        if (valorTotal == null || valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor total do pedido deve ser maior que zero");
        }
        this.nome = nome;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }
}
