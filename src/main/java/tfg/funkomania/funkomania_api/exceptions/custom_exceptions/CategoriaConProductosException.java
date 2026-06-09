package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * Excepción personalizada que se lanza cuando se intenta eliminar una categoría que tiene productos asociados en el sistema.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.6.0
 */
public class CategoriaConProductosException extends RuntimeException {
    public CategoriaConProductosException(String message) {
        super(message);
    }
}
