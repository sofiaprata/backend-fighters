package com.fighter.fighterbackend.controller;

import com.fighter.fighterbackend.dto.LoginRequest;
import com.fighter.fighterbackend.dto.LoginResponse;
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

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            FirebaseToken decodedToken = authService.verifyIdToken(loginRequest.getIdToken());
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String displayName = decodedToken.getName(); // Pode ser nulo se não definido

            // Opcional: Buscar dados adicionais do perfil no Firestore
            Map<String, Object> userProfile = authService.getUserProfileFromFirestore(uid);

            // Construir a resposta com base nos dados do token e do perfil
            LoginResponse response = new LoginResponse(uid, email, displayName, "Login bem-sucedido!");
            if (userProfile != null) {
                // Adicione outros campos do perfil se necessário
                // response.setSomeOtherField((String) userProfile.get("someOtherField"));
            }

            return ResponseEntity.ok(response);

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
