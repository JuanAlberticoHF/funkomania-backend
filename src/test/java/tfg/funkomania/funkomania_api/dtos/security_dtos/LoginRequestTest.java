package tfg.funkomania.funkomania_api.dtos.security_dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para la validacion de LoginRequest.
 */
class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory();) {
            validator = factory.getValidator();
        }
    }

    /**
     * Debe conservar los valores proporcionados en el record.
     */
    @Test
    void constructor_deberiaConservarValores() {
        LoginRequest request = new LoginRequest("user@example.com", "Password123");

        assertThat(request.username()).isEqualTo("user@example.com");
        assertThat(request.password()).isEqualTo("Password123");
    }

    /**
     * Debe detectar campos obligatorios en blanco.
     */
    @Test
    void validar_camposObligatorios_deberiaDetectarBlancos() {
        LoginRequest request = new LoginRequest("", "");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }
}

