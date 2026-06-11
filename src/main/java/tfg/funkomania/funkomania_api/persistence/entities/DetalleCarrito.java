package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * <p>Entidad que representa un producto dentro del carrito de compras en el sistema de Funkomania.</p>
 * <p>La entidad mapea tabla {@code DetalleCarrito} de la base de datos</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Detalle_Carrito")
public class DetalleCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrito;

    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;

    @NotNull(message = "La cantidad del producto en el carrito no puede ser nula.")
    @Positive(message = "La cantidad del producto en el carrito debe ser un número positivo.")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}
