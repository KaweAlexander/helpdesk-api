package com.kawedev.HelpDesk_API.service;

import com.kawedev.HelpDesk_API.exceptions.RecursoNaoEncontradoException;
import com.kawedev.HelpDesk_API.model.Usuario;
import com.kawedev.HelpDesk_API.model.enums.Perfil;
import com.kawedev.HelpDesk_API.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarUsuario(String nome, String email, String senha){

        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        Usuario usuario = new Usuario(nome, email, senha);
        return usuarioRepository.save(usuario);
    }

    public Usuario alterarPerfil(Long usuarioId, Perfil perfil) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário não encontrado."));

        if (perfil == null) {
            throw new IllegalArgumentException("Perfil é obrigatório.");
        }
        usuario.setPerfil(perfil);
        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado."));

        usuarioRepository.delete(usuario);
    }
}
