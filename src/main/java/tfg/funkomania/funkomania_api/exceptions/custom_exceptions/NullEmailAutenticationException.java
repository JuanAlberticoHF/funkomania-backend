package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando el email de autenticación es nulo durante el proceso de autenticación de un usuario.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.4.0
 */
public class NullEmailAutenticationException extends RuntimeException {
    public NullEmailAutenticationException(String message) {
        super(message);
    }
}
