package com.conselho.pedido_service.controller;

import com.conselho.pedido_service.dto.PedidoRequest;
import com.conselho.pedido_service.entity.Pedido;
import com.conselho.pedido_service.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedidoPorId(@PathVariable Long id) {
        var pedido = pedidoService.buscarPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody @Valid PedidoRequest request) {
        var novoPedido = pedidoService.criarPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

}
