package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Set;
import jakarta.validation.ConstraintViolation;

/**
 * Pruebas unitarias para la entidad Producto.
 *
 * @version 1.0.0
 * @since 0.2.0
 */
class ProductoTest {
    /** Validador para las pruebas de validación de la entidad Producto. */
    private Validator validator;

    @BeforeEach
    void configurarValidador() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    /**
     * Verifica que el constructor de la clase Producto permita crear una instancia correctamente.
     */
    @Test
    void constructor_deberiaPermitirCrearProducto() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria cat =
                tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                        .nombre("C")
                        .build();

        tfg.funkomania.funkomania_api.persistence.entities.Producto p =
                tfg.funkomania.funkomania_api.persistence.entities.Producto.builder()
                        .nombre("Prod")
                        .precio(BigDecimal.valueOf(10))
                        .stock(1)
                        .iva(BigDecimal.valueOf(21))
                        .activo(true)
                        .enOferta(false)
                        .descuento(BigDecimal.ZERO)
                        .categoria(cat)
                        .build();

        assertEquals("Prod", p.getNombre());
    }

    /**
     * Válida que el constructor de la clase Producto permita crear una instancia con solo los campos obligatorios correctamente.
     */
    @Test
    void constructor_deberiaPermitirCrearProductoConSoloCamposObligatorio() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria cat =
                tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                        .nombre("C2")
                        .build();

        tfg.funkomania.funkomania_api.persistence.entities.Producto p =
                tfg.funkomania.funkomania_api.persistence.entities.Producto.builder()
                        .nombre("Prod2")
                        .precio(BigDecimal.valueOf(5))
                        .stock(0)
                        .iva(BigDecimal.valueOf(0))
                        .activo(true)
                        .enOferta(false)
                        .descuento(BigDecimal.ZERO)
                        .categoria(cat)
                        .build();

        assertNotNull(p);
    }

    /**
     * Válida que el validador detecte campos obligatorios en la clase Producto.
     */
    @Test
    void validar_deberiaDetectarCamposObligatorios() {
        tfg.funkomania.funkomania_api.persistence.entities.Producto p =
                new tfg.funkomania.funkomania_api.persistence.entities.Producto();
        Set<ConstraintViolation<tfg.funkomania.funkomania_api.persistence.entities.Producto>> violations = validator.validate(p);
        assertFalse(violations.isEmpty());
    }
}
