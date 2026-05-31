package tfg.funkomania.funkomania_api.dtos.security_dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para la respuesta de autenticación.
 * @param token El token JWT generado para el usuario autenticado.
 * @param email El correo electrónico del usuario autenticado.
 * @param username El nombre de usuario del usuario autenticado.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
public record TokenResponse (
        @NotBlank String token,
        @NotBlank String email,
        @NotBlank String username
) {}
