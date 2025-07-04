package com.fighter.fighterbackend.dto;

import java.util.Date;
import java.util.List;

public class UsuarioResponseDTO {
    private String id;
    private String nome;
    private String email;
    private String sexo;
    private String pesoCategoria;
    private int alturaEmCm;
    private List<String> arteMarcial;
    private String nivelExperiencia;
    private String localizacao;
    private String fotoPerfilUrl;
    private Double reputacao;
    private String descricao;
    private Date createdAt;
    private Date lastActive;

    // Construtor vazio
    public UsuarioResponseDTO() {}

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getPesoCategoria() { return pesoCategoria; }
    public void setPesoCategoria(String pesoCategoria) { this.pesoCategoria = pesoCategoria; }
    public int getAlturaEmCm() { return alturaEmCm; }
    public void setAlturaEmCm(int alturaEmCm) { this.alturaEmCm = alturaEmCm; }
    public List<String> getArteMarcial() { return arteMarcial; }
    public void setArteMarcial(List<String> arteMarcial) { this.arteMarcial = arteMarcial; }
    public String getNivelExperiencia() { return nivelExperiencia; }
    public void setNivelExperiencia(String nivelExperiencia) { this.nivelExperiencia = nivelExperiencia; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public String getFotoPerfilUrl() { return fotoPerfilUrl; }
    public void setFotoPerfilUrl(String fotoPerfilUrl) { this.fotoPerfilUrl = fotoPerfilUrl; }
    public Double getReputacao() { return reputacao; }
    public void setReputacao(Double reputacao) { this.reputacao = reputacao; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getLastActive() { return lastActive; }
    public void setLastActive(Date lastActive) { this.lastActive = lastActive; }
}