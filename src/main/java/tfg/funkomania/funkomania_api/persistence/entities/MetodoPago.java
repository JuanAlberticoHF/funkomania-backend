package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * <p>Entidad que representa un metodo de pago en el sistema de Funkomania.</p>
 * <p>La entidad mapea tabla {@code Metodo_Pago} de la base de datos</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "Metodo_Pago")
public class MetodoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMetodoPago", nullable = false)
    private Long id;

    @Size(max = 50, message = "El nombre del método de pago no debe exceder los 50 caracteres.")
    @Column(name = "Nombre", nullable = false, unique = true)
    private String nombre;

    @NotNull(message = "El campo 'activo' no puede ser nulo")
    @Column(name = "Activo", nullable = false)
    private Boolean activo;
}
