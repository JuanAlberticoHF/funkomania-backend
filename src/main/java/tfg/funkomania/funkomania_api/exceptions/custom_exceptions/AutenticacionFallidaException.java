package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada para indicar que la autenticación ha fallado.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.8.5
 */
public class AutenticacionFallidaException extends RuntimeException {
    public AutenticacionFallidaException(String message) {
        super(message);
    }
}
