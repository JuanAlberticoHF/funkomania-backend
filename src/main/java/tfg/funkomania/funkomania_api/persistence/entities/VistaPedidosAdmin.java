package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Immutable;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPagoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPedidoEnum;

import java.time.LocalDateTime;

/**
 * <p>Entidad que representa una vista del historial de pedidos de la aplicación para administrador en Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Immutable
@Table(name = "VPedidos_Admin")
public class VistaPedidosAdmin {
    @Positive(message = "El ID del pedido debe ser un número positivo.")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPedido", nullable = false)
    private Long idPedido;

    @NotNull(message = "El código del pedido no puede ser nulo.")
    @Size(max = 30, message = "El código del pedido no puede exceder los 30 caracteres.")
    @Column(name = "CodigoPedido", nullable = false, unique = true)
    private String codigoPedido;

    @NotNull(message = "El email del usuario no puede ser nulo.")
    @Size(max = 255, message = "El email del usuario no puede exceder los 255 caracteres.")
    @Column(name = "Email_Usuario", nullable = false)
    private String emailUsuario;

    @NotNull(message = "El nombre del usuario no puede ser nulo.")
    @Size(max = 152, message = "El nombre del usuario no puede exceder los 152 caracteres.")
    @Column(name = "Nombre_Usuario", nullable = false)
    private String nombreUsuario;

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

    @NotNull(message = "El método de pago del pedido no puede ser nulo.")
    @Size(max = 50, message = "El método de pago del pedido no puede exceder los 50 caracteres.")
    @Column(name = "Metodo_Pago", nullable = false)
    private String metodoPago;
}
