package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.security_dtos.LoginRequest;
import tfg.funkomania.funkomania_api.dtos.security_dtos.TokenResponse;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;

/**
 * Interfaz de servicio del núcleo de autenticación.
 * Define los métodos para realizar operaciones de registro de un usuario.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
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

    /**
     * Realiza el proceso de autenticación de un usuario y genera un token JWT si las credenciales son válidas.
     * @param loginRequest El objeto de tipo LoginRequest que contiene el correo electrónico y la contraseña del usuario
     *                     que intenta iniciar sesión.
     * @return Un objeto de tipo TokenResponse que contiene el token JWT generado para el usuario autenticado, username y
     * su nombre.
     */
    TokenResponse login(LoginRequest loginRequest);
}
