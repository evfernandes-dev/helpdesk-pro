package com.example.helpdesk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> tratarResponseStatus(ResponseStatusException ex) {
        String mensagem = ex.getReason() != null ? ex.getReason() : "Erro na requisição";
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("erro", mensagem));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .orElse("Dados inválidos");
        return ResponseEntity.badRequest().body(Map.of("erro", mensagem));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> tratarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        String mensagem = "Valor inválido para '" + ex.getName() + "': " + ex.getValue();
        if ("status".equals(ex.getName())) {
            mensagem += ". Use ABERTO, EM_ANDAMENTO ou FECHADO";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", mensagem));
    }
}
