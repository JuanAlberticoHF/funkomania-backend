package tfg.funkomania.funkomania_api.dtos.security_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tfg.funkomania.funkomania_api.persistence.enums.RoleEnum;

/**
 * DTO para la respuesta de autenticación.
 * @param token El token JWT generado para el usuario autenticado.
 * @param username El correo electrónico del usuario autenticado.
 * @param name El nombre de usuario del usuario autenticado.
 * @param role El rol del usuario autenticado.
 *
 * @author JuanAlbeticoHF
 * @version 1.1.0
 * @since 0.1.0
 */
public record TokenResponse (
        @NotBlank String token,
        @NotBlank String username,
        @NotBlank String name,
        @NotNull RoleEnum role
) {}
