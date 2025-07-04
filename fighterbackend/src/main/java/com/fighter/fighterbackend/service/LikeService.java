package com.fighter.fighterbackend.service;

import com.fighter.fighterbackend.entity.Like;
import com.fighter.fighterbackend.entity.Match;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class LikeService {

    @Autowired
    private Firestore firestore;

    @Autowired
    private MatchService matchService;

    @Autowired
    private ChatService chatService;

    private static final String COLLECTION_NAME = "likes";

    public String createLike(String senderId, String receiverId) throws ExecutionException, InterruptedException {
        String likeId = senderId + "_" + receiverId;
        Like like = new Like(senderId, receiverId);
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(likeId).set(like);
        future.get();

        if (hasLiked(receiverId, senderId)) {
            String matchId = matchService.createMatch(senderId, receiverId);
            chatService.createChat(matchId, senderId, receiverId);
        }
        return likeId;
    }

    public List<String> getLikedUsers(String userId) throws ExecutionException, InterruptedException {
        List<String> likedUsers = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("senderId", userId)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Like like = document.toObject(Like.class);
            likedUsers.add(like.getReceiverId());
        }
        return likedUsers;
    }

    public List<String> getLikesReceivedByUser(String userId) throws ExecutionException, InterruptedException {
        List<String> likers = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("receiverId", userId)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Like like = document.toObject(Like.class);
            likers.add(like.getSenderId());
        }
        return likers;
    }

    public boolean hasLiked(String senderId, String receiverId) throws ExecutionException, InterruptedException {
        String likeId = senderId + "_" + receiverId;
        ApiFuture<DocumentSnapshot> future = firestore.collection(COLLECTION_NAME).document(likeId).get();
        DocumentSnapshot document = future.get();
        return document.exists();
    }
}