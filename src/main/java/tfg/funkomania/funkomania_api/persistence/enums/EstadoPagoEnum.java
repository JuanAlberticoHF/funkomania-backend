package tfg.funkomania.funkomania_api.persistence.enums;

/**
 * <p>Enum para definir el estado de pago de un pedido en el sistema. Actualmente, se definen tres estados:</p>
 * <li>{@code PENDIENTE}: Indica que el pago del pedido está pendiente de ser realizado o procesado.</li>
 * <li>{@code PAGADO}: Indica que el pago del pedido ha sido realizado y procesado correctamente.</li>
 * <li>{@code RECHAZADO}: Indica que el pago del pedido ha sido rechazado por el sistema de pago o por el cliente.</li>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public enum EstadoPagoEnum {
    PENDIENTE,
    PAGADO,
    RECHAZADO
}
