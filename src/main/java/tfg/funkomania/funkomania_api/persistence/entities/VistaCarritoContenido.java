package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>Entidad que representa una vista del contenido del carrito de un cliente en Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Immutable
@Table(name = "VCarrito_Contenido")
public class VistaCarritoContenido {
    @NotNull(message = "El ID del usuario no puede ser nulo.")
    @Positive(message = "El ID del usuario debe ser un número positivo.")
    @Id
    @Column(name = "idUsuario", nullable = false)
    private Long idUsuario;

    @NotNull(message = "El ID del carrito no puede ser nulo.")
    @Positive(message = "El ID del carrito debe ser un número positivo.")
    @Column(name = "idCarrito", nullable = false)
    private Long idCarrito;

    @NotNull(message = "El ID del producto no puede ser nulo.")
    @Positive(message = "El ID del producto debe ser un número positivo.")
    @Column(name = "idProducto", nullable = false)
    private Long idProducto;

    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @Size(max = 150, message = "El nombre del producto no puede exceder los 150 caracteres.")
    @Column(name = "Producto", nullable = false)
    private String producto;

    @Size(max = 255, message = "La URL de la imagen no puede exceder los 255 caracteres.")
    @Column(name = "Image")
    private String image;

    @Positive(message = "La cantidad del producto en el carrito debe ser un número positivo.")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio original sin iva del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio original sin iva debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio original sin iva del producto debe ser un número positivo o cero.")
    @Column(name = "PrecioOriginal_SinIVA", nullable = false)
    private BigDecimal precioOriginalSinIVA;

    @NotNull
    @Column(name = "EnOferta", nullable = false)
    private Boolean enOferta;

    @Column(name = "FechaFinOferta")
    private LocalDateTime fechaFinOferta;

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El descuento debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "90.00", message = "El descuento del producto no puede exceder el 90%.")
    @PositiveOrZero(message = "El descuento del producto debe ser un número positivo o cero.")
    @Column(name = "Descuento", nullable = false, precision = 7, scale = 2)
    private BigDecimal descuento;

    @NotNull(message = "El precio unitario sin iva del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio unitario sin iva debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio unitario sin iva del producto debe ser un número positivo o cero.")
    @Column (name = "PrecioUnitario_SinIVA", nullable = false)
    private BigDecimal precioUnitarioSinIVA;

    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El porcentaje de iva debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "100.00", message = "El porcentaje de iva del producto no puede exceder el 100%.")
    @PositiveOrZero(message = "El porcentaje de iva del producto debe ser un número positivo o cero.")
    @Column(name = "IVA_Porcentaje", nullable = false, precision = 7, scale = 2)
    private BigDecimal ivaPorcentaje;

    @NotNull(message = "El precio unitario con iva del producto no puede ser nulo.")
    @Digits(integer = 13, fraction = 2, message = "El precio unitario con iva debe ser un número con hasta 13 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio unitario con iva del producto debe ser un número positivo o cero.")
    @Column (name = "PrecioUnitario_ConIVA", nullable = false)
    private BigDecimal precioUnitarioConIVA;

    @NotNull(message = "El subtotal del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El subtotal debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El subtotal del producto debe ser un número positivo o cero.")
    @Column (name = "Subtotal_Posicion", nullable = false)
    private BigDecimal subtotalPosicion;

}
