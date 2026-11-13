package com.conselho.notificacao_service.service;

import com.conselho.notificacao_service.dto.PedidoCriadoEvent;
import com.conselho.notificacao_service.entity.Notificacao;
import com.conselho.notificacao_service.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    @Transactional
    public void registrar(PedidoCriadoEvent evento) {
        if (notificacaoRepository.existsByPedidoId(evento.id())) {
            log.warn("Notificação do pedido {} já existe. Mensagem duplicada ignorada.", evento.id());
            return;
        }

        var mensagem = "Pedido %d criado: %d x %s, total R$ %s"
                .formatted(evento.id(), evento.quantidade(), evento.nome(), evento.valorTotal());

        notificacaoRepository.save(new Notificacao(evento.id(), mensagem));
        log.info("Notificação registrada para o pedido {}", evento.id());
    }

    public List<Notificacao> listar() {
        return notificacaoRepository.findAll();
    }
}