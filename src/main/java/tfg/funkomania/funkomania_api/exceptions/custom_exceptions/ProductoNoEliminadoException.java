package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando no se pudo eliminar un producto de forma lógica.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.6.0
 */
public class ProductoNoEliminadoException extends RuntimeException {
    public ProductoNoEliminadoException(String message) {
        super(message);
    }
}
