package com.fighter.fighterbackend.controller;

import com.fighter.fighterbackend.entity.Usuario;
import com.fighter.fighterbackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {


    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @Autowired
    public UsuarioService usuarioService;

    @PostMapping("/create")
    public String createUsuario(@RequestBody Usuario usuario) throws InterruptedException, ExecutionException {
        return usuarioService.createUsuario(usuario);
    }

    @GetMapping("/get")
    public Usuario getUsuario(@RequestParam String id) throws InterruptedException, ExecutionException {
        return usuarioService.getUsuario(id);
    }

    @PutMapping("/update")
    public String updateUsuario(@RequestBody Usuario usuario) throws InterruptedException, ExecutionException {
        return usuarioService.updateUsuario(usuario);
    }

    @DeleteMapping("/delete")
    public String updateUsuario(@RequestParam String id) throws InterruptedException, ExecutionException {
        return usuarioService.deleteUsuario(id);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testGetEndpoint() {
        return ResponseEntity.ok("Test Get Endpoint is Working");
    }
}
