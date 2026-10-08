package com.conselho.pedido_service.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(String message) {
        super(message);
    }

    public ProdutoNaoEncontradoException() {
        super("Produto não encontrado");
    }
}
