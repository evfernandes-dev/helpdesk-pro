package com.example.helpdesk.service;

import com.example.helpdesk.model.Chamado;
import com.example.helpdesk.model.StatusChamado;
import com.example.helpdesk.repository.ChamadoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository) {
        this.chamadoRepository = chamadoRepository;
    }

    public List<Chamado> listarTodos() {
        return chamadoRepository.findAllByOrderByDataCriacaoDesc();
    }

    public Optional<Chamado> buscarPorId(Long id) {
        return chamadoRepository.findById(id);
    }

    public List<Chamado> buscarPorStatus(StatusChamado status) {
        return chamadoRepository.findByStatus(status);
    }

    public Chamado criar(Chamado chamado) {
        chamado.setId(null);
        chamado.setDataCriacao(LocalDateTime.now());
        if (chamado.getStatus() == null) {
            chamado.setStatus(StatusChamado.ABERTO);
        }
        return chamadoRepository.save(chamado);
    }

    public Chamado atualizar(Long id, Chamado chamadoAtualizado) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado com ID: " + id));

        chamado.setTitulo(chamadoAtualizado.getTitulo());
        chamado.setDescricao(chamadoAtualizado.getDescricao());
        chamado.setStatus(chamadoAtualizado.getStatus());

        if (StatusChamado.FECHADO.equals(chamado.getStatus()) && chamado.getDataFechamento() == null) {
            chamado.setDataFechamento(LocalDateTime.now());
        } else if (!StatusChamado.FECHADO.equals(chamado.getStatus())) {
            chamado.setDataFechamento(null);
        }

        return chamadoRepository.save(chamado);
    }

    public void deletar(Long id) {
        if (!chamadoRepository.existsById(id)) {
            throw new RuntimeException("Chamado não encontrado com ID: " + id);
        }
        chamadoRepository.deleteById(id);
    }
}
