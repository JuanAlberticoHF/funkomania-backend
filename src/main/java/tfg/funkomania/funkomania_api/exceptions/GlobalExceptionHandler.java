package tfg.funkomania.funkomania_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.*;

/**
 * <p>Manejador global de excepciones controladas de la API de Funkomania.</p>
 *
 * <p>Esta clase captura y maneja las excepciones que ocurren en toda la aplicación, proporcionando respuestas HTTP
 * utilizando el formato ProblemDetail.</p>
 *
 * @author JuanAlbeticoHF
 * @version 0.12.0
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

    /**
     * Maneja la excepción {@code UsuarioNotFoundException} que se lanza cuando no se encuentra un usuario en la base de datos.
     * @param ex Excepción de tipo {@code UsuarioNotFoundException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 404 (Not Found).
     */
    @ExceptionHandler(UsuarioNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleException(UsuarioNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Usuario no encontrado");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code DireccionNotFoundException} que se lanza cuando no se encuentra una direccion en la base de datos.
     * @param ex Excepción de tipo {@code DireccionNotFoundException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 404 (Not Found).
     */
    @ExceptionHandler(DireccionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleException(DireccionNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Direccion no encontrada");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code ProductoYaEnListaDeseadosException} que se lanza cuando se intenta agregar un
     * producto a la lista de deseados de un usuario y ese producto ya está en la lista.
     * @param ex Excepción de tipo {@code ProductoYaEnListaDeseadosException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 409 (Conflict).
     */
    @ExceptionHandler(ProductoYaEnListaDeseadosException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleException(ProductoYaEnListaDeseadosException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Producto ya en lista de deseados");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code NotificacionNotFoundException} que se lanza cuando no se encuentra una notificación en la base de datos.
     * @param ex Excepción de tipo {@code NotificacionNotFoundException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 409 (CONFLICT).
     */
    @ExceptionHandler(NotificacionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleException(NotificacionNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Notificación no encontrada");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code NotNotificationOwnerException} que se lanza cuando un usuario intenta acceder o modificar una notificación que no le pertenece.
     * @param ex Excepción de tipo {@code NotNotificationOwnerException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 409 (CONFLICT).
     */
    @ExceptionHandler(NotNotificationOwnerException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleException(NotNotificationOwnerException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("No eres el propietario de esta notificación");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code NotificacionYaLeidaException} que se lanza cuando la notificación ya ha sido leída.
     * @param ex Excepción de tipo {@code NotificacionYaLeidaException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 409 (CONFLICT).
     */
    @ExceptionHandler(NotificacionYaLeidaException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleException(NotificacionYaLeidaException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("La notificación ya ha sido leída");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code CategoriaNotFoundException} que se lanza cuando no se encuentra una categoria en la base de datos.
     * @param ex Excepción de tipo {@code CategoriaNotFoundException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 409 (CONFLICT).
     */
    @ExceptionHandler(CategoriaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleException(CategoriaNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Categoría no encontrada");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code CategoriaConProductosException} que se lanza cuando se intenta eliminar una categoría que tiene productos asociados.
     * @param ex Excepción de tipo {@code CategoriaConProductosException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 409 (CONFLICT).
     */
    @ExceptionHandler(CategoriaConProductosException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleException(CategoriaConProductosException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("No se puede eliminar la categoría porque tiene productos asociados");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code ProductoNoEliminadoException} que se lanza cuando se intenta eliminar un producto que no se puede eliminar por alguna razón.
     * @param ex Excepción de tipo {@code ProductoNoEliminadoException}.
     * @return Un objeto ProblemDetails con el mensaje de error y un código de estado HTTP 409 (CONFLICT).
     */
    @ExceptionHandler(ProductoNoEliminadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleException(ProductoNoEliminadoException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("No se pudo eliminar el producto");
        return problemDetail;
    }
}
