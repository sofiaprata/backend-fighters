package com.fighter.fighterbackend.dto;

public class RegisterRequest {
    private String email;
    private String password;
    private String displayName; // Opcional

    // Getters e Setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
