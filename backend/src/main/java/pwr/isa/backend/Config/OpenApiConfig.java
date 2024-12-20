package pwr.isa.backend.Config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@OpenAPIDefinition(
        info = @Info(
                /*
                contact = @Contact(
                        name = "Marek Kocik",
                        email = "marekkocikw@gmail.com"),
                */
                title = "Backend Serivce for gaming students platform",
                version = "0.1",
                description = "API for managing user posters, team posters, player ratings, and user accounts"),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local development server")
        }

)
@SecurityScheme(
        name = "bearerAuth",
        description = "Custom Bearer Token authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "CustomToken",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {
}
