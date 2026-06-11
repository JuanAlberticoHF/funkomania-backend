package tfg.funkomania.funkomania_api.dtos.carrito_dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import tfg.funkomania.funkomania_api.persistence.entities.VistaCarritoContenido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>DTO que representa la vista de contenido de un carrito.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VistaCarritoContenidoDTOId {
    @NotNull(message = "El ID del usuario no puede ser nulo.")
    @Positive(message = "El ID del usuario debe ser un número positivo.")
    private Long idUsuario;

    @NotNull(message = "El ID del carrito no puede ser nulo.")
    @Positive(message = "El ID del carrito debe ser un número positivo.")
    private Long idCarrito;

    @NotNull(message = "El ID del producto no puede ser nulo.")
    @Positive(message = "El ID del producto debe ser un número positivo.")
    private Long idProducto;

    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @Size(max = 150, message = "El nombre del producto no puede exceder los 150 caracteres.")
    private String producto;

    @Size(max = 255, message = "La URL de la imagen no puede exceder los 255 caracteres.")
    private String image;

    @Positive(message = "La cantidad del producto en el carrito debe ser un número positivo.")
    private Integer cantidad;

    @NotNull(message = "El precio original sin iva del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio original sin iva debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio original sin iva del producto debe ser un número positivo o cero.")
    private BigDecimal precioOriginalSinIVA;

    @NotNull
    private Boolean enOferta;

    private LocalDateTime fechaFinOferta;

    @NotNull(message = "El descuento del producto no puede ser nulo.")
    @Digits(integer = 5, fraction = 2, message = "El descuento debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "90.00", message = "El descuento del producto no puede exceder el 90%.")
    @PositiveOrZero(message = "El descuento del producto debe ser un número positivo o cero.")
    private BigDecimal descuento;

    @NotNull(message = "El precio unitario sin iva del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio unitario sin iva debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio unitario sin iva del producto debe ser un número positivo o cero.")
    private BigDecimal precioUnitarioSinIVA;

    @NotNull(message = "El porcentaje de iva del producto en el carrito no puede ser nulo.")
    @Digits(integer = 5, fraction = 2, message = "El porcentaje de iva debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "100.00", message = "El porcentaje de iva del producto no puede exceder el 100%.")
    @PositiveOrZero(message = "El porcentaje de iva del producto debe ser un número positivo o cero.")
    private BigDecimal ivaPorcentaje;

    @NotNull(message = "El precio unitario con iva del producto no puede ser nulo.")
    @Digits(integer = 13, fraction = 2, message = "El precio unitario con iva debe ser un número con hasta 13 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio unitario con iva del producto debe ser un número positivo o cero.")
    private BigDecimal precioUnitarioConIVA;

    @NotNull(message = "El subtotal del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El subtotal debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El subtotal del producto debe ser un número positivo o cero.")
    private BigDecimal subtotalPosicion;

    public VistaCarritoContenidoDTOId(VistaCarritoContenido vistaCarritoContenido) {
        this.idUsuario = vistaCarritoContenido.getIdUsuario();
        this.idCarrito = vistaCarritoContenido.getIdCarrito();
        this.idProducto = vistaCarritoContenido.getIdProducto();
        this.producto = vistaCarritoContenido.getProducto();
        this.image = vistaCarritoContenido.getImage();
        this.cantidad = vistaCarritoContenido.getCantidad();
        this.precioOriginalSinIVA = vistaCarritoContenido.getPrecioOriginalSinIVA();
        this.enOferta = vistaCarritoContenido.getEnOferta();
        this.fechaFinOferta = vistaCarritoContenido.getFechaFinOferta();
        this.descuento = vistaCarritoContenido.getDescuento();
        this.precioUnitarioSinIVA = vistaCarritoContenido.getPrecioUnitarioSinIVA();
        this.ivaPorcentaje = vistaCarritoContenido.getIvaPorcentaje();
        this.precioUnitarioConIVA = vistaCarritoContenido.getPrecioUnitarioConIVA();
        this.subtotalPosicion = vistaCarritoContenido.getSubtotalPosicion();
    }
}
