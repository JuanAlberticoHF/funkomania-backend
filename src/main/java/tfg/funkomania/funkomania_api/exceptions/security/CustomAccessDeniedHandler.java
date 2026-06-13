package tfg.funkomania.funkomania_api.exceptions.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>Clase personalizada para manejar las excepciones de acceso denegado en la aplicación.</p>
 * <p>Esta clase implementa la interfaz {@link AccessDeniedHandler} de Spring Security </p>
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final SecurityResponseHandler securityResponseHandler;

    public CustomAccessDeniedHandler(SecurityResponseHandler securityResponseHandler) {
        this.securityResponseHandler = securityResponseHandler;
    }

    @Override
    public void handle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                       @NonNull AccessDeniedException accessDeniedException) throws IOException {
        securityResponseHandler.handle(
                response,
                HttpStatus.FORBIDDEN,
                "Acceso denegado",
                "No tienes permisos suficientes para acceder a este recurso."
        );
    }
}
