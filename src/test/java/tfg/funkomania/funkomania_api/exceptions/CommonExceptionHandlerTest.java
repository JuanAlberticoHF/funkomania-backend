 package tfg.funkomania.funkomania_api.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para CommonExceptionHandler.
 *
 * @version 1.0.0
 * @since 0.1.0
 */
class CommonExceptionHandlerTest {

    /**
     * Debe devolver un ProblemDetail para argumentos no validos.
     */
    @Test
    void methodArgumentNotValidException_deberiaDevolverProblemDetail() throws Exception {
        CommonExceptionHandler handler = new CommonExceptionHandler();
        MethodParameter parameter = obtenerParametroMetodo();
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        ProblemDetail problemDetail = handler.methodArgumentNotValidException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getTitle()).isEqualTo("Argumento no válido");
    }

    /**
     * Debe devolver un ProblemDetail para tipo de argumento incorrecto.
     */
    @Test
    void methodArgumentTypeMismatchException_deberiaDevolverProblemDetail() throws Exception {
        CommonExceptionHandler handler = new CommonExceptionHandler();
        MethodParameter parameter = obtenerParametroMetodo();
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "abc",
                Integer.class,
                "id",
                parameter,
                new IllegalArgumentException("bad")
        );

        ProblemDetail problemDetail = handler.methodArgumentTypeMismatchException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getTitle()).isEqualTo("Error de tipo de argumento");
    }

    /**
     * Debe devolver un ProblemDetail para errores de validacion.
     */
    @Test
    void constraintViolationException_deberiaDevolverProblemDetail() {
        CommonExceptionHandler handler = new CommonExceptionHandler();
        ConstraintViolationException exception = new ConstraintViolationException(
                "validation error",
                Collections.<ConstraintViolation<?>>emptySet()
        );

        ProblemDetail problemDetail = handler.constraintViolationException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getTitle()).isEqualTo("Error de validación");
    }

    /**
     * Debe devolver un ProblemDetail para errores de integridad de datos.
     */
    @Test
    void dataIntegrityViolationException_deberiaDevolverProblemDetail() {
        CommonExceptionHandler handler = new CommonExceptionHandler();
        DataIntegrityViolationException exception = new DataIntegrityViolationException("db error");

        ProblemDetail problemDetail = handler.dataIntegrityViolationException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getTitle()).isEqualTo("Error de integridad de datos");
    }

    private MethodParameter obtenerParametroMetodo() throws Exception {
        Method method = CommonExceptionHandlerTest.class.getDeclaredMethod("metodoEjemplo", String.class);
        return new MethodParameter(method, 0);
    }

    private void metodoEjemplo(String value) {
        // Metodo auxiliar para construir MethodParameter.
    }
}
