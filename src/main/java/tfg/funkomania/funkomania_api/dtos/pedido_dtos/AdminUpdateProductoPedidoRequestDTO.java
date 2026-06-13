package tfg.funkomania.funkomania_api.dtos.pedido_dtos;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * <p>DTO que representa la información necesaria para actualizar una línea de pedido por un administrador en el sistema de Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public record AdminUpdateProductoPedidoRequestDTO (
        @Positive(message = "El identificador del producto debe ser un número positivo")
        Integer cantidad,
        @PositiveOrZero(message = "El precio unitario sin IVA debe ser un número positivo o cero")
        BigDecimal PrecioUnitario_SinIVA,
        @PositiveOrZero(message = "El IVA debe ser un número positivo o cero")
        BigDecimal IVA
) {
}
