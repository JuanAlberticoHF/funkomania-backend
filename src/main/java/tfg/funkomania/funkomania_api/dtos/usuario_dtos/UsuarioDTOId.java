package tfg.funkomania.funkomania_api.dtos.usuario_dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.funkomania.funkomania_api.enums.RoleEnum;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;

import java.time.LocalDateTime;

/**
 * DTO que representa un usuario con todos sus datos.
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
public class UsuarioDTOId {
    @Positive(message = "El ID del usuario debe ser un número positivo.")
    private Long id;

    @NotBlank(message = "El correo electrónico no debe estar vacío.")
    @Size(max = 255, message = "El correo electrónico no debe exceder los 255 caracteres.")
    private String email;

    @NotBlank(message = "La contraseña no debe estar vacía.")
    @Size(max = 255, message = "La contraseña no debe exceder los 255 caracteres.")
    private String password;

    @NotBlank(message = "El nombre no debe estar vacío.")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres.")
    private String nombre;

    @Size(max = 50, message = "El primer apellido no debe exceder los 50 caracteres.")
    private String apellido1;

    @Size(max = 50, message = "El segundo apellido no debe exceder los 50 caracteres.")
    private String apellido2;

    @Size(max = 20, message = "El teléfono no debe exceder los 20 caracteres.")
    private String telefono;

    @NotNull(message = "La fecha de registro no debe ser nula.")
    @PastOrPresent(message = "La fecha de registro no puede ser futura.")
    private LocalDateTime fechaRegistro;

    @PastOrPresent(message = "El último login no puede ser una fecha futura.")
    private LocalDateTime ultimoLogin;

    @NotBlank(message = "El rol no debe estar vacío.")
    private RoleEnum rol;

    @NotNull
    private boolean activo;

    public UsuarioDTOId(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.password = usuario.getPassword();
        this.nombre = usuario.getNombre();
        this.apellido1 = usuario.getApellido1();
        this.apellido2 = usuario.getApellido2();
        this.telefono = usuario.getTelefono();
        this.fechaRegistro = usuario.getFechaRegistro();
        this.ultimoLogin = usuario.getUltimoLogin();
        this.rol = usuario.getRol();
        this.activo = usuario.isActivo();
    }
}
