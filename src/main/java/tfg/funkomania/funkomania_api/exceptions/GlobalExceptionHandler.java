package tfg.funkomania.funkomania_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioAlreadyExistsException;

/**
 * <p>Manejador global de excepciones de la API de Funkomania.</p>
 *
 * <p>Esta clase captura y maneja las excepciones que ocurren en toda la aplicación, proporcionando respuestas HTTP
 * utilizando el formato ProblemDetail.</p>
 *
 * @author JuanAlbeticoHF
 * @version 0.1.0
 * @since 0.1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsuarioAlreadyExistsException.class)
    public ProblemDetail handleException(UsuarioAlreadyExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Usuario ya existe");
        return problemDetail;
    }
}
