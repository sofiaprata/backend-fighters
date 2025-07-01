package com.fighter.fighterbackend.dto;

public class LoginRequest {
    private String idToken; // O token JWT retornado pelo Firebase Auth SDK no Flutter

    // Getters e Setters
    public String getIdToken() { return idToken; }
    public void setIdToken(String idToken) { this.idToken = idToken; }
}
