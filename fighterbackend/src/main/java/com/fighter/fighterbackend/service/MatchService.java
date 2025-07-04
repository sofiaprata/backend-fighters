package com.fighter.fighterbackend.service;

import com.fighter.fighterbackend.entity.Match;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
     * Recupera um match pelo seu ID.
     * @param matchId O ID do match a ser recuperado.
     * @return O objeto Match correspondente ao ID, ou null se não encontrado.
     * @throws ExecutionException Se um erro ocorrer durante a execução da operação assíncrona.
     * @throws InterruptedException Se a thread atual for interrompida enquanto espera.
     */
    public Match getMatchById(String matchId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(matchId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Match match = document.toObject(Match.class);
            match.setId(document.getId()); // Garante que o ID do documento seja definido no objeto
            return match;
        }
        return null;
    }

    /**
     * Cria um novo match entre dois usuários no banco de dados Firestore.
     * O ID do match é gerado de forma consistente, garantindo que um match entre
     * o usuário A e o usuário B sempre terá o mesmo ID, independentemente da ordem
     * em que os IDs dos usuários são fornecidos.
     *
     * Este método inicializa o match com o timestamp atual para os campos
     * `matchedAt` e `lastMessageAt`.
     *
     * @param user1Id O ID do primeiro usuário envolvido no match.
     * @param user2Id O ID do segundo usuário envolvido no match.
     * @return O ID consistente do match criado.
     * @throws ExecutionException Se ocorrer um erro durante a execução da operação no Firestore.
     * @throws InterruptedException Se a thread atual for interrompida enquanto espera a conclusão da operação no Firestore.
     */
    public String createMatch(String user1Id, String user2Id) throws ExecutionException, InterruptedException {
        // Usa o helper para garantir que o ID do documento seja consistente
        String consistentMatchId = createMatchId(user1Id, user2Id);
        Match match = new Match();
        match.setId(consistentMatchId); // Define o ID consistente no objeto Match
        match.setUser1Id(user1Id);
        match.setUser2Id(user2Id);
        match.setMatchedAt(new Date()); // Define o timestamp atual para o momento do match
        match.setLastMessageAt(new Date()); // Inicializa o timestamp da última mensagem

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
