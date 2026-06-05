package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando no se encuentra un usuario en la base de datos.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.4.0
 */
public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
