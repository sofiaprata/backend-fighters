package com.fighter.fighterbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Indica que esta classe contém configurações para o Spring
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //Classe criada pra testar os endpoints num front end web

        registry.addMapping("/**") // Aplica a configuração CORS para TODOS os endpoints da sua API
                .allowedOrigins(
                        "http://localhost:5500",    // Adicione a URL exata do seu Live Server
                        "http://127.0.0.1:5500",     // Live Server também pode usar 127.0.0.1
                        // Adicione aqui outras origens se você tiver, ex: "http://localhost:3000" para React/Angular
                        "http://localhost:62863"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite os métodos HTTP comuns
                .allowedHeaders("*") // Permite todos os cabeçalhos na requisição (incluindo 'Authorization')
                .allowCredentials(true) // Importante se você estiver usando cookies ou autenticação baseada em sessão
                .maxAge(3600); // Define por quanto tempo (em segundos) a resposta preflight pode ser armazenada em cache
    }
}
