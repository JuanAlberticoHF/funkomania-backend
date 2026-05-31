package tfg.funkomania.funkomania_api.exceptions.custom_exceptions;

/**
 * <p>Excepción personalizada para indicar que un usuario con el mismo correo electrónico ya existe.</p>
 * Casos de uso de esta excepción:
 * <ul>
 * <li>El correo electrónico ya existe.</li>
 * </ul>
 *
 * @author JuanAlberticoHF
 * @version 1.0
 * @since 0.1.0
 */
public class UsuarioAlreadyExistsException extends RuntimeException {
    public UsuarioAlreadyExistsException(String message) {
        super(message);
    }
}
