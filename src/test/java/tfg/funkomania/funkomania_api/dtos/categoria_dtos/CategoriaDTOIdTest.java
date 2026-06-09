package tfg.funkomania.funkomania_api.dtos.categoria_dtos;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;

/**
 * Pruebas unitarias para la clase {@link CategoriaDTOId}.
 *
 * @version 1.0.0
 * @since 0.2.0
 */
class CategoriaDTOIdTest {
    /** Validador para las pruebas de validación de la clase CategoriaDTOId. */
    private Validator validator;

    @BeforeEach
    void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * Verifica que el constructor de la clase CategoriaDTOId permita crear una instancia correctamente.
     */
    @Test
    void constructor_deberiaPermitirCrearBaseACategoria() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria padre =
                tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                        .id(1L)
                        .nombre("Padre")
                        .build();

        CategoriaDTOId dto = new CategoriaDTOId(padre);

        assertEquals(padre.getId(), dto.getId());
        assertEquals(padre.getNombre(), dto.getNombre());
    }

    /**
     * Verifica que el constructor de la clase CategoriaDTOId permita crear una instancia con solo los campos obligatorios correctamente.
     */
    @Test
    void constructor_deberiaPermitirTodosLosValores() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria padre =
                tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                        .id(1L)
                        .nombre("Padre")
                        .build();

        CategoriaDTOId dto = new CategoriaDTOId(2L, "Hija", new CategoriaDTOIdNoCategoriaPadre(padre));

        assertEquals(2L, dto.getId());
        assertEquals("Hija", dto.getNombre());
        assertNotNull(dto.getCategoriaPadre());
    }

    /**
     * Verifica que el constructor de la clase CategoriaDTOId permita crear una instancia con todos los campos correctamente, excepto el campo de categoría padre (para permitir categorías raíz).
     */
    @Test
    void constructor_deberiaPermitirTodosLosValoresMenosConstructorPadre() {
        CategoriaDTOId dto = new CategoriaDTOId(3L, "Raiz", null);
        assertEquals(3L, dto.getId());
        assertEquals("Raiz", dto.getNombre());
        assertNull(dto.getCategoriaPadre());
    }

    /**
     * Válida que el validador detecte campos nulos en la clase CategoriaDTOId.
     */
    @Test
    void validar_todosLosCampos() {
        CategoriaDTOId dto = new CategoriaDTOId(null, "", null);
        Set<ConstraintViolation<CategoriaDTOId>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    /**
     * Válida que el validador detecte campos nulos en la clase CategoriaDTOId, excepto el campo de categoría padre (para permitir categorías raíz).
     */
    @Test
    void validar_categoriaPadre() {
        CategoriaDTOId dto = new CategoriaDTOId(1L, "Nombre", null);
        Set<ConstraintViolation<CategoriaDTOId>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    /**
     * Válida que el validador detecte campos nulos en la clase CategoriaDTOId, excepto el campo de categoría padre (para permitir categorías raíz).
     */
    @Test
    void validar_categoriaHija() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria padre =
                tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                        .id(1L)
                        .nombre("Padre")
                        .build();

        CategoriaDTOId dto = new CategoriaDTOId(2L, "Hija", new CategoriaDTOIdNoCategoriaPadre(padre));
        Set<ConstraintViolation<CategoriaDTOId>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
