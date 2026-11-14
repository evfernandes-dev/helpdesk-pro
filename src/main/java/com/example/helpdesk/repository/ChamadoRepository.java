package com.example.helpdesk.repository;

import com.example.helpdesk.model.Chamado;
import com.example.helpdesk.model.StatusChamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    List<Chamado> findByStatusOrderByDataCriacaoDesc(StatusChamado status);
    List<Chamado> findAllByOrderByDataCriacaoDesc();
}
