package com.fighter.fighterbackend.service;

import com.fighter.fighterbackend.dto.UsuarioRequestDTO;
import com.fighter.fighterbackend.dto.UsuarioResponseDTO;
import com.fighter.fighterbackend.entity.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    final String FB_COLLECTION_NAME = "usuarios";
    private final Firestore firestore;

    @Autowired
    public UsuarioService(Firestore firestore) {
        this.firestore = firestore;
    }

    private UsuarioResponseDTO convertToDto(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setSexo(usuario.getSexo());
        dto.setPesoCategoria(usuario.getPesoCategoria());
        dto.setAlturaEmCm(usuario.getAlturaEmCm());
        dto.setArteMarcial(usuario.getArteMarcial());
        dto.setNivelExperiencia(usuario.getNivelExperiencia());
        dto.setLocalizacao(usuario.getLocalizacao());
        dto.setFotoPerfilUrl(usuario.getFotoPerfilUrl());
        dto.setReputacao(usuario.getReputacao());
        dto.setDescricao(usuario.getDescricao());

        if (usuario.getCreatedAt() != null) {
            dto.setCreatedAt(usuario.getCreatedAt());
        }
        if (usuario.getLastActive() != null) {
            dto.setLastActive(usuario.getLastActive());
        }

        return dto;
    }

    public UsuarioResponseDTO getUsuarioById(String id) throws InterruptedException, ExecutionException {
        DocumentReference documentReference = firestore.collection(FB_COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Usuario usuario = document.toObject(Usuario.class);
            if (usuario != null) {
                usuario.setId(document.getId());
                if (document.contains("createdAt")) {
                    usuario.setCreatedAt(document.getTimestamp("createdAt").toDate());
                }
                if (document.contains("lastActive")) {
                    usuario.setLastActive(document.getTimestamp("lastActive").toDate());
                }
                return convertToDto(usuario);
            }
        }
        return null;
    }

    public List<UsuarioResponseDTO> getUsuariosBySexo(String sexo) throws ExecutionException, InterruptedException {
        List<Usuario> users = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(FB_COLLECTION_NAME)
                .whereEqualTo("sexo", sexo)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Usuario usuario = document.toObject(Usuario.class);
            if (usuario != null) {
                usuario.setId(document.getId());
                if (document.contains("createdAt")) {
                    usuario.setCreatedAt(document.getTimestamp("createdAt").toDate());
                }
                if (document.contains("lastActive")) {
                    usuario.setLastActive(document.getTimestamp("lastActive").toDate());
                }
                users.add(usuario);
            }
        }
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> getUsuariosByArteMarcial(String arteMarcial) throws ExecutionException, InterruptedException {
        List<Usuario> usuarios = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection(FB_COLLECTION_NAME)
                .whereArrayContains("arteMarcial", arteMarcial)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Usuario usuario = document.toObject(Usuario.class);
            if (usuario != null) {
                usuario.setId(document.getId());
                if (document.contains("createdAt")) {
                    usuario.setCreatedAt(document.getTimestamp("createdAt").toDate());
                }
                if (document.contains("lastActive")) {
                    usuario.setLastActive(document.getTimestamp("lastActive").toDate());
                }
                usuarios.add(usuario);
            }
        }
        return usuarios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO updateUsuario(String id, UsuarioRequestDTO usuarioDto) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(FB_COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Usuario existingUsuario = document.toObject(Usuario.class);
            if (existingUsuario == null) {
                return null;
            }

            if (usuarioDto.getNome() != null) {
                existingUsuario.setNome(usuarioDto.getNome());
            }
            if (usuarioDto.getEmail() != null) {
                existingUsuario.setEmail(usuarioDto.getEmail());
            }
            if (usuarioDto.getSexo() != null) {
                existingUsuario.setSexo(usuarioDto.getSexo());
            }
            if (usuarioDto.getPesoCategoria() != null) {
                existingUsuario.setPesoCategoria(usuarioDto.getPesoCategoria());
            }
            if (usuarioDto.getAlturaEmCm() != 0) {
                existingUsuario.setAlturaEmCm(usuarioDto.getAlturaEmCm());
            }
            if (usuarioDto.getArteMarcial() != null) {
                existingUsuario.setArteMarcial(usuarioDto.getArteMarcial());
            }
            if (usuarioDto.getNivelExperiencia() != null) {
                existingUsuario.setNivelExperiencia(usuarioDto.getNivelExperiencia());
            }
            if (usuarioDto.getLocalizacao() != null) {
                existingUsuario.setLocalizacao(usuarioDto.getLocalizacao());
            }
            if (usuarioDto.getFotoPerfilUrl() != null) {
                existingUsuario.setFotoPerfilUrl(usuarioDto.getFotoPerfilUrl());
            }
            if (usuarioDto.getDescricao() != null) {
                existingUsuario.setDescricao(usuarioDto.getDescricao());
            }

            existingUsuario.setLastActive(new Date());

            ApiFuture<WriteResult> writeResult = docRef.set(existingUsuario);
            writeResult.get();

            existingUsuario.setId(id);
            if (document.contains("createdAt")) {
                existingUsuario.setCreatedAt(document.getTimestamp("createdAt").toDate());
            }

            return convertToDto(existingUsuario);
        }
        return null;
    }

    public String deleteUsuario(String id) throws InterruptedException, ExecutionException {
        ApiFuture<WriteResult> deleteResult = firestore.collection(FB_COLLECTION_NAME).document(id).delete();
        deleteResult.get();
        return "Usuário com ID " + id + " deletado com sucesso!";
    }
}