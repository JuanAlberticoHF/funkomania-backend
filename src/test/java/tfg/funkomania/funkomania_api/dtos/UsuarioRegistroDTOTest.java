package tfg.funkomania.funkomania_api.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioRegistroDTO;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para la validación de UsuarioRegistroDTO.
 *
 * @version 1.0.1
 * @since 0.1.0
 */
class UsuarioRegistroDTOTest {

    private Validator validator;

    @BeforeEach
    void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * Debe mapear los campos con los getters y setters.
     */
    @Test
    void gettersYSetters_deberianMapearCampos() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
        dto.setEmail("user@example.com");
        dto.setPassword("Password123");
        dto.setNombre("Nombre");

        assertThat(dto.getEmail()).isEqualTo("user@example.com");
        assertThat(dto.getPassword()).isEqualTo("Password123");
        assertThat(dto.getNombre()).isEqualTo("Nombre");
    }

    /**
     * Debe detectar campos obligatorios en blanco.
     */
    @Test
    void validar_camposObligatorios_deberiaDetectarBlancos() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO("", "", "");

        Set<ConstraintViolation<UsuarioRegistroDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nombre"));
    }

    /**
     * Debe validar la longitud maxima de email, password y name.
     */
    @Test
    void validar_longitudesMaximas_deberiaDetectarExcesos() {
        String largoEmail = "a".repeat(256);
        String largoPassword = "b".repeat(256);
        String largoNombre = "c".repeat(51);

        UsuarioRegistroDTO dto = new UsuarioRegistroDTO(largoEmail, largoPassword, largoNombre);

        Set<ConstraintViolation<UsuarioRegistroDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nombre"));
    }
}

