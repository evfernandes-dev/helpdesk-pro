package com.example.helpdesk.service;

import com.example.helpdesk.model.Chamado;
import com.example.helpdesk.model.StatusChamado;
import com.example.helpdesk.repository.ChamadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;

    @InjectMocks
    private ChamadoService chamadoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTodosOsChamados() {
        Chamado chamado = new Chamado();
        chamado.setId(1L);
        chamado.setTitulo("Erro no login");
        chamado.setDescricao("Não consigo logar no sistema");

        when(chamadoRepository.findAllByOrderByDataCriacaoDesc()).thenReturn(Arrays.asList(chamado));

        List<Chamado> resultado = chamadoService.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Erro no login", resultado.get(0).getTitulo());
        verify(chamadoRepository, times(1)).findAllByOrderByDataCriacaoDesc();
    }

    @Test
    void deveCriarChamadoComSucesso() {
        Chamado chamado = new Chamado();
        chamado.setTitulo("Nova impressora");
        chamado.setDescricao("Precisa configurar a impressora do RH");

        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocation -> {
            Chamado c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        Chamado criado = chamadoService.criar(chamado);

        assertNotNull(criado.getId());
        assertEquals(StatusChamado.ABERTO, criado.getStatus());
        assertNotNull(criado.getDataCriacao());
        verify(chamadoRepository, times(1)).save(any(Chamado.class));
    }

    @Test
    void deveAtualizarChamadoEFechar() {
        Chamado existente = new Chamado();
        existente.setId(1L);
        existente.setTitulo("Computador travando");
        existente.setDescricao("Tela azul constante");
        existente.setStatus(StatusChamado.ABERTO);

        Chamado atualizacao = new Chamado();
        atualizacao.setTitulo("Computador travando");
        atualizacao.setDescricao("Tela azul constante");
        atualizacao.setStatus(StatusChamado.FECHADO);

        when(chamadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chamado atualizado = chamadoService.atualizar(1L, atualizacao);

        assertEquals(StatusChamado.FECHADO, atualizado.getStatus());
        assertNotNull(atualizado.getDataFechamento());
        verify(chamadoRepository, times(1)).findById(1L);
        verify(chamadoRepository, times(1)).save(any(Chamado.class));
    }
}
