package com.conselho.pedido_service.service;

import com.conselho.pedido_service.dto.PedidoCriadoEvent;
import com.conselho.pedido_service.dto.PedidoRequest;
import com.conselho.pedido_service.entity.Pedido;
import com.conselho.pedido_service.exception.ProdutoNaoEncontradoException;
import com.conselho.pedido_service.rabbitmq.PedidoEventPublisher;
import com.conselho.pedido_service.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoEventPublisher pedidoEventPublisher;

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(ProdutoNaoEncontradoException::new);
    }

    @Transactional
    public Pedido criarPedido(PedidoRequest request) {
        var pedido = new Pedido(request.nome(), request.quantidade(), BigDecimal.valueOf(request.valorTotal()));
        pedido = pedidoRepository.save(pedido);

        PedidoCriadoEvent event = new PedidoCriadoEvent(
                pedido.getId(),
                pedido.getNome(),
                pedido.getQuantidade(),
                pedido.getValorTotal()
        );
        pedidoEventPublisher.publicarPedidoCriado(event);

        return pedido;

//        return pedidoRepository.save(pedido);
    }

}
