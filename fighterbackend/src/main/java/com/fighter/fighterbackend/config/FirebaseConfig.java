package com.fighter.fighterbackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service.account.path}")
    private String serviceAccountPath;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        try {
            FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://fighters-f93c0-default-rtdb.firebaseio.com")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK inicializado com sucesso!");
            }
        } catch (IOException e) {
            System.err.println("ERRO: Não foi possível inicializar o Firebase Admin SDK");
            System.err.println("Caminho do arquivo tentado: " + serviceAccountPath);
            e.printStackTrace();
            throw e;
        }
    }

    @Bean
    public Firestore firestore() {
        return FirestoreClient.getFirestore();
    }
}