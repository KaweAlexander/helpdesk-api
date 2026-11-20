package com.kawedev.HelpDesk_API.repository;


import com.kawedev.HelpDesk_API.model.Chamado;
import com.kawedev.HelpDesk_API.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findByClienteId(Long clienteId);

    List<Chamado> findByTecnicoId(Long tecnicoId);

    List<Chamado> findByStatus(Status status);
}
