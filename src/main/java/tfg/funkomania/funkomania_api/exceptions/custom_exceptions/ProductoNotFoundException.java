package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando no se encuentra un producto en la base de datos.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.3.0
 */
public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(String message) {
        super(message);
    }
}
