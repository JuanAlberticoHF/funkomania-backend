package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando no se encuentra una dirección en el sistema.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public class DireccionNotFoundException extends RuntimeException {
    public DireccionNotFoundException(String message) {
        super(message);
    }
}
