package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando no se encuentra una notificacion en el sistema.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public class NotificacionNotFoundException extends RuntimeException {
    public NotificacionNotFoundException(String message) {
        super(message);
    }
}
