package com.fighter.fighterbackend.entity;

import java.util.Date;

public class Chat {
    private String id; // Firestore document ID (e.g., matchId)
    private String user1Id;
    private String user2Id;
    private String lastMessage;
    private String lastMessageSenderId;
    private Date lastMessageAt;

    public Chat(){}

    //Getters e Setters
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getUser1Id() {return user1Id;}
    public void setUser1Id(String user1Id) {this.user1Id = user1Id;}
    public String getUser2Id() {return user2Id;}
    public void setUser2Id(String user2Id) {this.user2Id = user2Id;}
    public String getLastMessage() {return lastMessage;}
    public void setLastMessage(String lastMessage) {this.lastMessage = lastMessage;}
    public String getLastMessageSenderId() {return lastMessageSenderId;}
    public void setLastMessageSenderId(String lastMessageSenderId) {this.lastMessageSenderId = lastMessageSenderId;}
    public Date getLastMessageAt() {return lastMessageAt;}
    public void setLastMessageAt(Date lastMessageAt) {this.lastMessageAt = lastMessageAt;}
}
