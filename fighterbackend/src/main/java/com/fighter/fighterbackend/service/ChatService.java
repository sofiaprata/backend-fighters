package com.fighter.fighterbackend.service;

import com.fighter.fighterbackend.entity.Chat;
import com.fighter.fighterbackend.entity.Mensagem;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ChatService {

    @Autowired
    private Firestore firestore;

    private static final String CHATS_COLLECTION = "chats";
    private static final String MESSAGES_SUBCOLLECTION = "mensagens";

    public List<Chat> getChatsForUsuario(String userId) throws ExecutionException, InterruptedException {
        List<Chat> chats = new ArrayList<>();

        // Consulta 1: Onde o usuário é user1Id
        ApiFuture<QuerySnapshot> future1 = firestore.collection(CHATS_COLLECTION)
                .whereEqualTo("user1Id", userId)
                .orderBy("lastMessageAt", Query.Direction.DESCENDING) // Ordena pelas mais recentes
                .get();
        for (QueryDocumentSnapshot document : future1.get().getDocuments()) {
            Chat chat = document.toObject(Chat.class);
            chat.setId(document.getId());
            chats.add(chat);
        }

        // Consulta 2: Onde o usuário é user2Id
        ApiFuture<QuerySnapshot> future2 = firestore.collection(CHATS_COLLECTION)
                .whereEqualTo("user2Id", userId)
                .orderBy("lastMessageAt", Query.Direction.DESCENDING)
                .get();
        for (QueryDocumentSnapshot document : future2.get().getDocuments()) {
            Chat chat = document.toObject(Chat.class);
            chat.setId(document.getId());
            // Evita duplicatas se o ID do match for o mesmo
            if (!chats.contains(chat)) {
                chats.add(chat);
            }
        }
        // Pode ser necessário reordenar a lista completa se houver duplicatas ou se a ordem for crítica
        chats.sort((c1, c2) -> c2.getLastMessageAt().compareTo(c1.getLastMessageAt()));
        return chats;
    }

    public List<Mensagem> getMensagensForChat(String chatId) throws ExecutionException, InterruptedException {
        List<Mensagem> mensagens = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(CHATS_COLLECTION)
                .document(chatId)
                .collection(MESSAGES_SUBCOLLECTION)
                .orderBy("timestamp", Query.Direction.ASCENDING) // Ordena as mensagens por tempo
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Mensagem mensagem = document.toObject(Mensagem.class);
            mensagem.setId(document.getId());
            mensagens.add(mensagem);
        }
        return mensagens;
    }

    public String addMensagemToChat(String conversationId, Mensagem mensagem) throws ExecutionException, InterruptedException {
        // Adiciona a mensagem à subcoleção
        DocumentReference messageRef = firestore.collection(CHATS_COLLECTION)
                .document(conversationId)
                .collection(MESSAGES_SUBCOLLECTION)
                .document(); // Gera um novo ID para a mensagem
        mensagem.setId(messageRef.getId());
        mensagem.setTimestamp(new Date()); // Define o timestamp atual
        ApiFuture<WriteResult> future = messageRef.set(mensagem);
        future.get();

        // Atualiza o chat principal com a última mensagem e timestamp
        DocumentReference chatRef = firestore.collection(CHATS_COLLECTION).document(conversationId);
        chatRef.update(
                "lastMessage", mensagem.getContent(),
                "lastMessageSenderId", mensagem.getSenderId(),
                "lastMessageAt", mensagem.getTimestamp()
        ).get();

        return mensagem.getId();
    }

    public String sendMensagem(String conversationId, String senderId, String receiverId, String content) throws ExecutionException, InterruptedException {
        Mensagem mensagem = new Mensagem();
        mensagem.setSenderId(senderId);
        mensagem.setReceiverId(receiverId); // Pode ser útil para filtros futuros ou validação no cliente
        mensagem.setContent(content);
        mensagem.setTimestamp(new Date()); // Define o timestamp atual
        mensagem.setRead(false); // Mensagem nova, ainda não lida

        DocumentReference messageRef = firestore.collection(CHATS_COLLECTION)
                .document(conversationId)
                .collection(MESSAGES_SUBCOLLECTION)
                .document(); // Gera um novo ID para a mensagem
        mensagem.setId(messageRef.getId()); // Define o ID gerado no objeto Mensagem

        ApiFuture<WriteResult> future = messageRef.set(mensagem);
        future.get(); // Espera a mensagem ser salva

        // Atualiza o chat principal com a última mensagem e timestamp
        DocumentReference conversationRef = firestore.collection(CHATS_COLLECTION).document(conversationId);
        conversationRef.update(
                "lastMessage", mensagem.getContent(),
                "lastMessageSenderId", mensagem.getSenderId(),
                "lastMessageAt", mensagem.getTimestamp()
        ).get(); // Espera a atualização do chat

        return mensagem.getId();
    }

    // Criar um novo chat (chamado após um match)
    public String createChat(String matchId, String user1Id, String user2Id) throws ExecutionException, InterruptedException {
        Chat chat = new Chat();
        chat.setId(matchId); // Usa o matchId como ID da conversa
        chat.setUser1Id(user1Id);
        chat.setUser2Id(user2Id);
        chat.setLastMessage(""); // Inicializa
        chat.setLastMessageAt(new Date()); // Inicializa com o tempo atual

        ApiFuture<WriteResult> future = firestore.collection(CHATS_COLLECTION).document(matchId).set(chat);
        future.get();
        return matchId;
    }
}