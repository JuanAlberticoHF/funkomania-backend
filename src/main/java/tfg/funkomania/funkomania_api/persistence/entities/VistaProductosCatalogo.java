package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>Entidad que representa una vista de productos en el catálogo de Funkomania.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.2.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Immutable
@Table(name = "VProductos_Catalogo")
public class VistaProductosCatalogo {
    /**
     * Identificador único del producto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto", nullable = false)
    private Long id;

    /**
     * Nombre del producto.
     */
    @NotNull(message = "El nombre del producto no puede ser nulo.")
    @Max(value = 150, message = "El nombre del producto no puede exceder los 150 caracteres.")
    @Column(name = "Nombre", nullable = false)
    private String nombre;

    /**
     * Precio original sin iva del producto.
     */
    @NotNull(message = "El precio original sin iva del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio original sin iva debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio original sin iva del producto debe ser un número positivo o cero.")
    @Column (name = "PrecioOriginal_SinIVA", nullable = false)
    private BigDecimal precioOriginalSinIVA;

    /**
     * Precio original sin iva del producto.
     */
    @NotNull(message = "El precio original con iva del producto no puede ser nulo.")
    @Digits(integer = 13, fraction = 2, message = "El precio original con iva debe ser un número con hasta 13 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio original con iva del producto debe ser un número positivo o cero.")
    @Column (name = "PrecioOriginal_ConIVA", nullable = false)
    private BigDecimal precioOriginalConIVA;

    /**
     * Producto en oferta o sin oferta
     */
    @NotNull
    @Column(name = "EnOferta", nullable = false)
    private boolean enOferta;

    /**
     * Descuento aplicado al producto si está en oferta.
     */
    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El descuento debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "90.00", message = "El descuento del producto no puede exceder el 90%.")
    @PositiveOrZero(message = "El descuento del producto debe ser un número positivo o cero.")
    @Column(name = "Descuento", nullable = false, precision = 7, scale = 2)
    private BigDecimal descuento;

    /**
     * Fecha de creación del producto.
     */
    @Column(name = "FechaFinOferta")
    private LocalDateTime fechaFinOferta;

    /**
     * Precio final sin iva del producto.
     */
    @NotNull(message = "El precio final sin iva del producto no puede ser nulo.")
    @Digits(integer = 10, fraction = 2, message = "El precio final sin iva debe ser un número con hasta 10 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio final sin iva del producto debe ser un número positivo o cero.")
    @Column (name = "PrecioFinal_SinIVA", nullable = false)
    private BigDecimal precioFinalSinIVA;

    /**
     * Precio final sin iva del producto.
     */
    @NotNull(message = "El precio final con iva del producto no puede ser nulo.")
    @Digits(integer = 13, fraction = 2, message = "El precio final con iva debe ser un número con hasta 13 dígitos enteros y 2 decimales.")
    @PositiveOrZero(message = "El precio final con iva del producto debe ser un número positivo o cero.")
    @Column (name = "PrecioFinal_ConIVA", nullable = false)
    private BigDecimal precioFinalConIVA;

    /**
     * Porcentaje IVA del producto
     */
    @NotNull
    @Digits(integer = 5, fraction = 2, message = "El iva debe ser un número con hasta 5 dígitos enteros y 2 decimales.")
    @DecimalMax(value = "100.00", message = "El iva del producto no puede exceder el 100%.")
    @PositiveOrZero(message = "El iva del producto debe ser un número positivo o cero.")
    @Column(name = "iva", nullable = false, precision = 7, scale = 2)
    private BigDecimal iva;

    /**
     * Stock disponible del producto.
     */
    @PositiveOrZero(message = "El stock del producto debe ser un número positivo o cero.")
    @Column(name = "Stock", nullable = false)
    private Integer stock;

    /**
     * URL de la imagen del producto.
     */
    @Max(value = 255, message = "La URL de la imagen no puede exceder los 255 caracteres.")
    @Column(name = "Image")
    private String imagen;

    /**
     * Descripción detallada del producto.
     */
    @Lob
    @Column(name = "Descripcion", columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Producto activado o inactivo
     */
    @NotNull
    @Column(name = "Activo", nullable = false)
    private boolean activo;

    /**
     * Identificador de la categoria del producto.
     */
    @NotNull(message = "El id de la categoria del producto no puede ser nulo.")
    @Column(name = "idCategoria", nullable = false)
    private Long idCategoria;

    /**
     * Nombre de la categoría a la que pertenece el producto.
     */
    @NotNull(message = "El nombre de la categoria del producto no puede ser nula.")
    @Column(name = "NombreCategoria", nullable = false)
    private String nombreCategoria;

    /**
     * Nombre de la categoría padre a la que pertenece el producto.
     */
    @NotNull(message = "El nombre de la categoria padre del producto no puede ser nula.")
    @Column(name = "NombreCategoriaPadre", nullable = false)
    private String nombreCategoriaPadre;
}
