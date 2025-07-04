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
    private MatchService matchService; // Injeta MatchService para criar matches

    @Autowired
    private ChatService chatService; // Injeta ChatService para criar conversas

    private static final String COLLECTION_NAME = "likes";

    /**
     * Registra um like de um usuário em outro.
     * Se for um like recíproco, cria um match e uma conversa.
     * @param senderId ID do usuário que deu o like.
     * @param receiverId ID do usuário que recebeu o like.
     * @return O ID do like criado/atualizado.
     */
    public String createLike(String senderId, String receiverId) throws ExecutionException, InterruptedException {
        // Gera um ID consistente para o like (senderId_receiverId)
        String likeId = senderId + "_" + receiverId;
        Like like = new Like(senderId, receiverId);
        like.setId(likeId);

        // Salva o like no Firestore
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(likeId).set(like);
        future.get(); // Espera a operação ser concluída

        // Verifica se há um like recíproco
        checkForReciprocalLikeAndCreateMatch(senderId, receiverId);

        return likeId;
    }

    /**
     * Verifica se o receiverId já deu like no senderId.
     * Se sim, cria um Match e um chat.
     */
    private void checkForReciprocalLikeAndCreateMatch(String senderId, String receiverId) throws ExecutionException, InterruptedException {
        // Tenta encontrar o like inverso (receiverId -> senderId)
        String reciprocalLikeId = receiverId + "_" + senderId;
        ApiFuture<DocumentSnapshot> future = firestore.collection(COLLECTION_NAME).document(reciprocalLikeId).get();
        DocumentSnapshot reciprocalLikeDoc = future.get();

        if (reciprocalLikeDoc.exists()) {
            // Há um like recíproco! Crie o match e o chat
            String matchId = matchService.createOrUpdateMatch(
                    new Match(createMatchId(senderId, receiverId), senderId, receiverId, "matched", new Date(), new Date())
            );
            chatService.createChat(matchId, senderId, receiverId);
            System.out.println("MATCH! " + senderId + " e " + receiverId + " formaram um match com ID: " + matchId);
        }
    }

    // Helper para criar um ID de match consistente (copiado de MatchService)
    private String createMatchId(String userAId, String userBId) {
        if (userAId.compareTo(userBId) < 0) {
            return userAId + "_" + userBId;
        } else {
            return userBId + "_" + userAId;
        }
    }

    /**
     * Retorna a lista de usuários para os quais o dado usuário deu like.
     */
    public List<String> getLikesGivenByUsuario(String userId) throws ExecutionException, InterruptedException {
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

    /**
     * Retorna a lista de usuários que deram like no dado usuário.
     */
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

    /**
     * Verifica se um like específico existe (user1 deu like em user2).
     */
    public boolean hasLiked(String senderId, String receiverId) throws ExecutionException, InterruptedException {
        String likeId = senderId + "_" + receiverId;
        ApiFuture<DocumentSnapshot> future = firestore.collection(COLLECTION_NAME).document(likeId).get();
        DocumentSnapshot document = future.get();
        return document.exists();
    }
}
