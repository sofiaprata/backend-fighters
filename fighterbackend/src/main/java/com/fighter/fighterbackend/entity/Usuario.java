package com.fighter.fighterbackend.entity;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class Usuario {
    // atributos
    private String id;
    private String nome;
    private String email;
    private String sexo;
    private String dataNascimento;
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

    // construtor vazio
    public Usuario(){}

    // Construtor completo
    public Usuario(String id, String nome, String email, String sexo, String dataNascimento,
                   String pesoCategoria, int alturaEmCm, List<String> arteMarcial,
                   String nivelExperiencia, String localizacao, String fotoPerfilUrl,
                   double reputacao, String descricao, Date createdAt, Date lastActive) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.pesoCategoria = pesoCategoria;
        this.alturaEmCm = alturaEmCm;
        this.arteMarcial = arteMarcial;
        this.nivelExperiencia = nivelExperiencia;
        this.localizacao = localizacao;
        this.fotoPerfilUrl = fotoPerfilUrl;
        this.reputacao = reputacao;
        this.descricao = descricao;
        this.createdAt = createdAt;
        this.lastActive = lastActive;
    }

    // getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getDataNascimento() { return dataNascimento;}
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento;}
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