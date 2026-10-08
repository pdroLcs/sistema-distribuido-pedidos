package com.conselho.pedido_service.exception;

import java.time.Instant;
import java.util.List;

public record ErroResponse(
        int status,
        String mensagem,
        List<String> erros,
        Instant timestamp
) {
}
