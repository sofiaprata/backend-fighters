package com.fighter.fighterbackend.controller;

import com.fighter.fighterbackend.dto.MatchRequestDTO;
import com.fighter.fighterbackend.dto.MatchResponseDTO;
import com.fighter.fighterbackend.entity.Match;
import com.fighter.fighterbackend.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    // Endpoint para buscar todos os matches de um usuário
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<MatchResponseDTO>> getMatchesForUser(@PathVariable String userId) {
        try {
            List<Match> matches = matchService.getMatchesForUsuarios(userId);
            List<MatchResponseDTO> matchDTOs = matches.stream()
                    .map(this::convertToMatchResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(matchDTOs);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para buscar um match específico pelo ID
    @GetMapping("/{matchId}")
    public ResponseEntity<MatchResponseDTO> getMatchById(@PathVariable String matchId) {
        try {
            Match match = matchService.getMatchById(matchId);
            if (match != null) {
                return ResponseEntity.ok(convertToMatchResponseDTO(match));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método auxiliar para converter Entidade para DTO de Resposta
    private MatchResponseDTO convertToMatchResponseDTO(Match match) {
        if (match == null) {
            return null;
        }
        MatchResponseDTO dto = new MatchResponseDTO();
        dto.setId(match.getId());
        dto.setUser1Id(match.getUser1Id());
        dto.setUser2Id(match.getUser2Id());
        // Considerando que Match não tem 'status' diretamente, ou você pode adicionar um status padrão
        dto.setStatus("MATCHED"); // Exemplo de status
        dto.setMatchedAt(match.getMatchedAt());
        return dto;
    }

    // Helper para criar um ID de match consistente (copiado do MatchService para uso aqui)
    private String createConsistentMatchId(String userAId, String userBId) {
        if (userAId.compareTo(userBId) < 0) {
            return userAId + "_" + userBId;
        } else {
            return userBId + "_" + userAId;
        }
    }
}
