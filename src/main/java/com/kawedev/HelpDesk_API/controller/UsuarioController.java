package com.kawedev.HelpDesk_API.controller;


import com.kawedev.HelpDesk_API.model.Usuario;
import com.kawedev.HelpDesk_API.model.enums.Perfil;
import com.kawedev.HelpDesk_API.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrarUsuario(@Valid @RequestBody Usuario usuario) {

        usuarioService.registrarUsuario(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}/perfil")
    public ResponseEntity<Void> alterarPerfil(
            @PathVariable Long id,
            @RequestParam Perfil perfil) {

        usuarioService.alterarPerfil(id, perfil);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
