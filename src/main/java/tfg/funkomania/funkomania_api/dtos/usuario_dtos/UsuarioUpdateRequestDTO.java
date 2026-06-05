package tfg.funkomania.funkomania_api.dtos.usuario_dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO para la actualización de un usuario. Contiene los campos que pueden ser actualizados por el usuario autenticado.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.4.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UsuarioUpdateRequestDTO {
    /**
     * Nombre del usuario.
     */
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres.")
    private String nombre;

    /**
     * Primer apellido del usuario.
     */
    @Size(max = 50, message = "El primer apellido no debe exceder los 50 caracteres.")
    private String apellido1;

    /**
     * Segundo apellido del usuario.
     */
    @Size(max = 50, message = "El segundo apellido no debe exceder los 50 caracteres.")
    private String apellido2;

    /**
     * Teléfono de contacto del usuario.
     */
    @Size(max = 20, message = "El teléfono no debe exceder los 20 caracteres.")
    private String telefono;
}
