package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando un usuario intenta acceder a una notificación que no le pertenece.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public class NotNotificationOwnerException extends RuntimeException {
    public NotNotificationOwnerException(String message) {
        super(message);
    }
}
