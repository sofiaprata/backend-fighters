package com.fighter.fighterbackend.service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.cloud.firestore.FieldValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AuthService {

    private final Firestore firestore;

    public AuthService(Firestore firestore) {
        this.firestore = firestore;
    }


    public UserRecord createUser(String email, String password, String displayName)
            throws FirebaseAuthException, ExecutionException, InterruptedException {

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(displayName)
                .setEmailVerified(false)
                .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Usuário criado no Firebase Auth: " + userRecord.getUid());

        saveUserProfileToFirestore(userRecord.getUid(), email, displayName);

        return userRecord;
    }

    public FirebaseToken verifyIdToken(String idToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    public Map<String, Object> getUserProfileFromFirestore(String uid) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection("usuarios").document(uid).get().get();
        if (document.exists()) {
            return document.getData();
        }
        return null;
    }

    private void saveUserProfileToFirestore(String uid, String email, String displayName)
            throws ExecutionException, InterruptedException {

        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("email", email);
        userProfile.put("nome", displayName);
        userProfile.put("createdAt", FieldValue.serverTimestamp());
        userProfile.put("lastActive", FieldValue.serverTimestamp());

        // Inicializa os campos de perfil com valores nulos ou padrão para serem atualizados posteriormente
        userProfile.put("sexo", null);
        userProfile.put("pesoCategoria", null);
        userProfile.put("alturaEmCm", 0);
        userProfile.put("arteMarcial", new ArrayList<String>());
        userProfile.put("nivelExperiencia", null);
        userProfile.put("localizacao", null);
        userProfile.put("fotoPerfilUrl", null);
        userProfile.put("reputacao", 0.0);
        userProfile.put("descricao", null);

        firestore.collection("usuarios").document(uid).set(userProfile).get();
        System.out.println("Perfil básico do usuário salvo no Firestore para UID: " + uid);
    }
}