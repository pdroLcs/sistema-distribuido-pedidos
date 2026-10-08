package com.conselho.pedido_service.service;

import com.conselho.pedido_service.dto.PedidoRequest;
import com.conselho.pedido_service.entity.Pedido;
import com.conselho.pedido_service.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com o ID: " + id));
    }

    public Pedido criarPedido(PedidoRequest request) {
        var pedido = new Pedido();
        pedido.setNome(request.nome());
        pedido.setQuantidade(request.quantidade());
        pedido.setValorTotal(BigDecimal.valueOf(request.valorTotal()));

        return pedidoRepository.save(pedido);
    }

}
