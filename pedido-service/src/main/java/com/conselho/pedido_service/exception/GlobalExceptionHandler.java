package com.conselho.pedido_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarProdutoNaoEncontradoException(ProdutoNaoEncontradoException ex) {
        var erroResponse = new ErroResponse(
                404,
                ex.getMessage(),
                List.of(ex.getMessage()),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        var erroResponse = new ErroResponse(
                400,
                "Erro de validação",
                erros,
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        var erroResponse = new ErroResponse(
                400,
                "Erro de leitura da mensagem",
                List.of(ex.getMessage()),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponse);
    }

}
