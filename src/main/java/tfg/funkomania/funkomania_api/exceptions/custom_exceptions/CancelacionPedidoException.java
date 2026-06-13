package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando se intenta cancelar un pedido que no puede ser cancelado.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.8.0
 */
public class CancelacionPedidoException extends RuntimeException {
    public CancelacionPedidoException(String message) {
        super(message);
    }
}
