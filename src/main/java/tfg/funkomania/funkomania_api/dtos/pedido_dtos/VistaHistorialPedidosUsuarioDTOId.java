
package tfg.funkomania.funkomania_api.dtos.pedido_dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import tfg.funkomania.funkomania_api.persistence.entities.VistaHistorialPedidosUsuario;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPagoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPedidoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>DTO que representa una vista del historial de pedidos de un usuario en Funkomania.</p>
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
public class VistaHistorialPedidosUsuarioDTOId {
    @NotNull(message = "El ID del pedido no puede ser nulo.")
    @Positive(message = "El ID del pedido debe ser un número positivo.")
    private Long idPedido;

    @NotNull(message = "El código del pedido no puede ser nulo.")
    @Size(max = 30, message = "El código del pedido no puede exceder los 30 caracteres.")
    private String codigoPedido;

    @NotNull(message = "El ID del usuario no puede ser nulo.")
    @Positive(message = "El ID del usuario debe ser un número positivo.")
    private Long idUsuario;

    @NotNull(message = "La fecha del pedido no puede ser nula.")
    @PastOrPresent(message = "La fecha del pedido no puede ser futura.")
    private LocalDateTime fechaPedido;

    @NotNull(message = "El estado del pedido no puede ser nulo.")
    private EstadoPedidoEnum estadoPedido;

    @NotNull(message = "El estado del pago no puede ser nulo.")
    private EstadoPagoEnum estadoPago;

    @NotNull(message = "El método de pago no puede ser nulo.")
    @Size(max = 50, message = "El método de pago no puede exceder los 50 caracteres.")
    private String metodoPago;

    @NotNull(message = "La dirección de envió no puede ser nulo.")
    @Size(max = 50, message = "La dirección de envió no puede exceder los 50 caracteres.")
    private String direccionEnvio;

    @NotNull(message = "El total del pedido no puede ser nulo.")
    @Digits(integer = 41, fraction = 2, message = "El total del pedido debe ser un número con hasta 41 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El total del pedido debe ser un número positivo o cero.")
    private BigDecimal totalPedido;

    public VistaHistorialPedidosUsuarioDTOId(VistaHistorialPedidosUsuario vista) {
        this.idPedido = vista.getIdPedido();
        this.codigoPedido = vista.getCodigoPedido();
        this.idUsuario = vista.getIdUsuario();
        this.fechaPedido = vista.getFechaPedido();
        this.estadoPedido = vista.getEstadoPedido();
        this.estadoPago = vista.getEstadoPago();
        this.metodoPago = vista.getMetodoPago();
        this.direccionEnvio = vista.getDireccionEnvio();
        this.totalPedido = vista.getTotalPedido();
    }
}
