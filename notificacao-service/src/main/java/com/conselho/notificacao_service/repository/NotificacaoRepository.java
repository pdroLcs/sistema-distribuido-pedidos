package com.conselho.notificacao_service.repository;

import com.conselho.notificacao_service.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    boolean existsByPedidoId(Long pedidoId);
}