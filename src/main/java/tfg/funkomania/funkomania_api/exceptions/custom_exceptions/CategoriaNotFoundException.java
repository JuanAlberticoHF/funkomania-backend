package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando no se encuentra una categoria en el sistema.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.6.0
 */
public class CategoriaNotFoundException extends RuntimeException {
    public CategoriaNotFoundException(String message) {
        super(message);
    }
}
