package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando no se encuentra un detalle de pedido para un pedido específico.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public class DetallePedidoNotFoundException extends RuntimeException {
    public DetallePedidoNotFoundException(String message) {
        super(message);
    }
}
