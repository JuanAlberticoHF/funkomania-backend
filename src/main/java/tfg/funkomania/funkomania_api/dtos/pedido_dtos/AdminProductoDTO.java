package tfg.funkomania.funkomania_api.dtos.pedido_dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Clase DTO para representar la información de un producto en el contexto de la administración de pedidos.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AdminProductoDTO {
    @NotNull(message = "El identificador del producto no puede ser nulo.")
    @Positive(message = "El identificador del producto debe ser un número positivo")
    private Long idProducto;

    @NotNull(message = "La cantidad del producto no puede ser nula")
    @Positive(message = "La cantidad del producto debe ser un número positivo")
    private Integer Cantidad;

    @NotNull(message = "El precio unitario sin iva del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio unitario sin iva debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio unitario sin iva del producto debe ser un número positivo o cero.")
    private BigDecimal precioUnitarioSinIVA;

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El iva debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "100.00", message = "El iva del producto no puede exceder el 100%.")
    @PositiveOrZero(message = "El iva del producto debe ser un número positivo o cero.")
    private BigDecimal iva;
}
