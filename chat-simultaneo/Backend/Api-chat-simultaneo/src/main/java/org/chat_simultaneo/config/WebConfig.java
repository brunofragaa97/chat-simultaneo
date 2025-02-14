package org.chat_simultaneo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Essa classe vai ser usada para configuração no Spring
public class WebConfig implements WebMvcConfigurer {

    // Configuração global de CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permitindo requisições de qualquer origem (mude para um domínio específico, se necessário)
        registry.addMapping("/**") // Permitindo CORS para todas as rotas
                .allowedOrigins("http://localhost:5173") // Origem do frontend (React)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos HTTP permitidos
                .allowedHeaders("*"); // Permitindo todos os cabeçalhos
    }
}
