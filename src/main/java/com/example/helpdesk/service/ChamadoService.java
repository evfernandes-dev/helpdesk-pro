package com.example.helpdesk.service;

import com.example.helpdesk.exception.RecursoNaoEncontradoException;
import com.example.helpdesk.model.Chamado;
import com.example.helpdesk.model.StatusChamado;
import com.example.helpdesk.repository.ChamadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service // Marca como serviço (regras de negócio)
@Transactional // Garante tudo roda numa transação
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository) {
        this.chamadoRepository = chamadoRepository;
    }

    @Transactional(readOnly = true) // Só leitura, economiza um pouco
    public List<Chamado> listarTodos() {
        // Mostra do mais novo pro mais antigo
        return chamadoRepository.findAllByOrderByDataCriacaoDesc();
    }

    @Transactional(readOnly = true)
    public Optional<Chamado> buscarPorId(Long id) {
        return chamadoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Chamado> buscarPorStatus(StatusChamado status) {
        return chamadoRepository.findByStatus(status);
    }

    public Chamado criar(Chamado chamado) {
        // Garante que não vai vir com id por engano
        chamado.setId(null);
        chamado.setDataCriacao(LocalDateTime.now());
        // Se não passar status, já abre como ABERTO
        if (chamado.getStatus() == null) {
            chamado.setStatus(StatusChamado.ABERTO);
        }
        return chamadoRepository.save(chamado);
    }

    public Chamado atualizar(Long id, Chamado chamadoAtualizado) {
        // Busca ou manda 404 se não achar
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Chamado não encontrado com ID: " + id));

        // Só atualiza se vier coisa válida (pra não sobrescrever com vazio)
        if (chamadoAtualizado.getTitulo() != null && !chamadoAtualizado.getTitulo().isBlank()) {
            chamado.setTitulo(chamadoAtualizado.getTitulo());
        }
        if (chamadoAtualizado.getDescricao() != null && !chamadoAtualizado.getDescricao().isBlank()) {
            chamado.setDescricao(chamadoAtualizado.getDescricao());
        }
        if (chamadoAtualizado.getStatus() != null) {
            chamado.setStatus(chamadoAtualizado.getStatus());
        }

        // Se fechou agora, marca a data de fechamento. Se reabriu, limpa ela
        if (StatusChamado.FECHADO.equals(chamado.getStatus()) && chamado.getDataFechamento() == null) {
            chamado.setDataFechamento(LocalDateTime.now());
        } else if (!StatusChamado.FECHADO.equals(chamado.getStatus())) {
            chamado.setDataFechamento(null);
        }

        return chamadoRepository.save(chamado);
    }

    public void deletar(Long id) {
        // Valida antes pra devolver 404 certinho
        if (!chamadoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Chamado não encontrado com ID: " + id);
        }
        chamadoRepository.deleteById(id);
    }
}
