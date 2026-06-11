package tfg.funkomania.funkomania_api.dtos.carrito_dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * <p>DTO que representa un detalle de carrito.
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
public class CarritoDTOId_IdProducto {
    @NotNull(message = "El ID del carrito no puede ser nulo.")
    @Positive(message = "El ID del carrito debe ser un número positivo.")
    private Long idCarrito;

    @NotNull(message = "El ID del producto no puede ser nulo.")
    @Positive(message = "El ID del producto debe ser un número positivo.")
    private Long idProducto;

    @NotNull(message = "La cantidad del producto en el carrito no puede ser nula.")
    @Positive(message = "La cantidad del producto en el carrito debe ser un número positivo.")
    private Integer cantidad;
}
