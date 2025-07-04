package com.fighter.fighterbackend.controller;

import com.fighter.fighterbackend.entity.Chat;
import com.fighter.fighterbackend.entity.Mensagem;
import com.fighter.fighterbackend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Controlador REST para gerenciar operações relacionadas a chats e mensagens.
 * Fornece endpoints para buscar chats de um usuário, mensagens de um chat,
 * enviar novas mensagens e criar novos chats.
 */
@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * Obtém todos os chats associados a um usuário específico.
     *
     * @param userId O ID do usuário para o qual os chats serão buscados.
     * @return Uma {@link ResponseEntity} contendo uma lista de objetos {@link Chat}
     * se a operação for bem-sucedida (HTTP 200 OK), ou um status de erro
     * (HTTP 500 INTERNAL_SERVER_ERROR) em caso de falha.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> getChatsForUsuario(@PathVariable String userId) {
        try {
            List<Chat> chats = chatService.getChatsForUsuario(userId);
            return ResponseEntity.ok(chats);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            // Retorna um erro interno do servidor com detalhes da exceção
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Ou um DTO de erro mais detalhado
        }
    }

    /**
     * Obtém todas as mensagens de um chat específico.
     * As mensagens são retornadas em ordem cronológica (do mais antigo para o mais recente).
     *
     * @param chatId O ID do chat (correspondente ao matchId) do qual as mensagens serão buscadas.
     * @return Uma {@link ResponseEntity} contendo uma lista de objetos {@link Mensagem}
     * se a operação for bem-sucedida (HTTP 200 OK), ou um status de erro
     * (HTTP 500 INTERNAL_SERVER_ERROR) em caso de falha.
     */
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<Mensagem>> getChatMensagens(@PathVariable String chatId) {
        try {
            List<Mensagem> messages = chatService.getMensagensForChat(chatId);
            return ResponseEntity.ok(messages);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            // Retorna um erro interno do servidor com detalhes da exceção
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Ou um DTO de erro mais detalhado
        }
    }

    /**
     * Envia uma nova mensagem para um chat específico.
     * A mensagem será associada ao chat pelo {@code chatId} fornecido no path.
     *
     * @param chatId O ID do chat (matchId) para o qual a mensagem será enviada.
     * @param mensagem O objeto {@link Mensagem} contendo o ID do remetente (senderId),
     * o ID do destinatário (receiverId) e o conteúdo da mensagem (content).
     * Os campos {@code timestamp} e {@code read} serão definidos pelo backend.
     * @return Uma {@link ResponseEntity} contendo um mapa com uma mensagem de sucesso e o ID da mensagem criada
     * (HTTP 201 CREATED) se a operação for bem-sucedida. Retorna HTTP 400 BAD_REQUEST
     * se os dados obrigatórios da mensagem estiverem ausentes, ou HTTP 500 INTERNAL_SERVER_ERROR
     * em caso de falha interna.
     */
    @PostMapping("/{chatId}/messages/send")
    public ResponseEntity<?> sendMensagem(@PathVariable String chatId, @RequestBody Mensagem mensagem) {
        if (mensagem.getSenderId() == null || mensagem.getReceiverId() == null || mensagem.getContent() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Sender ID, Receiver ID e conteúdo da mensagem são obrigatórios."));
        }
        try {
            String messageId = chatService.sendMensagem(
                    chatId,
                    mensagem.getSenderId(),
                    mensagem.getReceiverId(),
                    mensagem.getContent()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Mensagem enviada com sucesso!", "messageId", messageId));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erro ao enviar mensagem: " + e.getMessage()));
        }
    }

    /**
     * Cria um novo chat entre dois usuários.
     * Este endpoint é tipicamente chamado automaticamente após a detecção de um "match" recíproco
     * entre dois usuários, usando o ID do match como o ID do chat.
     *
     * @param requestBody Um mapa contendo:
     * - "matchId": O ID do match que originou o chat (será o ID do documento do chat).
     * - "user1Id": O ID do primeiro usuário participante do chat.
     * - "user2Id": O ID do segundo usuário participante do chat.
     * @return Uma {@link ResponseEntity} contendo um mapa com uma mensagem de sucesso e o ID do chat criado
     * (HTTP 201 CREATED) se a operação for bem-sucedida. Retorna HTTP 400 BAD_REQUEST
     * se os IDs obrigatórios estiverem ausentes, ou HTTP 500 INTERNAL_SERVER_ERROR
     * em caso de falha interna.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createChat(@RequestBody Map<String, String> requestBody) {
        String matchId = requestBody.get("matchId");
        String user1Id = requestBody.get("user1Id");
        String user2Id = requestBody.get("user2Id");

        if (matchId == null || user1Id == null || user2Id == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "matchId, user1Id e user2Id são obrigatórios para criar um chat."));
        }

        try {
            String createdChatId = chatService.createChat(matchId, user1Id, user2Id);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Chat criado com sucesso!", "chatId", createdChatId));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erro ao criar chat: " + e.getMessage()));
        }
    }
}