package com.fighter.fighterbackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class ControleUsuario {

    @GetMapping
    public Usuario getUsuario() {
        return new Usuario("01", "Sofia", "sofia@email", "1234", "feminino",
                54.2, 158, "karate" , "faixa marrom",
                "fortaleza","urlfotoperfil", 5.0, "oi, vamos treinar?");
    }

}
