package com.kawedev.HelpDesk_API.controller;


import com.kawedev.HelpDesk_API.model.Chamado;
import com.kawedev.HelpDesk_API.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @PostMapping
    public ResponseEntity<Chamado> abrirChamado(@Valid @RequestBody Chamado chamado, @RequestParam Long clienteId){

        Chamado novoChamado = chamadoService.abrirChamado(
                chamado.getTitulo(),
                chamado.getDescricao(),
                clienteId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(novoChamado);
    }
}

