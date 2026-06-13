package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

/**
 * <p>Entidad que representa una vista del detalle del historial de pedidos de un usuario en Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Immutable
@Table(name = "VDetalle_Pedido")
@IdClass(VistaDetallePedidoId.class)
public class VistaDetallePedido {
    @Id
    @NotNull(message = "El ID del pedido no puede ser nulo.")
    @Positive(message = "El ID del pedido debe ser un número positivo.")
    @Column(name = "idPedido", nullable = false)
    private Long idPedido;

    @Id
    @NotNull(message = "El código del pedido no puede ser nulo.")
    @Size(max = 30, message = "El código del pedido no puede exceder los 30 caracteres.")
    @Column(name = "CodigoPedido", nullable = false, unique = true)
    private String codigoPedido;

    @Id
    @NotNull(message = "El ID del producto no puede ser nulo.")
    @Positive(message = "El ID del producto debe ser un número positivo.")
    @Column(name = "idProducto", nullable = false)
    private Long idProducto;

    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @Size(max = 150, message = "El nombre del producto no puede exceder los 150 caracteres.")
    @Column(name = "NombreProducto", nullable = false)
    private String nombreProducto;

    @NotNull(message = "La cantidad del producto no puede ser nulo.")
    @Positive(message = "La cantidad del producto debe ser un número positivo.")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio unitario debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio unitario del producto debe ser un número positivo o cero.")
    @Column (name = "PrecioUnitario", nullable = false)
    private BigDecimal precioUnitario;

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El porcentaje de iva debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "100.00", message = "El porcentaje de iva del producto no puede exceder el 100%.")
    @PositiveOrZero(message = "El porcentaje de iva del producto debe ser un número positivo o cero.")
    @Column(name = "IVA_Porcentaje", nullable = false, precision = 7, scale = 2)
    private BigDecimal ivaPorcentaje;

    @NotNull(message = "El subtotal del producto no puede ser nulo.")
    @Digits(integer = 19, fraction = 2, message = "El subtotal debe ser un número con hasta 19 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El subtotal del producto debe ser un número positivo o cero.")
    @Column (name = "Subtotal_Linea", nullable = false)
    private BigDecimal subtotalLinea;
}
