package com.fighter.fighterbackend.service;

import com.fighter.fighterbackend.entity.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

interface UsuarioServiceIO {
    String createUsuario(Usuario usuario) throws ExecutionException, InterruptedException;
    Usuario getUsuario(String id) throws InterruptedException, ExecutionException;
    String updateUsuario(Usuario usuario);
    String deleteUsuario(String id);
}


@Service
public class UsuarioService implements UsuarioServiceIO{
    final String FB_COLLECTION_NAME = "usuarios";

    @Override
    public String createUsuario(Usuario usuario) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = firestore.collection(FB_COLLECTION_NAME).document(usuario.getNome()).set(usuario);

        return collectionsApiFuture.get().getUpdateTime().toString();

    }

    @Override
    public Usuario getUsuario(String id) throws InterruptedException, ExecutionException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(FB_COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        Usuario usuario;
        if(document.exists()) {
            usuario = document.toObject(Usuario.class);
            return usuario;
        }
            return null;
    }

    @Override
    public String updateUsuario(Usuario usuario){
        return "";
    }

    @Override
    public String deleteUsuario(String id){
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = firestore.collection(FB_COLLECTION_NAME).document(id).delete();
        return "Deletado com sucesso " + id;
    }

}
