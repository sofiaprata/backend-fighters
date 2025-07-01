package com.fighter.fighterbackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig{
    @PostConstruct
    public void configureFirebaseConnection(){

        final String SERVICE_ACCOUNT_KEY_PATH = "C:\\Users\\Rubens\\Documents\\IFCE\\POO\\chave-firebase\\serviceAccountKey.json";

        try {
            FileInputStream serviceAccount =
                    new FileInputStream(SERVICE_ACCOUNT_KEY_PATH);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://fighters-f93c0-default-rtdb.firebaseio.com")
                    .build();

            // Inicializa o FirebaseApp apenas se ainda não foi inicializado
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK inicializado com sucesso!");
            }
        } catch (IOException e) {
            System.err.println("Erro ao inicializar o Firebase Admin SDK: " + e.getMessage());
        }

    }




}
