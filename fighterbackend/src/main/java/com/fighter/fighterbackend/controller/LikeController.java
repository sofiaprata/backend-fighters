package com.fighter.fighterbackend.controller;

import com.fighter.fighterbackend.entity.Like;
import com.fighter.fighterbackend.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/create")
    public ResponseEntity<?> createLike(@RequestParam String senderId, @RequestParam String receiverId) {
        try {
            String likeId = likeService.createLike(senderId, receiverId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Like criado/processado com sucesso!");
            response.put("likeId", likeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro ao criar/processar like: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<String>> getLikedUsers(@PathVariable String userId) {
        try {
            List<String> likedUsers = likeService.getLikedUsers(userId);
            return new ResponseEntity<>(likedUsers, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<List<String>> getLikesReceivedByUser(@PathVariable String userId) {
        try {
            List<String> likers = likeService.getLikesReceivedByUser(userId);
            return new ResponseEntity<>(likers, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> hasLiked(@RequestParam String senderId, @RequestParam String receiverId) {
        try {
            boolean hasLiked = likeService.hasLiked(senderId, receiverId);
            return new ResponseEntity<>(hasLiked, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}