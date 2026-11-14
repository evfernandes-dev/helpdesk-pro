package com.example.helpdesk.service;

import com.example.helpdesk.exception.ChamadoNotFoundException;
import com.example.helpdesk.model.Chamado;
import com.example.helpdesk.model.StatusChamado;
import com.example.helpdesk.repository.ChamadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository) {
        this.chamadoRepository = chamadoRepository;
    }

    @Transactional(readOnly = true)
    public List<Chamado> listarTodos() {
        return chamadoRepository.findAllByOrderByDataCriacaoDesc();
    }

    @Transactional(readOnly = true)
    public Optional<Chamado> buscarPorId(Long id) {
        return chamadoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Chamado> buscarPorStatus(StatusChamado status) {
        return chamadoRepository.findByStatusOrderByDataCriacaoDesc(status);
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
                .orElseThrow(() -> new ChamadoNotFoundException(id));

        if (chamadoAtualizado.getTitulo() != null && !chamadoAtualizado.getTitulo().isBlank()) {
            chamado.setTitulo(chamadoAtualizado.getTitulo());
        }
        if (chamadoAtualizado.getDescricao() != null && !chamadoAtualizado.getDescricao().isBlank()) {
            chamado.setDescricao(chamadoAtualizado.getDescricao());
        }
        if (chamadoAtualizado.getStatus() != null) {
            aplicarStatus(chamado, chamadoAtualizado.getStatus());
        }

        return chamadoRepository.save(chamado);
    }

    public Chamado atualizarStatus(Long id, StatusChamado novoStatus) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNotFoundException(id));

        aplicarStatus(chamado, novoStatus);
        return chamadoRepository.save(chamado);
    }

    private void aplicarStatus(Chamado chamado, StatusChamado novoStatus) {
        chamado.setStatus(novoStatus);
        if (StatusChamado.FECHADO.equals(novoStatus)) {
            if (chamado.getDataFechamento() == null) {
                chamado.setDataFechamento(LocalDateTime.now());
            }
        } else {
            chamado.setDataFechamento(null);
        }
    }

    public void deletar(Long id) {
        if (!chamadoRepository.existsById(id)) {
            throw new ChamadoNotFoundException(id);
        }
        chamadoRepository.deleteById(id);
    }
}
