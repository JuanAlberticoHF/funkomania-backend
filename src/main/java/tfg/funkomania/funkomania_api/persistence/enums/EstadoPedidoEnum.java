package tfg.funkomania.funkomania_api.persistence.enums;

/**
 * <p>Enum para definir el estado de un pedido en el sistema. Actualmente, se definen cinco estados:</p>
 * <li>{@code PENDIENTE}: Indica que el pedido ha sido creado, pero aún no ha sido procesado.</li>
 * <li>{@code PROCESANDO}: Indica que el pedido está siendo procesado por el sistema.</li>
 * <li>{@code ENVIADO}: Indica que el pedido ha sido enviado al cliente.</li>
 * <li>{@code ENTREGADO}: Indica que el pedido ha sido entregado al cliente.</li>
 * <li>{@code CANCELADO}: Indica que el pedido ha sido cancelado por el cliente o por el sistema.</li>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public enum EstadoPedidoEnum {
    PENDIENTE,
    PROCESANDO,
    ENVIADO,
    ENTREGADO,
    CANCELADO
}
