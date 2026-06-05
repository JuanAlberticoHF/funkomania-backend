package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>Entidad que representa una vista del perfil de un cliente de Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.4.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Immutable
@Table(name = "VUsuario_Perfil_Cliente")
public class VistaUsuarioPerfilCliente {
    /**
     * Identificador único del usuario.
     */
    @Id
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
     * Nombre del cliente.
     */
    @NotBlank(message = "El nombre no debe estar vacío.")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres.")
    @Column(name = "Nombre", nullable = false)
    private String nombre;

    /**
     * Apellidos del cliente.
     */
    @Size(max = 101, message = "Los apellidos no debe exceder los 101 caracteres.")
    @Column(name = "Apellidos")
    private String apellidos;

    /**
     * Teléfono de contacto del cliente.
     */
    @Size(max = 20, message = "El teléfono no debe exceder los 20 caracteres.")
    @Column(name = "Telefono")
    private String telefono;

    /**
     * Fecha de registro del cliente en el sistema, no puede ser una fecha futura.
     */
    @PastOrPresent(message = "La fecha de registro no puede ser futura.")
    @Column(name = "FechaRegistro", nullable = false)
    private LocalDateTime fechaRegistro;

    /**
     * Dirección principal del cliente, puede ser nula pero no debe exceder los 270 caracteres si se proporciona.
     */
    @Size(max = 270, message = "La dirección principal no debe exceder los 270 caracteres.")
    @Column(name = "DireccionPrincipal")
    private String direccionPrincipal;

    /**
     * Cantidad total de pedidos realizados por el cliente, debe ser un número positivo o cero.
     */
    @PositiveOrZero(message = "La cantidad de pedidos del cliente debe ser un numero positivo o cero")
    @Column(name = "CantidadPedidos", nullable = false)
    private Long cantidadPedidos;

    /**
     * Monto total gastado por el cliente en sus pedidos, debe ser un número positivo o cero y no puede exceder los 41 dígitos enteros y 2 decimales.
     */
    @Digits(integer = 41, fraction = 2, message = "El precio debe ser un número con hasta 41 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El total gastado por el cliente debe ser un número positivo o cero.")
    @Column(name = "TotalGastado", nullable = false)
    private BigDecimal totalGastado;
}
