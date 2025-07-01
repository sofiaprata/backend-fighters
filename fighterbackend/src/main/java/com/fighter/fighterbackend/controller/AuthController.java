package com.fighter.fighterbackend.controller;

import com.fighter.fighterbackend.dto.RegisterRequest;
import com.fighter.fighterbackend.service.AuthService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            UserRecord userRecord = authService.createUser(
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getDisplayName()
            );
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuário registrado com sucesso!");
            response.put("uid", userRecord.getUid());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (FirebaseAuthException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro ao registrar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. Verificar se o cabeçalho Authorization existe e tem o formato correto
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Cabeçalho de autorização inválido ou ausente.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            // 2. Extrair o ID Token do cabeçalho
            String idToken = authorizationHeader.substring(7); // Remove "Bearer " (7 caracteres)

            // 3. Verificar o token com o Firebase Auth
            FirebaseToken decodedToken = authService.verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String displayName = decodedToken.getName(); // Pode ser nulo se não definido

            // Opcional: Buscar dados adicionais do perfil no Firestore
            Map<String, Object> userProfile = authService.getUserProfileFromFirestore(uid);

            // 4. Construir a resposta com base nos dados do token e do perfil
            // Adapte sua classe LoginResponse ou crie uma DTO de resposta adequada
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "Token validado com sucesso!");
            successResponse.put("uid", uid);
            successResponse.put("email", email);
            successResponse.put("displayName", displayName);

            if (userProfile != null) {
                successResponse.put("userProfile", userProfile); // Adiciona o perfil ao corpo da resposta
            }

            return ResponseEntity.ok(successResponse);

        } catch (FirebaseAuthException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Token inválido ou expirado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
