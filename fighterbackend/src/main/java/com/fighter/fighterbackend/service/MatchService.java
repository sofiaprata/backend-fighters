package com.fighter.fighterbackend.service;

import com.fighter.fighterbackend.entity.Match;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class MatchService {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "matches";

    public List<Match> getMatchesForUsuarios(String userId) throws ExecutionException, InterruptedException {
        List<Match> matches = new ArrayList<>();

        // Consulta 1: Onde o usuário é user1Id
        ApiFuture<QuerySnapshot> future1 = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("user1Id", userId)
                .get();
        for (QueryDocumentSnapshot document : future1.get().getDocuments()) {
            Match match = document.toObject(Match.class);
            match.setId(document.getId());
            matches.add(match);
        }

        // Consulta 2: Onde o usuário é user2Id
        ApiFuture<QuerySnapshot> future2 = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("user2Id", userId)
                .get();
        for (QueryDocumentSnapshot document : future2.get().getDocuments()) {
            Match match = document.toObject(Match.class);
            match.setId(document.getId());
            matches.add(match);
        }
        return matches;
    }

    public Match getMatchBetweenUsuarios(String user1Id, String user2Id) throws ExecutionException, InterruptedException {
        // Para garantir a ordem, crie o ID composto de forma consistente
        String matchId = createMatchId(user1Id, user2Id);
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(matchId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Match match = document.toObject(Match.class);
            match.setId(document.getId());
            return match;
        }
        return null;
    }

    /**
     * Cria ou atualiza um match. Este método será chamado pelo LikeService.
     * Garante que o ID do match seja consistente (menor_maior).
     * @param match O objeto Match a ser salvo.
     * @return O ID do match.
     */
    public String createOrUpdateMatch(Match match) throws ExecutionException, InterruptedException {
        // Usa o helper para garantir que o ID do documento seja consistente
        String consistentMatchId = createMatchId(match.getUser1Id(), match.getUser2Id());
        match.setId(consistentMatchId); // Define o ID consistente no objeto Match

        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME).document(consistentMatchId).set(match);
        future.get();
        return consistentMatchId;
    }

    // Helper para criar um ID de match consistente
    private String createMatchId(String userAId, String userBId) {
        if (userAId.compareTo(userBId) < 0) {
            return userAId + "_" + userBId;
        } else {
            return userBId + "_" + userAId;
        }
    }
}
