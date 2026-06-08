package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando se intenta agregar un producto a la lista de deseos del usuario y ese producto ya se encuentra en la lista.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public class ProductoYaEnListaDeseadosException extends RuntimeException {
    public ProductoYaEnListaDeseadosException(String message) {
        super(message);
    }
}
