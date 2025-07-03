package com.fighter.fighterbackend.entity;

import java.util.Date;

public class Match {
    private String id; // Firestore document ID (e.g., userA_userB)
    private String user1Id;
    private String user2Id;
    private String status; // "matched", "pending", "rejected"
    private Date matchedAt;
    private Date lastMessageAt;

    public Match(){}

    //Getters e Setters
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getUser1Id() {return user1Id;}
    public void setUser1Id(String user1Id) {this.user1Id = user1Id;}
    public String getUser2Id() {return user2Id;}
    public void setUser2Id(String user2Id) {this.user2Id = user2Id;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public Date getMatchedAt() {return matchedAt;}
    public void setMatchedAt(Date matchedAt) {this.matchedAt = matchedAt;}
    public Date getLastMessageAt() {return lastMessageAt;}
    public void setLastMessageAt(Date lastMessageAt) {this.lastMessageAt = lastMessageAt;}
}
