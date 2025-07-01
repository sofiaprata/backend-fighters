package com.fighter.fighterbackend.service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AuthService {

    public UserRecord createUser(String email, String password, String displayName) throws FirebaseAuthException, ExecutionException, InterruptedException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(displayName)
                .setEmailVerified(false) // Trocar para true se o email for verificado no frontend
                .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Usuário criado no Firebase Auth: " + userRecord.getUid());

        // Salva informações adicionais no Firestore
        saveUserProfileToFirestore(userRecord.getUid(), email, displayName);

        return userRecord;
    }

    public FirebaseToken verifyIdToken(String idToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    public Map<String, Object> getUserProfileFromFirestore(String uid) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentSnapshot document = dbFirestore.collection("usuarios").document(uid).get().get();
        if (document.exists()) {
            return document.getData();
        }
        return null; // Perfil não encontrado
    }

    private void saveUserProfileToFirestore(String uid, String email, String displayName) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("email", email);
        userProfile.put("nome", displayName);
        userProfile.put("createdAt", com.google.cloud.firestore.FieldValue.serverTimestamp()); // Timestamp do servidor

        dbFirestore.collection("usuarios").document(uid).set(userProfile).get(); // .get() para esperar a conclusão
        System.out.println("Perfil do usuário salvo no Firestore para UID: " + uid);
    }
}