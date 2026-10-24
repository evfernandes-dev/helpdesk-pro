package com.example.helpdesk.repository;

import com.example.helpdesk.model.Chamado;
import com.example.helpdesk.model.StatusChamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    List<Chamado> findByStatus(StatusChamado status);
    List<Chamado> findAllByOrderByDataCriacaoDesc();
}
