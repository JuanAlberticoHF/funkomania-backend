package tfg.funkomania.funkomania_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.NullEmailAutenticationException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.ProductoNotFoundException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioAlreadyExistsException;

/**
 * <p>Manejador global de excepciones controladas de la API de Funkomania.</p>
 *
 * <p>Esta clase captura y maneja las excepciones que ocurren en toda la aplicación, proporcionando respuestas HTTP
 * utilizando el formato ProblemDetail.</p>
 *
 * @author JuanAlbeticoHF
 * @version 0.3.0
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

    /**
     * Maneja la excepción {@code ProductoNotFoundException} que se lanza cuando no se encuentra un producto en la base de datos.
     * @param ex Excepción de tipo {@code ProductoNotFoundException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 404 (Not Found).
     */
    @ExceptionHandler(ProductoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleException(ProductoNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Producto no encontrado");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code NullEmailAutenticationException} que se lanza cuando el email de autenticación es nulo.
     * @param ex Excepción de tipo {@code NullEmailAutenticationException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 500 (Internal Server Error).
     */
    @ExceptionHandler(NullEmailAutenticationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleException(NullEmailAutenticationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setTitle("Error en la autenticación: email nulo");
        return problemDetail;
    }
}
