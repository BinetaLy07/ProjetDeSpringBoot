package com.examen.projet_spring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eduPlusOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EduPlus API")
                        .description("API REST de gestion scolaire — Étudiants, Enseignants, Cours, Inscriptions, Upload de fichiers")
                        .version("1.0.0"));
    }
}