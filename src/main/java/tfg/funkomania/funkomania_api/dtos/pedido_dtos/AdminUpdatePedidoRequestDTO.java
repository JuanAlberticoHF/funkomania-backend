package tfg.funkomania.funkomania_api.dtos.pedido_dtos;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPagoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPedidoEnum;

/**
 * <p>DTO que representa la información necesaria para actualizar un pedido por un administrador en el sistema de Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public record AdminUpdatePedidoRequestDTO(
        EstadoPedidoEnum estadoPedido,
        EstadoPagoEnum estadoPago,
        @Positive(message = "El identificador del método de pago debe ser un número positivo")
        Long idMetodoPago,
        @Size(max = 500, message = "Los comentarios no pueden exceder los 500 caracteres")
        String comentarios
) {
}
