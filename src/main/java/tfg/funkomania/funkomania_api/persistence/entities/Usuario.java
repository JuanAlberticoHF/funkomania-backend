package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.UsuarioRegistroDTO;
import tfg.funkomania.funkomania_api.enums.RoleEnum;

import java.time.LocalDateTime;

/**
 * <p>Entidad que representa un usuario en el sistema de Funkomania.</p>
 * <p>La entidad mapea tabla {@code usuario} de la base de datos</p>
 *
 * @author JuanAlbeticoHF
 * @version 0.1
 * @since 0.1.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "Usuario")
public class Usuario {

    /**
     * Identificador único del usuario.
     */
    @Positive(message = "El ID del usuario debe ser un número positivo.")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario", nullable = false)
    private Long id;

    /**
     * Correo electrónico único del usuario.
     */
    @NotBlank(message = "El correo electrónico no debe estar vacío.")
    @Size(max = 255, message = "El correo electrónico no debe exceder los 255 caracteres.")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * Contraseña del usuario, almacenada de forma segura ya encriptada (hash).
     */
    @NotBlank(message = "La contraseña no debe estar vacía.")
    @Size(max = 255, message = "La contraseña no debe exceder los 255 caracteres.")
    @Column(name = "passwordHash", nullable = false)
    private String password;

    @NotBlank(message = "El nombre no debe estar vacío.")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres.")
    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Size(max = 50, message = "El primer apellido no debe exceder los 50 caracteres.")
    @Column(name = "Apellido1")
    private String apellido1;

    @Size(max = 50, message = "El segundo apellido no debe exceder los 50 caracteres.")
    @Column(name = "Apellido2")
    private String apellido2;

    @Size(max = 20, message = "El teléfono no debe exceder los 20 caracteres.")
    @Column(name = "Telefono")
    private String telefono;

    @NotNull(message = "La fecha de registro no debe ser nula.")
    @PastOrPresent(message = "La fecha de registro no puede ser futura.")
    @Column(name = "FechaRegistro", nullable = false)
    private LocalDateTime fechaRegistro;

    @PastOrPresent(message = "El último login no puede ser una fecha futura.")
    @Column(name = "UltimoLogin")
    private LocalDateTime ultimoLogin;

    @NotNull(message = "El rol del usuario no debe ser nulo.")
    @Column(name = "Rol", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum rol;

    @NotNull
    @Column(name = "Activo", nullable = false)
    private boolean activo;

    /**
     * Crea un nuevo usuario a partir de un DTO de registro. El ID se establece como {@code null} para que sea autogenerado por la base de datos.
     * @param usuarioRegistroDTO DTO con los datos necesarios para registrar un nuevo usuario.
     */
    public Usuario(UsuarioRegistroDTO usuarioRegistroDTO) {
        this.id = null;
        this.email = usuarioRegistroDTO.getEmail();
        this.password = usuarioRegistroDTO.getPassword();
        this.nombre = usuarioRegistroDTO.getNombre();
        this.apellido1 = null;
        this.apellido2 = null;
        this.telefono = null;
        this.fechaRegistro = null;
        this.ultimoLogin = null;
        this.rol = null;
        this.activo = false;
    }
}
