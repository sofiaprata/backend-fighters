package com.fighter.fighterbackend.dto;

public class LoginResponseDTO {
    private String uid;
    private String email;
    private String displayName;
    private String message;
    // ... outros dados do perfil que você queira retornar

    public LoginResponseDTO(String uid, String email, String displayName, String message) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.message = message;
    }

    // Getters e Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}