package com.fighter.fighterbackend.entity;

import java.util.Date;

public class Mensagem {
    private String id; // Firestore document ID
    private String senderId;
    private String receiverId;
    private String content;
    private Date timestamp;
    private boolean read;

    public Mensagem(){}

    //Getters e Setters
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getSenderId() {return senderId;}
    public void setSenderId(String senderId) {this.senderId = senderId;}
    public String getReceiverId() {return receiverId;}
    public void setReceiverId(String receiverId) {this.receiverId = receiverId;}
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}
    public Date getTimestamp() {return timestamp;}
    public void setTimestamp(Date timestamp) {this.timestamp = timestamp;}
    public boolean isRead() {return read;}
    public void setRead(boolean read) {this.read = read;}
}
