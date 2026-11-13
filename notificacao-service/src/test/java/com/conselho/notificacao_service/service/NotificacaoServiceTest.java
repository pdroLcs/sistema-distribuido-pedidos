package com.conselho.notificacao_service.service;

import com.conselho.notificacao_service.dto.PedidoCriadoEvent;
import com.conselho.notificacao_service.entity.Notificacao;
import com.conselho.notificacao_service.repository.NotificacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private NotificacaoService notificacaoService;

    private final PedidoCriadoEvent evento =
            new PedidoCriadoEvent(42L, "Teclado", 2, new BigDecimal("300.00"));

    @Test
    @DisplayName("Deve salvar uma notificação para um pedido novo")
    void deveSalvarNotificacaoParaPedidoNovo() {
        when(notificacaoRepository.existsByPedidoId(42L)).thenReturn(false);

        notificacaoService.registrar(evento);

        var captor = ArgumentCaptor.forClass(Notificacao.class);
        verify(notificacaoRepository).save(captor.capture());
        assertEquals(42L, captor.getValue().getPedidoId());
        assertTrue(captor.getValue().getMensagem().contains("Teclado"));
    }

    @Test
    @DisplayName("Deve ignorar mensagem duplicada do mesmo pedido")
    void deveIgnorarMensagemDuplicada() {
        when(notificacaoRepository.existsByPedidoId(42L)).thenReturn(true);

        notificacaoService.registrar(evento);

        verify(notificacaoRepository, never()).save(any());
    }
}
