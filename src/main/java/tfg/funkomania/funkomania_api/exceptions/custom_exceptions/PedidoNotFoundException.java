package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando no se encuentra un pedido en el sistema.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException(String message) {
        super(message);
    }
}
