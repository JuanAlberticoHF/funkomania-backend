package tfg.funkomania.funkomania_api.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class CommonExceptionHandlerTest {

    private final CommonExceptionHandler handler = new CommonExceptionHandler();

    @Test
    void methodArgumentNotValidException_deberiaDevolverProblemDetail() throws Exception {
        MethodParameter parameter = obtenerParametroMetodo();
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        ProblemDetail problemDetail = handler.methodArgumentNotValidException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getTitle()).isEqualTo("Argumento no válido");
    }

    @Test
    void methodArgumentTypeMismatchException_deberiaDevolverProblemDetail() throws Exception {
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

    @Test
    void constraintViolationException_deberiaDevolverProblemDetail() {
        ConstraintViolationException exception = new ConstraintViolationException(
                "validation error",
                Collections.emptySet()
        );

        ProblemDetail problemDetail = handler.constraintViolationException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getTitle()).isEqualTo("Error de validación");
    }

    @Test
    void dataIntegrityViolationException_deberiaDevolverProblemDetail() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("db error");

        ProblemDetail problemDetail = handler.dataIntegrityViolationException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getTitle()).isEqualTo("Error de integridad de datos");
    }

    @Test
    void invalidDataAccessResourceUsageException_deberiaDevolverProblemDetail() {
        InvalidDataAccessResourceUsageException exception = new InvalidDataAccessResourceUsageException("db error");

        ProblemDetail problemDetail = handler.invalidDataAccessResourceUsageException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getTitle()).isEqualTo("Error de uso de recurso de acceso a datos");
    }

    @Test
    void illegalArgumentException_deberiaDevolverProblemDetail() {
        IllegalArgumentException exception = new IllegalArgumentException("arg error");

        ProblemDetail problemDetail = handler.illegalArgumentException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getTitle()).isEqualTo("Argumento ilegal");
    }

    private MethodParameter obtenerParametroMetodo() throws Exception {
        Method method = CommonExceptionHandlerTest.class.getDeclaredMethod("metodoEjemplo", String.class);
        return new MethodParameter(method, 0);
    }

    private void metodoEjemplo(String value) {
        // Metodo auxiliar para construir MethodParameter.
    }
}
