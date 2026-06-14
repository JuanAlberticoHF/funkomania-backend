package tfg.funkomania.funkomania_api.dtos.security_dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfg.funkomania.funkomania_api.persistence.enums.RoleEnum;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para la validación de TokenResponse.
 * @version 1.0.2
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
        TokenResponse response = new TokenResponse("token", "user@example.com", "Nombre", RoleEnum.CLIENTE);

        assertThat(response.token()).isEqualTo("token");
        assertThat(response.username()).isEqualTo("user@example.com");
        assertThat(response.name()).isEqualTo("Nombre");
        assertThat(response.role()).isEqualTo(RoleEnum.CLIENTE);
    }

    /**
     * Debe detectar campos obligatorios en blanco.
     */
    @Test
    void validar_camposObligatorios_deberiaDetectarBlancos() {
        TokenResponse response = new TokenResponse("", "", "", null);

        Set<ConstraintViolation<TokenResponse>> violations = validator.validate(response);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("token"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("role"));
    }
}

