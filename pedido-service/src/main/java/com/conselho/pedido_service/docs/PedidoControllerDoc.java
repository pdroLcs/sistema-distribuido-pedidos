package com.conselho.pedido_service.docs;

import com.conselho.pedido_service.dto.PedidoRequest;
import com.conselho.pedido_service.entity.Pedido;
import com.conselho.pedido_service.exception.ErroResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(
        name = "Pedidos",
        description = "Operações para listar, buscar e criar pedidos."
)
public interface PedidoControllerDoc {

    @Operation(
            summary = "Listar Pedidos",
            description = "Retorna uma lista de todos os pedidos cadastrados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de pedidos retornada com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Pedido.class))
                            )
                    )
            }
    )
    ResponseEntity<List<Pedido>> listarPedidos();

    @Operation(
            summary = "Buscar Pedido por ID",
            description = "Retorna os detalhes de um pedido específico com base no ID fornecido.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pedido encontrado com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Pedido.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pedido não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErroResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<Pedido> buscarPedidoPorId(
            @Parameter(
                    description = "ID do pedido a ser buscado.",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id);

    @Operation(
            summary = "Criar Pedidos",
            description = "Cria um novo pedido com os dados fornecidos.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Pedido criado com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Pedido.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos fornecidos.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErroResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<Pedido> criarPedido(
            @RequestBody
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do pedido a ser criado.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de pedido válido",
                                    value = """
                                            {
                                              "nome": "Teclado",
                                              "quantidade": 2,
                                              "valorTotal": 250.0
                                            }
                                            """,
                                    summary = "Exemplo de pedido válido"
                            )
                    )
            )
            PedidoRequest request);

}
