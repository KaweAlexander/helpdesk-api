package com.kawedev.HelpDesk_API.model;


import com.kawedev.HelpDesk_API.model.enums.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @Column(nullable = false, unique = true) // garante que emails sejam unicos
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    @OneToMany(mappedBy = "cliente")
    private List<Chamado> chamadosAbertos = new ArrayList<>();

    @OneToMany(mappedBy = "tecnico")
    private List<Chamado> chamadosAtendidos = new ArrayList<>();


    public Usuario() {
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = Perfil.CLIENTE;
    }

    public Long getId() {
        return id;
    }

    public List<Chamado> getChamadosAtendidos() {
        return chamadosAtendidos;
    }

    public void setChamadosAtendidos(List<Chamado> chamadosAtendidos) {
        this.chamadosAtendidos = chamadosAtendidos;
    }

    public List<Chamado> getChamadosAbertos() {
        return chamadosAbertos;
    }

    public void setChamadosAbertos(List<Chamado> chamadosAbertos) {
        this.chamadosAbertos = chamadosAbertos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
