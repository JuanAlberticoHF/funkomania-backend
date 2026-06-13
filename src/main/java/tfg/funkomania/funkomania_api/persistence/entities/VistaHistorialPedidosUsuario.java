package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPagoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPedidoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>Entidad que representa una vista del historial de pedidos de un usuario en Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Immutable
@Table(name = "VHistorial_Pedidos_Usuario")
@IdClass(VistaHistorialPedidosUsuarioId.class)
public class VistaHistorialPedidosUsuario {
    @Id
    @NotNull(message = "El ID del pedido no puede ser nulo.")
    @Positive(message = "El ID del pedido debe ser un número positivo.")
    @Column(name = "idPedido", nullable = false)
    private Long idPedido;

    @Id
    @NotNull(message = "El código del pedido no puede ser nulo.")
    @Size(max = 30, message = "El código del pedido no puede exceder los 30 caracteres.")
    @Column(name = "CodigoPedido", nullable = false, unique = true)
    private String codigoPedido;

    @Id
    @NotNull(message = "El ID del usuario no puede ser nulo.")
    @Positive(message = "El ID del usuario debe ser un número positivo.")
    @Column(name = "idUsuario", nullable = false)
    private Long idUsuario;

    @NotNull(message = "La fecha del pedido no puede ser nula.")
    @PastOrPresent(message = "La fecha del pedido no puede ser futura.")
    @Column(name = "FechaPedido", nullable = false)
    private LocalDateTime fechaPedido;

    @NotNull(message = "El estado del pedido no puede ser nulo.")
    @Column(name = "EstadoPedido", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPedidoEnum estadoPedido;

    @NotNull(message = "El estado del pago no puede ser nulo.")
    @Column(name = "EstadoPago", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPagoEnum estadoPago;

    @NotNull(message = "El método de pago no puede ser nulo.")
    @Size(max = 50, message = "El método de pago no puede exceder los 50 caracteres.")
    @Column(name = "MetodoPago", nullable = false)
    private String metodoPago;

    @NotNull(message = "La dirección de envió no puede ser nulo.")
    @Size(max = 50, message = "La dirección de envió no puede exceder los 50 caracteres.")
    @Column(name = "DireccionEnvio", nullable = false)
    private String direccionEnvio;

    @NotNull(message = "El total del pedido no puede ser nulo.")
    @Digits(integer = 41, fraction = 2, message = "El total del pedido debe ser un número con hasta 41 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El total del pedido debe ser un número positivo o cero.")
    @Column (name = "TotalPedido", nullable = false)
    private BigDecimal totalPedido;
}
