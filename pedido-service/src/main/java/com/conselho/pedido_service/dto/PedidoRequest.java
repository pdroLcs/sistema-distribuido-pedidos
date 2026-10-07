package com.conselho.pedido_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PedidoRequest(

        @NotBlank(message = "O nome do pedido não pode ser vazio")
        @Size(max = 100, message = "O nome do pedido não pode ter mais de {max} caracteres")
        String nome,

        @NotNull(message = "A quantidade do pedido não pode ser nula")
        @Positive(message = "A quantidade do pedido deve ser maior que zero")
        int quantidade,

        @NotNull(message = "O valor total do pedido não pode ser nulo")
        @Positive(message = "O valor total do pedido deve ser maior que zero")
        double valorTotal
) {
}
