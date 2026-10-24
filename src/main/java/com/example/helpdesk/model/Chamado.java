package com.example.helpdesk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity // Diz pro JPA que isso vira uma tabela
@Table(name = "chamados")
public class Chamado {

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera o id sozinho
    private Long id;

    @NotBlank(message = "O título não pode estar em branco")
    @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
    @Column(nullable = false, length = 255)
    private String titulo;

    @NotBlank(message = "A descrição não pode estar em branco")
    @Column(nullable = false, columnDefinition = "TEXT") // Usa TEXT pra descrição maior
    private String descricao;

    @Enumerated(EnumType.STRING) // Salva o nome do enum no banco, não o número
    @Column(nullable = false)
    private StatusChamado status;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column
    private LocalDateTime dataFechamento;

    public Chamado() {
        // Já marca a data assim que cria o objeto
        this.dataCriacao = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public StatusChamado getStatus() { return status; }
    public void setStatus(StatusChamado status) { this.status = status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public LocalDateTime getDataFechamento() { return dataFechamento; }
    public void setDataFechamento(LocalDateTime dataFechamento) { this.dataFechamento = dataFechamento; }
}
