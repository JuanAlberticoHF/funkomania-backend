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
 * @version 1.1.0
 * @since 0.7.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Detalle_Carrito")
public class DetalleCarrito {
    @EmbeddedId
    private DetalleCarritoId id = new DetalleCarritoId();

    // Mapeamos la relación apuntando al campo de la clave compuesta
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCarrito") // Extrae el ID del Carrito y lo mete en id.idCarrito
    @JoinColumn(name = "idCarrito")
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProducto") // Extrae el ID del Producto y lo mete en id.idProducto
    @JoinColumn(name = "idProducto")
    private Producto producto;

    @NotNull(message = "La cantidad del producto en el carrito no puede ser nula.")
    @Positive(message = "La cantidad del producto en el carrito debe ser un número positivo.")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}
