package tfg.funkomania.funkomania_api.exceptions.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * <p>Clase encargada de manejar las respuestas de seguridad cuando un usuario no tiene los permisos necesarios para acceder a un recurso.</p>
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Component
public class SecurityResponseHandler {

    private final ObjectMapper objectMapper;

    public SecurityResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handle(HttpServletResponse response, HttpStatus status, String title, String detail) throws IOException {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), problemDetail);
    }
}
