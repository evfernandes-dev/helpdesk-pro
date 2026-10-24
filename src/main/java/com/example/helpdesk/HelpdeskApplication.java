package com.example.helpdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Ponto de partida da aplicação Spring Boot
@SpringBootApplication
public class HelpdeskApplication {
    public static void main(String[] args) {
        // Sobe tudo: servidor, rotas, banco etc
        SpringApplication.run(HelpdeskApplication.class, args);
    }
}