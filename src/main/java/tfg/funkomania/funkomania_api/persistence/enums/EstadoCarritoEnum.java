package tfg.funkomania.funkomania_api.persistence.enums;

/**
 * <p>Enum para definir el estado de un carrito de compras en el sistema. Actualmente, se definen dos estados:</p>
 * <li>{@code ACTIVO}: Indica que el carrito está activo y en uso por el usuario.</li>
 * <li>{@code ABANDONADO}: Indica que el carrito ha sido abandonado por el usuario, lo que puede ocurrir si el usuario
 * no completa la compra después de un período de tiempo determinado.</li>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public enum EstadoCarritoEnum {
    ACTIVO,
    ABANDONADO
}
