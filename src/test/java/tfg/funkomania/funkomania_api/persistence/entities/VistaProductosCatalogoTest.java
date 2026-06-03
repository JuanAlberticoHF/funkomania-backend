package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import jakarta.validation.ConstraintViolation;

/**
 * Pruebas unitarias para la entidad Usuario.
 *
 * @version 1.0.0
 * @since 0.2.0
 */
class VistaProductosCatalogoTest {
    /** Validador para las pruebas de validación de la entidad VistaProductosCatalogo. */
    private Validator validator;

    @BeforeEach
    void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * Verifica que el constructor de la clase VistaProductosCatalogo permita crear una instancia correctamente.
     */
    @Test
    void constructor_deberiaPermitirCrearVistaProductosCatalogo() {
        tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo v =
                new tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo(
                        1L,
                        "Prod",
                        BigDecimal.valueOf(10),
                        BigDecimal.valueOf(12),
                        true,
                        BigDecimal.valueOf(5),
                        LocalDateTime.now().plusDays(1),
                        BigDecimal.valueOf(9),
                        BigDecimal.valueOf(10),
                        BigDecimal.valueOf(21),
                        5,
                        "img",
                        "desc",
                        true,
                        1L,
                        "Cat",
                        "Padre"
                );

        assertEquals("Prod", v.getNombre());
    }

    /**
     * Válida que el constructor de la clase VistaProductosCatalogo permita crear una instancia con todos los campos correctamente.
     */
    @Test
    void validar_deberiaDetectarCamposObligatorios() {
        tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo v =
                new tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo();
        Set<ConstraintViolation<tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo>> violations = validator.validate(v);
        assertFalse(violations.isEmpty());
    }

    /**
     * Válida que el validador detecte campos nulos en la clase VistaProductosCatalogo.
     */
    @Test
    void validar_deberiaDetectarCamposNulo() {
        tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo v =
                tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo.builder()
                        .nombre(null)
                        .build();
        Set<ConstraintViolation<tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo>> violations = validator.validate(v);
        assertFalse(violations.isEmpty());
    }

    /**
     * Válida que el validador detecte campos vacíos en la clase VistaProductosCatalogo.
     */
    @Test
    void validar_deberiaDetectarCampoVacio() {
        tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo v =
                tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo.builder()
                        .nombre("")
                        .build();
        Set<ConstraintViolation<tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo>> violations = validator.validate(v);
        assertFalse(violations.isEmpty());
    }
}
