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
 * Pruebas unitarias para la validación de TokenResponse.
 * @version 1.0.1
 * @since 0.1.0
 */
class TokenResponseTest {

    private Validator validator;

    @BeforeEach
    void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * Debe conservar los valores proporcionados en el record.
     */
    @Test
    void constructor_deberiaConservarValores() {
        TokenResponse response = new TokenResponse("token", "user@example.com", "Nombre");

        assertThat(response.token()).isEqualTo("token");
        assertThat(response.username()).isEqualTo("user@example.com");
        assertThat(response.name()).isEqualTo("Nombre");
    }

    /**
     * Debe detectar campos obligatorios en blanco.
     */
    @Test
    void validar_camposObligatorios_deberiaDetectarBlancos() {
        TokenResponse response = new TokenResponse("", "", "");

        Set<ConstraintViolation<TokenResponse>> violations = validator.validate(response);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("token"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }
}

