package es.anusky.rating_books.shared;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookStarOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BookStar API")
                        .description("Backend de la aplicación de reseñas de libros BookStar 📚⭐")
                        .version("v1.0.0")
                );
    }
}

