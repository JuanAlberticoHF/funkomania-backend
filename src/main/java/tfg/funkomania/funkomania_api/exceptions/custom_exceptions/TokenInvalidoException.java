package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando un token JWT es inválido.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
public class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException(String message) {
        super(message);
    }
}
