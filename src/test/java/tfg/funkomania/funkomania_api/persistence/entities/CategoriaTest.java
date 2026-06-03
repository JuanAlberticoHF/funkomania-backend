package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import jakarta.validation.ConstraintViolation;


/**
 * Pruebas unitarias para la entidad Categoria.
 *
 * @version 1.0.0
 * @since 0.2.0
 */
class CategoriaTest {
    /** Validador para las pruebas de validación de la entidad Categoria. */
    private Validator validator;

    @BeforeEach
    void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * Verifica que el constructor de la clase Categoria permita crear una instancia correctamente.
     */
    @Test
    void constructor_deberiaPermitirCrearCategoria() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria c =
                tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                        .nombre("Cat")
                        .build();
        assertEquals("Cat", c.getNombre());
    }

    /**
     * Verifica que el constructor de la clase Categoria permita crear una instancia sin un padre (categoría raíz).
     */
    @Test
    void constructor_deberiaPermitirCrearCategoriaSinPadre() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria c =
                tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                        .nombre("Raiz")
                        .categoriaPadre(null)
                        .build();
        assertNull(c.getCategoriaPadre());
    }

    /**
     * Válida que el validador detecte campos obligatorios en la clase Categoria.
     */
    @Test
    void validar_deberiaDetectarCamposObligatorios() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria c =
                new tfg.funkomania.funkomania_api.persistence.entities.Categoria();
        Set<ConstraintViolation<tfg.funkomania.funkomania_api.persistence.entities.Categoria>> violations = validator.validate(c);
        assertFalse(violations.isEmpty());
    }

    /**
     * Válida que el validador detecte campos nulos en la clase Categoria.
     */
    @Test
    void validar_deberiaDetectarCamposNulo() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria c =
                tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                        .nombre(null)
                        .build();
        Set<ConstraintViolation<tfg.funkomania.funkomania_api.persistence.entities.Categoria>> violations = validator.validate(c);
        assertFalse(violations.isEmpty());
    }

    /**
     * Válida que el validador detecte campos vacíos en la clase Categoria.
     */
    @Test
    void validar_deberiaDetectarCampoVacio() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria c =
                tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                        .nombre("")
                        .build();
        Set<ConstraintViolation<tfg.funkomania.funkomania_api.persistence.entities.Categoria>> violations = validator.validate(c);
        assertFalse(violations.isEmpty());
    }
}
