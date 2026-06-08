package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando un usuario intenta marcar una notificación como leída que ya ha sido marcada previamente.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public class NotificacionYaLeidaException extends RuntimeException {
    public NotificacionYaLeidaException(String message) {
        super(message);
    }
}
