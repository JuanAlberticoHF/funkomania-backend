package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando un carrito de compra no existe o no esta creado.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public class CarritoNotFoundException extends RuntimeException {
    public CarritoNotFoundException(String message) {
        super(message);
    }
}
