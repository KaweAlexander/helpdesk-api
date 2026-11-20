package com.kawedev.HelpDesk_API.service;


import com.kawedev.HelpDesk_API.exceptions.RecursoNaoEncontradoException;
import com.kawedev.HelpDesk_API.model.Chamado;
import com.kawedev.HelpDesk_API.model.Mensagem;
import com.kawedev.HelpDesk_API.model.Usuario;
import com.kawedev.HelpDesk_API.model.enums.Perfil;
import com.kawedev.HelpDesk_API.model.enums.Status;
import com.kawedev.HelpDesk_API.repository.ChamadoRepository;
import com.kawedev.HelpDesk_API.repository.MensagemRepository;
import com.kawedev.HelpDesk_API.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;

    public MensagemService(MensagemRepository mensagemRepository, ChamadoRepository chamadoRepository, UsuarioRepository usuarioRepository) {
        this.mensagemRepository = mensagemRepository;
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Mensagem enviarMensagem(String conteudo, Long chamadoId, Long usuarioId) {
        Chamado chamado = buscarChamado(chamadoId);
        Usuario autor = buscarUsuario(usuarioId);

        validarParticipante(chamado, autor);

        if (chamado.getStatus() == Status.FINALIZADO) {
            throw new IllegalArgumentException(
                    "Não é possível enviar mensagens em um chamado finalizado.");
        }

        if (conteudo == null || conteudo.isBlank()) {
            throw new IllegalArgumentException(
                    "O conteúdo da mensagem é obrigatório.");
        }

        Mensagem mensagem = new Mensagem(conteudo, autor, chamado);
        return mensagemRepository.save(mensagem);
    }

    public List<Mensagem> listarMensagens(Long chamadoId, Long usuarioId) {
        Chamado chamado = buscarChamado(chamadoId);
        Usuario usuario = buscarUsuario(usuarioId);

        validarParticipante(chamado, usuario);

        return mensagemRepository
                .findByChamadoIdOrderByDataEnvioAscIdAsc(chamadoId);
    }

    private Chamado buscarChamado(Long chamadoId) {
        return chamadoRepository.findById(chamadoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Chamado não encontrado."));
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado."));
    }

    private void validarParticipante(Chamado chamado, Usuario usuario) {
        boolean clienteDoChamado =
                usuario.getPerfil() == Perfil.CLIENTE
                        && chamado.getCliente().getId().equals(usuario.getId());

        boolean tecnicoResponsavel =
                usuario.getPerfil() == Perfil.TECNICO
                        && chamado.getTecnico() != null
                        && chamado.getTecnico().getId().equals(usuario.getId());

        if (!clienteDoChamado && !tecnicoResponsavel) {
            throw new IllegalArgumentException(
                    "Apenas o cliente do chamado ou o técnico responsável podem acessar a conversa.");
        }
    }
}
