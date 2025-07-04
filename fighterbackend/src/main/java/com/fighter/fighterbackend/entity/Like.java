package com.fighter.fighterbackend.entity;

import java.util.Date;

public class Like {
    private String id; // Firestore document ID (e.g., senderId_receiverId)
    private String senderId;
    private String receiverId;
    private Date createdAt;

    public Like() {}

    public Like(String senderId, String receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.createdAt = new Date(); // Define o timestamp ao criar
    }

    // Getters e Setters
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getSenderId() {return senderId;}
    public void setReceiverId(String receiverId) {this.receiverId = receiverId;}
    public String getReceiverId() {return receiverId;}
    public Date getCreatedAt() {return createdAt;}
    public void setCreatedAt(Date createdAt) {this.createdAt = createdAt;}
}
