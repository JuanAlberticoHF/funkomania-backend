package tfg.funkomania.funkomania_api.dtos.usuario_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;

/**
 * DTO para el registro de un nuevo usuario. Contiene los campos necesarios para crear una cuenta de usuario.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
 *
 * @see Usuario
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioRegistroDTO {
    @NotBlank(message = "El correo electrónico no debe estar vacío.")
    @Size(max = 255, message = "El correo electrónico no debe exceder los 255 caracteres.")
    private String email;

    @NotBlank(message = "La contraseña no debe estar vacía.")
    @Size(max = 255, message = "La contraseña no debe exceder los 255 caracteres.")
    private String password;

    @NotBlank(message = "El nombre no debe estar vacío.")
    @Size(max = 50)
    private String nombre;
}
