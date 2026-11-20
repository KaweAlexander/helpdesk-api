package com.kawedev.HelpDesk_API.repository;


import com.kawedev.HelpDesk_API.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    List<Mensagem> findByChamadoIdOrderByDataEnvioAscIdAsc(Long chamadoId);
}
