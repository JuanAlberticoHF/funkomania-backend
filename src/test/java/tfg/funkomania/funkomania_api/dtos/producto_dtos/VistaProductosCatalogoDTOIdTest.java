package tfg.funkomania.funkomania_api.dtos.producto_dtos;

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
 * Pruebas unitarias para la clase {@link VistaProductosCatalogoDTOId}.
 *
 * @version 1.0.0
 * @since 0.2.0
 */
class VistaProductosCatalogoDTOIdTest {
    /** Validador para las pruebas de validación de la clase VistaProductosCatalogoDTOId. */
    private Validator validator;

    @BeforeEach
    void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * Verifica que el constructor de la clase VistaProductosCatalogoDTOId permita crear una instancia correctamente.
     */
    @Test
    void constructor_deberiaPermitirTodosLosValores() {
        tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo view =
                new tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo(
                        1L,
                        "Prod",
                        BigDecimal.valueOf(10),
                        BigDecimal.valueOf(12.1),
                        true,
                        BigDecimal.valueOf(10),
                        LocalDateTime.now().plusDays(1),
                        BigDecimal.valueOf(9),
                        BigDecimal.valueOf(10.89),
                        BigDecimal.valueOf(21),
                        5,
                        "img",
                        "desc",
                        true,
                        1L,
                        "Cat",
                        "CatPadre"
                );

        VistaProductosCatalogoDTOId dto = new VistaProductosCatalogoDTOId(view);
        assertEquals(view.getId(), dto.getId());
        assertEquals(view.getNombre(), dto.getNombre());
    }

    /**
     * Válida que el constructor de la clase VistaProductosCatalogoDTOId permita crear una instancia con solo los campos obligatorios correctamente.
     */
    @Test
    void validar_camposObligatorios_() {
        VistaProductosCatalogoDTOId dto = new VistaProductosCatalogoDTOId();
        dto.setNombre("X");
        dto.setPrecioOriginalSinIVA(BigDecimal.valueOf(10));
        dto.setPrecioOriginalConIVA(BigDecimal.valueOf(10));
        dto.setEnOferta(true);
        dto.setDescuento(BigDecimal.valueOf(10));
        dto.setPrecioFinalSinIVA(BigDecimal.valueOf(10));
        dto.setPrecioFinalConIVA(BigDecimal.valueOf(10));
        dto.setIva(BigDecimal.valueOf(10));
        dto.setActivo(true);
        dto.setIdCategoria(1L);
        dto.setNombreCategoria("Cat");
        dto.setNombreCategoriaPadre("Padre");

        Set<ConstraintViolation<VistaProductosCatalogoDTOId>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    /**
     * Válida que el validador detecte campos nulos en la clase VistaProductosCatalogoDTOId.
     */
    @Test
    void validar_todosLosCampos() {
        VistaProductosCatalogoDTOId dto = new VistaProductosCatalogoDTOId();
        Set<ConstraintViolation<VistaProductosCatalogoDTOId>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
