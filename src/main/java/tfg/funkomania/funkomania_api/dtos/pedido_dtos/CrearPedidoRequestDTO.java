package tfg.funkomania.funkomania_api.dtos.pedido_dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * <p>DTO que representa la información necesaria para crear un nuevo pedido en el sistema de Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public record CrearPedidoRequestDTO (
        @NotNull(message = "El identificador del usuario no puede ser nulo")
        @Positive(message = "El identificador de la dirección debe ser un número positivo")
        Long idDireccion,
        @NotNull(message = "El identificador del método de pago no puede ser nulo")
        @Positive(message = "El identificador del método de pago debe ser un número positivo")
        Long idMetodoPago,
        @Size(max = 500, message = "Los comentarios no pueden exceder los 500 caracteres")
        String comentarios
) {}
