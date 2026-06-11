package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando un producto no existe en el carrito del usuario.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public class ProductoNotFoundInCarritoException extends RuntimeException {
    public ProductoNotFoundInCarritoException(String message) {
        super(message);
    }
}
