package com.fighter.fighterbackend.entity;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class Usuario {
	//atributos
    private String id;
	private String nome;
    private String email;
	private String sexo;
	private Double pesoEmKg;
	private int alturaEmCm;
    private String arteMarcial;
    private String nivelExperiencia;
    private String localizacao;
    private String fotoPerfilUrl;
    private Double reputacao;
    private String descricao;

    
    //construtor
    public Usuario(){}
    public Usuario(String id, String nome, String email,  String sexo,
            double pesoEmKg, int alturaEmCm, String arteMarcial, String nivelExperiencia,
            String localizacao, String fotoPerfilUrl, double reputacao, String descricao) {
    	this.id = id;
    	this.nome = nome;
    	this.email = email;
    	this.sexo = sexo;
    	this.pesoEmKg = pesoEmKg;
    	this.alturaEmCm = alturaEmCm;
    	this.arteMarcial = arteMarcial;
    	this.nivelExperiencia = nivelExperiencia;
    	this.localizacao = localizacao;
    	this.fotoPerfilUrl = fotoPerfilUrl;
    	this.reputacao = reputacao;
    	this.descricao = descricao;
}
    
    // getters e setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public double getPesoEmKg() {
        return pesoEmKg;
    }

    public void setPesoEmKg(double pesoEmKg) {
        this.pesoEmKg = pesoEmKg;
    }

    public int getAlturaEmCm() {
        return alturaEmCm;
    }

    public void setAlturaEmCm(int alturaEmCm) {
        this.alturaEmCm = alturaEmCm;
    }

    public String getArteMarcial() {
        return arteMarcial;
    }

    public void setArteMarcial(String arteMarcial) {
        this.arteMarcial = arteMarcial;
    }

    public String getNivelExperiencia() {
        return nivelExperiencia;
    }

    public void setNivelExperiencia(String nivelExperiencia) {
        this.nivelExperiencia = nivelExperiencia;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public double getReputacao() {
        return reputacao;
    }

    public void setReputacao(double reputacao) {
        this.reputacao = reputacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
    
