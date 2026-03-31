package com.bellibabs.trackingapi.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI trackingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tracking API")
                        .description("""
                                API de suivi GPS en temps réel.

                                **REST** : création et consultation de trajets.

                                **WebSocket (STOMP)** :
                                - Connexion : `ws://localhost:8080/ws`
                                - Envoi position : `/app/position`
                                - Réception live : `/topic/trajet/{trajetId}`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("BelliBabs")
                                .email("contact@bellibabs.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local")
                ));
    }
}