package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

/**
 * <p>Entidad que representa el detalle de un pedido en el sistema de Funkomania, que incluye información sobre los productos asociados a cada pedido.</p>
 * <p>La entidad mapea tabla {@code DetallePedido} de la base de datos</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.2
 * @since 0.7.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Detalle_Pedido")
public class DetallePedido {

    @EmbeddedId
    private DetallePedidoId id = new DetallePedidoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPedido") // Extrae el ID del Producto y lo mete en id.idPedido
    @JoinColumn(name = "idPedido")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProducto") // Extrae el ID del Producto y lo mete en id.idProducto
    @JoinColumn(name = "idProducto")
    private Producto producto;

    @NotNull(message = "El precio unitario del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio unitario debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio unitario del producto debe ser un número positivo o cero.")
    @Column (name = "precioUnitario", nullable = false)
    private BigDecimal precioUnitario;

    @NotNull(message = "La cantidad del producto no puede ser nula.")
    @Positive(message = "La cantidad del producto debe ser un número positivo.")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El iva del producto no puede ser nulo.")
    @Digits(integer = 5, fraction = 2, message = "El iva debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El iva del producto debe ser un número positivo o cero.")
    @Column (name = "iva", nullable = false)
    private BigDecimal iva;

}
