package tfg.funkomania.funkomania_api.dtos.pedido_dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import tfg.funkomania.funkomania_api.persistence.entities.VistaPedidosAdmin;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPagoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPedidoEnum;

import java.time.LocalDateTime;

/**
 * <p>DTO que representa una vista del historial de pedidos de la aplicación para administrador en Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Immutable
public class VistaPedidosAdminDTOId {
    @Positive(message = "El ID del pedido debe ser un número positivo.")
    private Long idPedido;

    @NotNull(message = "El código del pedido no puede ser nulo.")
    @Size(max = 30, message = "El código del pedido no puede exceder los 30 caracteres.")
    private String codigoPedido;

    @NotNull(message = "El email del usuario no puede ser nulo.")
    @Size(max = 255, message = "El email del usuario no puede exceder los 255 caracteres.")
    private String emailUsuario;

    @NotNull(message = "El nombre del usuario no puede ser nulo.")
    @Size(max = 152, message = "El nombre del usuario no puede exceder los 152 caracteres.")
    private String nombreUsuario;

    @NotNull(message = "La fecha del pedido no puede ser nula.")
    @PastOrPresent(message = "La fecha del pedido no puede ser futura.")
    private LocalDateTime fechaPedido;

    @NotNull(message = "El estado del pedido no puede ser nulo.")
    private EstadoPedidoEnum estadoPedido;

    @NotNull(message = "El estado del pago no puede ser nulo.")
    private EstadoPagoEnum estadoPago;

    @NotNull(message = "El método de pago del pedido no puede ser nulo.")
    @Size(max = 50, message = "El método de pago del pedido no puede exceder los 50 caracteres.")
    private String metodoPago;

    /**
     * Constructor que crea un objeto {@link VistaPedidosAdminDTOId} a partir de un objeto {@link VistaPedidosAdmin}.
     * @param vistaPedidosAdmin El objeto {@link VistaPedidosAdmin} del cual se extraerán los datos para crear el DTO.
     */
    public VistaPedidosAdminDTOId(VistaPedidosAdmin vistaPedidosAdmin) {
        this.idPedido = vistaPedidosAdmin.getIdPedido();
        this.codigoPedido = vistaPedidosAdmin.getCodigoPedido();
        this.emailUsuario = vistaPedidosAdmin.getEmailUsuario();
        this.nombreUsuario = vistaPedidosAdmin.getNombreUsuario();
        this.fechaPedido = vistaPedidosAdmin.getFechaPedido();
        this.estadoPedido = vistaPedidosAdmin.getEstadoPedido();
        this.estadoPago = vistaPedidosAdmin.getEstadoPago();
        this.metodoPago = vistaPedidosAdmin.getMetodoPago();
    }
}
