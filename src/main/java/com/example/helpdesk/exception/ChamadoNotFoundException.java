package com.example.helpdesk.exception;

public class ChamadoNotFoundException extends RuntimeException {

    public ChamadoNotFoundException(Long id) {
        super("Chamado não encontrado com ID: " + id);
    }
}
