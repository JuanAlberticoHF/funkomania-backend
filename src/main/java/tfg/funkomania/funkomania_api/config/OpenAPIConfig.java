package tfg.funkomania.funkomania_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Configuración de OpenAPI para la documentación de la API REST de E-Commerce.</p>
 *
 * <p>Esta clase define la información general de la API y el esquema de seguridad para la autenticación y autorización JWT.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.3
 * @since 0.4.0
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Funkomania API",
                version = "0.5.0-RELEASE-1",
                description = "Documentación de la API de Funkomania"
        )
)
@SecurityScheme(
        name = "Bearer Authentication", // Identificador interno
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Pega el token en la casilla de texto (sin la palabra 'Bearer')"
)
public class OpenAPIConfig {}
