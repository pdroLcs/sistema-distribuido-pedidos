package com.conselho.pedido_service.dto;

import java.math.BigDecimal;

public record PedidoCriadoEvent(
        Long id,
        String nome,
        int quantidade,
        BigDecimal valorTotal
) {
}
