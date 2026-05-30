package tfg.funkomania.funkomania_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioAlreadyExistsException;

/**
 * <p>Manejador global de excepciones controladas de la API de Funkomania.</p>
 *
 * <p>Esta clase captura y maneja las excepciones que ocurren en toda la aplicación, proporcionando respuestas HTTP
 * utilizando el formato ProblemDetail.</p>
 *
 * @author JuanAlbeticoHF
 * @version 0.1.1
 * @since 0.1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja la excepción {@code UsuarioAlreadyExistsException} que se lanza cuando se intenta registrar un usuario con un email que ya existe.
     * @param ex Excepción de tipo {@code UsuarioAlreadyExistsException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 409 (Conflict).
     */
    @ExceptionHandler(UsuarioAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleException(UsuarioAlreadyExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("El usuario ya existe");
        return problemDetail;
    }
}
