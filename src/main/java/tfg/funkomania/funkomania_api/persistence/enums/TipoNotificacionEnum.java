package tfg.funkomania.funkomania_api.persistence.enums;

/**
 * <p>Enum para definir el tipo de una notificación en el sistema. Actualmente, se definen siete tipos:</p>
 * <li>{@code REGISTRO}: Notificación relacionada con el registro de un nuevo usuario.</li>
 * <li>{@code COMPRA}: Notificación relacionada con la realización de una compra por parte de un usuario.</li>
 * <li>{@code ESTADO_PEDIDO}: Notificación relacionada con el cambio de estado de un pedido realizado por un usuario.</li>
 * <li>{@code CARRITO_ABANDONADO}: Notificación relacionada con un carrito de compras abandonado por un usuario.</li>
 * <li>{@code PAGO_ERROR}: Notificación relacionada con un error en el proceso de pago de una compra realizada por un usuario.</li>
 * <li>{@code LISTADESEOS_STOCK}: Notificación relacionada con la disponibilidad de un producto en la lista de deseos de un usuario.</li>
 * <li>{@code BIENVENIDA}: Notificación de bienvenida enviada a un nuevo usuario después de completar su registro.</li>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public enum TipoNotificacionEnum {
    REGISTRO,
    COMPRA,
    ESTADO_PEDIDO,
    CARRITO_ABANDONADO,
    PAGO_ERROR,
    LISTADESEOS_STOCK,
    BIENVENIDA
}
