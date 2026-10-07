package com.conselho.pedido_service.repository;

import com.conselho.pedido_service.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
