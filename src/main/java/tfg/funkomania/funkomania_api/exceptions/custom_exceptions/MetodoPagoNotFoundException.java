package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando no se encuentra un metodo de pago específico.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public class MetodoPagoNotFoundException extends RuntimeException {
    public MetodoPagoNotFoundException(String message) {
        super(message);
    }
}
