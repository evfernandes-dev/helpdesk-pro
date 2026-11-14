package com.example.helpdesk.service;

import com.example.helpdesk.exception.ChamadoNotFoundException;
import com.example.helpdesk.model.Chamado;
import com.example.helpdesk.model.StatusChamado;
import com.example.helpdesk.repository.ChamadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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
    void deveBuscarChamadoPorId() {
        Chamado chamado = new Chamado();
        chamado.setId(1L);
        chamado.setTitulo("Erro no login");
        chamado.setDescricao("Não consigo logar no sistema");

        when(chamadoRepository.findById(1L)).thenReturn(Optional.of(chamado));

        Optional<Chamado> resultado = chamadoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Erro no login", resultado.get().getTitulo());
        verify(chamadoRepository, times(1)).findById(1L);
    }

    @Test
    void deveRetornarVazioAoBuscarChamadoInexistente() {
        when(chamadoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Chamado> resultado = chamadoService.buscarPorId(99L);

        assertTrue(resultado.isEmpty());
        verify(chamadoRepository, times(1)).findById(99L);
    }

    @Test
    void deveBuscarChamadosPorStatus() {
        Chamado aberto = new Chamado();
        aberto.setId(1L);
        aberto.setStatus(StatusChamado.ABERTO);

        when(chamadoRepository.findByStatusOrderByDataCriacaoDesc(StatusChamado.ABERTO))
                .thenReturn(Arrays.asList(aberto));

        List<Chamado> resultado = chamadoService.buscarPorStatus(StatusChamado.ABERTO);

        assertEquals(1, resultado.size());
        assertEquals(StatusChamado.ABERTO, resultado.get(0).getStatus());
        verify(chamadoRepository, times(1)).findByStatusOrderByDataCriacaoDesc(StatusChamado.ABERTO);
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

    @Test
    void deveAtualizarStatusParaFechado() {
        Chamado existente = new Chamado();
        existente.setId(1L);
        existente.setTitulo("Notebook sem internet");
        existente.setDescricao("Wi-Fi não conecta");
        existente.setStatus(StatusChamado.ABERTO);

        when(chamadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chamado atualizado = chamadoService.atualizarStatus(1L, StatusChamado.FECHADO);

        assertEquals(StatusChamado.FECHADO, atualizado.getStatus());
        assertNotNull(atualizado.getDataFechamento());
        verify(chamadoRepository, times(1)).save(any(Chamado.class));
    }

    @Test
    void deveReabrirChamadoLimpandoDataFechamento() {
        Chamado existente = new Chamado();
        existente.setId(1L);
        existente.setTitulo("Impressora sem toner");
        existente.setDescricao("Substituir toner");
        existente.setStatus(StatusChamado.FECHADO);
        existente.setDataFechamento(LocalDateTime.now().minusDays(1));

        when(chamadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chamado atualizado = chamadoService.atualizarStatus(1L, StatusChamado.EM_ANDAMENTO);

        assertEquals(StatusChamado.EM_ANDAMENTO, atualizado.getStatus());
        assertNull(atualizado.getDataFechamento());
    }

    @Test
    void deveLancarErroAoAtualizarStatusDeChamadoInexistente() {
        when(chamadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ChamadoNotFoundException.class,
                () -> chamadoService.atualizarStatus(99L, StatusChamado.FECHADO));
        verify(chamadoRepository, never()).save(any(Chamado.class));
    }

    @Test
    void deveManterStatusAoAtualizarSemEnviarStatus() {
        Chamado existente = new Chamado();
        existente.setId(1L);
        existente.setTitulo("Titulo Antigo");
        existente.setDescricao("Desc Antiga");
        existente.setStatus(StatusChamado.FECHADO);
        existente.setDataFechamento(LocalDateTime.now());

        Chamado atualizacao = new Chamado();
        atualizacao.setTitulo("Titulo Novo");
        atualizacao.setDescricao("Desc Nova");
        atualizacao.setStatus(null);

        when(chamadoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chamado atualizado = chamadoService.atualizar(1L, atualizacao);

        assertEquals(StatusChamado.FECHADO, atualizado.getStatus());
        assertNotNull(atualizado.getDataFechamento());
        assertEquals("Titulo Novo", atualizado.getTitulo());
    }

    @Test
    void deveLancarErroAoAtualizarChamadoInexistente() {
        Chamado atualizacao = new Chamado();
        atualizacao.setTitulo("Qualquer titulo");
        atualizacao.setDescricao("Qualquer descricao");

        when(chamadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ChamadoNotFoundException.class,
                () -> chamadoService.atualizar(99L, atualizacao));
        verify(chamadoRepository, never()).save(any(Chamado.class));
    }

    @Test
    void deveDeletarChamadoExistente() {
        when(chamadoRepository.existsById(1L)).thenReturn(true);

        chamadoService.deletar(1L);

        verify(chamadoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarErroAoDeletarChamadoInexistente() {
        when(chamadoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ChamadoNotFoundException.class,
                () -> chamadoService.deletar(99L));
        verify(chamadoRepository, never()).deleteById(99L);
    }
}
