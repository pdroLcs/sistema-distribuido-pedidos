package com.conselho.pedido_service.service;

import com.conselho.pedido_service.dto.PedidoRequest;
import com.conselho.pedido_service.entity.Pedido;
import com.conselho.pedido_service.exception.ProdutoNaoEncontradoException;
import com.conselho.pedido_service.repository.PedidoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Nested
    class ListarPedidos {

        @Test
        @DisplayName("Deve retornar a lista de pedidos do repositório")
        void deveRetornarPedidosDoRepositorio() {
            var pedidos = List.of(new Pedido(1L, "Pedido 1", 2, BigDecimal.valueOf(50)));
            when(pedidoRepository.findAll()).thenReturn(pedidos);

            var resultado = pedidoService.listarPedidos();

            assertSame(pedidos, resultado);
            verify(pedidoRepository).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver pedidos")
        void deveRetornarListaVaziaQuandoNaoHouverPedidos() {
            when(pedidoRepository.findAll()).thenReturn(List.of());

            var resultado = pedidoService.listarPedidos();

            assertEquals(List.of(), resultado);
            verify(pedidoRepository).findAll();
        }
    }

    @Nested
    class BuscarPedidoPorId {

        @Test
        @DisplayName("Deve retornar pedido quando encontrado")
        void deveRetornarPedidoQuandoEncontrado() {
            var pedido = new Pedido(1L, "Pedido", 2, BigDecimal.valueOf(50));
            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

            var resultado = pedidoService.buscarPedidoPorId(1L);

            assertSame(pedido, resultado);
            verify(pedidoRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando pedido não for encontrado")
        void deveLancarExcecaoQuandoPedidoNaoForEncontrado() {
            when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

            var excecao = assertThrows(
                    ProdutoNaoEncontradoException.class,
                    () -> pedidoService.buscarPedidoPorId(1L)
            );

            assertEquals("Produto não encontrado", excecao.getMessage());
            verify(pedidoRepository).findById(1L);
        }
    }

    @Nested
    class CriarPedido {

        @Test
        @DisplayName("Deve criar pedido e armazená-lo no repositório")
        void deveCriarPedidoEArmazenaLoNoRepositorio() {
            var request = new PedidoRequest("Pedido", 3, 42.50);
            when(pedidoRepository.save(any(Pedido.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            var resultado = pedidoService.criarPedido(request);

            assertEquals("Pedido", resultado.getNome());
            assertEquals(3, resultado.getQuantidade());
            assertEquals(BigDecimal.valueOf(42.50), resultado.getValorTotal());
            verify(pedidoRepository).save(any(Pedido.class));
        }

        @ParameterizedTest
        @DisplayName("Deve rejeitar dados inválidos")
        @MethodSource("requestsInvalidos")
        void deveRejeitarDadosInvalidos(PedidoRequest request) {
            assertThrows(IllegalArgumentException.class, () -> pedidoService.criarPedido(request));

            verifyNoInteractions(pedidoRepository);
        }

        private static Stream<PedidoRequest> requestsInvalidos() {
            return Stream.of(
                    new PedidoRequest(" ", 1, 10.0),
                    new PedidoRequest("Pedido", 0, 10.0),
                    new PedidoRequest("Pedido", 1, 0.0)
            );
        }
    }
}