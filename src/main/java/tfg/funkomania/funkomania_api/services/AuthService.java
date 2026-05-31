package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.persistence.entities.Usuario;

/**
 * Interfaz de servicio del núcleo de autenticación.
 * Define los métodos para realizar operaciones de registro de un usuario.
 *
 * @author JuanAlbeticoHF
 * @version 0.2.1
 * @since 0.1.0
 */
public interface AuthService {
    /**
     * Registra un nuevo usuario en el sistema.
     * @param usuario El objeto de tipo Usuario que se desea registrar en el sistema.
     * @return Devuelve el objeto Usuario que ha sido registrado en el sistema, incluyendo su ID generado automáticamente.
     */
    Usuario register(Usuario usuario);

    /**
     * Verifica si existe un usuario en el sistema por su correo electrónico.
     * @param email El correo electrónico a buscar en el sistema.
     * @return {@code true} si existe un usuario con el correo electrónico proporcionado, {@code false} en caso contrario.
     */
    boolean existsUsuarioByEmail(String email);
}
