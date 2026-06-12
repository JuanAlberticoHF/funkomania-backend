package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

/**
 * <p>Entidad que representa una vista de alertas de stock para el administrador en Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.7.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Immutable
@Table(name = "VAdmin_Alertas_Stock")
public class VistaAdminAlertasStock {
    @Id
    @NotNull(message = "El ID del producto no puede ser nulo.")
    @Positive(message = "El ID del producto debe ser un número positivo.")
    @Column(name = "idProducto", nullable = false)
    private Long idProducto;

    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @Size(max = 150, message = "El nombre del producto no puede exceder los 150 caracteres.")
    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @NotNull(message = "El stock del producto no puede ser nulo.")
    @Positive(message = "El stock del producto debe ser un número positivo.")
    @Column(name = "Stock", nullable = false)
    private Integer stock;

    @NotNull(message = "El ID de la categoria no puede ser nulo.")
    @Positive(message = "El ID de la categoria debe ser un número positivo.")
    @Column(name = "idCategoria", nullable = false)
    private Long idCategoria;

    @Size(max = 7, message = "La prioridad no puede exceder los 7 caracteres.")
    @Column(name = "Prioridad", nullable = false)
    private String prioridad;
}
