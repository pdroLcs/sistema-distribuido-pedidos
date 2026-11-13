package com.conselho.notificacao_service.rabbitmq;

import com.conselho.notificacao_service.config.RabbitMQConfig;
import com.conselho.notificacao_service.dto.PedidoCriadoEvent;
import com.conselho.notificacao_service.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoCriadoListener {

    private final NotificacaoService notificacaoService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receber(PedidoCriadoEvent evento) {
        log.info("Evento pedido.criado recebido: {}", evento);
        notificacaoService.registrar(evento);
    }
}
