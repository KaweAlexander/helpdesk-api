package com.kawedev.HelpDesk_API.service;

import com.kawedev.HelpDesk_API.exceptions.RecursoNaoEncontradoException;
import com.kawedev.HelpDesk_API.model.Chamado;
import com.kawedev.HelpDesk_API.model.Usuario;
import com.kawedev.HelpDesk_API.model.enums.Perfil;
import com.kawedev.HelpDesk_API.model.enums.Status;
import com.kawedev.HelpDesk_API.repository.ChamadoRepository;
import com.kawedev.HelpDesk_API.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;

    public ChamadoService(ChamadoRepository chamadoRepository,
                          UsuarioRepository usuarioRepository) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Chamado abrirChamado(String titulo, String descricao, Long clienteId) {
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Cliente não encontrado."));

        if (cliente.getPerfil() != Perfil.CLIENTE) {
            throw new IllegalArgumentException(
                    "Apenas clientes podem abrir chamados.");
        }

        Chamado chamado = new Chamado(titulo, descricao, cliente);
        return chamadoRepository.save(chamado);
    }

    public Chamado buscarPorId(Long chamadoId) {
        return chamadoRepository.findById(chamadoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Chamado não encontrado."));
    }

    public Chamado assumirChamado(Long chamadoId, Long tecnicoId) {
        Chamado chamado = buscarPorId(chamadoId);

        Usuario tecnico = usuarioRepository.findById(tecnicoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Técnico não encontrado."));

        if (tecnico.getPerfil() != Perfil.TECNICO) {
            throw new IllegalArgumentException(
                    "Apenas técnicos podem assumir chamados.");
        }

        if (chamado.getStatus() != Status.ABERTO
                || chamado.getTecnico() != null) {
            throw new IllegalArgumentException(
                    "Chamado não está disponível para atendimento.");
        }

        chamado.setTecnico(tecnico);
        chamado.setStatus(Status.EM_ATENDIMENTO);

        return chamadoRepository.save(chamado);
    }

    public Chamado finalizarChamado(Long chamadoId, Long usuarioId) {
        Chamado chamado = buscarPorId(chamadoId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado."));

        boolean clienteDoChamado =
                usuario.getPerfil() == Perfil.CLIENTE
                        && chamado.getCliente().getId().equals(usuario.getId());

        boolean tecnicoResponsavel =
                usuario.getPerfil() == Perfil.TECNICO
                        && chamado.getTecnico() != null
                        && chamado.getTecnico().getId().equals(usuario.getId());

        if (!clienteDoChamado && !tecnicoResponsavel) {
            throw new IllegalArgumentException(
                    "Apenas o cliente do chamado ou o técnico responsável podem finalizar.");
        }

        if (chamado.getStatus() != Status.ABERTO
                && chamado.getStatus() != Status.EM_ATENDIMENTO) {
            throw new IllegalArgumentException(
                    "Chamado não está disponível para finalização.");
        }

        chamado.setStatus(Status.FINALIZADO);
        return chamadoRepository.save(chamado);
    }

    public List<Chamado> listarPorCliente(Long clienteId){

        if (!usuarioRepository.existsById(clienteId)) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado.");
        }
        return chamadoRepository.findByClienteId(clienteId);
    }

    public List<Chamado> listarPorTecnico( Long tecnicoId){

        Usuario tecnico = usuarioRepository.findById(tecnicoId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Técnico não encontrado."));

        if (tecnico.getPerfil() != Perfil.TECNICO) {
            throw new IllegalArgumentException(
                    "O usuário informado não é um técnico.");
        }

        return chamadoRepository.findByTecnicoId(tecnicoId);
    }

    public List<Chamado> listarAbertos(){
        return chamadoRepository.findByStatus(Status.ABERTO);
    }
}

