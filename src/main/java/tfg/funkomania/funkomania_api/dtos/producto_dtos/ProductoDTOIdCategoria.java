package tfg.funkomania.funkomania_api.dtos.producto_dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>DTO que representa un producto en el catálogo de Funkomania</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.6.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductoDTOIdCategoria {
    @NotNull(message = "El ID de la categoria no puede ser nulo.")
    @Positive(message = "El ID de la categoria debe ser un número positivo")
    private Long idCategoria;

    /**
     * Nombre del producto.
     */
    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @Size(max = 150, message = "El nombre del producto no puede exceder los 150 caracteres.")
    private String nombre;

    /**
     * Precio del producto.
     */
    @NotNull(message = "El precio del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio del producto debe ser un número positivo o cero.")
    private BigDecimal precio;

    /**
     * Stock disponible del producto.
     */
    @PositiveOrZero(message = "El stock del producto debe ser un número positivo o cero.")
    private Integer stock;

    /**
     * URL de la imagen del producto.
     */
    @Max(value = 255, message = "La URL de la imagen no puede exceder los 255 caracteres.")
    private String imagen;

    /**
     * Descripción detallada del producto.
     */
    private String descripcion;

    /**
     * Porcentaje IVA del producto
     */
    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El iva debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "100.00", message = "El iva del producto no puede exceder el 100%.")
    @PositiveOrZero(message = "El iva del producto debe ser un número positivo o cero.")
    private BigDecimal iva;

    /**
     * Producto activado o inactivo
     */
    @NotNull
    private boolean activo;

    /**
     * Producto en oferta o sin oferta
     */
    @NotNull
    private boolean enOferta;

    /**
     * Descuento aplicado al producto si está en oferta.
     */
    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El descuento debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "90.00", message = "El descuento del producto no puede exceder el 90%.")
    @PositiveOrZero(message = "El descuento del producto debe ser un número positivo o cero.")
    private BigDecimal descuento;

    /**
     * Fecha de creación del producto.
     */
    private LocalDateTime fechaFinOferta;
}
