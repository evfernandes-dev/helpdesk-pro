package com.example.helpdesk.controller;

import com.example.helpdesk.model.Chamado;
import com.example.helpdesk.model.StatusChamado;
import com.example.helpdesk.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @GetMapping
    public ResponseEntity<List<Chamado>> listarTodos() {
        return ResponseEntity.ok(chamadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chamado> buscarPorId(@PathVariable Long id) {
        return chamadoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Chamado>> buscarPorStatus(@PathVariable StatusChamado status) {
        List<Chamado> chamados = chamadoService.buscarPorStatus(status);
        return ResponseEntity.ok(chamados);
    }

    @PostMapping
    public ResponseEntity<Chamado> criar(@Valid @RequestBody Chamado chamado) {
        Chamado novoChamado = chamadoService.criar(chamado);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoChamado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chamado> atualizar(@PathVariable Long id, @Valid @RequestBody Chamado chamadoAtualizado) {
        try {
            Chamado chamadoSalvo = chamadoService.atualizar(id, chamadoAtualizado);
            return ResponseEntity.ok(chamadoSalvo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            chamadoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
