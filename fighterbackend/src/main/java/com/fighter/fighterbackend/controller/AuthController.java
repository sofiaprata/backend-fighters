package com.fighter.fighterbackend.controller;

import com.fighter.fighterbackend.dto.RegisterRequestDTO;
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
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            UserRecord userRecord = authService.createUser(
                    registerRequestDTO.getEmail(),
                    registerRequestDTO.getPassword(),
                    registerRequestDTO.getDisplayName()
            );
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuário registrado com sucesso!");
            response.put("uid", userRecord.getUid());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (FirebaseAuthException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro ao registrar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (ExecutionException | InterruptedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor ao salvar perfil: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");
        if (idToken == null || idToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token não fornecido."));
        }

        try {
            FirebaseToken decodedToken = authService.verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String displayName = decodedToken.getName();

            Map<String, Object> userProfile = authService.getUserProfileFromFirestore(uid);

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "Token validado com sucesso!");
            successResponse.put("uid", uid);
            successResponse.put("email", email);
            successResponse.put("displayName", displayName);

            if (userProfile != null) {
                successResponse.put("userProfile", userProfile);
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