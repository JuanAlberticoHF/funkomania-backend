package tfg.funkomania.funkomania_api.dtos.security_dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * Clase que representa la solicitud de inicio de sesión. Se utiliza para recibir los datos de autenticación del cliente
 * por medio de una petición HTTP.
 * @param username El usuario que se desea autenticar.
 * @param password La contraseña del usuario.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
public record LoginRequest (
        @NotBlank String username,
        @NotBlank String password
){}
