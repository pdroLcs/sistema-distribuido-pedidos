package com.conselho.pedido_service.rabbitmq;

import com.conselho.pedido_service.dto.PedidoCriadoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarPedidoCriado(PedidoCriadoEvent event) {
        rabbitTemplate.convertAndSend(
                "pedido.exchange",
                "pedido.criado",
                event
        );
    }

}
