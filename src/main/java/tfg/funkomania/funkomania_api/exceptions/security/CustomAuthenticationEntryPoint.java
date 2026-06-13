package tfg.funkomania.funkomania_api.exceptions.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>Clase que maneja las excepciones de autenticación y autorización en la aplicación.</p>
 * <p>Implementa las interfaces {@link AuthenticationEntryPoint} y {@link AccessDeniedHandler}
 * para gestionar las respuestas cuando un usuario no autenticado intenta acceder a un recurso protegido o cuando un
 * usuario autenticado intenta acceder a un recurso para el cual no tiene permisos.</p>
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SecurityResponseHandler securityResponseHandler;

    public CustomAuthenticationEntryPoint(SecurityResponseHandler securityResponseHandler) {
        this.securityResponseHandler = securityResponseHandler;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        securityResponseHandler.handle(
                response,
                HttpStatus.UNAUTHORIZED,
                "No autorizado",
                authException.getMessage()
        );
    }
}
