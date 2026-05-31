package tfg.funkomania.funkomania_api.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioAlreadyExistsException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para GlobalExceptionHandler.
 */
class GlobalExceptionHandlerTest {

    /**
     * Debe devolver un ProblemDetail con estado 409 y titulo configurado.
     */
    @Test
    void handleException_deberiaDevolverProblemDetailConflict() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        UsuarioAlreadyExistsException exception = new UsuarioAlreadyExistsException("El usuario ya existe");

        ProblemDetail problemDetail = handler.handleException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(problemDetail.getTitle()).isEqualTo("El usuario ya existe");
        assertThat(problemDetail.getDetail()).isEqualTo("El usuario ya existe");
    }
}

