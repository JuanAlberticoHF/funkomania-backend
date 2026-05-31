package tfg.funkomania.funkomania_api.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * <p>Manejador global de excepciones de la API de Funkomania para excepciones no controladas.</p>
 *
 * <p>Esta clase captura y maneja las excepciones que ocurren en toda la aplicación, proporcionando respuestas HTTP
 * utilizando el formato ProblemDetail.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
@RestControllerAdvice
public class CommonExceptionHandler {
    /**
     * Maneja la excepción {@code MethodArgumentNotValidException} cuando el argumento o parametro no es valido (capa web).
     * @param ex Excepción de tipo {@code MethodArgumentNotValidException}.
     * @return ProblemDetail con el mensaje de error y el código de estado HTTP {@code 400 Bad Request}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail methodArgumentNotValidException(MethodArgumentNotValidException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Argumento no válido");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code MethodArgumentTypeMismatchException} cuando el argumento o parametro no es un tipo valido.
     * @param ex Excepción de tipo {@code MethodArgumentTypeMismatchException}.
     * @return Un objeto ProblemDetail con el mensaje de error y un código de estado HTTP {@code 400 Bad Request}.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Error de tipo de argumento");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code ConstraintViolationException} cuando varios argumentos no son correctos (capa web) o
     * al validar un registro que no es correcto base entidad (Spring Data JPA/Hibernate)
     * @param ex Excepción de tipo {@code ConstraintViolationException}.
     * @return Un objeto {@code ProblemDetail} con detalles sobre el error de validación.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail constraintViolationException(ConstraintViolationException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Error de validación");
        return problemDetail;
    }

    /**
     * Maneja la excepción {@code DataIntegrityViolationException} cuando la base de datos emite un error.
     * @param ex Excepción de tipo {@code DataIntegrityViolationException}.
     * @return Un objeto {@code ProblemDetail} con detalles sobre el error de integridad de datos.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail dataIntegrityViolationException(DataIntegrityViolationException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Error de integridad de datos");
        return problemDetail;
    }
}
