package com.fighter.fighterbackend.controller;

import com.fighter.fighterbackend.dto.UsuarioRequestDTO;
import com.fighter.fighterbackend.dto.UsuarioResponseDTO;
import com.fighter.fighterbackend.entity.Usuario;
import com.fighter.fighterbackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {


    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @Autowired
    public UsuarioService usuarioService;


    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable String id) {
        try {
            UsuarioResponseDTO usuarioDto = usuarioService.getUsuarioById(id);
            if (usuarioDto != null) {
                return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<UsuarioResponseDTO>> getUsuariosFiltrados(
            @RequestParam(required = false) String sexo,
            @RequestParam(required = false) String arteMarcial,
            @RequestParam(required = false) String localizacao,
            @RequestParam(required = false) String nivelExperiencia,
            @RequestParam(required = false) String pesoCategoria
            ) {
        try {
            List<UsuarioResponseDTO> usuarios = usuarioService.getUsuariosFiltrados(sexo, arteMarcial, localizacao, nivelExperiencia, pesoCategoria);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sexo/{sexo}")
    public ResponseEntity<List<UsuarioResponseDTO>> getUsuariosBySexo(@PathVariable String sexo) {
        try {
            List<UsuarioResponseDTO> usuarios = usuarioService.getUsuariosBySexo(sexo);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/arteMarcial/{arteMarcial}")
    public ResponseEntity<List<UsuarioResponseDTO>> getUsuariosByArteMarcial(@PathVariable String arteMarcial) {
        try {
            List<UsuarioResponseDTO> usuarios = usuarioService.getUsuariosByArteMarcial(arteMarcial);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(@PathVariable String id, @RequestBody UsuarioRequestDTO usuarioDto) {
        try {
            UsuarioResponseDTO updatedUsuarioDto = usuarioService.updateUsuario(id, usuarioDto);
            if (updatedUsuarioDto != null) {
                return new ResponseEntity<>(updatedUsuarioDto, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
