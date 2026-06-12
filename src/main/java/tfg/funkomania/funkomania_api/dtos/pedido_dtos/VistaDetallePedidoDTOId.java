package tfg.funkomania.funkomania_api.dtos.pedido_dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import tfg.funkomania.funkomania_api.persistence.entities.VistaDetallePedido;

import java.math.BigDecimal;

/**
 * <p>DTO que representa una vista del detalle del historial de pedidos de un usuario en Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class VistaDetallePedidoDTOId {
    @NotNull(message = "El ID del pedido no puede ser nulo.")
    @Positive(message = "El ID del pedido debe ser un número positivo.")
    private Long idPedido;

    @NotNull(message = "El código del pedido no puede ser nulo.")
    @Size(max = 30, message = "El código del pedido no puede exceder los 30 caracteres.")
    private String codigoPedido;

    @NotNull(message = "El ID del producto no puede ser nulo.")
    @Positive(message = "El ID del producto debe ser un número positivo.")
    private Long idProducto;

    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @Size(max = 150, message = "El nombre del producto no puede exceder los 150 caracteres.")
    private String nombreProducto;

    @NotNull(message = "La cantidad del producto no puede ser nulo.")
    @Positive(message = "La cantidad del producto debe ser un número positivo.")
    private Integer cantidad;

    @NotNull(message = "El precio unitario del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio unitario debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio unitario del producto debe ser un número positivo o cero.")
    private BigDecimal precioUnitario;

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El porcentaje de iva debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "100.00", message = "El porcentaje de iva del producto no puede exceder el 100%.")
    @PositiveOrZero(message = "El porcentaje de iva del producto debe ser un número positivo o cero.")
    private BigDecimal ivaPorcentaje;

    @NotNull(message = "El subtotal del producto no puede ser nulo.")
    @Digits(integer = 19, fraction = 2, message = "El subtotal debe ser un número con hasta 19 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El subtotal del producto debe ser un número positivo o cero.")
    private BigDecimal subtotalLinea;

    public VistaDetallePedidoDTOId(VistaDetallePedido vistaDetallePedido) {
        this.idPedido = vistaDetallePedido.getIdPedido();
        this.codigoPedido = vistaDetallePedido.getCodigoPedido();
        this.idProducto = vistaDetallePedido.getIdProducto();
        this.nombreProducto = vistaDetallePedido.getNombreProducto();
        this.cantidad = vistaDetallePedido.getCantidad();
        this.precioUnitario = vistaDetallePedido.getPrecioUnitario();
        this.subtotalLinea = vistaDetallePedido.getSubtotalLinea();
        this.ivaPorcentaje = vistaDetallePedido.getIvaPorcentaje();
    }
}
